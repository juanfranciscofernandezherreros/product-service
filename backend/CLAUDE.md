# CLAUDE.md — backend (Product CRUD, Java 21, Hexagonal)

Guía para arrancar, probar y extender este microservicio.

## Stack
- Spring Boot 3.3.5 · Java 21 · PostgreSQL (runtime) · H2 (test) · Cucumber BDD · ArchUnit · Checkstyle.

## Comandos

- Build + verificación completa: `mvnw.cmd clean verify` (o `./mvnw clean verify`)
- Solo tests unitarios: `mvnw.cmd test`
- Arrancar (perfil `local`, PostgreSQL): `mvnw.cmd spring-boot:run`
- Arrancar con otro perfil: `mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev`
- Análisis estático: `mvnw.cmd checkstyle:check`
- Levantar PostgreSQL local: `docker compose up -d`
- Reporte Cucumber (tras `verify`): `backend/target/cucumber-reports/cucumber.html`
- Documentación funcional: `backend/docs/index.html`

## Perfiles
- `local` (por defecto): PostgreSQL en `localhost:5432/product_db` (vía `docker compose up -d`), `ddl-auto=update`.
- `dev`: PostgreSQL por variables de entorno (`DB_HOST_DEV`, `DB_NAME_DEV`, `DB_USER_DEV`, `DB_PASS_DEV`), `ddl-auto=validate`.
- `test`: H2 en memoria (modo PostgreSQL), `ddl-auto=create-drop`. Lo usan los tests.

## Reglas de arquitectura
1. **Hexagonal estricto:**
   - `domain`: Java puro. CERO Spring/JPA/Lombok. Tipos inmutables / `record`.
   - `application`: puertos (`port.in`, `port.out`) y casos de uso (`usecase`).
   - `infrastructure.adapter.in.web`: controladores REST, DTOs, validación `jakarta.validation`.
   - `infrastructure.adapter.out.persistence`: entidades JPA separadas del dominio, mappers explícitos, repositorios.
2. **Inyección por constructor** (`private final`). Prohibido `@Autowired` en campos.
3. **Wiring explícito** de casos de uso y adapters en `infrastructure.config.BeanConfiguration` (`@Bean`).
4. **Cobertura por endpoint:** cada endpoint de `specs/crud-spec.yaml` tiene test unitario (JUnit 5 + Mockito)
   y escenario Cucumber BDD (happy path + errores).
5. **ArchUnit** valida el aislamiento del dominio.

## Verificación final
Ninguna tarea se da por terminada hasta que `mvnw.cmd clean verify` pase en verde
(Checkstyle + ArchUnit + JUnit + Cucumber) y el reporte HTML de Cucumber se haya generado.
