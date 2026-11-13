# CLAUDE.md - Orquestador AI CRUD (Java 21 + Spring Boot 3 + Hexagonal + Docker PGSQL)

Este repositorio es **solo el generador** de microservicios CRUD (API-First o Database-First): reglas, skills, prompts, plantillas y especificaciones de entrada. Cada microservicio se genera en su propia carpeta y repositorio (`run-ai-crud.bat` / `run-ai-crud.sh` + `.ai/prompts/00..05`); aquí no se guarda código de ningún servicio. Este fichero es el índice; las normas viven en **reglas** (siempre activas) y los procedimientos en **skills** (se cargan cuando la tarea lo pide).

`CLAUDE.md`, `.claude/` y `.ai/prompts/` se copian tal cual a cada servicio generado, así que deben valer en los dos sitios: en el servicio, las rutas `specs/…`, `plans/…` y `backend/…` son relativas a su carpeta.

## Reglas (`.claude/rules/`) — se aplican siempre
- `un-servicio-un-repositorio.md` — un microservicio = una carpeta = un repositorio Git; manifiesto `.ai/services.txt`; Git y remoto.
- `arquitectura-hexagonal.md` — stack obligatorio (Java 21, Spring Boot 3, PostgreSQL), capas hexagonales, inyección por constructor, planificación previa.
- `tests-y-cobertura.md` — JUnit 5 + Cucumber por endpoint, JaCoCo ≥ 80%, reporte HTML de Cucumber, perfil `test`.
- `base-de-datos-sdd.md` — el DDL como fuente de verdad, Flyway + `ddl-auto: validate`, perfiles `local`/`dev`/`test`.
- `supabase-acceso-datos.md` — política de seguridad del perfil `dev` (JWT + RLS, `service_role`, secretos fuera del repo).
- `verificacion-final.md` — definición de «terminado» y entregables de aceptación (CA-11 incluido).

## Skills (`.claude/skills/`) — procedimientos
- `generar-crud` — ejecutar o continuar el pipeline (Fases 0→5) en orden estricto, en modo API-First o Database-First.
- `nuevo-esquema-database-first` — de un `.sql` nuevo a un microservicio CRUD completo.
- `documentacion-funcional-html` — generar `backend/docs/index.html` (request/response, modelo de datos, diagrama SVG).

## Comandos
En el generador (este repositorio):
- Validar manifiesto y contratos OpenAPI: `python scripts/validate_specs.py` (requiere `pyyaml` y `openapi-spec-validator`)
- Exportar el esquema real de Supabase a `specs/supabase-schema.sql`: `python scripts/export_supabase_schema.py` (requiere `psql` y la variable de entorno `SUPABASE_DB_URL`; solo lee el catálogo)
- Comparar los dominios con ese export: `python scripts/check_domains.py`
- Generar los servicios del manifiesto: `run-ai-crud.bat` (Windows) o `./run-ai-crud.sh` (Linux/macOS)

En un servicio generado (en su `backend/`):
- Levantar PostgreSQL local: `docker compose up -d`
- Build & Full Verify: `mvnw.cmd clean verify` (o `./mvnw clean verify`)
- Tests Unitarios (JUnit 5): `mvnw.cmd test`
- Tests de Aceptación (Cucumber BDD): `mvnw.cmd verify -Dspring.profiles.active=test`
- Análisis Estático: `mvnw.cmd checkstyle:check`

## Índice del generador
- **Manifiesto:** `.ai/services.txt` (una línea por servicio: nombre, carpeta, remoto, OpenAPI, DDL).
- **Prompts del pipeline:** `.ai/prompts/00..05`.
- **Esquemas (fuente de verdad del modelo):** `specs/domains/<servicio>.sql`. Deben coincidir con las tablas reales de Supabase: el export completo está en `specs/supabase-schema.sql` (se regenera con el script, no se edita a mano).
- **Contratos OpenAPI:** `specs/openapi/<servicio>.yaml` (derivados del DDL).
- **Criterios de aceptación de entrada (opcional, API-First):** `specs/acceptance/<SERVICE_NAME>.md`.
- **Plantillas que se copian a cada servicio:** `templates/` (`checkstyle.xml`, `.editorconfig`, `plans/`, y `database-spec.sql` como ejemplo de DDL).
- **Ejemplo API-First:** `examples/fund-service/`.
- **Evidencia histórica de aceptación:** `evidence/` (resultado de una ejecución anterior; no es entrada).
- **Índice navegable:** `specs/INDICE.md`.

## Índice de un servicio generado
- **Contexto:** `.ai/project-context.txt` (`SERVICE_NAME`, `REMOTE_URL`, `TARGET_DIR`, `GENERATION_MODE`), escrito por el script.
- **Contrato y esquema:** `specs/crud-spec.yaml` (OpenAPI) y `specs/database-spec.sql` (DDL, fuente de verdad del modelo).
- **Plan:** `plans/IMPLEMENTATION_PLAN.md` y `plans/TASKS.md`.
- **Código y guía operativa:** `backend/` (`backend/README.md`, `backend/CLAUDE.md`).
- **Documentación funcional:** `backend/docs/index.html`.
- **Aceptación y evidencia:** `specs/CRITERIOS-ACEPTACION.html` (CA-01..CA-11), `specs/PRUEBAS-CURL-CRUD.html` (CA-11, `curl` por endpoint), `specs/VERIFICACION-DATOS.md` (datos reales).
