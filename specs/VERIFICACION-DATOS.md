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
