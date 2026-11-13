---
name: generar-crud
description: Genera o regenera microservicios CRUD con scripts/generate.py (plantillas deterministas desde el DDL, sin IA) y decide cuándo hace falta la IA. Úsala cuando se pida generar, regenerar, compilar o subir un servicio, ejecutar el pipeline, o usar el modo antiguo por fases de .ai/prompts.
---

# Generar un microservicio CRUD

El generador vive en el repositorio del generador (`generator/` y `scripts/generate.py`). Cada servicio se escribe en su `TARGET_DIR`, **fuera** de ese repositorio. Si estás dentro de un servicio generado, no lo regeneres desde ahí: ve al generador.

## Modo por defecto: plantillas (sin IA)

```
python scripts/generate.py                      # todos los servicios del manifiesto
python scripts/generate.py -s access-service    # solo uno
python scripts/generate.py --build              # además mvnw clean verify
python scripts/generate.py --build --push       # y commit + push al remoto del servicio
python scripts/generate.py --build --create-repos --push   # crea antes en GitHub los repositorios que falten
```

`--create-repos` (o `python scripts/create_repos.py` por separado) necesita `GITHUB_TOKEN` en el entorno y crea los repositorios privados salvo que se pase `--public`. No pidas ni escribas el token: lo define el usuario en su entorno.

Qué hace por cada servicio de `.ai/services.txt`:
1. Lee su DDL (`DB_SPEC`) y construye el modelo: tablas, columnas, claves, tipos.
2. Si el DDL, el generador y las plantillas no han cambiado desde la última vez, **lo omite** (`--force` para regenerar).
3. Escribe en `TARGET_DIR`: `backend/` completo (dominio, puertos, casos de uso, controladores, DTO, entidades JPA, adaptadores, tests unitarios, features de Cucumber, ArchUnit, migraciones, perfiles, `pom.xml`, wrapper de Maven, `docker-compose.yml`, `docs/index.html`, README y CLAUDE.md), `specs/crud-spec.yaml`, `specs/database-spec.sql`, `specs/PRUEBAS-CURL-CRUD.html`, las reglas y skills, y `.ai/project-context.txt`.
4. Solo toca los ficheros que genera (lista en `.ai/generated-files.txt`); lo escrito a mano se conserva.

No leas ni revises el código generado «por si acaso»: es idéntico en estilo en todos los servicios y lo valida la CI del generador. Para comprobar un servicio, ejecuta el build.

## Cuándo interviene la IA

Solo en estos dos casos, y con el contexto mínimo:

- **Build roto** (`--ai-fix N`): el script te pasa `.ai/prompts/fix-build.md` y el final del registro. Abre solo los ficheros que nombra el error, aplica el cambio mínimo y no ejecutes el build (lo relanza el script). Si el fallo viene de una plantilla, corrígelo en `generator/` para que no se repita en los demás servicios.
- **Reglas de negocio** (`--ai-rules`): si existe `specs/business-rules/<SERVICE_NAME>.md` en el generador, el script te pasa `.ai/prompts/business-rules.md` con esas reglas. Impleméntalas en la capa de aplicación o dominio, con su test unitario y su escenario Cucumber en un `.feature` nuevo.

`run-ai-crud.bat` / `run-ai-crud.sh` equivalen a `python scripts/generate.py --build --ai-fix 2 --ai-rules --push`, con `--create-repos` si hay un token de GitHub en el entorno.

## Cambiar lo que se genera

- Otro modelo de datos → cambia el DDL (ver skill `nuevo-esquema-database-first`), nunca el código generado.
- Otro estilo de código, endpoint o test → cambia la plantilla en `generator/` (`java.py`, `fixed.py`, `resources.py`, `openapi.py`, `docs.py`) y regenera. La CI del generador compila todos los servicios en cada cambio.

## Modo antiguo: la IA escribe todo (`--mode ai`)

`python scripts/generate.py --mode ai` copia las reglas, los prompts y las plantillas a `TARGET_DIR` y ejecuta las fases en orden estricto, cada una con su prompt de `.ai/prompts/`:

0. `00-db-to-openapi.md` *(solo si `SPEC_FILE` está vacío)* → derivar `specs/crud-spec.yaml` y `specs/acceptance-criteria.md` del DDL.
1. `01-plan-and-tasks.md` → `plans/IMPLEMENTATION_PLAN.md` y `plans/TASKS.md`.
2. `02-domain.md` → dominio puro en Java 21.
3. `03-application.md` → puertos, casos de uso y tests JUnit 5.
4. `04-adapters-and-tests.md` → adaptadores, perfiles, Cucumber, ArchUnit.
5. `05-e2e-verification.md` → `mvnw clean verify`, documentación, entregables y push.

Úsalo solo cuando el servicio no sea un CRUD derivable del esquema. Consume muchos más tokens y el resultado no es reproducible.

## Reglas que aplican siempre
`.claude/rules/`: `un-servicio-un-repositorio.md`, `arquitectura-hexagonal.md`, `tests-y-cobertura.md`, `base-de-datos-sdd.md`, `supabase-acceso-datos.md`, `verificacion-final.md`.
