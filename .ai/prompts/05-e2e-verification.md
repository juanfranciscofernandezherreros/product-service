Lee `.ai/project-context.txt` para obtener `SERVICE_NAME` y `REMOTE_URL`.
Ejecuta `mvnw.cmd clean verify` en la raíz del servicio.
Comprueba obligatoriamente:
1. El proyecto compila con Java 21 y Checkstyle pasa sin errores.
2. ArchUnit pasa sin errores.
3. Todos los tests de JUnit 5 y Cucumber BDD pasan.
4. Se ha generado el reporte HTML de Cucumber en `target/cucumber-reports/cucumber.html`.
5. Existen `README.md` y `CLAUDE.md` bien documentados.
6. Audita el código generado frente a `specs/crud-spec.yaml`, `specs/database-spec.sql` y `specs/acceptance-criteria.md`: confirma endpoint por endpoint, esquema por esquema y código HTTP por código HTTP, y **tabla por tabla / columna por columna** que la base de datos, las tablas y las entidades JPA coinciden 1:1 con el DDL (Flyway aplica `V1__init.sql`; `ddl-auto: validate` no debe fallar).
7. Comprueba la **cobertura global del microservicio con JaCoCo**: debe ser **≥ 80%** (líneas/instrucciones). El informe queda en `target/site/jacoco/index.html` y la regla `check` enlazada a `verify` debe hacer fallar el build si no se alcanza.
8. Genera `docs/index.html` como **Documentación Funcional Oficial** autocontenida (CSS embebido profesional, lista para publicar en GitHub/GitLab Pages) explicando de forma funcional el servicio `SERVICE_NAME`, reglas de negocio, el **modelo de datos EXACTO** derivado de `specs/database-spec.sql` (cada tabla con columnas, tipos PostgreSQL, nulabilidad, PK/FK, UNIQUE e índices), entornos (`local` Docker / `dev`) y resumen de cobertura. **Para cada endpoint es OBLIGATORIO mostrar su Request (parámetros y cuerpo JSON de ejemplo + esquema/validaciones) y su Response (cuerpo JSON de ejemplo por código HTTP, incluyendo un ejemplo de error).** El documento DEBE incluir además al menos un **diagrama embebido como SVG inline** (arquitectura hexagonal o flujo de una petición), sin imágenes externas.
9. **Especificaciones de aceptación y acceso a datos (OBLIGATORIO antes del commit/push):**
   - Configura el perfil `dev` hacia **Supabase** (`ddl-auto: none`, `flyway.enabled: false`; el esquema lo gestiona Supabase) y aplica la política «Acceso a datos en Supabase» del SDD (lecturas de usuario por **REST + JWT**; `service_role` solo para backend sin usuario; **secretos solo por variable de entorno**, nunca en el repo).
   - Genera o actualiza `specs/VERIFICACION-DATOS.md`: prueba de que **cada endpoint principal devuelve datos reales** de Supabase (consulta de solo lectura equivalente).
   - Genera o actualiza `specs/PRUEBAS-CURL-CRUD.html` (**criterio CA-11**): un `curl` por **CADA** endpoint del servicio, en secuencia CRUD + subrecursos, con método, ruta, puerto y cuerpo JSON derivado de los **DTOs reales**.
   - Genera o actualiza `specs/CRITERIOS-ACEPTACION.html`: criterios **CA-01..CA-11** con su evidencia (build en verde, cobertura ≥ 80%, datos reales).
10. Inicializa Git en el proyecto (`git init`), añade el repositorio remoto `REMOTE_URL` (`git remote add origin <REMOTE_URL>`), realiza `git add .`, `git commit -m "feat: complete java 21 hexagonal crud microservice <SERVICE_NAME>"` y `git push -u origin main`.
Si falta algo o hay algún fallo, corrígelo y vuelve a ejecutar hasta que todo pase en verde y el push sea exitoso.
Actualiza `plans/TASKS.md` marcando todas las tareas completadas.
