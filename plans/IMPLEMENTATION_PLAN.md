# Plan de Implementación (Java 21 + Docker PostgreSQL + Git Push)

> Este archivo debe ser completado por la IA en la Fase 1 antes de generar código.

## 1. Resumen de la Especificación
- Nombre del Microservicio: **product-service** (ver `.ai/project-context.txt`)
- Versión de Java: **Java 21**
- Entidad principal: **Product** (id: UUID, sku: String [min 3], name: String [min 2], price: BigDecimal [>= 0.01]).
- Endpoints a implementar (base `/api/v1/products`): `POST` (201/400), `GET` lista (200), `GET /{id}` (200/404), `PUT /{id}` (200/400/404), `DELETE /{id}` (204/404).

> Estado: implementado y verificado. `docker-compose.yml` (PostgreSQL `product_db`), perfiles `local`/`dev`/`test`, reporte HTML Cucumber, `backend/docs/index.html` y docs generados. `mvn clean verify` en verde. Pendiente opcional: `git push` al remoto (REMOTE_URL no provisto).

## 2. Diseño por Capas (Hexagonal)
- **Domain:** Entidades, Value Objects (`record`), Excepciones de dominio.
- **Application:** Puertos de entrada/salida, Casos de Uso.
- **Infrastructure:** Controladores REST, Entidades JPA (PostgreSQL via Docker Compose), Mappers, Repositorios, Perfiles (`local`, `dev`, `test`).

## 3. Estrategia de Testing, Reportes, Documentación HTML y Git
- **Docker Compose:** Levantamiento de PostgreSQL para el perfil `local`.
- **ArchUnit:** Validar aislamiento del dominio.
- **JUnit 5:** Cobertura unitaria de cada Caso de Uso y Controlador.
- **Cucumber BDD:** Un `.feature` cubriendo todos los endpoints con reporte HTML en `target/cucumber-reports/cucumber.html`.
- **Documentación:** `backend/README.md`, `backend/CLAUDE.md` y `backend/docs/index.html`.
- **Git Push:** Inicialización de repositorio y sincronización con el remoto indicado en `.ai/project-context.txt`.
