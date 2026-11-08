# CLAUDE.md — backend-transactions (Transaction Service, Java 21, Hexagonal)

## Stack
- Spring Boot 3.3.5 · Java 21 · PostgreSQL (runtime) · H2 (test) · Cucumber BDD · ArchUnit · Checkstyle.

## Comandos (en `backend-transactions/`)
- Levantar PostgreSQL local: `docker compose up -d`
- Build + verificación: `mvnw.cmd clean verify` (o `./mvnw clean verify`)
- Tests unitarios: `mvnw.cmd test`
- Arrancar (perfil `local`): `mvnw.cmd spring-boot:run`
- Reporte Cucumber (tras `verify`): `backend-transactions/target/cucumber-reports/cucumber.html`
- Documentación funcional: `backend-transactions/docs/index.html`

## Perfiles
- `local` (por defecto): PostgreSQL `localhost:5432/transaction_db` (vía `docker compose up -d`), `ddl-auto=update`.
- `dev`: PostgreSQL por env vars (`DB_HOST_DEV`, `DB_NAME_DEV`, `DB_USER_DEV`, `DB_PASS_DEV`), `ddl-auto=validate`.
- `test`: H2 en memoria (modo PostgreSQL), `ddl-auto=create-drop`. Lo usan los tests.

## Reglas de arquitectura
1. Hexagonal estricto: `domain` Java puro (sin Spring/JPA/Lombok); `application` puertos + casos de uso; `infrastructure` adaptadores REST/JPA.
2. Inyección por constructor (`private final`); prohibido `@Autowired` en campos.
3. Wiring explícito en `infrastructure.config.BeanConfiguration` (`@Bean`).
4. Cobertura por endpoint: JUnit 5 + Mockito y escenario Cucumber BDD.
5. ArchUnit valida el aislamiento del dominio.

## Contrato
- Fuente de verdad: `specs/crud-spec.yaml` (API de Transacciones). Error `{ code, message }`.
- Endpoints base `/v1/transactions`: POST (201/400/500), GET lista paginada/filtrada (200/400/500), GET /{id} (200/400/404/500).

## Verificación final
`mvnw.cmd clean verify` en verde (Checkstyle + ArchUnit + JUnit + Cucumber) y reporte HTML generado.
