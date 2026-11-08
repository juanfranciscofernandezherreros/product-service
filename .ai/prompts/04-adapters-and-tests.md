Lee `.ai/project-context.txt` para usar el `SERVICE_NAME` exacto.
Configura `backend/pom.xml` con **Java 21** (`<java.version>21</java.version>`), `<artifactId>` igual al `SERVICE_NAME`, Spring Boot 3, PostgreSQL, Checkstyle, ArchUnit, JUnit 5 y Cucumber BDD.
Implementa:
1. `docker-compose.yml` en la raíz de `backend/` para levantar PostgreSQL en local (`port: 5432`, `db: product_db`, `user: postgres`, `password: postgres`).
2. Adaptadores de salida (Spring Data JPA para PostgreSQL con entidades separadas del dominio y mappers) y adaptadores de entrada (Controladores REST con `jakarta.validation`).
3. Perfiles `application.yml` (`spring.application.name` igual a `SERVICE_NAME`), `application-local.yml`, `application-dev.yml` y `application-test.yml` según `CLAUDE.md`.
4. Tests unitarios/WebMvcTest en JUnit 5 y escenarios en Cucumber BDD (`.feature` + Step Definitions) con reporte HTML en `target/cucumber-reports/cucumber.html` por cada endpoint de `specs/crud-spec.yaml`.
5. Test de ArchUnit verificando que `domain` no depende de `infrastructure`.
6. Configura **JaCoCo** (`jacoco-maven-plugin`) con `report` y una regla `check` (`BUNDLE`, `LINE` ≥ 0.80) enlazada a `verify`, y escribe los tests necesarios para superar el **80% de cobertura global** del microservicio.
7. `backend/README.md` y `backend/CLAUDE.md` (documentación y guía operativa para arrancar PostgreSQL con Docker, compilar, probar y verificar).
Actualiza `plans/TASKS.md` al terminar.
