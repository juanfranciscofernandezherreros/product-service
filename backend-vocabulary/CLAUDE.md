# CLAUDE.md — backend-vocabulary (Vocabulary Service, Java 21, Hexagonal, Database-First)

## Stack
- Spring Boot 3.3.5 · Java 21 · PostgreSQL + Flyway (SDD) · H2 (test) · Cucumber BDD · ArchUnit · Checkstyle · JaCoCo.

## Comandos (en `backend-vocabulary/`)
- Levantar PostgreSQL: `docker compose up -d`
- Build + verificación (cobertura ≥ 80%): `mvnw.cmd clean verify`
- Arrancar (perfil `local`): `mvnw.cmd spring-boot:run`
- Reporte Cucumber: `target/cucumber-reports/cucumber.html`
- Reporte JaCoCo: `target/site/jacoco/index.html`

## SDD (esquema como fuente de verdad)
- DDL en `src/main/resources/db/migration/V1__init.sql` (derivado de `specs/domains/vocabulary.sql`).
- Flyway crea las tablas; `spring.jpa.hibernate.ddl-auto: validate` comprueba que las entidades coinciden 1:1.
- FK a `auth.users` eliminada; `id` pasa a IDENTITY para permitir inserciones.

## Arquitectura
- `domain`: `VocabularyItem`, `VocabularyProgress`, `Level`, excepciones. Java puro.
- `application`: puertos + `VocabularyService`.
- `infrastructure`: controllers (`/v1/vocabulary/items`, `/v1/vocabulary/users/{userId}/progress`), DTOs, mappers,
  `GlobalExceptionHandler` (`{code,message}`), persistencia JPA, wiring `@Bean`.

## Errores
- `BAD_REQUEST` (400), `RESOURCE_NOT_FOUND` (404), `RESOURCE_ALREADY_EXISTS` (409), `INTERNAL_ERROR` (500).

## Verificación final
`mvnw.cmd clean verify` en verde (Checkstyle + ArchUnit + JUnit + Cucumber + JaCoCo ≥ 80%) y docs generadas.

## Acceso a datos en Supabase (dev)
Política completa en el SDD raíz (`../.claude/rules/supabase-acceso-datos.md`) y en `../specs/VERIFICACION-DATOS.md`.
- Lecturas en nombre del usuario: por la **API REST de Supabase** reenviando su **JWT** (`Authorization: Bearer <jwt>` + `apikey: <clave publicable>`), respetando RLS. Sin secretos en el repo.
- Tareas de backend sin usuario: **`service_role` key** como secreto en variable de entorno (`SUPABASE_SERVICE_ROLE_KEY`), nunca en Git.
- La clave publicable solo alcanza tablas públicas; la contraseña `postgres` (JDBC) se salta RLS y queda para administración controlada.
