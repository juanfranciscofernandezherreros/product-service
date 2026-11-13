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
- `generator/` + `scripts/generate.py` — generador determinista: del DDL al servicio completo, sin IA.
- `.ai/prompts/fix-build.md`, `.ai/prompts/business-rules.md` — lo único que hace la IA en el modo por defecto.
- `.ai/prompts/00..05` — pipeline antiguo por fases (`--mode ai`): plan → dominio → aplicación → adaptadores/tests → verificación.
- `.ai/services.txt` — manifiesto (un servicio = un repositorio).
- `run-ai-crud.bat` / `run-ai-crud.sh` — pipeline completo por cada servicio del manifiesto.
- `scripts/validate_specs.py` — valida el manifiesto y los contratos OpenAPI (también en CI).

## 2. Contrato y modelo de datos (entradas del generador)
- `specs/supabase-schema.sql` — export del esquema de Supabase (`scripts/export_supabase_schema.py`). Origen de todo.
- `specs/domain-map.txt` — reparto de tablas entre servicios.
- `specs/domains/<dominio>.sql` — DDL por servicio, recortado del export (`scripts/split_domains.py`); `scripts/check_domains.py` comprueba que es igual que Supabase.
- `specs/openapi/<dominio>.yaml` — OpenAPI por servicio, derivado del DDL (`scripts/generate.py --openapi`).
- `specs/business-rules/<SERVICE_NAME>.md` — reglas de negocio que no salen del esquema (opcional).
- `examples/fund-service/` — ejemplo de contrato y criterios en API-First (modo `--mode ai`).

En el servicio generado quedan como `specs/database-spec.sql` y `specs/crud-spec.yaml`.

## 3. Documentación funcional (una por microservicio)
- `backend/docs/index.html` — propósito, endpoints con **request/response**,
  **modelo de datos exacto**, **diagrama SVG** y evidencias de calidad.

## 4. Criterios de aceptación y evidencia (por microservicio)
- `specs/CRITERIOS-ACEPTACION.html` — criterios **CA-01..CA-11** + evidencia (no lo escribe el generador; se elabora al aceptar la entrega).
- `specs/PRUEBAS-CURL-CRUD.html` — **CA-11**: un `curl` por CADA endpoint (secuencia CRUD + subrecursos).
- `specs/VERIFICACION-DATOS.md` — prueba de que cada endpoint principal **devuelve datos reales** de Supabase (requiere credenciales; no lo escribe el generador).

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
