# Vocabulary Service

Microservicio REST del dominio **vocabulary**, generado en modo **Database-First** a partir del
esquema PostgreSQL (SDD): **Flyway** crea las tablas y **JPA valida** (`ddl-auto: validate`).
Java 21 · Spring Boot 3.3 · arquitectura hexagonal.

## Requisitos
- JDK 21 · Maven (wrapper incluido) · PostgreSQL (perfiles `local`/`dev`; tests con H2 + Flyway)

## Perfiles
| Perfil  | Base de datos                            | Esquema |
|---------|------------------------------------------|---------|
| `local` | PostgreSQL `localhost:5432/vocabulary_db`| Flyway + `validate` |
| `dev`   | PostgreSQL (env `DB_HOST_DEV`, …)        | Flyway + `validate` |
| `test`  | H2 en memoria (modo PostgreSQL)          | Flyway + `validate` |

## Arranque
```bash
cd backend-vocabulary
docker compose up -d
./mvnw spring-boot:run
```
API en `http://localhost:8083`.

## Endpoints (base `/v1/vocabulary`)
| Método | Ruta | Respuestas |
|--------|------|------------|
| POST | `/v1/vocabulary/items` | 201 / 400 / 409 |
| GET | `/v1/vocabulary/items` | 200 (filtro `level`, `limit`, `offset`) |
| GET | `/v1/vocabulary/items/{id}` | 200 / 404 |
| PUT | `/v1/vocabulary/items/{id}` | 200 / 400 / 404 |
| DELETE | `/v1/vocabulary/items/{id}` | 204 / 404 |
| GET | `/v1/vocabulary/users/{userId}/progress` | 200 |
| PUT | `/v1/vocabulary/users/{userId}/progress` | 200 / 400 / 404 |

### Ejemplo
```bash
curl -X POST http://localhost:8083/v1/vocabulary/items \
  -H "Content-Type: application/json" \
  -d '{"en":"book","es":"libro","example":"a book","level":"A1"}'
```

## Tests y cobertura
```bash
cd backend-vocabulary
./mvnw clean verify
```
Reporte Cucumber: `target/cucumber-reports/cucumber.html` · Cobertura JaCoCo (≥80%): `target/site/jacoco/index.html`.

## Documentación funcional
[`docs/index.html`](docs/index.html) — diagrama, request/response por endpoint y esquema exacto.

## Nota SDD
El esquema es la fuente de verdad (`src/main/resources/db/migration/V1__init.sql`, derivado del export
Supabase). Se eliminó la FK a `auth.users` (otro schema): `user_id` queda como `uuid` simple.
