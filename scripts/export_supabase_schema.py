#!/usr/bin/env python3
"""Exporta el esquema "public" de Supabase a specs/supabase-schema.sql.

Lee solo el catalogo de PostgreSQL (scripts/export-schema.sql); no modifica la base ni lee datos.

Requisitos:
  * psql en el PATH (cliente de PostgreSQL).
  * Variable de entorno SUPABASE_DB_URL con la cadena de conexion, p. ej.
      postgresql://postgres:<password>@db.<ref>.supabase.co:5432/postgres
    Es un secreto: definela en el entorno o en un .env local, nunca en el repositorio.

Uso:
  python scripts/export_supabase_schema.py            # sobrescribe specs/supabase-schema.sql
  python scripts/export_supabase_schema.py --check    # no escribe; falla si el fichero esta desfasado
  python scripts/export_supabase_schema.py -o otro.sql

Despues, compara los dominios con el export: python scripts/check_domains.py
"""
from __future__ import annotations

import argparse
import os
import re
import shutil
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
QUERY = ROOT / "scripts" / "export-schema.sql"
DEFAULT_OUTPUT = ROOT / "specs" / "supabase-schema.sql"
RULE = "-- " + "=" * 69


def project_ref(url: str) -> str:
    """Saca la referencia del proyecto de la URL (host db.<ref>.supabase.co o usuario postgres.<ref>)."""
    match = re.search(r"@db\.([a-z0-9]+)\.supabase\.co", url) or re.search(r"//postgres\.([a-z0-9]+)[:@]", url)
    return match.group(1) if match else "desconocido"


def export(url: str) -> str:
    result = subprocess.run(
        ["psql", url, "-X", "-q", "-A", "-t", "-v", "ON_ERROR_STOP=1", "-f", str(QUERY)],
        capture_output=True, text=True, encoding="utf-8", check=False,
    )
    if result.returncode != 0:
        # No se imprime la URL: contiene la contrasena.
        sys.exit(f"[ERROR] psql ha fallado:\n{result.stderr.strip()}")
    body = result.stdout.strip() + "\n"
    tables = len(re.findall(r"^CREATE TABLE ", body, re.MULTILINE))
    if tables == 0:
        sys.exit("[ERROR] El export no contiene ninguna tabla en el schema public.")
    header = "\n".join([
        RULE,
        "--  Export de esquema PostgreSQL (Supabase)",
        f"--  Proyecto: {project_ref(url)}  ·  Schema: public  ·  {tables} tablas",
        "--  Generado con scripts/export_supabase_schema.py desde pg_catalog",
        "--  (columnas, tipos, identidad, PK/FK, UNIQUE, CHECK). No editar a mano.",
        "--  Nota: varias FK referencian auth.users(id) (schema de Supabase Auth),",
        "--        que no forma parte de este export.",
        RULE,
        "",
        "",
    ])
    return header + body


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    parser.add_argument("-o", "--output", type=Path, default=DEFAULT_OUTPUT, help="fichero de salida")
    parser.add_argument("--check", action="store_true",
                        help="no escribe; devuelve 1 si el fichero no coincide con la base")
    args = parser.parse_args()

    url = os.environ.get("SUPABASE_DB_URL", "").strip()
    if not url:
        sys.exit("[ERROR] Define SUPABASE_DB_URL con la cadena de conexion de Supabase (no la guardes en el repo).")
    if shutil.which("psql") is None:
        sys.exit("[ERROR] No se encuentra 'psql' en el PATH (instala el cliente de PostgreSQL).")

    ddl = export(url)
    tables = len(re.findall(r"^CREATE TABLE ", ddl, re.MULTILINE))

    if args.check:
        current = args.output.read_text(encoding="utf-8") if args.output.is_file() else ""
        if current == ddl:
            print(f"{args.output.name} esta al dia ({tables} tablas).")
            return 0
        print(f"[DESFASADO] {args.output.name} no coincide con la base ({tables} tablas). "
              "Ejecuta el script sin --check para actualizarlo.")
        return 1

    args.output.write_text(ddl, encoding="utf-8", newline="\n")
    print(f"{tables} tablas exportadas a {args.output}")
    print("Siguiente paso: python scripts/check_domains.py")
    return 0


if __name__ == "__main__":
    sys.exit(main())
