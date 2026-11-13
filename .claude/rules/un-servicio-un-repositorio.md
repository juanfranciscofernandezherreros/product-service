# Un servicio = un repositorio (modelo multi-microservicio)

**REGLA OBLIGATORIA:** cada microservicio se genera en **su propia carpeta** y se publica en **su propio repositorio Git**. **NUNCA** se combinan varios servicios en el mismo repositorio ni en el mismo checkout.

- La lista de servicios a generar vive en `.ai/services.txt` (manifiesto), una línea por servicio con campos `;`-separados: `SERVICE_NAME;TARGET_DIR;REMOTE_URL;SPEC_FILE;DB_SPEC`. `DB_SPEC` (el DDL del servicio, en `specs/domains/`) es obligatorio; `SPEC_FILE` vacío activa Database-First. `TARGET_DIR` puede ser absoluta o relativa a la raíz del generador, pero siempre **fuera** de este repositorio.
- `run-ai-crud.bat` (Windows) y `run-ai-crud.sh` (Linux/macOS) recorren ese manifiesto y, por cada servicio: comprueban que `REMOTE_URL` existe, preparan su carpeta `TARGET_DIR`, copian las reglas y skills (`CLAUDE.md` + `.claude/`), los prompts (`.ai/prompts/`) y las plantillas (`templates/`), copian su esquema (`DB_SPEC` → `specs/database-spec.sql`) y su contrato (`SPEC_FILE` → `specs/crud-spec.yaml`), escriben su `.ai/project-context.txt` propio y ejecutan las fases **en esa carpeta**; la Fase 5 hace `git init` + `git remote add origin <REMOTE_URL>` + `git push -u origin main` **a su remoto**.
- **Este repositorio es solo el generador:** aquí no se guarda código de ningún microservicio (ninguna carpeta `backend*`). La CI lo comprueba.
- Un contrato OpenAPI distinto ⇒ un microservicio distinto ⇒ un repositorio distinto. Si llega un contrato nuevo, se genera un servicio nuevo en su propio repo, no se añade a uno existente.

## Git y repositorio remoto
- **Dentro del pipeline** (ejecución de `run-ai-crud.bat`/`.sh` o petición de ejecutar la Fase 5), el commit y el push al `REMOTE_URL` del servicio forman parte de la fase: al finalizar la Fase 5, inicializa Git (`git init`), configura el remote (`git remote add origin <REMOTE_URL>`), añade todos los ficheros (`git add .`), haz commit (`git commit -m "feat: initial hexagonal crud microservice <SERVICE_NAME> in java 21"`) y push (`git push -u origin main`).
- **Fuera del pipeline** (cambios sueltos en un servicio o en este generador), commit y push **solo con petición explícita**.
- `REMOTE_URL` se lee de `.ai/project-context.txt`. Nunca se hace push de un servicio al repositorio del generador ni al de otro servicio.
