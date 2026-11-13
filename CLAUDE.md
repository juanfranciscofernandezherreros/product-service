# CLAUDE.md - Orquestador AI CRUD (Java 21 + Spring Boot 3 + Hexagonal + Docker PGSQL)

Este repositorio es **solo el generador** de microservicios CRUD. Cada microservicio se genera en su propia carpeta y repositorio; aquí no se guarda código de ningún servicio.

El código de un servicio sale de **plantillas deterministas** a partir de su DDL (`generator/` + `scripts/generate.py`): sin IA y sin tokens. La IA solo interviene para lo que el esquema no dice (reglas de negocio) o para corregir un build que falla. El pipeline antiguo, en el que la IA escribe todo fase a fase (`.ai/prompts/00..05`), sigue disponible con `--mode ai`.

Este fichero es el índice; las normas viven en **reglas** (siempre activas) y los procedimientos en **skills** (se cargan cuando la tarea lo pide). `CLAUDE.md` y `.claude/` se copian tal cual a cada servicio generado, así que deben valer en los dos sitios: en el servicio, las rutas `specs/…` y `backend/…` son relativas a su carpeta.

## Reglas (`.claude/rules/`) — se aplican siempre
- `un-servicio-un-repositorio.md` — un microservicio = una carpeta = un repositorio Git; manifiesto `.ai/services.txt`; Git y remoto.
- `arquitectura-hexagonal.md` — stack obligatorio (Java 21, Spring Boot 3, PostgreSQL), capas hexagonales, inyección por constructor.
- `tests-y-cobertura.md` — JUnit 5 + Cucumber por endpoint, JaCoCo ≥ 80%, reporte HTML de Cucumber, perfil `test`.
- `base-de-datos-sdd.md` — el DDL (igual que Supabase) como fuente de verdad, Flyway + `ddl-auto: validate`, perfiles `local`/`dev`/`test`.
- `supabase-acceso-datos.md` — política de seguridad del perfil `dev` (JWT + RLS, `service_role`, secretos fuera del repo).
- `verificacion-final.md` — definición de «terminado» y entregables de aceptación (CA-11 incluido).

## Skills (`.claude/skills/`) — procedimientos
- `generar-crud` — generar o regenerar servicios con `scripts/generate.py` y cuándo usar la IA.
- `nuevo-esquema-database-first` — de un cambio en Supabase (tabla o servicio nuevo) a un microservicio actualizado.
- `documentacion-funcional-html` — contenido obligatorio de `backend/docs/index.html`.

## Comandos
En el generador (este repositorio):
- Exportar el esquema real de Supabase: `python scripts/export_supabase_schema.py` (requiere `psql` y `SUPABASE_DB_URL`; solo lee el catálogo)
- Repartir el export en dominios según `specs/domain-map.txt`: `python scripts/split_domains.py`
- Comprobar que los dominios son iguales que Supabase: `python scripts/check_domains.py`
- Regenerar los contratos OpenAPI desde el DDL: `python scripts/generate.py --openapi`
- Validar manifiesto y contratos: `python scripts/validate_specs.py` (requiere `pyyaml` y `openapi-spec-validator`)
- Generar servicios sin IA: `python scripts/generate.py [-s <servicio>] [--build]`
- Crear en GitHub los repositorios de los servicios que falten: `python scripts/create_repos.py` (requiere `GITHUB_TOKEN`; privados salvo `--public`), o `--create-repos` en `generate.py`
- Pipeline completo (generar, compilar, IA solo si falla, crear repositorios si hay token, push): `run-ai-crud.bat` o `./run-ai-crud.sh`

En un servicio generado (en su `backend/`):
- Levantar PostgreSQL local: `docker compose up -d`
- Build & Full Verify: `mvnw.cmd clean verify` (o `./mvnw clean verify`); no necesita Docker
- Tests unitarios y Cucumber: `mvnw.cmd test`
- Análisis estático: `mvnw.cmd checkstyle:check`

## Índice del generador
- **Generador:** `generator/` (modelo desde el DDL, Java, recursos, OpenAPI, documentación) y `scripts/generate.py` (orquestador).
- **Manifiesto:** `.ai/services.txt` (una línea por servicio: nombre, carpeta, remoto, OpenAPI, DDL).
- **Esquema de Supabase:** `specs/supabase-schema.sql` (export; no se edita a mano).
- **Reparto de tablas:** `specs/domain-map.txt` (qué tabla va a qué servicio; este sí se edita).
- **Esquemas por servicio:** `specs/domains/<dominio>.sql` (los escribe `split_domains.py`; fuente de verdad de cada servicio).
- **Contratos OpenAPI:** `specs/openapi/<dominio>.yaml` (derivados del DDL por el generador).
- **Reglas de negocio (opcional):** `specs/business-rules/<SERVICE_NAME>.md`; la IA las aplica con `--ai-rules`.
- **Prompts para la IA:** `.ai/prompts/fix-build.md`, `.ai/prompts/business-rules.md` y, para `--mode ai`, `.ai/prompts/00..05`.
- **Plantillas que se copian a cada servicio:** `templates/` (`checkstyle.xml`, `.editorconfig`, `service.gitignore`, `maven-wrapper/`; `plans/` solo en `--mode ai`).
- **Ejemplo API-First:** `examples/fund-service/`. **Evidencia histórica:** `evidence/`. **Índice navegable:** `specs/INDICE.md`.

## Índice de un servicio generado
- **Contexto:** `.ai/project-context.txt` (`SERVICE_NAME`, `REMOTE_URL`, `TARGET_DIR`, `GENERATION_MODE`).
- **Contrato y esquema:** `specs/crud-spec.yaml` (OpenAPI) y `specs/database-spec.sql` (DDL, fuente de verdad del modelo).
- **Código y guía operativa:** `backend/` (`backend/README.md`, `backend/CLAUDE.md`).
- **Documentación funcional:** `backend/docs/index.html`. **Pruebas cURL (CA-11):** `specs/PRUEBAS-CURL-CRUD.html`.
- **Código generado:** no se edita a mano (la lista está en `.ai/generated-files.txt` y se sobrescribe al regenerar). Las reglas de negocio van en ficheros nuevos o se piden al generador.
