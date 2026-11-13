---
name: generar-crud
description: Ejecuta o continúa el pipeline de generación de un microservicio CRUD (Fases 0→5, API-First o Database-First) en el orden obligatorio que automatiza run-ai-crud.bat. Úsala cuando se pida generar, ejecutar o continuar el flujo del CRUD, o una fase concreta de .ai/prompts.
---

# Generar un microservicio CRUD (pipeline por fases)

## 0. Dónde se ejecuta
El pipeline se ejecuta **en la carpeta del servicio** (`TARGET_DIR`), nunca dentro del repositorio del generador. Si estás en el generador, lanza `run-ai-crud.bat` / `run-ai-crud.sh` o prepara antes la carpeta del servicio.

## 1. Lee el contexto del proyecto
Antes de ejecutar cualquier fase, lee `.ai/project-context.txt` (creado automáticamente por `run-ai-crud.bat` / `run-ai-crud.sh` en la carpeta del servicio) para conocer:
- **Nombre del Microservicio (`SERVICE_NAME`):** úsalo en `pom.xml` (`<artifactId>` y `<name>`), en `application.yml` (`spring.application.name`), en `README.md` y en `docs/index.html`.
- **Repositorio Remoto (`REMOTE_URL`):** úsalo para configurar `git remote add origin <REMOTE_URL>` y hacer `git push -u origin main`.
- **Modo (`GENERATION_MODE`):** `api-first` | `db-first`.

## 2. Determina el modo de generación
El flujo admite dos puntos de partida; ambos acaban en el mismo pipeline (dominio → aplicación → adaptadores → verificación → docs → push):
- **API-First (por defecto):** la especificación de entrada es el OpenAPI (`specs/crud-spec.yaml`) y, opcionalmente, el esquema (`specs/database-spec.sql`). Se ejecutan las Fases 1→5.
- **Database-First:** la especificación de entrada es **solo el esquema** (`specs/database-spec.sql`). Se ejecuta primero la **Fase 0** (`.ai/prompts/00-db-to-openapi.md`), que **genera** `specs/crud-spec.yaml` y `specs/acceptance-criteria.md` a partir de las tablas del DDL; después continúan las Fases 1→5 con ese contrato derivado. El esquema SQL sigue siendo la fuente de verdad (SDD): Flyway crea las tablas y JPA valida.
- Se selecciona con `GENERATION_MODE` en `.ai/project-context.txt`; en el manifiesto (`.ai/services.txt`), una línea con `SPEC_FILE` vacío implica `db-first` (el OpenAPI se derivará del `DB_SPEC`).

## 3. Ejecuta las fases en orden estricto
**DEBES seguir estrictamente este orden** (automatizado en `run-ai-crud.bat` / `run-ai-crud.sh`). Cada fase tiene su prompt en `.ai/prompts/`; léelo y síguelo. En modo Database-First se antepone la Fase 0.

0. `00-db-to-openapi.md` *(solo Database-First)* → Derivar `specs/crud-spec.yaml` y `specs/acceptance-criteria.md` del DDL.
1. `01-plan-and-tasks.md` → Analizar specs y actualizar `plans/IMPLEMENTATION_PLAN.md` y `plans/TASKS.md`.
2. `02-domain.md` → Implementar capa de dominio puro en **Java 21**.
3. `03-application.md` → Implementar puertos, casos de uso y tests JUnit 5.
4. `04-adapters-and-tests.md` → Implementar microservicio en `backend/` (Java 21, REST/JPA PostgreSQL, `docker-compose.yml` para local, perfiles `local`/`dev`/`test`, JUnit 5, Cucumber BDD con reporte HTML, ArchUnit, `backend/README.md` y `backend/CLAUDE.md`).
5. `05-e2e-verification.md` → Ejecutar `mvnw.cmd clean verify`, verificar reporte HTML de Cucumber, generar la **Documentación Funcional Oficial en HTML** (`backend/docs/index.html`, ver skill `documentacion-funcional-html`), generar/actualizar los entregables de aceptación (`specs/VERIFICACION-DATOS.md`, `specs/PRUEBAS-CURL-CRUD.html` [CA-11], `specs/CRITERIOS-ACEPTACION.html`), aplicar la política de acceso a Supabase, y hacer commit y push al repositorio Git remoto indicado.

No saltes fases ni las ejecutes en paralelo: cada una parte del resultado de la anterior. Si se pide «continuar», consulta `plans/TASKS.md` para saber en qué fase quedó el trabajo.

## Reglas que aplican durante todo el pipeline
Viven en `.claude/rules/` y se cargan siempre: `un-servicio-un-repositorio.md`, `arquitectura-hexagonal.md`, `tests-y-cobertura.md`, `base-de-datos-sdd.md`, `supabase-acceso-datos.md`, `verificacion-final.md`.
