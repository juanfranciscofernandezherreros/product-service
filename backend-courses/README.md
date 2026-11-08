# courses-service

Microservicio hexagonal (Java 21 + Spring Boot 3) para el dominio **Courses**: cursos, unidades, actividades, preguntas de actividad e intentos. Database-First (Flyway crea el esquema, JPA valida con `ddl-auto: validate`).

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET/POST | `/v1/courses` | Listar (filtro activo) / crear curso |
| GET/PUT/DELETE | `/v1/courses/{id}` | Obtener / actualizar / eliminar curso |
| GET/POST | `/v1/courses/units` | Listar (filtro nivel/publicado) / crear unidad |
| GET | `/v1/courses/units/{id}` | Obtener unidad |
| GET/POST | `/v1/courses/activities` | Listar (filtro destreza/publicado, paginado) / crear actividad |
| GET | `/v1/courses/activities/{id}` | Obtener actividad |
| GET/POST | `/v1/courses/activities/{id}/questions` | Preguntas de una actividad |
| POST | `/v1/courses/activities/{id}/attempts` | Registrar intento de actividad |

## Ejecución

```bash
docker compose up -d        # PostgreSQL local (courses_db)
mvn spring-boot:run         # perfil local, puerto 8091
```

## Calidad

```bash
mvn clean verify            # JUnit 5 + Mockito, ArchUnit, Cucumber BDD, Checkstyle, JaCoCo (≥80%)
```

- Reporte Cucumber: `target/cucumber-reports/cucumber.html`
- Reporte JaCoCo: `target/site/jacoco/index.html` (cobertura actual **86%**)
- Documentación funcional: [`docs/index.html`](docs/index.html)

## Perfiles

- `local`: PostgreSQL vía Docker (`jdbc:postgresql://localhost:5432/courses_db`).
- `dev`: PostgreSQL parametrizado por variables de entorno.
- `test`: H2 en memoria (modo PostgreSQL) con Flyway habilitado.
