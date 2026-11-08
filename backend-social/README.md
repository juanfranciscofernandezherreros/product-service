# Social Service

Microservicio REST del dominio **social** (perfiles públicos, seguimientos, mensajes privados y solicitudes),
generado en modo **Database-First** (SDD): Flyway crea las tablas y JPA valida (`ddl-auto: validate`).
Java 21 · Spring Boot 3.3 · arquitectura hexagonal.

## Perfiles
| Perfil | Base de datos | Esquema |
|--------|---------------|---------|
| `local`| PostgreSQL `localhost:5432/social_db` | Flyway + `validate` |
| `dev`  | PostgreSQL (env `DB_HOST_DEV`…)       | Flyway + `validate` |
| `test` | H2 (modo PostgreSQL)                  | Flyway + `validate` |

## Arranque
```bash
cd backend-social
docker compose up -d
./mvnw spring-boot:run
```
API en `http://localhost:8087`.

## Endpoints (base `/v1/social`)
| Método | Ruta | Respuestas |
|--------|------|------------|
| POST | `/public-profiles` | 201/400/409 |
| GET/PUT | `/public-profiles/{userId}` | 200/404 · 200/400/404 |
| GET/POST | `/users/{userId}/following` | 200 · 201/400 |
| DELETE | `/users/{userId}/following/{seguido}` | 204/404 |
| POST | `/messages` | 201/400 |
| POST | `/message-requests` | 201/400/409 |
| PUT | `/message-requests/{id}` | 200/400/404 |

## Tests y cobertura
```bash
cd backend-social
./mvnw clean verify
```
Cucumber: `target/cucumber-reports/cucumber.html` · JaCoCo (≥80%): `target/site/jacoco/index.html`.

## Documentación funcional
[`docs/index.html`](docs/index.html).

## Notas SDD
Esquema en `src/main/resources/db/migration/V1__init.sql` (de `specs/domains/social.sql`); FK a `auth.users` eliminadas. El patrón de `alias` y el `avatar_emoji` (nombres simbólicos: BOOK, GRAD…) se validan en el dominio para portabilidad H2/Flyway.
