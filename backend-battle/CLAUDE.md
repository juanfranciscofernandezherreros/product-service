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

## Acceso a datos en Supabase (dev)
Política completa en el SDD raíz (`../.claude/rules/supabase-acceso-datos.md`) y en `../specs/VERIFICACION-DATOS.md`.
- Lecturas en nombre del usuario: por la **API REST de Supabase** reenviando su **JWT** (`Authorization: Bearer <jwt>` + `apikey: <clave publicable>`), respetando RLS. Sin secretos en el repo.
- Tareas de backend sin usuario: **`service_role` key** como secreto en variable de entorno (`SUPABASE_SERVICE_ROLE_KEY`), nunca en Git.
- La clave publicable solo alcanza tablas públicas; la contraseña `postgres` (JDBC) se salta RLS y queda para administración controlada.
