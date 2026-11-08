# CLAUDE.md — backend-exams (Exams Service, Java 21, Hexagonal, Database-First)

## Stack
Spring Boot 3.3.5 · Java 21 · PostgreSQL + Flyway (SDD) · H2 (test) · Cucumber · ArchUnit · Checkstyle · JaCoCo.

## Comandos (en `backend-exams/`)
- `docker compose up -d` · `mvnw.cmd clean verify` · `mvnw.cmd spring-boot:run`
- Reportes: `target/cucumber-reports/cucumber.html`, `target/site/jacoco/index.html`

## SDD
DDL en `src/main/resources/db/migration/V1__init.sql`; Flyway crea, JPA `validate`. FK a `auth.users` eliminadas.
jsonb → VARCHAR JSON (opciones como lista; respuestas/desglose como JSON crudo).

## Arquitectura
- `domain`: Exam, Question, Result, Diagnostic, Simulation + enum Modalidad.
- `application`: puertos + `ExamsService`.
- `infrastructure`: controllers (`/v1/exams/...`), DTOs, persistencia JPA, wiring `@Bean`.

## Errores
`BAD_REQUEST` (400), `RESOURCE_NOT_FOUND` (404), `RESOURCE_ALREADY_EXISTS` (409), `INTERNAL_ERROR` (500).
