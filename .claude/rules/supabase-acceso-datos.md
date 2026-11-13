# Acceso a datos en Supabase (perfil `dev`) — política de seguridad OBLIGATORIA

Cuando el perfil `dev` apunta a Supabase, el acceso a datos sigue esta política (evidencia y pruebas en `specs/VERIFICACION-DATOS.md`):

- **Lecturas en nombre de un usuario (forma recomendada, «igual que la web»):** el microservicio recibe el **JWT** del usuario (el que emite Supabase Auth) y lo **reenvía** a la API REST de Supabase (`https://<ref>.supabase.co/rest/v1/<tabla>`) en la cabecera `Authorization: Bearer <jwt>` junto con `apikey: <clave publicable>`. Así el backend ve exactamente lo que ese usuario puede ver, **respetando RLS**, y **sin ningún secreto en el repositorio**.
- **Tareas de backend sin usuario** (batch, administración, o cuando deba saltarse RLS a propósito): se usa la **`service_role` key** de Supabase, **siempre** como secreto en variable de entorno (`SUPABASE_SERVICE_ROLE_KEY`), **nunca** en Git, ni en el frontend, ni confundida con la clave publicable.
- **PROHIBIDO:** usar `service_role` para peticiones originadas por el usuario (anularía la seguridad por fila), y escribir cualquier clave secreta o contraseña de base de datos en el repositorio.
- **Credenciales por entorno:** toda contraseña/clave se inyecta por variable de entorno (p. ej. `DB_PASS_DEV`, `SUPABASE_SERVICE_ROLE_KEY`) y se mantiene fuera del control de versiones (`.gitignore` incluye `.env` y `.env.local`).
- **La clave publicable** (`sb_publishable_…`) es pública y solo alcanza las tablas con permiso para el rol anónimo (`anon`); no sirve como credencial de backend para datos protegidos por RLS.
- **Prueba final de datos (OBLIGATORIA):** la entrega incluye una verificación registrada en `specs/VERIFICACION-DATOS.md` que confirma, **por cada microservicio**, que la consulta de su endpoint principal **devuelve datos reales** desde Supabase (dev). Debe mantenerse coherente con esta política de acceso y con la configuración de perfiles (`dev` por defecto, credenciales por variable de entorno).
