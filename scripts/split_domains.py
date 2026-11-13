#!/usr/bin/env python3
"""Regenera specs/domains/<dominio>.sql recortando specs/supabase-schema.sql segun specs/domain-map.txt.

Cada tabla se copia tal cual del export (columnas, tipos, defectos, CHECK, PK, UNIQUE). Lo unico que se
omite son las claves foraneas hacia tablas que no pertenecen al servicio (otro dominio o auth.users),
porque cada servicio tiene su propia base en local; quedan listadas en la cabecera del fichero.

Uso:
  python scripts/split_domains.py           # reescribe los dominios
  python scripts/split_domains.py --check   # no escribe; devuelve 1 si algun dominio esta desfasado
"""
from __future__ import annotations

import argparse
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(ROOT))

from generator.model import Table, parse_tables  # noqa: E402

EXPORT = ROOT / "specs" / "supabase-schema.sql"
MAP = ROOT / "specs" / "domain-map.txt"
DOMAINS = ROOT / "specs" / "domains"


def read_map() -> dict[str, list[str]]:
    domains: dict[str, list[str]] = {}
    current = None
    for raw in MAP.read_text(encoding="utf-8").splitlines():
        line = raw.split("#", 1)[0].strip()
        if not line:
            continue
        start = re.match(r"^([a-z][a-z0-9-]*):(.*)$", line)
        if start:
            current, line = start.group(1), start.group(2)
            domains.setdefault(current, [])
        if current is None:
            raise SystemExit(f"[ERROR] Linea sin dominio en domain-map.txt: {raw}")
        domains[current] += [name.strip() for name in line.split(",") if name.strip()]
    return domains


def ordered(tables: list[Table]) -> list[Table]:
    """Orden topologico: una tabla va despues de las que referencia dentro del dominio."""
    names = {t.name for t in tables}
    pending, result = list(tables), []
    while pending:
        placed = {t.name for t in result}
        ready = [t for t in pending
                 if all(fk.ref_table in placed or fk.ref_table == t.name or fk.ref_table not in names for fk in t.fks)]
        if not ready:
            raise SystemExit("[ERROR] Dependencia circular entre: " + ", ".join(t.name for t in pending))
        result.append(ready[0])
        pending.remove(ready[0])
    return result


def render(domain: str, tables: list[Table]) -> str:
    names = {t.name for t in tables}
    omitted, blocks = [], []
    for table in ordered(tables):
        external = {fk.name for fk in table.fks if fk.ref_table not in names}
        omitted += [f"{table.name}.{fk.name} -> {fk.ref_table}({', '.join(fk.ref_columns)})"
                    for fk in table.fks if fk.name in external]
        lines = [line for line in table.lines
                 if not any(re.match(rf'CONSTRAINT\s+"?{re.escape(name)}"?\s', line) for name in external)]
        blocks.append(f"CREATE TABLE {table.name} (\n" + ",\n".join("    " + line for line in lines) + "\n);\n")
    header = [
        f"-- database: {domain.replace('-', '_')}_db",
        f"-- Dominio {domain}: {len(tables)} tablas copiadas tal cual de specs/supabase-schema.sql.",
        "-- Generado por scripts/split_domains.py a partir de specs/domain-map.txt. No editar a mano.",
    ]
    if omitted:
        header.append("-- Claves foraneas omitidas (apuntan a tablas de fuera del servicio; en Supabase si existen):")
        header += [f"--   {line}" for line in omitted]
    return "\n".join(header) + "\n\n" + "\n".join(blocks)


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    parser.add_argument("--check", action="store_true", help="no escribe; devuelve 1 si hay dominios desfasados")
    args = parser.parse_args()

    export = {t.name: t for t in parse_tables(EXPORT.read_text(encoding="utf-8"))}
    domains = read_map()
    assigned: dict[str, str] = {}
    errors = []
    for domain, names in domains.items():
        for name in names:
            if name not in export:
                errors.append(f"{domain}: la tabla '{name}' no existe en el export")
            elif name in assigned:
                errors.append(f"la tabla '{name}' esta en {assigned[name]} y en {domain}")
            else:
                assigned[name] = domain
    errors += [f"la tabla '{name}' del export no esta asignada a ningun dominio"
               for name in export if name not in assigned]
    if errors:
        print("\n".join(f"[ERROR] {error}" for error in errors))
        return 1

    stale = []
    for domain, names in domains.items():
        content = render(domain, [export[name] for name in names])
        path = DOMAINS / f"{domain}.sql"
        if not path.is_file() or path.read_text(encoding="utf-8") != content:
            stale.append(path.name)
            if not args.check:
                path.write_text(content, encoding="utf-8", newline="\n")
    extra = sorted(p.name for p in DOMAINS.glob("*.sql") if p.stem not in domains)
    if extra:
        print("[ERROR] Dominios sin entrada en domain-map.txt: " + ", ".join(extra))
        return 1
    if args.check:
        print("Dominios desfasados: " + ", ".join(stale) if stale else f"{len(domains)} dominios al dia.")
        return 1 if stale else 0
    print(f"{len(domains)} dominios, {len(assigned)} tablas. Reescritos: {', '.join(stale) if stale else 'ninguno'}.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
