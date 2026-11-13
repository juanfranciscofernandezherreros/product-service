Lee `.ai/project-context.txt` para usar el `SERVICE_NAME` exacto.
Configura `pom.xml` con **Java 21** (`<java.version>21</java.version>`), `<artifactId>` igual al `SERVICE_NAME`, Spring Boot 3, PostgreSQL, Checkstyle, ArchUnit, JUnit 5 y Cucumber BDD.
Implementa:
1. `docker-compose.yml` en la raíz del servicio para levantar PostgreSQL en local: `POSTGRES_DB` = nombre de la directiva `-- database:` de `specs/database-spec.sql` (si falta, `<SERVICE_NAME>_db`), `user: postgres`, `password: postgres`, `port: 5432`.
1b. **Esquema SDD con Flyway:** añade `flyway-core` (+ `flyway-database-postgresql`) al `pom.xml`, copia el DDL de `specs/database-spec.sql` a `src/main/resources/db/migration/V1__init.sql`, y configura `spring.jpa.hibernate.ddl-auto: validate` en `local` (Flyway crea las tablas; Hibernate solo valida); en `dev` contra Supabase, `ddl-auto: none` y `flyway.enabled: false`. El perfil `test` usa H2 (`create-drop`, Flyway off) o Testcontainers con el mismo `V1__init.sql`. Las entidades JPA deben coincidir 1:1 con el DDL.
2. Adaptadores de salida (Spring Data JPA para PostgreSQL con entidades separadas del dominio y mappers) y adaptadores de entrada (Controladores REST con `jakarta.validation`).
3. Perfiles `application.yml` (`spring.application.name` igual a `SERVICE_NAME`), `application-local.yml`, `application-dev.yml` y `application-test.yml` según `CLAUDE.md`.
4. Tests unitarios/WebMvcTest en JUnit 5 y escenarios en Cucumber BDD (`.feature` + Step Definitions) con reporte HTML en `target/cucumber-reports/cucumber.html` por cada endpoint de `specs/crud-spec.yaml`.
5. Test de ArchUnit verificando que `domain` no depende de `infrastructure`.
6. Configura **JaCoCo** (`jacoco-maven-plugin`) con `report` y una regla `check` (`BUNDLE`, `LINE` ≥ 0.80) enlazada a `verify`, y escribe los tests necesarios para superar el **80% de cobertura global** del microservicio.
7. `README.md` y `CLAUDE.md` (documentación y guía operativa para arrancar PostgreSQL con Docker, compilar, probar y verificar).
Actualiza `plans/TASKS.md` al terminar.
