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

## Acceso a datos en Supabase (dev)
Política completa en el SDD raíz (`../.claude/rules/supabase-acceso-datos.md`) y en `../specs/VERIFICACION-DATOS.md`.
- Lecturas en nombre del usuario: por la **API REST de Supabase** reenviando su **JWT** (`Authorization: Bearer <jwt>` + `apikey: <clave publicable>`), respetando RLS. Sin secretos en el repo.
- Tareas de backend sin usuario: **`service_role` key** como secreto en variable de entorno (`SUPABASE_SERVICE_ROLE_KEY`), nunca en Git.
- La clave publicable solo alcanza tablas públicas; la contraseña `postgres` (JDBC) se salta RLS y queda para administración controlada.
