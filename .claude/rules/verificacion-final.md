# Verificación final contra especificaciones (definición de «terminado»)

- Ninguna tarea se considera terminada hasta que `mvnw.cmd clean verify` pase en verde, exista el reporte HTML de Cucumber, se cumpla la **cobertura ≥ 80%** (JaCoCo), exista `backend/docs/index.html` **con el Request y Response de cada endpoint**, se haya auditado frente a `specs/crud-spec.yaml` y se haya completado el commit y push al repositorio remoto.
- **Prueba final de datos:** cuando el servicio se conecta a Supabase (perfil `dev`), debe existir además la verificación en `specs/VERIFICACION-DATOS.md` que demuestre que cada endpoint principal **devuelve datos reales**, coherente con la política de `supabase-acceso-datos.md`.
- **Prueba CRUD completa por endpoint (OBLIGATORIA — criterio CA-11):** debe existir y mantenerse `specs/PRUEBAS-CURL-CRUD.html`, un documento HTML con el **`curl` de CADA endpoint de CADA microservicio** en secuencia CRUD (crear → listar → obtener → actualizar → borrar, y subrecursos), con su método, ruta, puerto y cuerpo JSON derivado de los DTOs reales. Cualquier endpoint nuevo o cambiado debe reflejarse en ese documento. El resumen de criterios de aceptación vive en `specs/CRITERIOS-ACEPTACION.html`.

Reglas relacionadas: `tests-y-cobertura.md` (JaCoCo, Cucumber) y la skill `documentacion-funcional-html` (contenido de `backend/docs/index.html`).
