# Study Plan Service

Microservicio REST del dominio **study-plan** (planes, plan IA, objetivos, sesiones y seguimiento, por usuario),
generado en modo **Database-First** (SDD): Flyway crea las tablas y JPA valida (`ddl-auto: validate`).
Java 21 · Spring Boot 3.3 · arquitectura hexagonal.

## Perfiles
| Perfil | Base de datos | Esquema |
|--------|---------------|---------|
| `local`| PostgreSQL `localhost:5432/study_plan_db` | Flyway + `validate` |
| `dev`  | PostgreSQL (env `DB_HOST_DEV`…)           | Flyway + `validate` |
| `test` | H2 (modo PostgreSQL)                      | Flyway + `validate` |

## Arranque
```bash
cd backend-study-plan
docker compose up -d
./mvnw spring-boot:run
```
API en `http://localhost:8086`.

## Endpoints (base `/v1/study-plan/users/{userId}`)
| Método | Ruta | Respuestas |
|--------|------|------------|
| GET/PUT | `/plan` | 200/404 · 200/400 |
| GET/PUT | `/ai-plan` | 200/404 · 200/400 |
| GET/PUT | `/goal` | 200/404 · 200/400 |
| GET/POST | `/sessions` | 200 · 201/400 |
| GET | `/weekly-reports` | 200 |

## Tests y cobertura
```bash
cd backend-study-plan
./mvnw clean verify
```
Cucumber: `target/cucumber-reports/cucumber.html` · JaCoCo (≥80%): `target/site/jacoco/index.html`.

## Documentación funcional
[`docs/index.html`](docs/index.html).

## Notas SDD
Esquema en `src/main/resources/db/migration/V1__init.sql` (de `specs/domains/study-plan.sql`); FK a `auth.users` eliminadas. Adaptaciones portables: `jsonb` (plan/informe) → VARCHAR JSON; `integer[]` (dias_semana) → CSV.
