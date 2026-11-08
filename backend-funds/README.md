# Fund Service

Microservicio REST para la **gestión de fondos de inversión** (CRUD completo + histórico NAV + órdenes),
con **Spring Boot 3.3 / Java 21**, **arquitectura hexagonal**, PostgreSQL y pruebas
(JUnit 5, ArchUnit, Cucumber BDD). Cobertura verificada con JaCoCo (≥ 80%).

## Requisitos
- JDK 21 · Maven (wrapper incluido) · PostgreSQL (perfiles `local`/`dev`; tests con H2)

## Perfiles
| Perfil  | Base de datos                          | ddl-auto     |
|---------|----------------------------------------|--------------|
| `local` | PostgreSQL `localhost:5432/fund_db`    | `update`     |
| `dev`   | PostgreSQL (env `DB_HOST_DEV`, …)      | `validate`   |
| `test`  | H2 en memoria (modo PostgreSQL)        | `create-drop`|

## Arranque
```bash
cd backend-funds
docker compose up -d
./mvnw spring-boot:run
```
API en `http://localhost:8082`.

## Endpoints (base `/v1`)
| Método | Ruta | Respuestas |
|--------|------|------------|
| GET | `/v1/funds` | 200 (paginado + filtros `category`/`currency`) |
| POST | `/v1/funds` | 201 / 400 / 409 |
| GET | `/v1/funds/{isin}` | 200 / 404 |
| PUT | `/v1/funds/{isin}` | 200 / 400 / 404 |
| DELETE | `/v1/funds/{isin}` | 204 / 404 / 422 |
| GET | `/v1/funds/{isin}/nav` | 200 (filtros `startDate`/`endDate`) |
| POST | `/v1/orders` | 201 |

### Ejemplo
```bash
curl -X POST http://localhost:8082/v1/funds -H "Content-Type: application/json" \
  -d '{"isin":"ES0123456789","name":"Global Growth FI","category":"RV","currency":"EUR","riskLevel":5}'
```

## Tests y cobertura
```bash
cd backend-funds
./mvnw clean verify
```
Reporte Cucumber: `target/cucumber-reports/cucumber.html` · Cobertura JaCoCo: `target/site/jacoco/index.html`.

## Documentación funcional
[`docs/index.html`](docs/index.html) — diagrama de arquitectura y Request/Response por endpoint.

## Nota de interpretación
El contrato no define un recurso de "posiciones"; un fondo tiene **posiciones activas** cuando existe una
orden suya con estado distinto de `REJECTED` (regla usada por el `DELETE` → 422). `currentNav` y el histórico
NAV derivan de los `NavPoint` (el contrato solo expone su lectura).
