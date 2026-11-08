# Exams Service

Microservicio REST del dominio **exams** (exámenes, preguntas, resultados, diagnósticos y simulacros),
generado en modo **Database-First** (SDD): Flyway crea las tablas y JPA valida (`ddl-auto: validate`).
Java 21 · Spring Boot 3.3 · arquitectura hexagonal.

## Perfiles
| Perfil | Base de datos | Esquema |
|--------|---------------|---------|
| `local`| PostgreSQL `localhost:5432/exams_db` | Flyway + `validate` |
| `dev`  | PostgreSQL (env `DB_HOST_DEV`…)      | Flyway + `validate` |
| `test` | H2 (modo PostgreSQL)                 | Flyway + `validate` |

## Arranque
```bash
cd backend-exams
docker compose up -d
./mvnw spring-boot:run
```
API en `http://localhost:8088`.

## Endpoints (base `/v1/exams`)
| Método | Ruta | Respuestas |
|--------|------|------------|
| POST/GET | `/v1/exams` | 201/400/409 · 200 (filtro nivel/publicado) |
| GET/PUT/DELETE | `/v1/exams/{id}` | 200/404 · 200/400/404 · 204/404 |
| GET/POST | `/v1/exams/{id}/questions` | 200/404 · 201/400/404/409 |
| POST | `/v1/exams/results` | 201/400/404 |
| GET | `/v1/exams/results/users/{userId}` | 200 |
| POST | `/v1/exams/diagnostics` | 201/400 |
| POST | `/v1/exams/simulations` | 201/400 |

## Tests y cobertura
```bash
cd backend-exams
./mvnw clean verify
```
Cucumber: `target/cucumber-reports/cucumber.html` · JaCoCo (≥80%): `target/site/jacoco/index.html`.

## Documentación funcional
[`docs/index.html`](docs/index.html).

## Notas SDD
Esquema en `src/main/resources/db/migration/V1__init.sql` (de `specs/domains/exams.sql`); FK a `auth.users` eliminadas; jsonb (opciones/respuestas/desglose) → VARCHAR JSON.
