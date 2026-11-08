# CLAUDE.md - Orquestador AI CRUD (Java 21 + Spring Boot 3 + Hexagonal + Docker PGSQL)

## Modelo multi-microservicio: un servicio = un repositorio
**REGLA OBLIGATORIA:** cada microservicio se genera en **su propia carpeta** y se publica en **su propio repositorio Git**. **NUNCA** se combinan varios servicios en el mismo repositorio ni en el mismo checkout.
- La lista de servicios a generar vive en `.ai/services.txt` (manifiesto), una línea por servicio con 4 campos `;`-separados: `SERVICE_NAME;TARGET_DIR;REMOTE_URL;SPEC_FILE`.
- `run-ai-crud.bat` recorre ese manifiesto y, por cada servicio: prepara su carpeta `TARGET_DIR`, copia su contrato (`SPEC_FILE` → `specs/crud-spec.yaml`), escribe su `.ai/project-context.txt` propio, ejecuta las 5 fases **en esa carpeta** y hace `git init` + `git remote add origin <REMOTE_URL>` + `git push -u origin main` **a su remoto**.
- Un contrato OpenAPI distinto ⇒ un microservicio distinto ⇒ un repositorio distinto. Si llega un contrato nuevo, se genera un servicio nuevo en su propio repo, no se añade a uno existente.

## Contexto del Proyecto (Generado por `run-ai-crud.bat`)
Antes de ejecutar cualquier fase, lee el archivo `.ai/project-context.txt` (creado automáticamente por `run-ai-crud.bat`) para conocer:
- **Nombre del Microservicio (`SERVICE_NAME`):** Úsalo en `pom.xml` (`<artifactId>` y `<name>`), en `application.yml` (`spring.application.name`), en `README.md` y en `docs/index.html`.
- **Repositorio Remoto (`REMOTE_URL`):** Úsalo para configurar `git remote add origin <REMOTE_URL>` y hacer `git push -u origin main`.

## Modos de generación: API-First y Database-First
El flujo admite dos puntos de partida; ambos acaban en el mismo pipeline (dominio → aplicación → adaptadores → verificación → docs → push):
- **API-First (por defecto):** la especificación de entrada es el OpenAPI (`specs/crud-spec.yaml`) y, opcionalmente, el esquema (`specs/database-spec.sql`). Se ejecutan las Fases 1→5.
- **Database-First:** la especificación de entrada es **solo el esquema** (`specs/database-spec.sql`). Se ejecuta primero la **Fase 0** (`.ai/prompts/00-db-to-openapi.md`), que **genera** `specs/crud-spec.yaml` y `specs/acceptance-criteria.md` a partir de las tablas del DDL; después continúan las Fases 1→5 con ese contrato derivado. El esquema SQL sigue siendo la fuente de verdad (SDD): Flyway crea las tablas y JPA valida.
- Se selecciona con la variable `GENERATION_MODE` (`api-first` | `db-first`) en `.ai/project-context.txt`; en el manifiesto, una línea con `SPEC_FILE` vacío implica `db-first` (el OpenAPI se derivará del `DB_SPEC`).

## Ejecución Secuencial Obligatoria (.bat y Fases)
Si se solicita ejecutar o continuar el flujo de generación del CRUD, **DEBES seguir estrictamente este orden** (automatizado en `run-ai-crud.bat`). En modo Database-First se antepone la Fase 0 (`00-db-to-openapi.md`):
1. `01-plan-and-tasks.md` -> Analizar specs y actualizar `plans/IMPLEMENTATION_PLAN.md` y `plans/TASKS.md`.
2. `02-domain.md` -> Implementar capa de dominio puro en **Java 21**.
3. `03-application.md` -> Implementar puertos, casos de uso y tests JUnit 5.
4. `04-adapters-and-tests.md` -> Implementar microservicio en `backend/` (Java 21, REST/JPA PostgreSQL, `docker-compose.yml` para local, perfiles `local`/`dev`/`test`, JUnit 5, Cucumber BDD con reporte HTML, ArchUnit, `backend/README.md` y `backend/CLAUDE.md`).
5. `05-e2e-verification.md` -> Ejecutar `mvnw.cmd clean verify`, verificar reporte HTML de Cucumber, generar la **Documentación Funcional Oficial en HTML** (`backend/docs/index.html`), generar/actualizar los entregables de aceptación (`specs/VERIFICACION-DATOS.md`, `specs/PRUEBAS-CURL-CRUD.html` [CA-11], `specs/CRITERIOS-ACEPTACION.html`), aplicar la política de acceso a Supabase, y hacer commit y push al repositorio Git remoto indicado.

## Flujo para un ESQUEMA NUEVO (Database-First): de DDL a CRUD completo
Cuando añadas un esquema nuevo (un `.sql` con el DDL), el resultado DEBE ser un microservicio completo que cumpla **todas** las especificaciones de este SDD. Pasos:
1. Añade el esquema como `specs/<servicio>.sql` y una línea en `.ai/services.txt` con `SPEC_FILE` **vacío** (eso activa Database-First; el OpenAPI se derivará del DDL).
2. **Fase 0** (`.ai/prompts/00-db-to-openapi.md`): genera `specs/crud-spec.yaml` con el **CRUD COMPLETO** (crear → listar → obtener → actualizar → borrar) por entidad raíz, **subrecursos REST** para las tablas hijas, y `specs/acceptance-criteria.md`. Ninguna tabla operable queda sin endpoints.
3. **Fases 1→5**: dominio puro (Java 21) → puertos/casos de uso + JUnit 5 → adaptadores web/JPA + Cucumber + ArchUnit → verificación `mvnw.cmd clean verify`.
4. **Entregables obligatorios** (criterios de aceptación): cobertura JaCoCo ≥ 80%, reporte Cucumber HTML, `backend/docs/index.html` (diagrama SVG + request/response por endpoint + esquema exacto), `README.md`/`CLAUDE.md`, perfil `dev`→Supabase con la política de acceso, `specs/VERIFICACION-DATOS.md`, `specs/PRUEBAS-CURL-CRUD.html` (CA-11) y `specs/CRITERIOS-ACEPTACION.html`.
5. **Git**: cada servicio en su carpeta y su propio repositorio remoto; commit y push solo con petición explícita.

## Índice de especificaciones y entregables
- **SDD / reglas:** este `CLAUDE.md` (fuente de verdad del proceso) + `backend-<servicio>/CLAUDE.md`.
- **Contrato y esquema:** `specs/crud-spec.yaml` (OpenAPI) y `specs/<servicio>.sql` (DDL, fuente de verdad del modelo).
- **Prompts del pipeline:** `.ai/prompts/00..05`.
- **Documentación funcional:** `backend-<servicio>/docs/index.html` (uno por servicio).
- **Aceptación y evidencia:** `specs/CRITERIOS-ACEPTACION.html` (CA-01..CA-11), `specs/PRUEBAS-CURL-CRUD.html` (CA-11, `curl` por endpoint), `specs/VERIFICACION-DATOS.md` (datos reales por servicio), `ACTA-CONFORMIDAD.html`.
- **Índice navegable:** `specs/INDICE.md`.

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

## Esquema de Base de Datos como Especificación (SDD + Flyway)
Además del contrato OpenAPI (`specs/crud-spec.yaml`), el esquema PostgreSQL se define como especificación en **`specs/database-spec.sql`** (DDL) y es la **fuente de verdad** del modelo de datos:
- **Creación de la base de datos:** el nombre se toma de la directiva `-- database: <nombre_db>` del fichero (si falta, `<SERVICE_NAME>_db`). `docker-compose.yml` crea esa base con `POSTGRES_DB`, y **`application-local.yml`** apunta a ella. En **`dev`** el servicio apunta a **Supabase**, cuyo esquema ya existe (ver «Acceso a datos en Supabase»).
- **Creación de las tablas (Flyway):** copia el DDL de `specs/database-spec.sql` a `src/main/resources/db/migration/V1__init.sql`. Añade `flyway-core` (y `flyway-database-postgresql`) y deja que Flyway cree las tablas al arrancar. **Configura `spring.jpa.hibernate.ddl-auto: validate`** en `local` (con Flyway): Hibernate NO crea el esquema, solo valida que las entidades JPA coinciden 1:1 con las tablas declaradas (columnas, tipos, PK/FK, UNIQUE, NOT NULL). Si no coinciden, el arranque falla: eso es SDD. En **`dev` contra Supabase** se usa `ddl-auto: none` y `flyway.enabled: false` (Supabase gestiona el esquema; el servicio no lo crea ni valida).
- **Tests:** para que `mvnw.cmd clean verify` sea autónomo, el perfil `test` usa H2 (modo PostgreSQL) con `ddl-auto: create-drop` y Flyway deshabilitado, **o** Testcontainers PostgreSQL aplicando el mismo `V1__init.sql` (opción más fiel; úsala si Docker está disponible).
- **Coherencia obligatoria:** las entidades JPA, los nombres de tabla/columna y los tipos deben derivarse del DDL, no al revés. Cualquier cambio de modelo empieza en `specs/database-spec.sql`.

## Base de Datos y Entornos (PostgreSQL + Docker + Spring Profiles)
- Persistencia obligatoria en **PostgreSQL** usando Spring Data JPA.
- Genera un `docker-compose.yml` en la raíz del microservicio `backend/` para levantar PostgreSQL en local (`port: 5432`, `db: product_db`, `user: postgres`, `password: postgres`).
- Genera los siguientes perfiles en `backend/src/main/resources/`:
  - `application.yml`: Configuración común con `spring.application.name: <SERVICE_NAME>`, **`spring.profiles.active: ${SPRING_PROFILES_ACTIVE:dev}`** (perfil **`dev`** activo por defecto) y `open-in-view: false`.
  - `application-local.yml`: Conexión a PostgreSQL local vía Docker (`jdbc:postgresql://localhost:5432/<db_local>`, `show-sql: true`), con Flyway (SDD) y `ddl-auto: validate` del común.
  - `application-dev.yml`: Conexión a **Supabase (dev)** (`jdbc:postgresql://${DB_HOST_DEV:db.<ref>.supabase.co}:5432/${DB_NAME_DEV:postgres}`, `${DB_USER_DEV:postgres}`; contraseña **solo** por `${DB_PASS_DEV}`, **nunca** en el repo). Como el esquema ya existe y lo gestiona Supabase, en `dev` se usa **`ddl-auto: none`** y **`flyway.enabled: false`** (el servicio solo se conecta; no crea ni valida el esquema). Ver la política «Acceso a datos en Supabase (perfil `dev`)».
- Para tests automatizados (`backend/src/test/resources/application-test.yml`): usa H2 en memoria en modo PostgreSQL (`jdbc:h2:mem:testdb;MODE=PostgreSQL`) o Testcontainers para que `mvnw.cmd clean verify` funcione sin depender de contenedores activos.

## Acceso a datos en Supabase (perfil `dev`) — política de seguridad OBLIGATORIA
Cuando el perfil `dev` apunta a Supabase, el acceso a datos sigue esta política (evidencia y pruebas en `specs/VERIFICACION-DATOS.md`):
- **Lecturas en nombre de un usuario (forma recomendada, «igual que la web»):** el microservicio recibe el **JWT** del usuario (el que emite Supabase Auth) y lo **reenvía** a la API REST de Supabase (`https://<ref>.supabase.co/rest/v1/<tabla>`) en la cabecera `Authorization: Bearer <jwt>` junto con `apikey: <clave publicable>`. Así el backend ve exactamente lo que ese usuario puede ver, **respetando RLS**, y **sin ningún secreto en el repositorio**.
- **Tareas de backend sin usuario** (batch, administración, o cuando deba saltarse RLS a propósito): se usa la **`service_role` key** de Supabase, **siempre** como secreto en variable de entorno (`SUPABASE_SERVICE_ROLE_KEY`), **nunca** en Git, ni en el frontend, ni confundida con la clave publicable.
- **PROHIBIDO:** usar `service_role` para peticiones originadas por el usuario (anularía la seguridad por fila), y escribir cualquier clave secreta o contraseña de base de datos en el repositorio.
- **Credenciales por entorno:** toda contraseña/clave se inyecta por variable de entorno (p. ej. `DB_PASS_DEV`, `SUPABASE_SERVICE_ROLE_KEY`) y se mantiene fuera del control de versiones (`.gitignore` incluye `.env` y `.env.local`).
- **La clave publicable** (`sb_publishable_…`) es pública y solo alcanza las tablas con permiso para el rol anónimo (`anon`); no sirve como credencial de backend para datos protegidos por RLS.
- **Prueba final de datos (OBLIGATORIA):** la entrega incluye una verificación registrada en `specs/VERIFICACION-DATOS.md` que confirma, **por cada microservicio**, que la consulta de su endpoint principal **devuelve datos reales** desde Supabase (dev). Debe mantenerse coherente con esta política de acceso y con la configuración de perfiles (`dev` por defecto, credenciales por variable de entorno).

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
  - **Modelo de datos exacto (OBLIGATORIO):** el documento debe reflejar fielmente el esquema de `specs/database-spec.sql`: por cada tabla, sus columnas con tipo PostgreSQL, nulabilidad, claves primarias/foráneas, UNIQUE e índices, tal y como se crean en la base de datos.
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
   - **Prueba final de datos:** cuando el servicio se conecta a Supabase (perfil `dev`), debe existir además la verificación en `specs/VERIFICACION-DATOS.md` que demuestre que cada endpoint principal **devuelve datos reales**, coherente con la política «Acceso a datos en Supabase» de este SDD.
   - **Prueba CRUD completa por endpoint (OBLIGATORIA — criterio CA-11):** debe existir y mantenerse `specs/PRUEBAS-CURL-CRUD.html`, un documento HTML con el **`curl` de CADA endpoint de CADA microservicio** en secuencia CRUD (crear → listar → obtener → actualizar → borrar, y subrecursos), con su método, ruta, puerto y cuerpo JSON derivado de los DTOs reales. Cualquier endpoint nuevo o cambiado debe reflejarse en ese documento. El resumen de criterios de aceptación vive en `specs/CRITERIOS-ACEPTACION.html`.
