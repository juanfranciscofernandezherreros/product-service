# CLAUDE.md — backend-funds (Fund Service, Java 21, Hexagonal)

## Stack
- Spring Boot 3.3.5 · Java 21 · PostgreSQL (runtime) · H2 (test) · Cucumber BDD · ArchUnit · Checkstyle · JaCoCo.

## Comandos (en `backend-funds/`)
- Levantar PostgreSQL: `docker compose up -d`
- Build + verificación (incluye cobertura ≥ 80%): `mvnw.cmd clean verify`
- Tests unitarios: `mvnw.cmd test`
- Arrancar (perfil `local`): `mvnw.cmd spring-boot:run`
- Reporte Cucumber: `target/cucumber-reports/cucumber.html`
- Reporte JaCoCo: `target/site/jacoco/index.html`
- Documentación funcional: `docs/index.html`

## Perfiles
- `local` (def.): PostgreSQL `localhost:5432/fund_db`, `ddl-auto=update`.
- `dev`: PostgreSQL por env (`DB_HOST_DEV`, `DB_NAME_DEV`, `DB_USER_DEV`, `DB_PASS_DEV`), `ddl-auto=validate`.
- `test`: H2 en memoria, `ddl-auto=create-drop`.

## Arquitectura
- `domain`: `Fund`, `NavPoint`, `Order`, enums `OperationType`/`OrderStatus`, excepciones. Java puro.
- `application`: puertos + `FundService`/`NavService`/`OrderService` (inyección por constructor).
- `infrastructure`: controllers REST (`/v1/funds`, `/v1/funds/{isin}/nav`, `/v1/orders`), DTOs, mappers,
  `GlobalExceptionHandler` (`{code,message}`), persistencia JPA y wiring `@Bean`.

## Contrato y reglas
- Fuente de verdad: `specs/crud-spec.yaml` (Fondos v1.1.0).
- Errores: `BAD_REQUEST` (400), `RESOURCE_NOT_FOUND` (404), `RESOURCE_ALREADY_EXISTS` (409),
  `FUND_HAS_ACTIVE_POSITIONS` (422), `INTERNAL_ERROR` (500).
- 422: un fondo con una orden ≠ `REJECTED` tiene posiciones activas.

## Verificación final
`mvnw.cmd clean verify` en verde (Checkstyle + ArchUnit + JUnit + Cucumber + JaCoCo ≥ 80%) y docs generadas.
