"""Escribe el proyecto completo de un servicio en su carpeta destino."""
from __future__ import annotations

import hashlib
import re
import shutil
from pathlib import Path

from . import docs, fixed, java, openapi, resources
from .model import Service, load_service

GENERATOR_DIR = Path(__file__).resolve().parent
STATE_HASH = ".ai/generated.sha"
STATE_FILES = ".ai/generated-files.txt"


def supabase_ref(root: Path) -> str | None:
    export = root / "specs" / "supabase-schema.sql"
    if not export.is_file():
        return None
    match = re.search(r"Proyecto:\s*([a-z0-9]{12,})", export.read_text(encoding="utf-8")[:2000])
    return match.group(1) if match else None


def build_files(s: Service, root: Path, port: int) -> dict[str, str]:
    """Todos los ficheros de texto generados: ruta relativa a la carpeta del servicio -> contenido."""
    files = {}
    files.update(resources.resource_files(s, port, supabase_ref(root)))
    files.update(fixed.fixed_sources(s))
    files.update(java.java_sources(s))
    files["docs/index.html"] = docs.index_html(s, port)
    files["README.md"] = docs.service_readme(s, port)
    files["CLAUDE.md"] = docs.service_claude(s)
    files["specs/crud-spec.yaml"] = openapi.render(s, port)
    files["specs/PRUEBAS-CURL-CRUD.html"] = docs.curl_html(s, port)
    return files


def fingerprint(root: Path, ddl: Path, name: str, port: int) -> str:
    """Huella de todo lo que determina la salida: DDL, generador, plantillas y parametros."""
    digest = hashlib.sha256(f"{name}:{port}".encode())
    inputs = [ddl, root / "CLAUDE.md", root / "specs" / "supabase-schema.sql"]
    inputs += sorted(GENERATOR_DIR.glob("*.py"))
    inputs += sorted(p for p in (root / "templates").rglob("*") if p.is_file() and "plans" not in p.parts)
    inputs += sorted(p for p in (root / ".claude").rglob("*") if p.is_file())
    for path in inputs:
        if path.is_file():
            digest.update(path.relative_to(root).as_posix().encode() if root in path.parents else path.name.encode())
            digest.update(path.read_bytes())
    return digest.hexdigest()


def is_current(target: Path, digest: str) -> bool:
    state = target / STATE_HASH
    return state.is_file() and state.read_text(encoding="utf-8").strip() == digest


def _remove_legacy_backend(target: Path) -> None:
    """Borra la carpeta backend/ de la disposicion antigua si solo quedan en ella restos generados."""
    legacy = target / "backend"
    if not legacy.is_dir():
        return
    leftovers = {"mvnw", "mvnw.cmd", ".mvn", "target", "src", "docs"}
    own_files = [p for p in legacy.rglob("*") if p.is_file()
                 and p.relative_to(legacy).parts[0] not in {"mvnw", "mvnw.cmd", ".mvn", "target"}]
    if not own_files and all(child.name in leftovers for child in legacy.iterdir()):
        shutil.rmtree(legacy)


def write_project(root: Path, name: str, ddl: Path, target: Path, port: int, context: dict[str, str]) -> Service:
    """Genera el servicio en `target`. Solo toca los ficheros que genera: lo escrito a mano se conserva."""
    s = load_service(name, ddl)
    files = build_files(s, root, port)

    previous = target / STATE_FILES
    if previous.is_file():
        for stale in set(previous.read_text(encoding="utf-8").split("\n")) - set(files):
            path = target / stale
            if stale and path.is_file():
                path.unlink()

    for relative, content in files.items():
        path = target / relative
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(content, encoding="utf-8", newline="\n")

    _remove_legacy_backend(target)

    # Copias literales: reglas y skills, plantillas comunes, wrapper de Maven y el DDL del servicio.
    if (target / ".claude").exists():
        shutil.rmtree(target / ".claude")
    shutil.copytree(root / ".claude", target / ".claude")
    templates = root / "templates"
    shutil.copy2(templates / "checkstyle.xml", target / "checkstyle.xml")
    shutil.copy2(templates / ".editorconfig", target / ".editorconfig")
    shutil.copy2(templates / "service.gitignore", target / ".gitignore")
    shutil.copytree(templates / "maven-wrapper", target, dirs_exist_ok=True)
    (target / "mvnw").chmod(0o755)
    (target / "specs").mkdir(exist_ok=True)
    shutil.copy2(ddl, target / "specs" / "database-spec.sql")

    (target / ".ai").mkdir(exist_ok=True)
    (target / ".ai" / "project-context.txt").write_text(
        "".join(f"{key}={value}\n" for key, value in context.items()), encoding="utf-8", newline="\n")
    (target / STATE_FILES).write_text("\n".join(sorted(files)) + "\n", encoding="utf-8", newline="\n")
    (target / STATE_HASH).write_text(fingerprint(root, ddl, name, port) + "\n", encoding="utf-8", newline="\n")
    return s
