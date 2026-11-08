# Plan de Implementación — Fund Service (Java 21 + Docker PostgreSQL)

## 1. Resumen de la Especificación
- Microservicio: **fund-service** (nuevo proyecto en `backend-funds/`, sin tocar los demás).
- Java 21. Contrato: `specs/crud-spec.yaml` (Fondos de Inversión v1.1.0, base `/v1`).
- Agregados: **Fund** (clave natural ISIN), **NavPoint** (histórico por fondo), **Order** (suscripción/reembolso).
- Endpoints:
  - `GET    /v1/funds`            → 200 (paginado + filtro `category`/`currency`)
  - `POST   /v1/funds`           → 201 / 400 / 409
  - `GET    /v1/funds/{isin}`    → 200 / 404
  - `PUT    /v1/funds/{isin}`    → 200 / 400 / 404
  - `DELETE /v1/funds/{isin}`    → 204 / 404 / 422
  - `GET    /v1/funds/{isin}/nav`→ 200 (filtro `startDate`/`endDate`)
  - `POST   /v1/orders`          → 201

## 2. Diseño por Capas (Hexagonal)
- **Domain:** `Fund`, `NavPoint`, `Order` (inmutables, invariantes); enums `OperationType`, `OrderStatus`; excepciones (`FundNotFound`, `FundAlreadyExists`, `InvalidFund`, `InvalidOrder`, `FundHasActivePositions`).
- **Application:** puertos `in`/`out` + casos de uso (`FundService`, `NavService`, `OrderService`), inyección por constructor.
- **Infrastructure:** adaptadores web (controllers Funds/Nav/Orders, DTOs, mappers, `GlobalExceptionHandler` con `{code,message}`), persistencia JPA (Fund/Nav/Order), perfiles y wiring `@Bean`.

## 3. Reglas e interpretaciones
- Error con forma `{ code, message }`: `VALIDATION_ERROR`/`BAD_REQUEST` (400), `RESOURCE_NOT_FOUND` (404), `RESOURCE_ALREADY_EXISTS` (409), `FUND_HAS_ACTIVE_POSITIONS` (422).
- **422:** un fondo tiene posiciones activas si existe una orden suya con estado ≠ `REJECTED`.
- `currentNav` y el histórico NAV derivan de `NavPoint` (el contrato solo expone lectura del NAV).

## 4. Testing, Reportes, Cobertura y Documentación
- ArchUnit (aislamiento del dominio), JUnit 5 + Mockito (dominio, casos de uso, `@WebMvcTest`), Cucumber BDD (reporte HTML).
- **JaCoCo ≥ 80%** global (regla `check` en `verify`).
- Docs: `README.md`, `CLAUDE.md`, `docs/index.html` (con **diagrama SVG** y **Request/Response por endpoint**).
- Docker: `docker-compose.yml` (PostgreSQL `fund_db`).
