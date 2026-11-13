#!/usr/bin/env python3
"""Compara los DDL de specs/domains/*.sql con el export de Supabase (specs/supabase-schema.sql).

Por cada tabla de cada dominio comprueba que existe en Supabase y que sus columnas (nombre, tipo,
NOT NULL y valor por defecto) son identicas. Al final lista las tablas de Supabase sin dominio.
Las claves foraneas hacia fuera del servicio se omiten a proposito en los dominios y no cuentan.

Uso:
  python scripts/check_domains.py        # informe; devuelve 1 si hay diferencias
"""
from __future__ import annotations

import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(ROOT))

from generator.model import parse_tables  # noqa: E402

EXPORT = ROOT / "specs" / "supabase-schema.sql"
DOMAINS = ROOT / "specs" / "domains"


def closest(columns: set[str], supabase: dict) -> tuple[str, int]:
    def overlap(name: str) -> float:
        other = {c.name for c in supabase[name].columns}
        return len(other & columns) / max(len(other | columns), 1)
    best = max(supabase, key=overlap)
    return best, len({c.name for c in supabase[best].columns} & columns)


def main() -> int:
    supabase = {t.name: t for t in parse_tables(EXPORT.read_text(encoding="utf-8"))}
    owned: dict[str, str] = {}
    differences = 0

    for path in sorted(DOMAINS.glob("*.sql")):
        print(f"\n### {path.stem}")
        for table in parse_tables(path.read_text(encoding="utf-8")):
            if table.name in owned:
                differences += 1
                print(f"  {table.name}: REPETIDA (ya esta en el dominio {owned[table.name]})")
                continue
            if table.name not in supabase:
                differences += 1
                names = {c.name for c in table.columns}
                best, common = closest(names, supabase)
                print(f"  {table.name}: NO EXISTE en Supabase "
                      f"(la mas parecida es {best}: {common} de {len(names)} columnas en comun)")
                continue
            owned[table.name] = path.stem
            real = {c.name: c for c in supabase[table.name].columns}
            mine = {c.name: c for c in table.columns}
            missing = [name for name in real if name not in mine]
            extra = [name for name in mine if name not in real]
            changed = [f"{name}: [{real[name].raw}] -> [{mine[name].raw}]" for name in mine
                       if name in real and " ".join(real[name].raw.split()) != " ".join(mine[name].raw.split())]
            if supabase[table.name].pk != table.pk:
                changed.append(f"clave primaria: {supabase[table.name].pk} -> {table.pk}")
            if not (missing or extra or changed):
                print(f"  {table.name}: igual")
                continue
            differences += 1
            print(f"  {table.name}: DISTINTA")
            if missing:
                print(f"     faltan columnas: {', '.join(missing)}")
            if extra:
                print(f"     columnas de mas: {', '.join(extra)}")
            for change in changed:
                print(f"     cambia (Supabase -> dominio) {change}")

    orphans = sorted(set(supabase) - set(owned))
    print(f"\n### Tablas de Supabase sin dominio ({len(orphans)})")
    print("  " + (", ".join(orphans) if orphans else "ninguna"))
    print(f"\n{len(supabase)} tablas en el export, {len(owned)} asignadas a un dominio, "
          f"{differences} tabla(s) de dominio con diferencias, {len(orphans)} sin dominio.")
    return 1 if differences or orphans else 0


if __name__ == "__main__":
    sys.exit(main())
