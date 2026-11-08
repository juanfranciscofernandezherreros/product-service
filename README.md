# AI-Driven Java CRUD Framework (Hexagonal + Spec-Driven + BDD)

Este workspace contiene la estructura completa para generar y validar un CRUD en Java (Spring Boot 3, Arquitectura Hexagonal) de forma automatizada mediante un agente de IA (ej. Claude Code CLI) en **Windows**.

## Flujo de Trabajo
1. Edita `specs/crud-spec.yaml` y `specs/acceptance-criteria.md` con tu dominio.
2. Ejecuta `run-ai-crud.bat` desde la terminal de Windows (PowerShell o CMD).
3. El script te preguntará por la **carpeta destino** donde quieres guardar el proyecto generado, copiará allí la configuración/especificaciones y ejecutará las 5 fases secuencialmente usando `%MODEL%`:
   - **Fase 1:** Genera el Plan de Implementación (`plans/IMPLEMENTATION_PLAN.md`) y la lista de Tareas (`plans/TASKS.md`).
   - **Fase 2:** Genera la capa de Dominio puro (`domain`).
   - **Fase 3:** Genera Puertos y Casos de Uso (`application`) + Tests JUnit 5.
   - **Fase 4:** Genera Adaptadores (REST + JPA) cubriendo **cada endpoint** con **JUnit 5**, **Cucumber BDD** y **ArchUnit**.
   - **Fase 5:** Ejecuta `mvnw.cmd clean verify` (Checkstyle, ArchUnit, JUnit, Cucumber) y valida que el software cumple al 100% con `specs/crud-spec.yaml` y `specs/acceptance-criteria.md`.
