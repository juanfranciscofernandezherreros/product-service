# Índice de especificaciones y documentación — AI CRUD

Punto único de entrada para entender el proceso y encontrar cada documento.
Objetivo: al añadir un **esquema nuevo**, se genera un **OpenAPI CRUD completo** y el
desarrollo sigue **todas** las especificaciones, produciendo todos los entregables.

Este repositorio es solo el generador. Las rutas de la sección 3 y 4 son las de **cada
servicio generado**, en su propio repositorio.

## 1. Proceso (fuente de verdad)
- [`../CLAUDE.md`](../CLAUDE.md) — SDD raíz (índice). Las normas están en [`../.claude/rules/`](../.claude/rules/)
  (arquitectura, Database-First/SDD, perfiles, **política de acceso a Supabase**, verificación final)
  y los procedimientos en [`../.claude/skills/`](../.claude/skills/) (pipeline y **flujo para un esquema nuevo**).
- `.ai/prompts/00-db-to-openapi.md` — **Fase 0**: de DDL a OpenAPI **CRUD completo** + subrecursos.
- `.ai/prompts/01..05` — Fases: plan → dominio → aplicación → adaptadores/tests → verificación E2E.
- `.ai/services.txt` — manifiesto (un servicio = un repositorio; `SPEC_FILE` vacío ⇒ Database-First).
- `run-ai-crud.bat` / `run-ai-crud.sh` — ejecutan el pipeline por cada servicio del manifiesto.
- `scripts/validate_specs.py` — valida el manifiesto y los contratos OpenAPI (también en CI).

## 2. Contrato y modelo de datos (entradas del generador)
- `specs/domains/<servicio>.sql` — DDL PostgreSQL por servicio (fuente de verdad del modelo).
- `specs/openapi/<servicio>.yaml` — OpenAPI por servicio (derivado del DDL, o aportado en API-First).
- `specs/acceptance/<SERVICE_NAME>.md` — criterios de aceptación de entrada (opcional).
- `specs/supabase-schema.sql` — export completo del esquema de Supabase (lo genera `scripts/export_supabase_schema.py`).
- `scripts/check_domains.py` — compara `specs/domains/` con ese export (tablas, columnas y tipos).
- `templates/database-spec.sql` — plantilla comentada para escribir un DDL nuevo.
- `examples/fund-service/` — ejemplo de contrato y criterios en API-First.

En el servicio generado, el script los deja como `specs/database-spec.sql`, `specs/crud-spec.yaml`
y `specs/acceptance-criteria.md`.

## 3. Documentación funcional (una por microservicio)
- `backend/docs/index.html` — propósito, endpoints con **request/response**,
  **modelo de datos exacto**, **diagrama SVG** y evidencias de calidad.

## 4. Criterios de aceptación y evidencia (por microservicio)
- `specs/CRITERIOS-ACEPTACION.html` — criterios **CA-01..CA-11** (Dado/Cuando/Entonces) + evidencia (build, cobertura, datos).
- `specs/PRUEBAS-CURL-CRUD.html` — **CA-11**: un `curl` por CADA endpoint (secuencia CRUD + subrecursos).
- `specs/VERIFICACION-DATOS.md` — prueba de que cada endpoint principal **devuelve datos reales** de Supabase + política de acceso.

Evidencia histórica de la ejecución anterior (nueve servicios, cuando vivían en este repositorio):
[`../evidence/`](../evidence/README.md).

## 5. Criterios de aceptación (resumen)
| ID | Criterio | Dónde se evidencia |
|----|----------|--------------------|
| CA-01 | `mvn clean verify` en BUILD SUCCESS | CRITERIOS-ACEPTACION.html §2 |
| CA-02 | Cobertura JaCoCo ≥ 80% | CRITERIOS-ACEPTACION.html §2 |
| CA-03 | Test unitario + Cucumber por endpoint | reporte `cucumber.html` |
| CA-04 | SDD: Flyway crea / JPA valida (local/test) | `V1__init.sql` + `validate` |
| CA-05 | Aislamiento hexagonal (ArchUnit) | `HexagonalArchitectureTest` |
| CA-06 | `docs/index.html`: diagrama + request/response + esquema | docs por servicio |
| CA-07 | Cada endpoint principal devuelve datos reales | VERIFICACION-DATOS.md |
| CA-08 | Política de acceso Supabase (REST+JWT / service_role) | `.claude/rules/supabase-acceso-datos.md` + VERIFICACION-DATOS.md |
| CA-09 | Un servicio = un repositorio | manifiesto `.ai/services.txt` + CI |
| CA-10 | Prueba por HTTP real del servicio | pendiente de credencial |
| CA-11 | `curl` CRUD completo por endpoint | PRUEBAS-CURL-CRUD.html |
