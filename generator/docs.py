"""Documentacion del servicio generada desde el modelo: docs/index.html, pruebas cURL, README y CLAUDE.md."""
from __future__ import annotations

import html
import json
from urllib.parse import quote

from .model import Service, Table

STYLE = """
:root{--ink:#1c2430;--muted:#5b6675;--line:#d9dee5;--bg:#f6f8fa;--accent:#0b5cad;--ok:#1a7f4b;--warn:#a15c00;--bad:#b3261e}
*{box-sizing:border-box}body{margin:0;font:15px/1.55 system-ui,-apple-system,"Segoe UI",sans-serif;color:var(--ink);background:#fff}
header{background:var(--ink);color:#fff;padding:28px 32px}header h1{margin:0 0 4px;font-size:26px}header p{margin:0;color:#c5cedb}
nav{position:sticky;top:0;background:#fff;border-bottom:1px solid var(--line);padding:10px 32px;display:flex;flex-wrap:wrap;gap:6px 16px;z-index:2}
nav a{color:var(--accent);text-decoration:none;font-size:14px}main{max-width:1100px;margin:0 auto;padding:8px 32px 64px}
h2{margin:40px 0 12px;font-size:21px;border-bottom:2px solid var(--line);padding-bottom:6px}h3{margin:28px 0 8px;font-size:17px}
h4{margin:18px 0 6px;font-size:14px;color:var(--muted);text-transform:uppercase;letter-spacing:.04em}
table{width:100%;border-collapse:collapse;margin:8px 0 16px;font-size:14px}th,td{border:1px solid var(--line);padding:6px 9px;text-align:left;vertical-align:top}
th{background:var(--bg)}code,pre{font-family:ui-monospace,Consolas,monospace;font-size:13px}pre{background:var(--bg);border:1px solid var(--line);border-radius:6px;padding:10px 12px;overflow-x:auto;margin:6px 0 14px}
.m{display:inline-block;min-width:58px;text-align:center;border-radius:4px;padding:1px 7px;color:#fff;font:600 12px ui-monospace,monospace}
.POST{background:var(--ok)}.GET{background:var(--accent)}.PUT{background:var(--warn)}.DELETE{background:var(--bad)}
.ep{border:1px solid var(--line);border-radius:8px;padding:4px 16px 8px;margin:14px 0}.note{color:var(--muted);font-size:14px}
svg{max-width:100%;height:auto}
"""

DIAGRAM = """<svg viewBox="0 0 900 250" role="img" aria-label="Arquitectura hexagonal del servicio">
<defs><marker id="a" markerWidth="9" markerHeight="9" refX="8" refY="4.5" orient="auto"><path d="M0,0 L9,4.5 L0,9 z" fill="#5b6675"/></marker></defs>
<g font-family="system-ui,sans-serif" font-size="13" fill="#1c2430">
<rect x="10" y="85" width="120" height="70" rx="8" fill="#f6f8fa" stroke="#5b6675"/><text x="70" y="116" text-anchor="middle">Cliente</text><text x="70" y="134" text-anchor="middle" fill="#5b6675">HTTP / JSON</text>
<rect x="180" y="30" width="170" height="180" rx="10" fill="#e8f1fb" stroke="#0b5cad"/><text x="265" y="54" text-anchor="middle" font-weight="600">Adaptador de entrada</text>
<text x="265" y="86" text-anchor="middle">Controladores REST</text><text x="265" y="108" text-anchor="middle">DTO + validacion</text><text x="265" y="130" text-anchor="middle">Gestor de errores</text>
<rect x="400" y="30" width="170" height="180" rx="10" fill="#eaf6ef" stroke="#1a7f4b"/><text x="485" y="54" text-anchor="middle" font-weight="600">Aplicacion</text>
<text x="485" y="86" text-anchor="middle">Puertos de entrada</text><text x="485" y="108" text-anchor="middle">Casos de uso CRUD</text><text x="485" y="130" text-anchor="middle">Puertos de salida</text>
<rect x="425" y="150" width="120" height="44" rx="8" fill="#fff" stroke="#1a7f4b"/><text x="485" y="177" text-anchor="middle" font-weight="600">Dominio</text>
<rect x="620" y="30" width="150" height="180" rx="10" fill="#fdf3e3" stroke="#a15c00"/><text x="695" y="54" text-anchor="middle" font-weight="600">Adaptador de salida</text>
<text x="695" y="86" text-anchor="middle">Entidades JPA</text><text x="695" y="108" text-anchor="middle">Spring Data</text><text x="695" y="130" text-anchor="middle">Mappers</text>
<rect x="810" y="85" width="80" height="70" rx="8" fill="#f6f8fa" stroke="#5b6675"/><text x="850" y="116" text-anchor="middle">PostgreSQL</text><text x="850" y="134" text-anchor="middle" fill="#5b6675">Supabase</text>
<g stroke="#5b6675" stroke-width="1.5" marker-end="url(#a)"><line x1="130" y1="120" x2="178" y2="120"/><line x1="350" y1="120" x2="398" y2="120"/><line x1="570" y1="120" x2="618" y2="120"/><line x1="770" y1="120" x2="808" y2="120"/></g>
</g></svg>"""


def esc(value) -> str:
    return html.escape(str(value))


def pretty(value) -> str:
    return esc(json.dumps(value, indent=2, ensure_ascii=False))


def literal_item(t: Table) -> str:
    return t.path + "".join("/" + quote(str(c.sample(t.name)), safe="") for c in t.pk_columns)


def endpoints(t: Table) -> list[dict]:
    """Operaciones de una tabla con sus ejemplos, en orden CRUD."""
    resource = t.resource()
    error404 = {"code": "RESOURCE_NOT_FOUND", "message": f"{t.name} no encontrado"}
    error400 = {"code": "BAD_REQUEST", "message": "Cuerpo de la peticion mal formado"}
    filters = ", ".join(f"<code>{esc(c.field)}</code>" for c in t.filter_columns) or "ninguno"
    result = [
        {"method": "POST", "path": t.path, "url": t.path, "summary": "Crea un registro", "body": t.body(0),
         "columns": t.create_columns, "ok": (201, resource),
         "errors": [(400, error400), (409, {"code": "RESOURCE_ALREADY_EXISTS", "message": f"{t.name} ya existe"})]},
        {"method": "GET", "path": t.path, "url": t.path + "?page=0&size=20", "summary": "Lista paginada",
         "params": f"Query: <code>page</code> (0), <code>size</code> (20, max. 100). Filtros: {filters}.",
         "ok": (200, {"items": [resource], "total": 1, "page": 0, "size": 20}), "errors": [(400, error400)]},
        {"method": "GET", "path": t.item_path, "url": literal_item(t), "summary": "Obtiene un registro",
         "ok": (200, resource), "errors": [(404, error404)]},
    ]
    if t.update_columns:
        updated = dict(resource)
        updated.update(t.body(1, update=True))
        result.append({"method": "PUT", "path": t.item_path, "url": literal_item(t), "summary": "Modifica un registro",
                       "body": t.body(1, update=True), "columns": t.update_columns, "ok": (200, updated),
                       "errors": [(400, error400), (404, error404)]})
    result.append({"method": "DELETE", "path": t.item_path, "url": literal_item(t), "summary": "Borra un registro",
                   "ok": (204, None), "errors": [(404, error404)]})
    return result


def _schema_table(columns) -> str:
    rows = "".join(
        f"<tr><td><code>{esc(c.field)}</code></td><td><code>{esc(c.sql_type)}</code></td>"
        f"<td>{'si' if c.required else 'no'}</td>"
        f"<td>{'max. ' + str(c.length) + ' caracteres' if c.length else ''}</td></tr>" for c in columns)
    return ("<table><tr><th>Campo</th><th>Tipo</th><th>Obligatorio</th><th>Validacion</th></tr>"
            + rows + "</table>")


def _endpoint_html(e: dict) -> str:
    parts = [f'<div class="ep"><h3><span class="m {e["method"]}">{e["method"]}</span> '
             f'<code>{esc(e["path"])}</code> — {esc(e["summary"])}</h3>', "<h4>Request</h4>"]
    if e.get("params"):
        parts.append(f"<p>{e['params']}</p>")
    if "{" in e["path"]:
        parts.append("<p>Parametros de ruta: la clave primaria del registro.</p>")
    if "body" in e:
        parts.append(_schema_table(e["columns"]) + f"<pre>{pretty(e['body'])}</pre>")
    elif not e.get("params") and "{" not in e["path"]:
        parts.append("<p>Sin parametros.</p>")
    status, payload = e["ok"]
    parts.append(f"<h4>Response {status}</h4>")
    parts.append(f"<pre>{pretty(payload)}</pre>" if payload is not None else "<p>Sin cuerpo.</p>")
    for code, error in e["errors"]:
        parts.append(f"<h4>Response {code} (error)</h4><pre>{pretty(error)}</pre>")
    return "".join(parts) + "</div>"


def _data_model(t: Table) -> str:
    rows = ""
    for c in t.columns:
        marks = []
        if c.pk:
            marks.append("PK")
        marks += [f"UNIQUE ({', '.join(cols)})" for _, cols in t.uniques if c.name in cols]
        marks += [f"FK → {fk.ref_table}({', '.join(fk.ref_columns)})" for fk in t.fks if c.name in fk.columns]
        rows += (f"<tr><td><code>{esc(c.name)}</code></td><td><code>{esc(c.sql_type)}</code></td>"
                 f"<td>{'NOT NULL' if c.not_null else 'NULL'}</td><td><code>{esc(c.default or '')}</code></td>"
                 f"<td>{esc('; '.join(marks))}</td></tr>")
    checks = "".join(f"<li><code>{esc(name)}</code>: <code>{esc(definition)}</code></li>"
                     for name, definition in t.checks)
    return (f'<h3 id="t-{t.name}"><code>{esc(t.name)}</code></h3>'
            "<table><tr><th>Columna</th><th>Tipo PostgreSQL</th><th>Nulos</th><th>Por defecto</th>"
            f"<th>Claves</th></tr>{rows}</table>"
            + (f"<p class='note'>Restricciones CHECK:</p><ul>{checks}</ul>" if checks else ""))


def index_html(s: Service, port: int) -> str:
    catalogue = "".join(
        f'<tr><td><span class="m {e["method"]}">{e["method"]}</span></td><td><code>{esc(e["path"])}</code></td>'
        f'<td>{esc(e["summary"])} de <code>{esc(t.name)}</code></td>'
        f'<td>{e["ok"][0]}, {", ".join(str(code) for code, _ in e["errors"])}</td></tr>'
        for t in s.tables for e in endpoints(t))
    sections = "".join(
        f'<h3 id="e-{t.name}">Recurso <code>{esc(t.name)}</code></h3>' + "".join(_endpoint_html(e) for e in endpoints(t))
        for t in s.tables)
    model = "".join(_data_model(t) for t in s.tables)
    return f"""<!DOCTYPE html>
<html lang="es"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>{esc(s.name)} — Documentacion funcional</title><style>{STYLE}</style></head><body>
<header><h1>{esc(s.name)}</h1><p>Documentacion funcional oficial · API CRUD hexagonal en Java 21 · {len(s.tables)} tablas</p></header>
<nav><a href="#proposito">Proposito</a><a href="#arquitectura">Arquitectura</a><a href="#catalogo">Catalogo</a>
<a href="#endpoints">Request y Response</a><a href="#datos">Modelo de datos</a><a href="#entornos">Entornos</a><a href="#calidad">Calidad</a></nav>
<main>
<h2 id="proposito">Proposito y reglas</h2>
<p><strong>{esc(s.name)}</strong> expone por REST el alta, consulta, modificacion y borrado de las tablas
{", ".join(f"<code>{esc(t.name)}</code>" for t in s.tables)}. El modelo de datos es el de Supabase: el codigo se genera
desde el DDL, que es la fuente de verdad.</p>
<ul>
<li>Las claves que genera la base (identidad o UUID por defecto) no se envian en el alta.</li>
<li>Un alta con una clave que ya existe devuelve <code>409</code>; una peticion mal formada o sin campos obligatorios, <code>400</code>; un recurso inexistente, <code>404</code>.</li>
<li>Las restricciones del esquema (CHECK, UNIQUE, claves foraneas) las aplica la base: si una operacion las incumple, la respuesta es <code>409</code>.</li>
<li>En una modificacion, los campos con valor por defecto que no se envian conservan su valor actual.</li>
<li>Los errores tienen siempre la forma <code>{{"code": "...", "message": "..."}}</code>.</li>
</ul>
<h2 id="arquitectura">Arquitectura</h2>{DIAGRAM}
<p class="note">Flujo de una peticion: el controlador valida el DTO y llama al caso de uso; este aplica las reglas y usa el puerto de salida; el adaptador JPA lee o escribe en PostgreSQL. El dominio y la aplicacion no dependen de Spring ni de JPA (lo comprueba ArchUnit).</p>
<h2 id="catalogo">Catalogo de endpoints</h2>
<table><tr><th>Metodo</th><th>Ruta</th><th>Funcion</th><th>Codigos HTTP</th></tr>{catalogue}</table>
<h2 id="endpoints">Request y Response por endpoint</h2>{sections}
<h2 id="datos">Modelo de datos</h2>
<p>Esquema exacto de <code>specs/database-spec.sql</code>, tal como existe en la base de datos.</p>{model}
<h2 id="entornos">Entornos</h2>
<table><tr><th>Perfil</th><th>Base de datos</th><th>Esquema</th></tr>
<tr><td><code>local</code></td><td>PostgreSQL en Docker (<code>docker compose up -d</code>), base <code>{esc(s.database)}</code></td><td>Flyway lo crea; JPA lo valida</td></tr>
<tr><td><code>dev</code> (por defecto)</td><td>Supabase; credenciales por variables de entorno</td><td>Lo gestiona Supabase; el servicio no lo crea ni lo valida</td></tr>
<tr><td><code>test</code></td><td>PostgreSQL embebido, sin Docker</td><td>Flyway aplica el esquema de pruebas; JPA lo valida</td></tr></table>
<p>Puerto del servicio: <code>{port}</code>.</p>
<h2 id="calidad">Evidencias de calidad</h2>
<ul>
<li>Pruebas unitarias JUnit 5 + Mockito de cada caso de uso y cada controlador.</li>
<li>Escenarios Cucumber por tabla contra la API real y PostgreSQL: ciclo CRUD, duplicados, peticiones no validas y recursos inexistentes. Informe en <code>target/cucumber-reports/cucumber.html</code>.</li>
<li>ArchUnit: el dominio y la aplicacion no dependen de la infraestructura ni de los frameworks.</li>
<li>JaCoCo: el build falla por debajo del 80 % de lineas. Informe en <code>target/site/jacoco/index.html</code>.</li>
<li>Checkstyle en la fase <code>validate</code>.</li>
</ul>
</main></body></html>
"""


def curl_html(s: Service, port: int) -> str:
    blocks = []
    for t in s.tables:
        commands = []
        for e in endpoints(t):
            command = f"curl -i -X {e['method']} 'http://localhost:{port}{e['url']}'"
            if "body" in e:
                command += (" \\\n  -H 'Content-Type: application/json' \\\n  -d '"
                            + json.dumps(e["body"], ensure_ascii=False) + "'")
            commands.append(f"<h4><span class='m {e['method']}'>{e['method']}</span> <code>{esc(e['path'])}</code> — "
                            f"{esc(e['summary'])} (espera {e['ok'][0]})</h4><pre>{esc(command)}</pre>")
        blocks.append(f"<h2><code>{esc(t.name)}</code></h2>" + "".join(commands))
    return f"""<!DOCTYPE html>
<html lang="es"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>{esc(s.name)} — Pruebas cURL CRUD (CA-11)</title><style>{STYLE}</style></head><body>
<header><h1>{esc(s.name)} — Pruebas cURL CRUD</h1><p>Criterio CA-11: un <code>curl</code> por cada endpoint, en secuencia crear → listar → obtener → modificar → borrar</p></header>
<main><p class="note">Arranca el servicio en el puerto {port} y ejecuta los comandos en orden. En las tablas con clave generada,
sustituye la clave de ejemplo de la ruta por la que devuelva el alta. Los cuerpos salen de los DTO reales del servicio.</p>
{"".join(blocks)}</main></body></html>
"""


def backend_readme(s: Service, port: int) -> str:
    resources = "\n".join(f"- `{t.path}` — tabla `{t.name}`" for t in s.tables)
    return f"""# {s.name}

API CRUD hexagonal (Java 21, Spring Boot 3, PostgreSQL) generada desde `specs/database-spec.sql`.

## Arrancar en local

```
docker compose up -d
./mvnw spring-boot:run -Dspring-boot.run.profiles=local      (Windows: mvnw.cmd)
```

Escucha en el puerto {port}. Flyway crea las tablas y JPA comprueba que las entidades coinciden con ellas.

## Arrancar contra Supabase (perfil `dev`, por defecto)

Define `DB_HOST_DEV` si no vale el de por defecto y `DB_PASS_DEV` (secreto: nunca en el repositorio) y ejecuta
`./mvnw spring-boot:run`.

## Compilar y probar

```
./mvnw clean verify
```

No necesita Docker: las pruebas usan un PostgreSQL embebido. Informes en `target/cucumber-reports/cucumber.html`
y `target/site/jacoco/index.html`.

## Recursos

{resources}

Cada recurso admite `POST`, `GET` (lista paginada con filtros), `GET` por clave, `PUT` y `DELETE`.
Detalle con ejemplos en `docs/index.html`; contrato en `../specs/crud-spec.yaml`.
"""


def backend_claude(s: Service) -> str:
    return f"""# CLAUDE.md — {s.name}

Servicio generado de forma determinista desde `../specs/database-spec.sql`. No edites a mano el codigo generado:
cambia el DDL en el generador y vuelve a generar. Las reglas de negocio propias van en clases nuevas.

## Comandos
- `docker compose up -d` · `./mvnw clean verify` · `./mvnw spring-boot:run -Dspring-boot.run.profiles=local`

## Estructura (`{s.package}`)
- `domain.model`: un `record` por tabla. `domain.exception`: errores de dominio.
- `application`: puertos de entrada y salida y un caso de uso CRUD por tabla, sin Spring.
- `infrastructure.adapter.in.web`: controladores, DTO y gestor de errores.
- `infrastructure.adapter.out.persistence`: entidades JPA, repositorios y adaptadores.
- `infrastructure.config.BeanConfiguration`: cableado de los casos de uso.

## Esquema
- `local`: Flyway aplica `db/migration/V1__init.sql`; JPA `validate`.
- `dev`: Supabase; `ddl-auto: none`, Flyway desactivado.
- `test`: PostgreSQL embebido con `db/testmigration/V1__init.sql` (sin CHECK ni FK); JPA `validate`.

## Errores
`BAD_REQUEST` (400), `RESOURCE_NOT_FOUND` (404), `RESOURCE_ALREADY_EXISTS` y `DATA_INTEGRITY_VIOLATION` (409),
`INTERNAL_ERROR` (500).
"""
