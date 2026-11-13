Este microservicio ya tiene el CRUD completo generado por plantillas a partir de `specs/database-spec.sql`. Tu trabajo es añadir solo las reglas de negocio que se describen al final de este mensaje; el resto no se toca.

1. Lee `backend/CLAUDE.md` para ver la estructura y abre solo las clases de las tablas afectadas.
2. Implementa cada regla en la capa de aplicación (el caso de uso `<Tabla>Service`) o en el dominio; nunca en el controlador ni en el adaptador de persistencia. Si la regla necesita un error nuevo, añade la excepción en `domain.exception` y su traducción en `GlobalExceptionHandler`.
3. Añade por cada regla un test unitario (JUnit 5 + Mockito) y un escenario Cucumber en un fichero `.feature` nuevo. No edites los `.feature` generados: se sobrescriben al regenerar.
4. No cambies el esquema, las migraciones ni los nombres de los endpoints existentes.
5. No ejecutes el build: lo lanza el script que te ha llamado.

Reglas de negocio:
