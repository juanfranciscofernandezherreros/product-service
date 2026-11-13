# Plan de Implementación — <SERVICE_NAME> (Java 21 + Docker PostgreSQL)

> Plantilla. La Fase 1 (`.ai/prompts/01-plan-and-tasks.md`) la completa a partir de las especificaciones del servicio.

## 1. Resumen de la Especificación
- Microservicio: `<SERVICE_NAME>` (de `.ai/project-context.txt`).
- Contrato: `specs/crud-spec.yaml`. Esquema: `specs/database-spec.sql` (fuente de verdad).
- Agregados y endpoints: _por completar_.

## 2. Diseño por Capas (Hexagonal)
- **Domain:** _por completar_.
- **Application:** _por completar_.
- **Infrastructure:** _por completar_.

## 3. Reglas e interpretaciones
- _por completar_ (formato de error, códigos HTTP, reglas de negocio).

## 4. Base de datos y entornos
- Nombre de la base, tablas, Flyway (`V1__init.sql`) y perfiles `local` / `dev` / `test`: _por completar_.

## 5. Testing, Reportes, Cobertura y Documentación
- ArchUnit, JUnit 5 + Mockito, Cucumber BDD (reporte HTML), JaCoCo ≥ 80%, `docs/index.html`: _por completar_.
