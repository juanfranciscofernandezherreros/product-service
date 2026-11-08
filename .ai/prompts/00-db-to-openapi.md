FASE 0 (solo en modo DATABASE-FIRST): deriva el contrato OpenAPI a partir del esquema de base de datos.

Lee `CLAUDE.md`, `.ai/project-context.txt` y **`specs/database-spec.sql`** (DDL PostgreSQL = fuente de verdad).
A partir de las tablas del DDL, GENERA `specs/crud-spec.yaml` (OpenAPI 3.0.3) y `specs/acceptance-criteria.md`:

1. Identifica la(s) **entidad(es) raíz** (normalmente la tabla principal; las tablas hijas con FK se modelan como sub-recursos o colecciones anidadas, no como CRUD independiente salvo que tengan sentido propio).
2. Por cada entidad raíz, crea el CRUD estándar bajo `/v1/<recurso-en-plural>`:
   - `POST /v1/<recurso>`            → 201 / 400 (y 409 si hay UNIQUE/clave natural duplicable).
   - `GET /v1/<recurso>`             → 200, paginado (`limit`/`offset`) y filtros por columnas indexadas/filtrables.
   - `GET /v1/<recurso>/{id}`        → 200 / 404, donde `{id}` es la clave primaria (UUID, serial o clave natural según el DDL).
   - `PUT /v1/<recurso>/{id}`        → 200 / 400 / 404.
   - `DELETE /v1/<recurso>/{id}`     → 204 / 404 (y 422 si una FK con restricción impide el borrado).
3. Deriva los **schemas** del OpenAPI directamente de las columnas: tipo (mapea `numeric`→number, `varchar`→string, `integer`→integer, `uuid`→string/uuid, `date`→string/date, `timestamptz`→string/date-time, `boolean`→boolean), obligatoriedad (NOT NULL → required), longitudes (`varchar(n)`→maxLength), y rangos de los `CHECK` como `minimum`/`maximum`.
4. Define un schema de error `{ code, message }` y las respuestas comunes (BadRequest, NotFound, Conflict).
5. Escribe `specs/acceptance-criteria.md` con escenarios BDD por endpoint (happy path + errores 400/404/409/422 cuando apliquen).

El OpenAPI y los criterios generados deben ser coherentes 1:1 con el DDL. No inventes columnas ni tablas que no estén en `specs/database-spec.sql`.
Tras generar, continúa con la Fase 1 (`01-plan-and-tasks.md`) usando ya ese `specs/crud-spec.yaml`.
