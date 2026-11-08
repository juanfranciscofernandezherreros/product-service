# CLAUDE.md — backend-study-plan (Study Plan Service, Java 21, Hexagonal, Database-First)

## Stack
Spring Boot 3.3.5 · Java 21 · PostgreSQL + Flyway (SDD) · H2 (test) · Cucumber · ArchUnit · Checkstyle · JaCoCo.

## Comandos (en `backend-study-plan/`)
- `docker compose up -d` · `mvnw.cmd clean verify` · `mvnw.cmd spring-boot:run`
- Reportes: `target/cucumber-reports/cucumber.html`, `target/site/jacoco/index.html`

## SDD
DDL en `src/main/resources/db/migration/V1__init.sql`; Flyway crea, JPA `validate`. FK a `auth.users` eliminadas.
Adaptaciones portables: jsonb→VARCHAR JSON; integer[]→CSV.

## Arquitectura
- `domain`: StudyPlan, AiPlan, WeeklyGoal, StudySession, WeeklyReport + enums (Prioridad, GoalType, Tarea).
- `application`: puertos + `StudyPlanService`.
- `infrastructure`: controller (`/v1/study-plan/users/{userId}/...`), DTOs, persistencia JPA (IdClass en sesiones/reports), wiring `@Bean`.

## Errores
`BAD_REQUEST` (400), `RESOURCE_NOT_FOUND` (404), `INTERNAL_ERROR` (500).
