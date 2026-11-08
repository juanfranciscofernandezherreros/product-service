# Índice de especificaciones y documentación — AI CRUD

Punto único de entrada para entender el proceso y encontrar cada documento.
Objetivo: al añadir un **esquema nuevo**, se genera un **OpenAPI CRUD completo** y el
desarrollo sigue **todas** las especificaciones, produciendo todos los entregables.

## 1. Proceso (fuente de verdad)
- [`../CLAUDE.md`](../CLAUDE.md) — SDD raíz: reglas de arquitectura, Database-First, perfiles,
  **política de acceso a Supabase**, verificación final y **flujo para un esquema nuevo**.
- `.ai/prompts/00-db-to-openapi.md` — **Fase 0**: de DDL a OpenAPI **CRUD completo** + subrecursos.
- `.ai/prompts/01..05` — Fases: plan → dominio → aplicación → adaptadores/tests → verificación E2E.
- `.ai/services.txt` — manifiesto (un servicio = un repositorio; `SPEC_FILE` vacío ⇒ Database-First).

## 2. Contrato y modelo de datos (por servicio)
- `specs/crud-spec.yaml` — OpenAPI generado (o aportado en API-First).
- `specs/<servicio>.sql` — DDL PostgreSQL (fuente de verdad del modelo).
- `specs/domains/*.sql` — DDL por dominio ya derivado.
- `specs/supabase-schema.sql` — export completo del esquema de Supabase.

## 3. Documentación funcional (una por microservicio)
- `backend-<servicio>/docs/index.html` — propósito, endpoints con **request/response**,
  **modelo de datos exacto**, **diagrama SVG** y evidencias de calidad.

## 4. Criterios de aceptación y evidencia
- [`CRITERIOS-ACEPTACION.html`](CRITERIOS-ACEPTACION.html) — criterios **CA-01..CA-11** (Dado/Cuando/Entonces) + evidencia (build, cobertura, datos).
- [`PRUEBAS-CURL-CRUD.html`](PRUEBAS-CURL-CRUD.html) — **CA-11**: un `curl` por CADA endpoint de CADA servicio (secuencia CRUD + subrecursos).
- [`VERIFICACION-DATOS.md`](VERIFICACION-DATOS.md) — prueba de que cada endpoint principal **devuelve datos reales** de Supabase + política de acceso.
- [`../ACTA-CONFORMIDAD.html`](../ACTA-CONFORMIDAD.html) — acta de conformidad de la entrega.

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
| CA-08 | Política de acceso Supabase (REST+JWT / service_role) | CLAUDE.md + VERIFICACION-DATOS.md |
| CA-09 | Un servicio = un repositorio | manifiesto `.ai/services.txt` |
| CA-10 | Prueba por HTTP real del servicio | pendiente de credencial |
| CA-11 | `curl` CRUD completo por endpoint | PRUEBAS-CURL-CRUD.html |
