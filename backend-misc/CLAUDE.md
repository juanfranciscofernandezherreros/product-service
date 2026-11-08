# CLAUDE.md — misc-service

Microservicio del dominio **Misc** generado desde el orquestador AI CRUD (Database-First / SDD).

## Arquitectura hexagonal
- `domain`: Java 21 puro (modelos inmutables `Suggestion`, `LogbookCategory`, `LogbookNote`, `WritingDraft`, enums `SuggestionStatus`/`WritingType`, excepciones). Sin Spring/JPA.
- `application`: puertos (`MiscOutPorts`, `MiscUseCase`), comandos (`MiscCommands`) y caso de uso (`MiscService`).
- `infrastructure.adapter.in.web`: controladores REST, DTOs `record`, `GlobalExceptionHandler`.
- `infrastructure.adapter.out.persistence`: entidades JPA, repositorios Spring Data (top-level), mapper y adaptadores.
- `infrastructure.config.BeanConfiguration`: wiring explícito con `@Bean` e inyección por constructor.

## Base de datos (fuente de verdad)
`src/main/resources/db/migration/V1__init.sql` (copiado de `specs/domains/misc.sql`): tablas `sugerencias`, `bitacora_categorias`, `bitacora_anotaciones`, `writing_borradores`. Flyway crea el esquema; Hibernate valida (`ddl-auto: validate`). Los UUID y los timestamps se generan en la aplicación (defaults de BD retirados para portabilidad H2).

## Comandos
- `docker compose up -d` — PostgreSQL local (`misc_db`).
- `mvn clean verify` — build + tests (JUnit 5, ArchUnit, Cucumber) + Checkstyle + JaCoCo (≥80%).
- `mvn spring-boot:run` — arranque local (puerto 8089).

## Reglas
- Inyección por constructor, nunca `@Autowired` en campo.
- Cada endpoint tiene test unitario y escenario Cucumber.
- Cobertura global ≥80% (actual 85%).
- No commitear/pushear sin petición explícita del usuario.
