# Evidencia de aceptación (histórico)

Estos documentos son el **resultado de una ejecución** del generador, no especificaciones de entrada:

- `CRITERIOS-ACEPTACION.html` — criterios CA-01..CA-11 con su evidencia.
- `PRUEBAS-CURL-CRUD.html` — `curl` por endpoint de los nueve servicios (CA-11).
- `VERIFICACION-DATOS.md` — comprobación de datos reales en Supabase y política de acceso.

Corresponden a los nueve servicios (`backend-*`) cuando aún vivían dentro de este repositorio (commit `1e05cf6`, en el historial de `main`). Esos servicios usaban un esquema adaptado que no coincidía con Supabase; los actuales se generan desde el esquema real. Las rutas `backend-<servicio>/…` que citan ya no existen aquí: cada servicio se genera ahora en su propio repositorio, y allí produce sus propios `specs/VERIFICACION-DATOS.md`, `specs/PRUEBAS-CURL-CRUD.html` y `specs/CRITERIOS-ACEPTACION.html`.
