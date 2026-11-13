# Verificación de datos — conexión a Supabase dev

Esta especificación registra la **prueba de consulta** que confirma que, con el perfil
`dev` apuntando a la base de datos de Supabase, **los 9 microservicios tienen datos
reales que consultar** en sus respectivas tablas.

## Configuración verificada
- Perfil por defecto: `dev` (`spring.profiles.active: ${SPRING_PROFILES_ACTIVE:dev}`).
- Datasource `dev` → Supabase (`db.wnluofonzteaqtlfshgy.supabase.co:5432/postgres`),
  contraseña **solo** por variable de entorno `DB_PASS_DEV` (nunca en el repositorio).
- `ddl-auto: none` y `flyway.enabled: false`: el servicio **solo se conecta**, no crea
  ni altera ni valida el esquema (Supabase gestiona el esquema).

## Prueba de consulta (solo lectura, sin cambios)
Consulta ejecutada contra Supabase el 8 de noviembre de 2026:

```sql
SELECT relname AS tabla, n_live_tup AS filas
FROM pg_stat_user_tables
WHERE schemaname = 'public'
ORDER BY relname;
```

## Resultado por microservicio

| # | Servicio | Tablas representativas (filas) | Trae datos |
|---|----------|--------------------------------|------------|
| 1 | vocabulary  | vocabulario_items (15), vocabulario_progreso (15) | ✅ |
| 2 | access      | roles (4), permisos (4), roles_permisos (6), usuarios_roles (2) | ✅ |
| 3 | battle      | battle_questions (120) | ✅ |
| 4 | study-plan  | planes_estudio_b2 (1), planes_ia_b2 (1), seguimiento_semanal (4) | ✅ |
| 5 | social      | mensajes_privados (7), perfiles_publicos (1), seguimientos (1) | ✅ |
| 6 | exams       | preguntas (503), examenes (24), resultados (23), simulacros_b2 (2) | ✅ |
| 7 | misc        | bitacora_anotaciones (28), bitacora_categorias (2) | ✅ |
| 8 | content     | movie_subtitle_phrases (2455), articulos (118), paginas_blog (8), categorias_blog (6), recursos_descargables (6) | ✅ |
| 9 | courses     | unidades_curso (51), actividad_preguntas (40), actividades (5) | ✅ |

**Conclusión:** los 9 dominios tienen datos en Supabase y son consultables. Tablas
operativas que aún están vacías (p. ej. `battle_rooms`, `actividad_intentos`,
`sugerencias`, `cursos`) corresponden a entidades sin registros todavía; no afectan a
la conexión.

## Pendiente: prueba por HTTP a través de cada servicio
La comprobación anterior es a nivel de base de datos. Para la prueba **arrancando cada
microservicio y consultando por su endpoint REST** hacen falta dos cosas:

1. Exportar la contraseña de Supabase antes de arrancar, p. ej.:
   ```bash
   export DB_PASS_DEV='<password_de_supabase>'
   mvn spring-boot:run        # en la carpeta de cada servicio
   ```
2. Tener en cuenta que el esquema real de Supabase usa tipos originales
   (`jsonb`, `bigint` sin identity, FKs a `auth.users`, CHECKs con regex). Las entidades
   JPA son las versiones **portables**, por lo que algún endpoint de lectura podría
   requerir un ajuste de mapeo (p. ej. columnas `jsonb`). Con `ddl-auto: none` el
   servicio arranca igualmente; el ajuste, si hiciera falta, sería en el momento de leer
   esas columnas.

## Alternativa sin contraseña: acceso como el frontend (REST + clave publicable)
Los microservicios pueden leer Supabase **igual que el frontend**: por la API REST
(PostgREST) con la **clave publicable** (`sb_publishable_…`), en lugar de JDBC con
contraseña. Prueba ejecutada el 8 de noviembre de 2026:

```bash
curl "https://wnluofonzteaqtlfshgy.supabase.co/rest/v1/articulos?select=id,titulo&limit=1" \
  -H "apikey: sb_publishable_Ef-AXAapcwaPExEFS6kmug_tKLaulM8" \
  -H "Authorization: Bearer sb_publishable_Ef-AXAapcwaPExEFS6kmug_tKLaulM8"
```

| Dominio · tabla | HTTP | Resultado |
|---|---|---|
| content · articulos | 200 | `{"id":162,"titulo":"Unidad 12 · Pronouns and determiners \| Curso B2"}` |
| content · movie_subtitle_phrases | 200 | frase real devuelta |
| courses · unidades_curso | 200 | `{"slug":"libro-ingles-unidad-01","titulo":"1. Present tenses","nivel":"Grammar"}` |
| courses · actividades | 200 | `{"slug":"demostracion-motor-universal","destreza":"use_of_english"}` |
| battle · battle_questions | 401 | permission denied (RLS) |
| vocabulary · vocabulario_items | 401 | permission denied (RLS) |
| exams · examenes | 401 | permission denied (RLS) |
| access · roles | 401 | permission denied (RLS) |
| social · perfiles_publicos | 401 | permission denied (RLS) |

**Conclusión:**
- Con la **clave publicable**, el backend lee lo mismo que un visitante anónimo: las
  **tablas públicas** (content, courses devolvieron datos reales). Las demás devuelven
  `permission denied` por sus políticas **RLS** (comportamiento correcto y deseado).
- Para que un microservicio lea **todas** las tablas desde el backend (saltándose RLS)
  se usa la **`service_role` key** de Supabase (secreta, en el panel), nunca la
  publicable ni en el repositorio.
- Implementarlo de forma permanente supone añadir un **adaptador de salida REST**
  (alternativo al JPA/JDBC) que llame a `…/rest/v1/<tabla>` con la clave en cabecera.
  Es un cambio de arquitectura por servicio, no una simple variable de configuración.

## Forma recomendada (política de acceso)
Confirmado con los permisos reales (`information_schema.role_table_grants`): las tablas que
devuelven 401 al anónimo solo conceden `SELECT` al rol `authenticated`; por eso en la web
funcionan (el usuario va autenticado y su petición lleva el **JWT** de rol `authenticated`).

Política recomendada (recogida también en el SDD raíz, `.claude/rules/supabase-acceso-datos.md`):
1. **Lecturas de un usuario → REST + reenvío de su JWT.** El microservicio reenvía
   `Authorization: Bearer <jwt_del_usuario>` + `apikey: <clave publicable>` a
   `…/rest/v1/<tabla>`. Respeta RLS, replica exactamente la web y **no guarda secretos**.
2. **Backend sin usuario (batch/admin) → `service_role` key**, como **secreto en variable
   de entorno** (`SUPABASE_SERVICE_ROLE_KEY`), nunca en el repositorio.
3. **Nunca** `service_role` para peticiones de usuario, **nunca** secretos en Git.

| Tabla | `anon` (publicable) | `authenticated` (JWT de sesión) |
|---|---|---|
| articulos | ✅ | ✅ |
| unidades_curso | ✅ | ✅ |
| examenes | — | ✅ |
| vocabulario_items | — | ✅ |
| roles | — | ✅ |
| perfiles_publicos | — | ✅ |

## Prueba por microservicio (consulta del endpoint principal)
Por cada microservicio se ejecutó la consulta equivalente a su endpoint principal
(solo lectura). Los 9 **devuelven datos reales**:

| Servicio | Endpoint | Filas | Muestra real |
|----------|----------|------:|--------------|
| vocabulary | `/v1/vocabulary/items` | 15 | `{en:"give up", es:"rendirse", level:"B1"}` |
| access | `/v1/access/roles` | 4 | `{codigo:"superadmin", descripcion:"Control total…"}` |
| battle | `/v1/battle/questions` | 120 | `{level:"A1", topic:"daily", prompt:"¿Qué significa 'reliable'?"}` |
| study-plan | `/v1/study-plan/users/{id}` | 1 | `{fecha_examen:"2027-08-02", minutos_dia:60, prioridad:"auto"}` |
| social | `/v1/social/profiles` | 1 | `{alias:"kiko", avatar:"🎓"}` |
| exams | `/v1/exams` | 24 | `{codigo:"EE-2026-10-10-002", titulo:"Examen diario 002", nivel:"B1-B2"}` |
| misc | `/v1/.../logbook/notes` | 28 | `{contenido:"Phrasal verb: Run out of…"}` |
| content | `/v1/content/articles` | 118 | `{slug:"b2-curso-unidad-12", titulo:"Unidad 12 · Pronouns…"}` |
| courses | `/v1/courses/activities` | 5 | `{slug:"demostracion-motor-universal", destreza:"use_of_english"}` |

Nota: la comprobación es a nivel de consulta (la misma que ejecutaría cada endpoint).
Arrancar los 9 procesos y llamarlos por HTTP requiere la credencial de Supabase según la
política de acceso (JWT del usuario por REST, o `service_role` para backend sin usuario).

