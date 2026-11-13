# CLAUDE.md — courses-service

Microservicio del dominio **Courses** generado desde el orquestador AI CRUD (Database-First / SDD).

## Arquitectura hexagonal
- `domain`: Java 21 puro. Modelos inmutables como **`record`** (Course, Unit, Activity, ActivityQuestion, ActivityAttempt), enums `Destreza`/`QuestionType`, excepciones. Sin Spring/JPA.
- `application`: puertos (`CoursesOutPorts`, `CoursesUseCase`), comandos (`CoursesCommands`) y caso de uso (`CoursesService`).
- `infrastructure.adapter.in.web`: `CourseController`, `UnitController`, `ActivityController` (+ preguntas/intentos), DTOs `record`, `GlobalExceptionHandler`.
- `infrastructure.adapter.out.persistence`: entidades JPA, repositorios Spring Data (top-level), filtrado de unidades/actividades con `JpaSpecificationExecutor`, mapper y adaptadores.
- `infrastructure.config.BeanConfiguration`: wiring explícito `@Bean`, inyección por constructor.

## Base de datos (fuente de verdad)
`src/main/resources/db/migration/V1__init.sql` (copiado de `specs/domains/courses.sql`): 5 tablas (`cursos`, `unidades_curso`, `actividades`, `actividad_preguntas`, `actividad_intentos`). Flyway crea; Hibernate valida. Los campos `jsonb` (configuracion, respuestas, detalle) se guardan como JSON en texto y se exponen como JSON crudo en la API; los CHECK con regex (slug de actividad, media URL) se validan en el dominio; las FK a `auth.users` se omiten.

## Comandos
- `docker compose up -d` — PostgreSQL local (`courses_db`).
- `mvn clean verify` — build + tests (JUnit 5, ArchUnit, Cucumber) + Checkstyle + JaCoCo (≥80%).
- `mvn spring-boot:run` — arranque local (puerto 8091).

## Reglas
- Inyección por constructor, nunca `@Autowired` en campo.
- Cada endpoint tiene test unitario y escenario Cucumber.
- Cobertura global ≥80% (actual 86%).
- No commitear/pushear sin petición explícita del usuario.

## Acceso a datos en Supabase (dev)
Política completa en el SDD raíz (`../.claude/rules/supabase-acceso-datos.md`) y en `../specs/VERIFICACION-DATOS.md`.
- Lecturas en nombre del usuario: por la **API REST de Supabase** reenviando su **JWT** (`Authorization: Bearer <jwt>` + `apikey: <clave publicable>`), respetando RLS. Sin secretos en el repo.
- Tareas de backend sin usuario: **`service_role` key** como secreto en variable de entorno (`SUPABASE_SERVICE_ROLE_KEY`), nunca en Git.
- La clave publicable solo alcanza tablas públicas; la contraseña `postgres` (JDBC) se salta RLS y queda para administración controlada.
