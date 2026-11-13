#!/usr/bin/env python3
"""Compara los DDL de specs/domains/*.sql con el export de Supabase (specs/supabase-schema.sql).

Por cada tabla de cada dominio indica si existe en Supabase y si coinciden columnas y tipos.
Al final lista las tablas de Supabase que no pertenecen a ningun dominio.
No compara restricciones (CHECK, FK, UNIQUE) ni valores por defecto.

Uso:
  python scripts/check_domains.py        # informe; devuelve 1 si hay diferencias
"""
from __future__ import annotations

import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
EXPORT = ROOT / "specs" / "supabase-schema.sql"
DOMAINS = ROOT / "specs" / "domains"

TABLE = re.compile(r'CREATE TABLE\s+(?:IF NOT EXISTS\s+)?(?:public\.)?"?(\w+)"?\s*\((.*?)\n\);', re.S | re.I)
NOT_A_COLUMN = re.compile(r"(CONSTRAINT|PRIMARY|UNIQUE|FOREIGN|CHECK)\b", re.I)
END_OF_TYPE = re.compile(r"\b(NOT NULL|NULL|DEFAULT|PRIMARY|REFERENCES|CHECK|UNIQUE|GENERATED|CONSTRAINT)\b", re.I)
SYNONYMS = {
    "character varying": "varchar",
    "timestamp with time zone": "timestamptz",
    "timestamp without time zone": "timestamp",
    "time without time zone": "time",
}


def split_top_level(body: str) -> list[str]:
    """Parte por comas que no esten dentro de parentesis."""
    parts, depth, current = [], 0, ""
    for char in body:
        depth += (char == "(") - (char == ")")
        if char == "," and depth == 0:
            parts.append(current)
            current = ""
        else:
            current += char
    return parts + [current]


def parse(path: Path) -> dict[str, dict[str, str]]:
    """Devuelve {tabla: {columna: tipo}} de un fichero de DDL."""
    text = re.sub(r"--[^\n]*", "", path.read_text(encoding="utf-8"))
    tables: dict[str, dict[str, str]] = {}
    for match in TABLE.finditer(text):
        columns: dict[str, str] = {}
        for part in split_top_level(match.group(2)):
            part = " ".join(part.split())
            if not part or NOT_A_COLUMN.match(part):
                continue
            name, _, rest = part.partition(" ")
            columns[name.strip('"')] = END_OF_TYPE.split(rest, maxsplit=1)[0].strip().lower()
        tables[match.group(1)] = columns
    return tables


def normalize(sql_type: str) -> str:
    for long_name, short_name in SYNONYMS.items():
        sql_type = sql_type.replace(long_name, short_name)
    return re.sub(r"\s+", "", sql_type)


def closest(columns: dict[str, str], supabase: dict[str, dict[str, str]]) -> tuple[str, int]:
    def overlap(table: str) -> float:
        return len(set(supabase[table]) & set(columns)) / max(len(set(supabase[table]) | set(columns)), 1)
    best = max(supabase, key=overlap)
    return best, len(set(supabase[best]) & set(columns))


def main() -> int:
    supabase = parse(EXPORT)
    owned: dict[str, str] = {}
    differences = 0

    for path in sorted(DOMAINS.glob("*.sql")):
        print(f"\n### {path.stem}")
        for table, columns in parse(path).items():
            if table in owned:
                differences += 1
                print(f"  {table}: REPETIDA (ya esta en el dominio {owned[table]})")
                continue
            if table not in supabase:
                differences += 1
                best, common = closest(columns, supabase)
                print(f"  {table}: NO EXISTE en Supabase "
                      f"(la mas parecida es {best}: {common} de {len(columns)} columnas en comun)")
                continue
            owned[table] = path.stem
            real = supabase[table]
            missing = [column for column in real if column not in columns]
            extra = [column for column in columns if column not in real]
            types = [f"{column}: {real[column]} -> {columns[column]}" for column in columns
                     if column in real and normalize(real[column]) != normalize(columns[column])]
            if not (missing or extra or types):
                print(f"  {table}: igual")
                continue
            differences += 1
            print(f"  {table}: DISTINTA")
            if missing:
                print(f"     faltan columnas: {', '.join(missing)}")
            if extra:
                print(f"     columnas de mas: {', '.join(extra)}")
            if types:
                print(f"     tipos (Supabase -> dominio): {'; '.join(types)}")

    orphans = sorted(set(supabase) - set(owned))
    print(f"\n### Tablas de Supabase sin dominio ({len(orphans)})")
    print("  " + (", ".join(orphans) if orphans else "ninguna"))
    print(f"\n{len(supabase)} tablas en el export, {len(owned)} asignadas a un dominio, "
          f"{differences} tabla(s) de dominio con diferencias, {len(orphans)} sin dominio.")
    return 1 if differences or orphans else 0


if __name__ == "__main__":
    sys.exit(main())
