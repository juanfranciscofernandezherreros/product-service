# Battle Service

Microservicio REST del dominio **battle** (preguntas, salas y jugadores), generado en modo
**Database-First** (SDD): Flyway crea las tablas y JPA valida (`ddl-auto: validate`).
Java 21 · Spring Boot 3.3 · arquitectura hexagonal.

## Perfiles
| Perfil | Base de datos | Esquema |
|--------|---------------|---------|
| `local`| PostgreSQL `localhost:5432/battle_db` | Flyway + `validate` |
| `dev`  | PostgreSQL (env `DB_HOST_DEV`…)       | Flyway + `validate` |
| `test` | H2 (modo PostgreSQL)                  | Flyway + `validate` |

## Arranque
```bash
cd backend-battle
docker compose up -d
./mvnw spring-boot:run
```
API en `http://localhost:8085`.

## Endpoints (base `/v1/battle`)
| Método | Ruta | Respuestas |
|--------|------|------------|
| POST/GET | `/v1/battle/questions` | 201/400/409 · 200 (filtro level/topic) |
| GET/PUT/DELETE | `/v1/battle/questions/{id}` | 200/404 · 200/400/404 · 204/404 |
| POST/GET | `/v1/battle/rooms` | 201/400 · 200 (filtro status) |
| GET | `/v1/battle/rooms/{roomId}` | 200/404 |
| GET/POST | `/v1/battle/rooms/{roomId}/players` | 200/404 · 201/400/404 |

## Tests y cobertura
```bash
cd backend-battle
./mvnw clean verify
```
Cucumber: `target/cucumber-reports/cucumber.html` · JaCoCo (≥80%): `target/site/jacoco/index.html`.

## Documentación funcional
[`docs/index.html`](docs/index.html).

## Notas SDD
Esquema en `src/main/resources/db/migration/V1__init.sql` (de `specs/domains/battle.sql`); FK/DEFAULT a `auth.*` eliminadas; `id`/`code` se generan en la aplicación; `options` se almacena como JSON. Alcance: preguntas, salas y jugadores (subconjunto del dominio battle).
