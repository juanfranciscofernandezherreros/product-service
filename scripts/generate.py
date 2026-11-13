#!/usr/bin/env python3
"""Genera los microservicios del manifiesto (.ai/services.txt), cada uno en su carpeta y repositorio.

Por defecto el codigo sale de plantillas a partir del DDL, sin IA y sin tokens. La IA solo interviene
si se pide: para arreglar un build que falla (--ai-fix) o para anadir reglas de negocio (--ai-rules).

Uso:
  python scripts/generate.py                         # genera todos los servicios (solo ficheros)
  python scripts/generate.py -s access-service       # solo uno (se puede repetir -s)
  python scripts/generate.py --build                 # ademas ejecuta mvnw clean verify
  python scripts/generate.py --build --ai-fix 2      # si el build falla, la IA lo corrige (max. 2 intentos)
  python scripts/generate.py --build --ai-rules      # aplica specs/business-rules/<servicio>.md con IA
  python scripts/generate.py --build --push          # commit y push al remoto del servicio si el build pasa
  python scripts/generate.py --build --create-repos --push   # crea antes en GitHub los repositorios que falten
  python scripts/generate.py --openapi               # solo regenera specs/openapi/*.yaml en el generador
  python scripts/generate.py --mode ai               # pipeline antiguo: todo el codigo lo escribe la IA

Un servicio cuyo DDL, generador y plantillas no han cambiado se omite (usa --force para regenerarlo).
Variables opcionales: MODEL (modelo de Claude) y CLAUDE_ARGS (argumentos extra de la CLI).
--create-repos necesita GITHUB_TOKEN (o GH_TOKEN) con permiso para crear repositorios.
"""
from __future__ import annotations

import argparse
import os
import re
import shlex
import shutil
import subprocess
import sys
from dataclasses import dataclass
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(ROOT))

import create_repos  # noqa: E402
from generator import openapi  # noqa: E402
from generator.model import load_service  # noqa: E402
from generator.project import fingerprint, is_current, write_project  # noqa: E402

MANIFEST = ROOT / ".ai" / "services.txt"
PROMPTS = ROOT / ".ai" / "prompts"
AI_PHASES = ["01-plan-and-tasks.md", "02-domain.md", "03-application.md", "04-adapters-and-tests.md",
             "05-e2e-verification.md"]
BASE_PORT = 8081
LOG_TAIL = 200


@dataclass
class Entry:
    name: str
    target: Path
    remote: str
    spec: str
    ddl: Path
    port: int


def read_manifest() -> list[Entry]:
    entries = []
    for number, raw in enumerate(MANIFEST.read_text(encoding="utf-8").splitlines(), start=1):
        line = raw.strip()
        if not line or line.startswith("#"):
            continue
        fields = [field.strip() for field in line.split(";")]
        if len(fields) != 5:
            sys.exit(f"[ERROR] .ai/services.txt:{number}: se esperaban 5 campos separados por ';'.")
        name, target, remote, spec, ddl = fields
        if not (name and target and remote and ddl):
            sys.exit(f"[ERROR] .ai/services.txt:{number}: SERVICE_NAME, TARGET_DIR, REMOTE_URL y DB_SPEC son "
                     "obligatorios.")
        target_path = Path(target)
        if not target_path.is_absolute():
            target_path = ROOT / target_path
        entries.append(Entry(name, target_path.resolve(), remote, spec, ROOT / ddl, BASE_PORT + len(entries)))
    return entries


def claude(prompt: str, cwd: Path) -> bool:
    """Ejecuta la CLI de Claude sin intervencion, pasando el prompt por la entrada estandar."""
    command = ["claude"]
    if os.environ.get("MODEL"):
        command += ["--model", os.environ["MODEL"]]
    command += shlex.split(os.environ.get("CLAUDE_ARGS", "")) + ["-p"]
    return subprocess.run(command, input=prompt, text=True, encoding="utf-8", cwd=cwd, check=False).returncode == 0


def maven_command(backend: Path) -> list[str]:
    """Comando de Maven con rutas absolutas: el wrapper de backend/ o, si falta, el mvn del sistema."""
    pom = backend / "pom.xml"
    wrapper = backend / ("mvnw.cmd" if os.name == "nt" else "mvnw")
    if wrapper.is_file():
        launcher = ["cmd", "/c", str(wrapper)] if os.name == "nt" else [str(wrapper)]
    elif shutil.which("mvn"):
        launcher = [shutil.which("mvn")]
    else:
        sys.exit(f"[ERROR] No hay wrapper de Maven en {backend} ni 'mvn' en el PATH.")
    return launcher + ["-B", "-f", str(pom), "clean", "verify"]


def check_java() -> None:
    """Comprueba antes de compilar que hay un JDK 21 o superior; si no, se para con un mensaje claro."""
    home = os.environ.get("JAVA_HOME", "").strip().strip('"')
    java = Path(home) / "bin" / ("java.exe" if os.name == "nt" else "java") if home else None
    if java is not None and not java.is_file():
        sys.exit(f"[ERROR] JAVA_HOME apunta a '{home}', pero ahi no hay un JDK (falta {java}). "
                 "Corrige JAVA_HOME para que apunte a la carpeta de un JDK 21.")
    executable = str(java) if java else shutil.which("java")
    if not executable:
        sys.exit("[ERROR] No se encuentra Java. Instala un JDK 21 y define JAVA_HOME con su carpeta "
                 "(o anade su carpeta bin al PATH).")
    result = subprocess.run([executable, "-version"], capture_output=True, text=True, check=False)
    output = result.stderr + result.stdout
    match = re.search(r'version "(\d+)(?:\.(\d+))?', output)
    major = int(match.group(1)) if match else 0
    if major == 1 and match.group(2):
        major = int(match.group(2))
    if major < 21:
        found = output.strip().splitlines()[0] if output.strip() else "version desconocida"
        sys.exit(f"[ERROR] Hace falta un JDK 21 o superior y se ha encontrado: {found} ({executable}). "
                 "Instala un JDK 21 y apunta JAVA_HOME a el.")
    if not home:
        print(f"[AVISO] JAVA_HOME no esta definido; se usara {executable}. Si el build falla al arrancar "
              "Maven, define JAVA_HOME con la carpeta del JDK 21.")


def maven(backend: Path, log: Path) -> bool:
    """Compila el proyecto de backend/ (donde esta el pom.xml), sea cual sea el directorio actual."""
    if not (backend / "pom.xml").is_file():
        log.write_text(f"No existe {backend / 'pom.xml'}\n", encoding="utf-8")
        return False
    with log.open("w", encoding="utf-8") as output:
        result = subprocess.run(maven_command(backend), cwd=backend, stdout=output,
                                stderr=subprocess.STDOUT, check=False)
    return result.returncode == 0


def tail(log: Path) -> str:
    return "\n".join(log.read_text(encoding="utf-8", errors="replace").splitlines()[-LOG_TAIL:])


def build(entry: Entry, ai_fix: int) -> bool:
    backend, log = entry.target / "backend", entry.target / ".ai" / "build.log"
    print(f"  build: mvnw clean verify en {backend} ...")
    if maven(backend, log):
        print("  build: correcto")
        return True
    for attempt in range(1, ai_fix + 1):
        print(f"  build: ha fallado; la IA lo revisa (intento {attempt} de {ai_fix})")
        prompt = (PROMPTS / "fix-build.md").read_text(encoding="utf-8") + "\n\n```\n" + tail(log) + "\n```\n"
        if not claude(prompt, entry.target):
            break
        if maven(backend, log):
            print("  build: correcto tras la correccion")
            return True
    lines = [line for line in tail(log).splitlines() if line.strip()]
    errors = [line for line in lines if "ERROR" in line or "FAIL" in line][:15]
    # Si Maven no llego a arrancar no hay lineas [ERROR]: se muestra el final del registro tal cual.
    print("\n".join("    " + line for line in (errors or lines[-15:] or ["(el registro esta vacio)"])))
    print(f"  [ERROR] El build falla. Registro completo en {log}")
    return False


def push(entry: Entry) -> bool:
    def git(*args: str) -> bool:
        return subprocess.run(["git", *args], cwd=entry.target, check=False).returncode == 0

    if not (entry.target / ".git").exists() and not (git("init", "-b", "main")
                                                     and git("remote", "add", "origin", entry.remote)):
        return False
    git("add", "-A")
    if subprocess.run(["git", "diff", "--cached", "--quiet"], cwd=entry.target, check=False).returncode == 0:
        print("  git: no hay cambios que subir")
        return True
    message = f"feat: generate {entry.name} from database schema"
    return git("commit", "-q", "-m", message) and git("push", "-u", "origin", "main")


def remote_exists(entry: Entry) -> bool:
    return subprocess.run(["git", "ls-remote", entry.remote], capture_output=True, check=False).returncode == 0


def context(entry: Entry, mode: str) -> dict[str, str]:
    return {"SERVICE_NAME": entry.name, "REMOTE_URL": entry.remote, "TARGET_DIR": str(entry.target),
            "GENERATION_MODE": mode}


def run_template(entry: Entry, args: argparse.Namespace) -> bool:
    digest = fingerprint(ROOT, entry.ddl, entry.name, entry.port)
    if is_current(entry.target, digest) and not args.force:
        print("  sin cambios en el DDL ni en el generador: se omite (usa --force para regenerar)")
        generated = False
    else:
        service = write_project(ROOT, entry.name, entry.ddl, entry.target, entry.port, context(entry, "template"))
        print(f"  generado: {len(service.tables)} tablas en {entry.target}")
        generated = True

    rules = ROOT / "specs" / "business-rules" / f"{entry.name}.md"
    if args.ai_rules and rules.is_file() and generated:
        print(f"  reglas de negocio: la IA aplica {rules.relative_to(ROOT)}")
        prompt = (PROMPTS / "business-rules.md").read_text(encoding="utf-8") + "\n\n" + rules.read_text("utf-8")
        if not claude(prompt, entry.target):
            return False
    if args.build and not build(entry, args.ai_fix):
        return False
    return not args.push or push(entry)


def run_ai(entry: Entry, args: argparse.Namespace) -> bool:
    """Pipeline antiguo: la IA escribe todo el servicio fase a fase (ver .ai/prompts/00..05)."""
    target = entry.target
    (target / ".ai").mkdir(parents=True, exist_ok=True)
    (target / "specs").mkdir(exist_ok=True)
    shutil.copy2(ROOT / "CLAUDE.md", target / "CLAUDE.md")
    shutil.copytree(ROOT / ".claude", target / ".claude", dirs_exist_ok=True)
    shutil.copytree(PROMPTS, target / ".ai" / "prompts", dirs_exist_ok=True)
    for name in ("checkstyle.xml", ".editorconfig"):
        shutil.copy2(ROOT / "templates" / name, target / name)
    shutil.copy2(ROOT / "templates" / "service.gitignore", target / ".gitignore")
    shutil.copytree(ROOT / "templates" / "plans", target / "plans", dirs_exist_ok=True)
    shutil.copy2(entry.ddl, target / "specs" / "database-spec.sql")
    mode = "api-first" if entry.spec else "db-first"
    if entry.spec:
        shutil.copy2(ROOT / entry.spec, target / "specs" / "crud-spec.yaml")
    (target / ".ai" / "project-context.txt").write_text(
        "".join(f"{key}={value}\n" for key, value in context(entry, mode).items()), encoding="utf-8")
    phases = (["00-db-to-openapi.md"] if mode == "db-first" else []) + AI_PHASES
    for phase in phases:
        print(f"  fase {phase}")
        if not claude((PROMPTS / phase).read_text(encoding="utf-8"), target):
            print(f"  [ERROR] La fase {phase} ha fallado.")
            return False
    return True


def write_openapi(entries: list[Entry]) -> None:
    for entry in entries:
        path = ROOT / "specs" / "openapi" / f"{entry.ddl.stem}.yaml"
        path.write_text(openapi.render(load_service(entry.name, entry.ddl), entry.port), encoding="utf-8",
                        newline="\n")
        print(f"  {path.relative_to(ROOT)}")


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    parser.add_argument("-s", "--service", action="append", help="servicio a generar (por defecto, todos)")
    parser.add_argument("--mode", choices=["template", "ai"], default="template")
    parser.add_argument("--force", action="store_true", help="regenera aunque no haya cambios")
    parser.add_argument("--build", action="store_true", help="ejecuta mvnw clean verify tras generar")
    parser.add_argument("--ai-fix", type=int, default=0, metavar="N",
                        help="si el build falla, deja que la IA lo corrija hasta N veces")
    parser.add_argument("--ai-rules", action="store_true",
                        help="aplica con IA specs/business-rules/<servicio>.md si existe")
    parser.add_argument("--push", action="store_true", help="commit y push al remoto del servicio")
    parser.add_argument("--create-repos", action="store_true",
                        help="crea en GitHub el repositorio del servicio si no existe (necesita GITHUB_TOKEN)")
    parser.add_argument("--public", action="store_true",
                        help="con --create-repos, crea los repositorios publicos (por defecto, privados)")
    parser.add_argument("--openapi", action="store_true", help="solo regenera specs/openapi/*.yaml")
    args = parser.parse_args()

    entries = read_manifest()
    if args.service:
        unknown = set(args.service) - {entry.name for entry in entries}
        if unknown:
            sys.exit("[ERROR] No estan en el manifiesto: " + ", ".join(sorted(unknown)))
        entries = [entry for entry in entries if entry.name in args.service]
    if not entries:
        print("[AVISO] El manifiesto no tiene ninguna linea activa.")
        return 0
    if args.openapi:
        write_openapi(entries)
        return 0

    needs_claude = args.mode == "ai" or args.ai_fix or args.ai_rules
    if needs_claude and shutil.which("claude") is None:
        sys.exit("[ERROR] No se encuentra la CLI 'claude' en el PATH.")
    if (args.push or args.mode == "ai") and shutil.which("git") is None:
        sys.exit("[ERROR] No se encuentra 'git' en el PATH.")
    if args.build and args.mode == "template":
        check_java()
    github_token = None
    if args.create_repos:
        try:
            github_token = create_repos.token()
        except create_repos.GitHubError as error:
            sys.exit(f"[ERROR] {error}")

    failed = []
    for entry in entries:
        print(f"\n== {entry.name} [{args.mode}] -> {entry.target}")
        if not entry.ddl.is_file():
            print(f"  [ERROR] No existe el esquema {entry.ddl}")
            failed.append(entry.name)
            continue
        if ROOT == entry.target or ROOT in entry.target.parents:
            print("  [ERROR] TARGET_DIR queda dentro del generador: un servicio = un repositorio.")
            failed.append(entry.name)
            continue
        if github_token:
            try:
                state = create_repos.ensure_repo(
                    entry.remote, github_token, private=not args.public,
                    description=f"{entry.name}: API CRUD generada desde el esquema de base de datos")
                print(f"  repositorio: {state} ({entry.remote})")
            except create_repos.GitHubError as error:
                print(f"  [ERROR] {error}")
                failed.append(entry.name)
                continue
        if (args.push or args.mode == "ai") and not remote_exists(entry):
            print(f"  [ERROR] El remoto {entry.remote} no existe o no es accesible. Crealo antes de generar.")
            failed.append(entry.name)
            continue
        entry.target.mkdir(parents=True, exist_ok=True)
        ok = run_ai(entry, args) if args.mode == "ai" else run_template(entry, args)
        if not ok:
            failed.append(entry.name)

    print(f"\nServicios procesados: {len(entries)}. Fallidos: {len(failed)}"
          + (f" ({', '.join(failed)})" if failed else "") + ".")
    return 1 if failed else 0


if __name__ == "__main__":
    sys.exit(main())
