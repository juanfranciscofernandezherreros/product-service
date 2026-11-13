---
name: nuevo-esquema-database-first
description: Convierte un esquema SQL nuevo (DDL PostgreSQL) en un microservicio CRUD completo en modo Database-First. Úsala cuando se añada un .sql nuevo en specs/, se pida un servicio a partir de tablas, o se quiera derivar el OpenAPI desde el DDL.
---

# Esquema nuevo (Database-First): de DDL a CRUD completo

Cuando se añade un esquema nuevo (un `.sql` con el DDL), el resultado DEBE ser un microservicio completo que cumpla **todas** las especificaciones del SDD.

## Pasos
1. **Registra el esquema.** Añádelo como `specs/domains/<servicio>.sql` (plantilla: `templates/database-spec.sql`) y añade una línea en `.ai/services.txt` con `SPEC_FILE` **vacío** (eso activa Database-First; el OpenAPI se derivará del DDL):
   ```
   <servicio>-service;../<servicio>-service;<REMOTE_URL>;;specs/domains/<servicio>.sql
   ```
   El repositorio `REMOTE_URL` debe existir antes de generar. Valida con `python scripts/validate_specs.py`.
2. **Fase 0** (`.ai/prompts/00-db-to-openapi.md`): genera `specs/crud-spec.yaml` con el **CRUD COMPLETO** (crear → listar → obtener → actualizar → borrar) por entidad raíz, **subrecursos REST** para las tablas hijas, y `specs/acceptance-criteria.md`. Ninguna tabla operable queda sin endpoints.
3. **Fases 1→5** (skill `generar-crud`): dominio puro (Java 21) → puertos/casos de uso + JUnit 5 → adaptadores web/JPA + Cucumber + ArchUnit → verificación `mvnw.cmd clean verify`.
4. **Entregables obligatorios** (criterios de aceptación):
   - cobertura JaCoCo ≥ 80% y reporte Cucumber HTML;
   - `backend/docs/index.html` (diagrama SVG + request/response por endpoint + esquema exacto; skill `documentacion-funcional-html`);
   - `README.md` / `CLAUDE.md` del servicio;
   - perfil `dev` → Supabase con la política de acceso (`.claude/rules/supabase-acceso-datos.md`);
   - `specs/VERIFICACION-DATOS.md`, `specs/PRUEBAS-CURL-CRUD.html` (CA-11) y `specs/CRITERIOS-ACEPTACION.html`.
5. **Git**: cada servicio en su carpeta y su propio repositorio remoto. El commit y push del servicio forman parte de la Fase 5 cuando se ejecuta el pipeline; fuera de él, solo con petición explícita (`.claude/rules/un-servicio-un-repositorio.md`).
6. **Contrato derivado**: si quieres conservarlo en el generador, copia el `specs/crud-spec.yaml` generado a `specs/openapi/<servicio>.yaml` y ponlo como `SPEC_FILE` en el manifiesto.

## Comprobaciones antes de dar por bueno el OpenAPI derivado
- Cada tabla operable del DDL tiene sus endpoints (raíz o subrecurso).
- Los nombres de tabla/columna y los tipos salen del DDL, no al revés (`.claude/rules/base-de-datos-sdd.md`).
