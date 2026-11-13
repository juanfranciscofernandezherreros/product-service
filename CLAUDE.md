# CLAUDE.md - Orquestador AI CRUD (Java 21 + Spring Boot 3 + Hexagonal + Docker PGSQL)

Este repositorio genera microservicios CRUD (API-First o Database-First) mediante un pipeline por fases (`run-ai-crud.bat` + `.ai/prompts/00..05`). Este fichero es el índice; las normas viven en **reglas** (siempre activas) y los procedimientos en **skills** (se cargan cuando la tarea lo pide).

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

## Comandos de Compilación, Calidad y Test (en `backend/`)
- Levantar PostgreSQL local: `docker compose up -d`
- Build & Full Verify: `mvnw.cmd clean verify` (o `./mvnw clean verify`)
- Tests Unitarios (JUnit 5): `mvnw.cmd test`
- Tests de Aceptación (Cucumber BDD): `mvnw.cmd verify -Dspring.profiles.active=test`
- Análisis Estático: `mvnw.cmd checkstyle:check`

## Índice de especificaciones y entregables
- **SDD / reglas:** este `CLAUDE.md` + `.claude/rules/` (fuente de verdad del proceso) + `backend-<servicio>/CLAUDE.md`.
- **Contexto del proyecto:** `.ai/project-context.txt` (`SERVICE_NAME`, `REMOTE_URL`, `GENERATION_MODE`), generado por `run-ai-crud.bat`.
- **Contrato y esquema:** `specs/crud-spec.yaml` (OpenAPI) y `specs/<servicio>.sql` (DDL, fuente de verdad del modelo).
- **Prompts del pipeline:** `.ai/prompts/00..05`.
- **Documentación funcional:** `backend-<servicio>/docs/index.html` (uno por servicio).
- **Aceptación y evidencia:** `specs/CRITERIOS-ACEPTACION.html` (CA-01..CA-11), `specs/PRUEBAS-CURL-CRUD.html` (CA-11, `curl` por endpoint), `specs/VERIFICACION-DATOS.md` (datos reales por servicio), `ACTA-CONFORMIDAD.html`.
- **Índice navegable:** `specs/INDICE.md`.
