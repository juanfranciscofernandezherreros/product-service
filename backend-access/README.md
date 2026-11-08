# Access Service

Microservicio REST del dominio **access** (roles, permisos y asignaciones), generado en modo
**Database-First** (SDD): Flyway crea las tablas y JPA valida (`ddl-auto: validate`).
Java 21 · Spring Boot 3.3 · arquitectura hexagonal.

## Perfiles
| Perfil | Base de datos | Esquema |
|--------|---------------|---------|
| `local`| PostgreSQL `localhost:5432/access_db` | Flyway + `validate` |
| `dev`  | PostgreSQL (env `DB_HOST_DEV`…)       | Flyway + `validate` |
| `test` | H2 (modo PostgreSQL)                  | Flyway + `validate` |

## Arranque
```bash
cd backend-access
docker compose up -d
./mvnw spring-boot:run
```
API en `http://localhost:8084`.

## Endpoints (base `/v1/access`)
| Método | Ruta | Respuestas |
|--------|------|------------|
| GET/POST | `/v1/access/roles` | 200 · 201/400/409 |
| GET/DELETE | `/v1/access/roles/{codigo}` | 200/404 · 204/404 |
| GET/POST | `/v1/access/roles/{codigo}/permissions` | 200/404 · 201/404 |
| GET/POST | `/v1/access/permissions` | 200 · 201/400/409 |
| GET/POST | `/v1/access/users/{userId}/roles` | 200 · 201/400/404 |

## Tests y cobertura
```bash
cd backend-access
./mvnw clean verify
```
Cucumber: `target/cucumber-reports/cucumber.html` · JaCoCo (≥80%): `target/site/jacoco/index.html`.

## Documentación funcional
[`docs/index.html`](docs/index.html).

## Nota SDD
Esquema en `src/main/resources/db/migration/V1__init.sql` (de `specs/domains/access.sql`); FK a `auth.users` eliminadas.
