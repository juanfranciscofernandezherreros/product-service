---
name: documentacion-funcional-html
description: Genera o actualiza la Documentación Funcional Oficial en HTML de un microservicio (docs/index.html) con request/response por endpoint, modelo de datos exacto y diagrama SVG. Úsala en la Fase 5, o cuando cambie un endpoint, el esquema o se pida regenerar la documentación.
---

# Documentación Funcional Oficial en HTML (`docs/index.html`)

En el modo por defecto este documento lo escribe el generador (`generator/docs.py`) desde el DDL: para cambiarlo, cambia esa plantilla y regenera; no edites el HTML del servicio. Esta skill describe el contenido que debe tener, y es el procedimiento a seguir a mano solo en `--mode ai` o al documentar reglas de negocio añadidas.

Es **OBLIGATORIO** generar un documento HTML autocontenido (CSS embebido, limpio, navegable y listo para publicar en GitHub/GitLab Pages) en `docs/index.html` que explique **de forma funcional** todo lo desarrollado.

## Fuentes
- `.ai/project-context.txt` → `SERVICE_NAME`.
- `specs/crud-spec.yaml` → endpoints, esquemas y códigos HTTP.
- `specs/database-spec.sql` → modelo de datos.
- DTOs y controladores reales del servicio → ejemplos JSON.

## Contenido obligatorio
1. **Propósito del servicio** (`<SERVICE_NAME>`) y reglas de negocio implementadas.
2. **Catálogo funcional de endpoints** (método, ruta, códigos HTTP y validaciones).
3. **Request y Response por cada endpoint (OBLIGATORIO):** para cada endpoint de `specs/crud-spec.yaml`, el documento debe mostrar explícitamente:
   - **Request:** parámetros (path/query) y, cuando aplique, el cuerpo JSON de ejemplo y su esquema (campos, tipos, obligatoriedad y validaciones).
   - **Response:** el cuerpo JSON de ejemplo de la respuesta de éxito por su código HTTP y, al menos, un ejemplo del cuerpo de error (`{ code, message }` o la estructura de error del servicio).
4. **Modelo de datos exacto (OBLIGATORIO):** el documento debe reflejar fielmente el esquema de `specs/database-spec.sql`: por cada tabla, sus columnas con tipo PostgreSQL, nulabilidad, claves primarias/foráneas, UNIQUE e índices, tal y como se crean en la base de datos.
5. **Diagrama (OBLIGATORIO):** al menos un diagrama embebido como **SVG inline** (sin imágenes externas) que represente la arquitectura hexagonal del servicio (adaptadores de entrada/web → aplicación/puertos y casos de uso → dominio, y adaptador de salida → PostgreSQL) o el flujo principal de una petición.
6. **Resumen de entornos** (`local` con Docker PostgreSQL y `dev`) y **evidencias de calidad** (tests JUnit 5, ArchUnit y Cucumber BDD).

## Comprobación final
- Todos los endpoints de `specs/crud-spec.yaml` aparecen con su request y su response.
- Todas las tablas de `specs/database-spec.sql` aparecen con sus columnas y restricciones.
- El HTML no depende de recursos externos (CSS e imágenes embebidos).
- Si cambió algún endpoint, actualiza también `specs/PRUEBAS-CURL-CRUD.html` (CA-11).
