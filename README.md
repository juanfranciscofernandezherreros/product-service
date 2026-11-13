# AI CRUD Generator (Java 21 + Hexagonal + Spec-Driven + BDD + PostgreSQL)

Generador de microservicios CRUD en **Java 21** (Spring Boot 3, arquitectura hexagonal, PostgreSQL) dirigido por especificaciones y ejecutado por un agente de IA (Claude Code CLI).

Este repositorio contiene **solo el generador**. Cada microservicio se genera en su propia carpeta y se publica en su propio repositorio Git: un servicio = un repositorio.

## Qué hay aquí

| Ruta | Contenido |
|------|-----------|
| `CLAUDE.md`, `.claude/rules/`, `.claude/skills/` | Normas y procedimientos que sigue el agente (se copian a cada servicio). |
| `.ai/prompts/00..05` | Un prompt por fase del pipeline. |
| `.ai/services.txt` | Manifiesto: qué servicios se generan, dónde y a qué remoto. |
| `specs/domains/*.sql` | DDL PostgreSQL por servicio (fuente de verdad del modelo). |
| `specs/openapi/*.yaml` | Contrato OpenAPI por servicio (derivado del DDL). |
| `specs/supabase-schema.sql` | Export completo del esquema de Supabase (referencia). |
| `templates/` | Ficheros que se copian a cada servicio (`checkstyle.xml`, `.editorconfig`, `plans/`) y plantilla de DDL. |
| `examples/fund-service/` | Ejemplo de contrato y criterios en modo API-First. |
| `evidence/` | Evidencia de aceptación de una ejecución anterior (histórico). |
| `scripts/validate_specs.py` | Valida el manifiesto y los contratos OpenAPI (también en CI). |

## Requisitos
- [Claude Code CLI](https://docs.claude.com/en/docs/claude-code) (`claude`) y `git` en el `PATH`.
- Para compilar y probar los servicios generados: JDK 21 y Docker.
- Un repositorio remoto **ya creado** por cada servicio del manifiesto (el script lo comprueba antes de empezar).

## Uso

1. **Define el servicio** en `.ai/services.txt`, una línea con cinco campos separados por `;`:

   ```
   SERVICE_NAME;TARGET_DIR;REMOTE_URL;SPEC_FILE;DB_SPEC
   ```

   - `TARGET_DIR`: absoluta o relativa a la raíz del generador, siempre fuera de este repositorio (p. ej. `../access-service`).
   - `SPEC_FILE`: el OpenAPI (**API-First**). Déjalo vacío para **Database-First**: el contrato se deriva del DDL en la Fase 0.
   - `DB_SPEC`: el DDL del servicio (obligatorio).

2. **Valida** las especificaciones:

   ```
   pip install pyyaml openapi-spec-validator
   python scripts/validate_specs.py
   ```

3. **Genera**:

   ```
   run-ai-crud.bat        (Windows, CMD o PowerShell)
   ./run-ai-crud.sh       (Linux, macOS, Git Bash)
   ```

   Variables opcionales:
   - `MODEL`: modelo de Claude a usar (por defecto, el de la CLI).
   - `CLAUDE_ARGS`: argumentos extra para la CLI. Las fases se ejecutan sin intervención (`claude -p`), así que normalmente hará falta indicar aquí el modo de permisos con el que quieres que el agente pueda escribir ficheros y ejecutar comandos.

## Qué hace el script por cada servicio

Prepara `TARGET_DIR` con las reglas, skills, prompts, plantillas y especificaciones del servicio, escribe su `.ai/project-context.txt` y ejecuta las fases en esa carpeta:

| Fase | Prompt | Resultado |
|------|--------|-----------|
| 0 *(solo Database-First)* | `00-db-to-openapi.md` | `specs/crud-spec.yaml` (CRUD completo + subrecursos) y `specs/acceptance-criteria.md` derivados del DDL. |
| 1 | `01-plan-and-tasks.md` | `plans/IMPLEMENTATION_PLAN.md` y `plans/TASKS.md`. |
| 2 | `02-domain.md` | Dominio puro en Java 21. |
| 3 | `03-application.md` | Puertos, casos de uso y tests JUnit 5. |
| 4 | `04-adapters-and-tests.md` | Microservicio en `backend/`: REST + JPA, Flyway, `docker-compose.yml`, perfiles `local`/`dev`/`test`, Cucumber BDD, ArchUnit, JaCoCo. |
| 5 | `05-e2e-verification.md` | `mvnw clean verify`, reporte de Cucumber, `backend/docs/index.html`, entregables de aceptación, commit y push al remoto del servicio. |

## Añadir un servicio nuevo desde un esquema

1. Guarda el DDL en `specs/domains/<servicio>.sql` (plantilla: `templates/database-spec.sql`).
2. Añade su línea al manifiesto con `SPEC_FILE` vacío.
3. Crea el repositorio remoto del servicio.
4. Ejecuta el script.

Detalle en `.claude/skills/nuevo-esquema-database-first/SKILL.md` y en `specs/INDICE.md`.
