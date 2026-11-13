#!/usr/bin/env python3
"""Crea en GitHub los repositorios de los servicios del manifiesto (.ai/services.txt) que aun no existan.

Usa la API REST de GitHub con la libreria estandar. Necesita un token en la variable de entorno
GITHUB_TOKEN (o GH_TOKEN) con permiso para crear repositorios: es un secreto, nunca va en el repositorio.

Uso:
  python scripts/create_repos.py                     # crea los que falten (privados)
  python scripts/create_repos.py -s access-service   # solo uno (se puede repetir -s)
  python scripts/create_repos.py --public            # los crea publicos
  python scripts/create_repos.py --dry-run           # solo dice cuales existen y cuales crearia

Para crear y subir en un paso: python scripts/generate.py --build --create-repos --push
"""
from __future__ import annotations

import argparse
import json
import os
import re
import sys
import urllib.error
import urllib.request
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
MANIFEST = ROOT / ".ai" / "services.txt"
API = os.environ.get("GITHUB_API_URL", "https://api.github.com").rstrip("/")
REMOTE_RE = re.compile(r"^(?:https://github\.com/|git@github\.com:)([\w.-]+)/([\w.-]+?)(?:\.git)?/?$")


class GitHubError(Exception):
    """Fallo al hablar con GitHub, con un mensaje listo para mostrar."""


def token() -> str:
    value = (os.environ.get("GITHUB_TOKEN") or os.environ.get("GH_TOKEN") or "").strip()
    if not value:
        raise GitHubError("Define GITHUB_TOKEN (o GH_TOKEN) con un token de GitHub que pueda crear repositorios.")
    return value


def parse_remote(remote: str) -> tuple[str, str]:
    match = REMOTE_RE.match(remote.strip())
    if not match:
        raise GitHubError(f"'{remote}' no es una URL de repositorio de github.com.")
    return match.group(1), match.group(2)


def request(method: str, path: str, auth: str, body: dict | None = None) -> tuple[int, dict]:
    data = json.dumps(body).encode() if body is not None else None
    call = urllib.request.Request(API + path, data=data, method=method, headers={
        "Authorization": f"Bearer {auth}",
        "Accept": "application/vnd.github+json",
        "X-GitHub-Api-Version": "2022-11-28",
        "User-Agent": "ai-crud-generator",
        "Content-Type": "application/json",
    })
    try:
        with urllib.request.urlopen(call, timeout=30) as response:
            return response.status, json.loads(response.read() or b"{}")
    except urllib.error.HTTPError as error:
        try:
            payload = json.loads(error.read() or b"{}")
        except ValueError:
            payload = {}
        return error.code, payload
    except urllib.error.URLError as error:
        raise GitHubError(f"No se puede conectar con {API}: {error.reason}") from error


def describe(status: int, payload: dict) -> str:
    details = "; ".join(str(item.get("message", item)) for item in payload.get("errors", []) if item)
    message = payload.get("message", "sin detalle")
    return f"HTTP {status}: {message}" + (f" ({details})" if details else "")


def ensure_repo(remote: str, auth: str, private: bool = True, description: str = "",
                dry_run: bool = False) -> str:
    """Devuelve 'existe', 'creado' o 'se crearia'. Lanza GitHubError si no se puede crear."""
    owner, name = parse_remote(remote)
    status, payload = request("GET", f"/repos/{owner}/{name}", auth)
    if status == 200:
        return "existe"
    if status == 401:
        raise GitHubError("GitHub rechaza el token (401). Comprueba que es valido y no ha caducado.")
    if status != 404:
        raise GitHubError(f"No se pudo consultar {owner}/{name}: {describe(status, payload)}")
    if dry_run:
        return "se crearia"

    status, user = request("GET", "/user", auth)
    if status != 200:
        raise GitHubError(f"No se pudo identificar al usuario del token: {describe(status, user)}")
    # El repositorio va a la cuenta del token o, si el propietario es otro, a esa organizacion.
    own_account = user.get("login", "").lower() == owner.lower()
    path = "/user/repos" if own_account else f"/orgs/{owner}/repos"
    status, payload = request("POST", path, auth, {
        "name": name, "private": private, "description": description, "auto_init": False})
    if status == 201:
        return "creado"
    hint = ""
    if status in (403, 404):
        hint = (" El token necesita permiso para crear repositorios"
                + ("" if own_account else f" en la organizacion '{owner}'") + ".")
    raise GitHubError(f"No se pudo crear {owner}/{name}: {describe(status, payload)}.{hint}")


def manifest_entries() -> list[tuple[str, str]]:
    entries = []
    for raw in MANIFEST.read_text(encoding="utf-8").splitlines():
        line = raw.strip()
        if line and not line.startswith("#"):
            fields = [field.strip() for field in line.split(";")]
            if len(fields) >= 3:
                entries.append((fields[0], fields[2]))
    return entries


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    parser.add_argument("-s", "--service", action="append", help="servicio (por defecto, todos)")
    parser.add_argument("--public", action="store_true", help="crea los repositorios publicos (por defecto, privados)")
    parser.add_argument("--dry-run", action="store_true", help="no crea nada; solo informa")
    args = parser.parse_args()

    entries = manifest_entries()
    if args.service:
        unknown = set(args.service) - {name for name, _ in entries}
        if unknown:
            sys.exit("[ERROR] No estan en el manifiesto: " + ", ".join(sorted(unknown)))
        entries = [entry for entry in entries if entry[0] in args.service]

    try:
        auth = token()
    except GitHubError as error:
        sys.exit(f"[ERROR] {error}")

    failed = 0
    for name, remote in entries:
        try:
            result = ensure_repo(remote, auth, private=not args.public, dry_run=args.dry_run,
                                 description=f"{name}: API CRUD generada desde el esquema de base de datos")
            print(f"{name}: {result} ({remote})")
        except GitHubError as error:
            failed += 1
            print(f"{name}: [ERROR] {error}")
    print(f"{len(entries)} repositorio(s) revisados, {failed} con error.")
    return 1 if failed else 0


if __name__ == "__main__":
    sys.exit(main())
