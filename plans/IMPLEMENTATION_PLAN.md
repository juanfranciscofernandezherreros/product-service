# Plan de Implementación — Transaction Service (Java 21 + Docker PostgreSQL)

## 1. Resumen de la Especificación
- Nombre del Microservicio: **transaction-service** (nuevo proyecto en `backend-transactions/`, sin tocar `product-service`).
- Versión de Java: **Java 21**
- Entidad principal: **Transaction** (id: UUID, amount: BigDecimal [>= 0.01], currency: String [3], type: credit|debit, status: pending|completed|failed|cancelled, account_id: UUID, description: opcional, created_at: date-time).
- Endpoints (`specs/crud-spec.yaml`, base `/v1`):
  - `POST   /v1/transactions`      → 201 / 400 / 500
  - `GET    /v1/transactions`      → 200 (paginado `limit`/`offset` + filtro `status`) / 400 / 500
  - `GET    /v1/transactions/{id}` → 200 / 400 / 404 / 500

## 2. Diseño por Capas (Hexagonal)
- **Domain:** `Transaction` inmutable con invariantes; enums `TransactionType`, `TransactionStatus`; excepciones de dominio. Java puro.
- **Application:** puertos `in`/`out`, `TransactionService` (crear, obtener, listar paginado/filtrado), inyección por constructor.
- **Infrastructure:**
  - `adapter.in.web`: `TransactionController`, DTOs (`TransactionRequest`, `TransactionResponse`, `PagedTransactionsResponse`, `ErrorResponse {code,message}`), mapper, `GlobalExceptionHandler`.
  - `adapter.out.persistence`: `TransactionJpaEntity`, repositorio Spring Data con filtro por estado y paginación por offset, adapter y mapper.
  - `config`: wiring `@Bean` + perfiles (`local`, `dev`, `test`).

## 3. Testing, Reportes y Documentación
- **ArchUnit:** aislamiento del dominio.
- **JUnit 5 + Mockito:** dominio, casos de uso y controlador (`@WebMvcTest`).
- **Cucumber BDD:** escenarios de `specs/acceptance-criteria.md` con reporte HTML (`target/cucumber-reports/cucumber.html`).
- **Cobertura:** JaCoCo con regla `check` ≥ **80%** global enlazada a `verify`; informe en `target/site/jacoco/index.html`.
- **Docs:** `backend-transactions/README.md`, `backend-transactions/CLAUDE.md`, `backend-transactions/docs/index.html` **con Request y Response de cada endpoint**.
- **Docker:** `backend-transactions/docker-compose.yml` (PostgreSQL `transaction_db`).

## 4. Modelo de error
- `{ code, message }` — 400 `INVALID_REQUEST`, 404 `NOT_FOUND`, 500 `INTERNAL_ERROR`.
