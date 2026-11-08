# CLAUDE.md — content-service

Microservicio del dominio **Content & Blog** generado desde el orquestador AI CRUD (Database-First / SDD).

## Arquitectura hexagonal
- `domain`: Java 21 puro. Modelos inmutables como **`record`** (Article, Comment, Category, Page, Collection, Downloadable, MoviePhrase), enum `CollectionType`, excepciones. Sin Spring/JPA.
- `application`: puertos (`ContentOutPorts`, `ContentUseCase`), comandos (`ContentCommands`) y caso de uso (`ContentService`).
- `infrastructure.adapter.in.web`: `ArticleController` (artículos + comentarios), `CatalogController` (categorías, páginas, colecciones, descargables, frases), DTOs `record`, `GlobalExceptionHandler`.
- `infrastructure.adapter.out.persistence`: entidades JPA, repositorios Spring Data (top-level), filtrado de artículos con `JpaSpecificationExecutor`, mapper y adaptadores.
- `infrastructure.config.BeanConfiguration`: wiring explícito `@Bean`, inyección por constructor.

## Base de datos (fuente de verdad)
`src/main/resources/db/migration/V1__init.sql` (copiado de `specs/domains/content.sql`): 8 tablas (`categorias_blog`, `articulos`, `comentarios_articulos`, `colecciones_material`, `coleccion_articulos`, `paginas_blog`, `recursos_descargables`, `movie_subtitle_phrases`). Flyway crea; Hibernate valida. Los CHECK con regex (ruta, nombre .pdf) se validan en el dominio para portabilidad H2; los bigint usan IDENTITY; las FK a `auth.users` se omiten.

## Comandos
- `docker compose up -d` — PostgreSQL local (`content_db`).
- `mvn clean verify` — build + tests (JUnit 5, ArchUnit, Cucumber) + Checkstyle + JaCoCo (≥80%).
- `mvn spring-boot:run` — arranque local (puerto 8090).

## Reglas
- Inyección por constructor, nunca `@Autowired` en campo.
- Cada endpoint tiene test unitario y escenario Cucumber.
- Cobertura global ≥80% (actual 89%).
- No commitear/pushear sin petición explícita del usuario.

## Acceso a datos en Supabase (dev)
Política completa en el SDD raíz (`../CLAUDE.md`) y en `../specs/VERIFICACION-DATOS.md`.
- Lecturas en nombre del usuario: por la **API REST de Supabase** reenviando su **JWT** (`Authorization: Bearer <jwt>` + `apikey: <clave publicable>`), respetando RLS. Sin secretos en el repo.
- Tareas de backend sin usuario: **`service_role` key** como secreto en variable de entorno (`SUPABASE_SERVICE_ROLE_KEY`), nunca en Git.
- La clave publicable solo alcanza tablas públicas; la contraseña `postgres` (JDBC) se salta RLS y queda para administración controlada.
