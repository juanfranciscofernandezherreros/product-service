# misc-service

Microservicio hexagonal (Java 21 + Spring Boot 3) para el dominio **Misc**: sugerencias, bitácora de usuario (categorías y anotaciones) y borradores de writing. Database-First (Flyway crea el esquema, JPA valida con `ddl-auto: validate`).

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/v1/suggestions` | Crear sugerencia (estado inicial `pendiente`) |
| GET | `/v1/suggestions/{id}` | Obtener sugerencia |
| GET | `/v1/suggestions?estado=&offset=&limit=` | Listar sugerencias (filtro por estado) |
| PATCH | `/v1/suggestions/{id}/status` | Cambiar estado (`pendiente`/`publicada`/`rechazada`) |
| POST | `/v1/users/{userId}/logbook/categories` | Crear categoría de bitácora |
| GET | `/v1/users/{userId}/logbook/categories` | Listar categorías del usuario |
| POST | `/v1/users/{userId}/logbook/notes` | Crear anotación (categoría opcional) |
| GET | `/v1/users/{userId}/logbook/notes` | Listar anotaciones del usuario |
| POST | `/v1/users/{userId}/writing-drafts` | Crear borrador de writing |
| GET | `/v1/users/{userId}/writing-drafts` | Listar borradores del usuario |

## Ejecución

```bash
docker compose up -d        # PostgreSQL local (misc_db)
mvn spring-boot:run         # perfil local, puerto 8089
```

## Calidad

```bash
mvn clean verify            # JUnit 5 + Mockito, ArchUnit, Cucumber BDD, Checkstyle, JaCoCo (≥80%)
```

- Reporte Cucumber: `target/cucumber-reports/cucumber.html`
- Reporte JaCoCo: `target/site/jacoco/index.html` (cobertura actual **85%**)
- Documentación funcional: [`docs/index.html`](docs/index.html)

## Perfiles

- `local`: PostgreSQL vía Docker (`jdbc:postgresql://localhost:5432/misc_db`).
- `dev`: PostgreSQL parametrizado por variables de entorno.
- `test`: H2 en memoria (modo PostgreSQL) con Flyway habilitado.
