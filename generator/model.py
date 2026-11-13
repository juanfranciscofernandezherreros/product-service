"""Modelo de un servicio a partir de su DDL PostgreSQL (formato de specs/supabase-schema.sql).

Todo lo demas del generador (Java, OpenAPI, migraciones, documentacion, pruebas) sale de aqui,
de modo que el DDL es la unica fuente de verdad.
"""
from __future__ import annotations

import re
import uuid
from dataclasses import dataclass, field
from pathlib import Path

TABLE_RE = re.compile(r'CREATE TABLE\s+(?:IF NOT EXISTS\s+)?(?:public\.)?"?(\w+)"?\s*\((.*?)\n\);', re.S | re.I)
TYPE_RE = re.compile(
    r"^(timestamp with(?:out)? time zone|time with(?:out)? time zone|character varying(?:\(\d+\))?"
    r"|double precision|[a-z0-9_]+(?:\(\d+(?:\s*,\s*\d+)?\))?)(\[\])?",
    re.I,
)
CONSTRAINT_RE = re.compile(r'^CONSTRAINT\s+"?(\w+)"?\s+(.*)$', re.S | re.I)
FK_RE = re.compile(r"^FOREIGN KEY \(([^)]*)\) REFERENCES ([\w.]+)\s*\(([^)]*)\)(.*)$", re.S | re.I)

JAVA_KEYWORDS = {
    "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue",
    "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "for", "goto", "if",
    "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package", "private",
    "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this",
    "throw", "throws", "transient", "try", "void", "volatile", "while", "record", "var", "yield", "page", "size",
}

# tipo SQL normalizado -> (tipo Java, tipo OpenAPI, formato OpenAPI)
SCALARS = {
    "text": ("String", "string", None),
    "varchar": ("String", "string", None),
    "uuid": ("java.util.UUID", "string", "uuid"),
    "bigint": ("Long", "integer", "int64"),
    "integer": ("Integer", "integer", "int32"),
    "smallint": ("Short", "integer", "int32"),
    "boolean": ("Boolean", "boolean", None),
    "timestamptz": ("java.time.OffsetDateTime", "string", "date-time"),
    "timestamp": ("java.time.LocalDateTime", "string", "date-time"),
    "date": ("java.time.LocalDate", "string", "date"),
    "time": ("java.time.LocalTime", "string", None),
    "numeric": ("java.math.BigDecimal", "number", None),
    "double precision": ("Double", "number", "double"),
    "real": ("Float", "number", "float"),
    "jsonb": ("String", "object", None),
    "json": ("String", "object", None),
}
SYNONYMS = {
    "character varying": "varchar", "timestamp with time zone": "timestamptz",
    "timestamp without time zone": "timestamp", "time without time zone": "time",
    "int4": "integer", "int8": "bigint", "int2": "smallint", "bool": "boolean", "decimal": "numeric",
}
INTEGER_TYPES = {"bigint", "integer", "smallint"}
GENERATED_DEFAULTS = ("gen_random_uuid()", "uuid_generate_v4()", "nextval(")


def split_top_level(body: str) -> list[str]:
    """Parte por comas que no esten dentro de parentesis, corchetes ni cadenas."""
    parts, depth, current, quoted = [], 0, "", False
    for char in body:
        if char == "'":
            quoted = not quoted
        elif not quoted:
            depth += (char in "([") - (char in ")]")
        if char == "," and depth == 0 and not quoted:
            parts.append(current)
            current = ""
        else:
            current += char
    return parts + [current]


def camel(name: str) -> str:
    head, *rest = name.split("_")
    result = head + "".join(part[:1].upper() + part[1:] for part in rest)
    return result + "Value" if result in JAVA_KEYWORDS else result


def pascal(name: str) -> str:
    return "".join(part[:1].upper() + part[1:] for part in name.split("_"))


@dataclass
class Column:
    name: str
    raw: str                  # definicion tal cual aparece en el DDL
    sql_type: str             # tipo completo en minusculas, p. ej. "character varying(20)" o "text[]"
    base: str                 # tipo base normalizado, p. ej. "varchar", "timestamptz"
    is_array: bool
    length: int | None
    not_null: bool
    default: str | None
    identity: bool
    stored: bool              # columna generada (GENERATED ALWAYS AS (...) STORED)
    pk: bool = False
    generated: bool = False   # el valor lo pone la base o el servicio; no viaja en el alta
    fk_table: str | None = None

    @property
    def field(self) -> str:
        return camel(self.name)

    @property
    def is_json(self) -> bool:
        return self.base in ("jsonb", "json") and not self.is_array

    @property
    def java(self) -> str:
        scalar = SCALARS.get(self.base, SCALARS["text"])[0]
        return f"java.util.List<{scalar}>" if self.is_array else scalar

    @property
    def required(self) -> bool:
        """Obligatoria en la peticion: NOT NULL sin valor por defecto."""
        return self.not_null and self.default is None and not self.generated and not self.stored

    def sample(self, table: str, variant: int = 0):
        """Valor de ejemplo determinista (variant 0 = alta, 1 = modificacion, 2 = clave inexistente)."""
        value = self._scalar_sample(table, variant)
        return [value] if self.is_array else value

    def _scalar_sample(self, table: str, variant: int):
        base = self.base
        if base in ("text", "varchar"):
            text = f"{self.name.replace('_', '-')}-{'abc'[variant]}"
            return text[: self.length] if self.length else text
        if base == "uuid":
            return str(uuid.uuid5(uuid.NAMESPACE_URL, f"{table}.{self.name}.{variant}"))
        if base in INTEGER_TYPES:
            return (1, 2, 999999)[variant] if base != "smallint" else (1, 2, 9999)[variant]
        if base == "boolean":
            return variant != 1
        if base == "timestamptz":
            return ("2026-01-01T10:00:00Z", "2026-02-01T10:00:00Z", "2030-01-01T10:00:00Z")[variant]
        if base == "timestamp":
            return ("2026-01-01T10:00:00", "2026-02-01T10:00:00", "2030-01-01T10:00:00")[variant]
        if base == "date":
            return ("2026-01-01", "2026-02-01", "2030-01-01")[variant]
        if base == "time":
            return ("10:00:00", "11:00:00", "23:00:00")[variant]
        if base in ("numeric", "double precision", "real"):
            return (1.5, 2.5, 9.5)[variant]
        if base in ("jsonb", "json"):
            return {"ejemplo": variant}
        return f"{self.name}-{variant}"


@dataclass
class ForeignKey:
    name: str
    columns: list[str]
    ref_table: str
    ref_columns: list[str]
    raw: str


@dataclass
class Table:
    name: str
    columns: list[Column]
    pk: list[str]
    uniques: list[tuple[str, list[str]]] = field(default_factory=list)
    checks: list[tuple[str, str]] = field(default_factory=list)
    fks: list[ForeignKey] = field(default_factory=list)
    lines: list[str] = field(default_factory=list)   # lineas originales del cuerpo, sin coma final

    @property
    def cls(self) -> str:
        return pascal(self.name)

    @property
    def var(self) -> str:
        return camel(self.name)

    @property
    def path(self) -> str:
        return "/v1/" + self.name.replace("_", "-")

    def column(self, name: str) -> Column:
        return next(column for column in self.columns if column.name == name)

    @property
    def pk_columns(self) -> list[Column]:
        return [self.column(name) for name in self.pk]

    @property
    def generated_key(self) -> bool:
        return len(self.pk) == 1 and self.pk_columns[0].generated

    @property
    def create_columns(self) -> list[Column]:
        return [c for c in self.columns if not c.generated and not c.stored]

    @property
    def update_columns(self) -> list[Column]:
        return [c for c in self.columns if not c.pk and not c.generated and not c.stored]

    @property
    def filter_columns(self) -> list[Column]:
        """Columnas por las que se puede filtrar el listado: claves foraneas y partes de clave compuesta."""
        names = {name for fk in self.fks for name in fk.columns}
        if len(self.pk) > 1:
            names |= set(self.pk)
        names |= {c.name for c in self.columns if c.name.endswith("_id") and not (c.pk and len(self.pk) == 1)}
        return [c for c in self.columns if c.name in names and not c.is_array and not c.is_json]

    @property
    def item_path(self) -> str:
        return self.path + "".join("/{" + c.field + "}" for c in self.pk_columns)

    def body(self, variant: int, update: bool = False) -> dict:
        columns = self.update_columns if update else self.create_columns
        return {c.field: c.sample(self.name, variant) for c in columns}

    def resource(self, variant: int = 0) -> dict:
        return {c.field: c.sample(self.name, variant) for c in self.columns}


def parse_column(part: str) -> Column:
    name, _, rest = part.partition(" ")
    name = name.strip('"')
    match = TYPE_RE.match(rest.strip())
    if not match:
        raise ValueError(f"No se reconoce el tipo de la columna: {part}")
    sql_type = (match.group(1) + (match.group(2) or "")).lower()
    tail = rest.strip()[match.end():]
    base = re.sub(r"\(.*\)", "", match.group(1).lower()).strip()
    base = SYNONYMS.get(base, base)
    length = None
    if base == "varchar":
        size = re.search(r"\((\d+)\)", match.group(1))
        length = int(size.group(1)) if size else None
    default = None
    default_match = re.search(r"\bDEFAULT\s+(.*)$", tail, re.S | re.I)
    stored = bool(re.search(r"GENERATED ALWAYS AS \(.*\) STORED", tail, re.S | re.I))
    if default_match and not stored:
        default = default_match.group(1).strip()
    return Column(
        name=name, raw=part, sql_type=sql_type, base=base, is_array=bool(match.group(2)), length=length,
        not_null=bool(re.search(r"\bNOT NULL\b", tail, re.I)), default=default,
        identity=bool(re.search(r"GENERATED (ALWAYS|BY DEFAULT) AS IDENTITY", tail, re.I)), stored=stored,
    )


def parse_tables(text: str) -> list[Table]:
    """Devuelve las tablas de un DDL, en el orden del fichero."""
    text = re.sub(r"^\s*--[^\n]*\n", "", text, flags=re.M)
    tables = []
    for match in TABLE_RE.finditer(text):
        table = Table(name=match.group(1), columns=[], pk=[])
        for part in split_top_level(match.group(2)):
            part = part.strip()
            if not part:
                continue
            table.lines.append(part)
            flat = " ".join(part.split())
            constraint = CONSTRAINT_RE.match(flat)
            if not constraint:
                if re.match(r"(PRIMARY KEY|UNIQUE|FOREIGN KEY|CHECK)\b", flat, re.I):
                    raise ValueError(f"Restriccion sin nombre en {table.name}: {flat}")
                table.columns.append(parse_column(flat))
                continue
            name, definition = constraint.group(1), constraint.group(2)
            upper = definition.upper()
            if upper.startswith("PRIMARY KEY"):
                table.pk = [c.strip().strip('"') for c in re.search(r"\(([^)]*)\)", definition).group(1).split(",")]
            elif upper.startswith("UNIQUE"):
                columns = [c.strip().strip('"') for c in re.search(r"\(([^)]*)\)", definition).group(1).split(",")]
                table.uniques.append((name, columns))
            elif upper.startswith("FOREIGN KEY"):
                fk = FK_RE.match(definition)
                table.fks.append(ForeignKey(
                    name=name, columns=[c.strip() for c in fk.group(1).split(",")], ref_table=fk.group(2),
                    ref_columns=[c.strip() for c in fk.group(3).split(",")], raw=flat))
            elif upper.startswith("CHECK"):
                table.checks.append((name, definition))
        if not table.pk:
            raise ValueError(f"La tabla {table.name} no tiene clave primaria: el generador la necesita.")
        for column in table.columns:
            column.pk = column.name in table.pk
        for fk in table.fks:
            if len(fk.columns) == 1:
                table.column(fk.columns[0]).fk_table = fk.ref_table
        _mark_generated(table)
        tables.append(table)
    return tables


def _mark_generated(table: Table) -> None:
    """Decide que claves genera la base: identidad, uuid/secuencia por defecto o clave entera sin defecto.

    Lo ultimo es una heuristica para exports antiguos que no guardaban GENERATED ... AS IDENTITY.
    """
    if len(table.pk) != 1:
        return
    key = table.column(table.pk[0])
    has_generated_default = key.default is not None and key.default.lower().startswith(GENERATED_DEFAULTS)
    integer_without_default = key.base in INTEGER_TYPES and key.default is None and key.fk_table is None
    key.generated = key.identity or has_generated_default or integer_without_default


@dataclass
class Service:
    name: str                 # p. ej. access-service
    database: str             # p. ej. access_db
    tables: list[Table]

    @property
    def package(self) -> str:
        return "com.example." + re.sub(r"[^a-z0-9]", "", self.name.lower())

    @property
    def app_class(self) -> str:
        return pascal(self.name.replace("-", "_")) + "Application"

    @property
    def uses_auth_uid(self) -> bool:
        return any("auth.uid()" in (c.default or "") for t in self.tables for c in t.columns)


def load_service(name: str, ddl_path: Path) -> Service:
    text = ddl_path.read_text(encoding="utf-8")
    directive = re.search(r"^--\s*database:\s*(\w+)", text, re.M)
    database = directive.group(1) if directive else re.sub(r"[^a-z0-9]+", "_", name.lower()) + "_db"
    tables = parse_tables(text)
    if not tables:
        raise ValueError(f"{ddl_path} no contiene ninguna tabla.")
    return Service(name=name, database=database, tables=tables)
