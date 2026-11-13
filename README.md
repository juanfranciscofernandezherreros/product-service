# AI CRUD Generator (Java 21 + Hexagonal + Spec-Driven + BDD + PostgreSQL)

Generador de microservicios CRUD en **Java 21** (Spring Boot 3, arquitectura hexagonal, PostgreSQL) a partir del esquema de base de datos.

Este repositorio contiene **solo el generador**. Cada microservicio se genera en su propia carpeta y se publica en su propio repositorio Git: un servicio = un repositorio.

El código sale de **plantillas deterministas**: no se usa IA ni se gastan tokens para generar un CRUD. La IA (Claude Code CLI) queda para lo que el esquema no dice —reglas de negocio— y para corregir un build que falle.

## Cómo funciona

```
Supabase ──export──▶ specs/supabase-schema.sql
                          │  specs/domain-map.txt (qué tabla va a qué servicio)
                          ▼
                 specs/domains/<dominio>.sql ──▶ specs/openapi/<dominio>.yaml
                          │
                          ▼  scripts/generate.py (plantillas de generator/)
            ../<servicio>/  backend completo, tests, docs, contrato
                          │
                          ▼  solo si hace falta
              IA: reglas de negocio y arreglo de builds rotos
```

## Qué hay aquí

| Ruta | Contenido |
|------|-----------|
| `generator/` | Plantillas: modelo desde el DDL, código Java, tests, migraciones, OpenAPI y documentación. |
| `scripts/generate.py` | Orquestador: genera, compila, llama a la IA si se pide y sube cada servicio. |
| `scripts/create_repos.py` | Crea en GitHub los repositorios de los servicios que aún no existan. |
| `scripts/export_supabase_schema.py` | Exporta el esquema `public` de Supabase (usa `scripts/export-schema.sql`). |
| `scripts/split_domains.py` | Recorta el export en un DDL por servicio según `specs/domain-map.txt`. |
| `scripts/check_domains.py` | Comprueba que los DDL de los servicios son iguales que Supabase. |
| `scripts/validate_specs.py` | Valida el manifiesto y los contratos OpenAPI. |
| `.ai/services.txt` | Manifiesto: qué servicios se generan, dónde y a qué remoto. |
| `specs/supabase-schema.sql` | Export del esquema de Supabase. No se edita a mano. |
| `specs/domain-map.txt` | Reparto de tablas entre servicios. Este sí se edita. |
| `specs/domains/*.sql`, `specs/openapi/*.yaml` | DDL y contrato de cada servicio, derivados. |
| `specs/business-rules/<servicio>.md` | Opcional: reglas de negocio que la IA añade al CRUD. |
| `CLAUDE.md`, `.claude/rules/`, `.claude/skills/` | Normas y procedimientos para el agente (se copian a cada servicio). |
| `.ai/prompts/` | Prompts de la IA: arreglo de build, reglas de negocio y el pipeline antiguo por fases. |
| `templates/` | Ficheros que se copian a cada servicio (Checkstyle, `.gitignore`, wrapper de Maven). |
| `examples/`, `evidence/` | Ejemplo API-First e histórico de una ejecución anterior. |

## Requisitos
- Python 3.10 o superior (el generador solo usa la librería estándar).
- JDK 21 para compilar los servicios. Docker solo para arrancar un servicio en local; los tests no lo necesitan.
- `psql` para exportar el esquema de Supabase.
- Claude Code CLI (`claude`) y `git`, solo si usas `--ai-fix`, `--ai-rules`, `--mode ai` o `--push`.

## Uso

```
python scripts/generate.py                      # genera todos los servicios (sin IA)
python scripts/generate.py -s access-service    # solo uno
python scripts/generate.py --build              # además ejecuta mvnw clean verify
python scripts/generate.py --build --ai-fix 2   # si el build falla, la IA lo corrige (máx. 2 intentos)
python scripts/generate.py --build --ai-rules   # la IA aplica specs/business-rules/<servicio>.md
python scripts/generate.py --build --push       # commit y push al remoto del servicio
python scripts/generate.py --build --create-repos --push   # crea antes los repositorios que falten
```

`run-ai-crud.bat` (Windows) y `./run-ai-crud.sh` (Linux/macOS) son el pipeline completo: `--build --ai-fix 2 --ai-rules --push`, más `--create-repos` si hay un token de GitHub en el entorno.

Un servicio cuyo DDL, generador y plantillas no han cambiado se omite; `--force` lo regenera. Al regenerar solo se tocan los ficheros generados: lo que hayas escrito a mano se conserva.

Variables opcionales para la IA: `MODEL` (modelo de Claude) y `CLAUDE_ARGS` (argumentos extra de la CLI, por ejemplo el modo de permisos, ya que se ejecuta sin intervención con `claude -p`).

## Crear los repositorios en GitHub

Cada servicio se sube a su propio repositorio (`REMOTE_URL` en el manifiesto). Para crear los que aún no existan:

```
set GITHUB_TOKEN=<token>            (CMD)      export GITHUB_TOKEN=<token>   (bash)
python scripts/create_repos.py --dry-run     # dice cuáles existen y cuáles crearía
python scripts/create_repos.py               # los crea, privados (--public para públicos)
```

El token es un secreto (nunca en el repositorio) y debe poder crear repositorios: un token clásico con el ámbito `repo`, o uno de grano fino con permiso de administración de repositorios. Si el propietario de la URL no es la cuenta del token, el repositorio se crea en esa organización.

Para hacerlo todo seguido —generar, compilar, crear el repositorio si falta y subir—: `python scripts/generate.py --build --create-repos --push`. El push usa tus credenciales de git habituales, no el token.

## Qué se genera por servicio

Por cada tabla: `POST`, `GET` paginado con filtros, `GET` por clave, `PUT` y `DELETE` bajo `/v1/<tabla>`, con:

- dominio (`record`), puertos y caso de uso sin dependencias de Spring;
- controlador, DTO con validación, entidad JPA, repositorio y adaptador;
- tests unitarios del caso de uso y del controlador, y una feature de Cucumber contra la API real y un PostgreSQL embebido;
- ArchUnit, Checkstyle y JaCoCo con mínimo del 80 %.

Y por servicio: migraciones Flyway, perfiles `local`/`dev`/`test`, `docker-compose.yml`, wrapper de Maven, `docs/index.html`, `specs/crud-spec.yaml` y `specs/PRUEBAS-CURL-CRUD.html`.

La CI de este repositorio genera los nueve servicios y ejecuta `mvnw clean verify` en cada uno en cada cambio.

## Mantener los esquemas iguales que Supabase

1. Define la cadena de conexión en el entorno (es un secreto: nunca en el repositorio):

   ```
   set SUPABASE_DB_URL=postgresql://postgres:<password>@db.<ref>.supabase.co:5432/postgres      (CMD)
   export SUPABASE_DB_URL='postgresql://postgres:<password>@db.<ref>.supabase.co:5432/postgres'  (bash)
   ```

2. Exporta el esquema (solo lee el catálogo, no toca datos). Sin `psql`, pega `scripts/export-schema.sql` en el SQL Editor de Supabase.

   ```
   python scripts/export_supabase_schema.py
   ```

3. Si hay tablas nuevas, asígnalas a un servicio en `specs/domain-map.txt`.

4. Regenera y comprueba:

   ```
   python scripts/split_domains.py      # reescribe specs/domains/*.sql
   python scripts/check_domains.py      # debe dar 0 diferencias
   python scripts/generate.py --openapi # reescribe specs/openapi/*.yaml
   python scripts/generate.py --build   # regenera solo los servicios que han cambiado
   ```

## Modo antiguo: todo con IA

`python scripts/generate.py --mode ai` ejecuta el pipeline por fases de `.ai/prompts/00..05`, en el que la IA escribe el servicio entero. Úsalo solo para servicios que no sean un CRUD derivable del esquema.

Más detalle en `.claude/skills/` y en `specs/INDICE.md`.
