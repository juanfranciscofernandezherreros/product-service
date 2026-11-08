# Plan de Implementación (Java 21 + Docker PostgreSQL + Git Push)

> Este archivo debe ser completado por la IA en la Fase 1 antes de generar código.

## 1. Resumen de la Especificación
- Nombre del Microservicio: (ver `.ai/project-context.txt`)
- Versión de Java: **Java 21**
- Entidad principal:
- Endpoints a implementar:

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
