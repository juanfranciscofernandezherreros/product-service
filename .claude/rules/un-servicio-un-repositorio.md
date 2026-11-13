# Un servicio = un repositorio (modelo multi-microservicio)

**REGLA OBLIGATORIA:** cada microservicio se genera en **su propia carpeta** y se publica en **su propio repositorio Git**. **NUNCA** se combinan varios servicios en el mismo repositorio ni en el mismo checkout.

- La lista de servicios a generar vive en `.ai/services.txt` (manifiesto), una línea por servicio con campos `;`-separados: `SERVICE_NAME;TARGET_DIR;REMOTE_URL;SPEC_FILE;DB_SPEC` (`DB_SPEC` es opcional; por defecto `specs/database-spec.sql`).
- `run-ai-crud.bat` recorre ese manifiesto y, por cada servicio: prepara su carpeta `TARGET_DIR`, copia las reglas y skills (`CLAUDE.md` + `.claude/`), copia su contrato (`SPEC_FILE` → `specs/crud-spec.yaml`), escribe su `.ai/project-context.txt` propio, ejecuta las fases **en esa carpeta** y hace `git init` + `git remote add origin <REMOTE_URL>` + `git push -u origin main` **a su remoto**.
- Un contrato OpenAPI distinto ⇒ un microservicio distinto ⇒ un repositorio distinto. Si llega un contrato nuevo, se genera un servicio nuevo en su propio repo, no se añade a uno existente.

## Git y repositorio remoto
- Al finalizar la Fase 5, inicializa Git (`git init`), configura el remote (`git remote add origin <REMOTE_URL>`), añade todos los ficheros (`git add .`), haz commit (`git commit -m "feat: initial hexagonal crud microservice <SERVICE_NAME> in java 21"`) y push (`git push -u origin main`).
- `REMOTE_URL` se lee de `.ai/project-context.txt`.
