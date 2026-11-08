# AI-Driven Java 21 CRUD Framework (Hexagonal + Spec-Driven + BDD + PostgreSQL + Docker)

Este workspace contiene la estructura completa para generar y validar un microservicio CRUD en **Java 21** (Spring Boot 3, Arquitectura Hexagonal, PostgreSQL mediante Docker Compose) de forma automatizada mediante un agente de IA (ej. Claude Code CLI) en **Windows**.

## Flujo de Trabajo
1. Edita `specs/crud-spec.yaml` y `specs/acceptance-criteria.md` con tu dominio.
2. Ejecuta `run-ai-crud.bat` desde la terminal de Windows (PowerShell o CMD).
3. El script te preguntará antes de empezar:
   - **Nombre del microservicio** (ej. `product-service`, usado para `artifactId`, `spring.application.name` y documentación).
   - **Carpeta destino** donde guardar el proyecto.
   - **URL del repositorio Git remoto** para subir todo.
4. El script copiará la configuración y ejecutará las 5 fases secuencialmente:
   - **Fase 1:** Genera el Plan de Implementación (`plans/IMPLEMENTATION_PLAN.md`) y la lista de Tareas (`plans/TASKS.md`).
   - **Fase 2:** Genera la capa de Dominio puro (`domain`) en **Java 21**.
   - **Fase 3:** Genera Puertos y Casos de Uso (`application`) + Tests JUnit 5.
   - **Fase 4:** Genera el microservicio en `backend/` con **Java 21**, `docker-compose.yml` para PostgreSQL local, adaptadores (REST + JPA), perfiles (`local`, `dev`, `test`), tests **JUnit 5**, **Cucumber BDD** con **reporte HTML**, **ArchUnit**, e inicializa `backend/README.md` y `backend/CLAUDE.md`.
   - **Fase 5:** Ejecuta `mvnw.cmd clean verify`, genera el reporte HTML de Cucumber (`backend/target/cucumber-reports/cucumber.html`), genera la **Documentación Funcional Oficial en HTML** (`backend/docs/index.html`) lista para publicar, y realiza commit + push al repositorio remoto.
