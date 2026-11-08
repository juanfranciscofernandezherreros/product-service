# CLAUDE.md — backend-battle (Battle Service, Java 21, Hexagonal, Database-First)

## Stack
Spring Boot 3.3.5 · Java 21 · PostgreSQL + Flyway (SDD) · H2 (test) · Cucumber · ArchUnit · Checkstyle · JaCoCo.

## Comandos (en `backend-battle/`)
- `docker compose up -d` · `mvnw.cmd clean verify` · `mvnw.cmd spring-boot:run`
- Reportes: `target/cucumber-reports/cucumber.html`, `target/site/jacoco/index.html`

## SDD
DDL en `src/main/resources/db/migration/V1__init.sql`; Flyway crea, JPA `validate`. FK/DEFAULT a `auth.*` eliminadas.

## Arquitectura
- `domain`: BattleQuestion, BattleRoom, BattlePlayer + enums (Level, QuestionTopic, RoomTopic, RoomStatus, PlayerStatus).
- `application`: puertos + `BattleService`.
- `infrastructure`: controllers (`/v1/battle/questions`, `/v1/battle/rooms`), DTOs, persistencia JPA (opciones JSON, IdClass en jugadores), wiring `@Bean`.

## Errores
`BAD_REQUEST` (400), `RESOURCE_NOT_FOUND` (404), `RESOURCE_ALREADY_EXISTS` (409), `INTERNAL_ERROR` (500).
