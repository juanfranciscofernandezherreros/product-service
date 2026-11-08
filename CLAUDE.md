# CLAUDE.md - Orquestador AI CRUD (Java 21 + Spring Boot 3 + Hexagonal + Docker PGSQL)

## Contexto del Proyecto (Generado por `run-ai-crud.bat`)
Antes de ejecutar cualquier fase, lee el archivo `.ai/project-context.txt` (creado automáticamente por `run-ai-crud.bat`) para conocer:
- **Nombre del Microservicio (`SERVICE_NAME`):** Úsalo en `pom.xml` (`<artifactId>` y `<name>`), en `application.yml` (`spring.application.name`), en `README.md` y en `docs/index.html`.
- **Repositorio Remoto (`REMOTE_URL`):** Úsalo para configurar `git remote add origin <REMOTE_URL>` y hacer `git push -u origin main`.

## Ejecución Secuencial Obligatoria (.bat y Fases)
Si se solicita ejecutar o continuar el flujo de generación del CRUD, **DEBES seguir estrictamente este orden** (automatizado en `run-ai-crud.bat`):
1. `01-plan-and-tasks.md` -> Analizar specs y actualizar `plans/IMPLEMENTATION_PLAN.md` y `plans/TASKS.md`.
2. `02-domain.md` -> Implementar capa de dominio puro en **Java 21**.
3. `03-application.md` -> Implementar puertos, casos de uso y tests JUnit 5.
4. `04-adapters-and-tests.md` -> Implementar microservicio en `backend/` (Java 21, REST/JPA PostgreSQL, `docker-compose.yml` para local, perfiles `local`/`dev`/`test`, JUnit 5, Cucumber BDD con reporte HTML, ArchUnit, `backend/README.md` y `backend/CLAUDE.md`).
5. `05-e2e-verification.md` -> Ejecutar `mvnw.cmd clean verify`, verificar reporte HTML de Cucumber, generar la **Documentación Funcional Oficial en HTML** (`backend/docs/index.html`), hacer commit y push al repositorio Git remoto indicado.

## Stack Tecnológico Obligatorio
- **Lenguaje:** **Java 21** (`<java.version>21</java.version>` en `pom.xml`). Aprovecha `record` para DTOs y Value Objects, `switch` pattern matching donde aplique y APIs modernas de Java 21.
- **Framework:** Spring Boot 3.x (Web, Data JPA, Validation).
- **Base de Datos:** PostgreSQL levantada mediante `docker-compose.yml` en local.

## Comandos de Compilación, Calidad y Test (en `backend/`)
- Levantar PostgreSQL local: `docker compose up -d`
- Build & Full Verify: `mvnw.cmd clean verify` (o `./mvnw clean verify`)
- Tests Unitarios (JUnit 5): `mvnw.cmd test`
- Tests de Aceptación (Cucumber BDD): `mvnw.cmd verify -Dspring.profiles.active=test`
- Análisis Estático: `mvnw.cmd checkstyle:check`

## Base de Datos y Entornos (PostgreSQL + Docker + Spring Profiles)
- Persistencia obligatoria en **PostgreSQL** usando Spring Data JPA.
- Genera un `docker-compose.yml` en la raíz del microservicio `backend/` para levantar PostgreSQL en local (`port: 5432`, `db: product_db`, `user: postgres`, `password: postgres`).
- Genera los siguientes perfiles en `backend/src/main/resources/`:
  - `application.yml`: Configuración común con `spring.application.name: <SERVICE_NAME>`, `spring.profiles.active: ${SPRING_PROFILES_ACTIVE:local}` y `open-in-view: false`.
  - `application-local.yml`: Conexión a PostgreSQL local vía Docker (`jdbc:postgresql://localhost:5432/product_db`, `ddl-auto: update`, `show-sql: true`).
  - `application-dev.yml`: Conexión a PostgreSQL de desarrollo parametrizada con variables de entorno (`jdbc:postgresql://${DB_HOST_DEV}:5432/${DB_NAME_DEV}`, `${DB_USER_DEV}`, `${DB_PASS_DEV}`, `ddl-auto: validate`, `show-sql: false`).
- Para tests automatizados (`backend/src/test/resources/application-test.yml`): usa H2 en memoria en modo PostgreSQL (`jdbc:h2:mem:testdb;MODE=PostgreSQL`) o Testcontainers para que `mvnw.cmd clean verify` funcione sin depender de contenedores activos.

## Reporte HTML de Cucumber BDD
- Configura el runner de Cucumber con el plugin:
  `plugin = {"pretty", "html:target/cucumber-reports/cucumber.html", "json:target/cucumber-reports/cucumber.json"}`
- El reporte HTML debe quedar generado tras ejecutar `mvnw.cmd clean verify` en `backend/target/cucumber-reports/cucumber.html`.

## Documentación Funcional Oficial en HTML (`backend/docs/index.html`)
- Al finalizar la verificación en la Fase 5, es **OBLIGATORIO** generar un documento HTML autocontenido (CSS embebido, limpio, navegable y listo para publicar en GitHub/GitLab Pages) en `backend/docs/index.html` que explique **de forma funcional** todo lo desarrollado:
  - Propósito del servicio (`<SERVICE_NAME>`) y reglas de negocio implementadas.
  - Catálogo funcional de endpoints (método, ruta, códigos HTTP y validaciones).
  - **Request y Response por cada endpoint (OBLIGATORIO):** para cada endpoint de `specs/crud-spec.yaml`, el documento debe mostrar explícitamente:
    - **Request:** parámetros (path/query) y, cuando aplique, el cuerpo JSON de ejemplo y su esquema (campos, tipos, obligatoriedad y validaciones).
    - **Response:** el cuerpo JSON de ejemplo de la respuesta de éxito por su código HTTP y, al menos, un ejemplo del cuerpo de error (`{ code, message }` o la estructura de error del servicio).
  - Modelo de datos y flujos principales.
  - **Diagrama (OBLIGATORIO):** al menos un diagrama embebido como **SVG inline** (sin imágenes externas) que represente la arquitectura hexagonal del servicio (adaptadores de entrada/web → aplicación/puertos y casos de uso → dominio, y adaptador de salida → PostgreSQL) o el flujo principal de una petición.
  - Resumen de entornos (`local` con Docker PostgreSQL y `dev`) y evidencias de calidad (tests JUnit 5, ArchUnit y Cucumber BDD).

## Git y Repositorio Remoto
- Al finalizar la Fase 5, inicializa Git (`git init`), configura el remote (`git remote add origin <REMOTE_URL>`), añade todos los ficheros (`git add .`), haz commit (`git commit -m "feat: initial hexagonal crud microservice <SERVICE_NAME> in java 21"`) y push (`git push -u origin main`).

## Reglas Estrictas de Arquitectura y Código
1. **Arquitectura Hexagonal (Ports & Adapters):**
   - `domain`: Puro Java 21. **CERO** anotaciones de Spring, JPA o Lombok (preferir Java `record` o POJOs inmutables).
   - `application`: Casos de uso (`UseCase`) y puertos (`ports.in`, `ports.out`). Orquestan la lógica de negocio.
   - `infrastructure.adapters.in.web`: Controladores REST, DTOs de entrada/salida (`record`), validación con `jakarta.validation`.
   - `infrastructure.adapters.out.persistence`: Entidades JPA separadas del dominio, mappers explícitos y repositorios Spring Data.
2. **Inyección de Dependencias:**
   - **SIEMPRE** inyección por constructor (`private final`). **PROHIBIDO** `@Autowired` en campos.
3. **Cobertura Obligatoria por Endpoint:**
   - Cada endpoint definido en `specs/crud-spec.yaml` DEBE tener:
     1. Test unitario en JUnit 5 + Mockito para su Caso de Uso y Controlador.
     2. Escenario Cucumber BDD (`.feature` + Step Definitions) validando el flujo completo (happy path y errores).
   - **Cobertura global mínima (OBLIGATORIO):** el microservicio completo debe superar el **80% de cobertura** (líneas/instrucciones) medido con **JaCoCo**. Configura el plugin `jacoco-maven-plugin` con `report` y una regla `check` (`BUNDLE`, `LINE` ≥ 0.80) enlazada a `verify`, de modo que `mvnw.cmd clean verify` falle si no se alcanza. El informe HTML queda en `target/site/jacoco/index.html`.
4. **Planificación y Tareas:**
   - Antes de escribir código, actualiza `plans/IMPLEMENTATION_PLAN.md` y `plans/TASKS.md` marcando el progreso.
5. **Verificación Final contra Especificaciones:**
   - Ninguna tarea se considera terminada hasta que `mvnw.cmd clean verify` pase en verde, exista el reporte HTML de Cucumber, se cumpla la **cobertura ≥ 80%** (JaCoCo), exista `backend/docs/index.html` **con el Request y Response de cada endpoint**, se haya auditado frente a `specs/crud-spec.yaml` y se haya completado el commit y push al repositorio remoto.
