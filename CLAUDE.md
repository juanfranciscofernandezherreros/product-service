# CLAUDE.md - Backend Java Spring Boot (Arquitectura Hexagonal)

## Ejecución Secuencial Obligatoria (.bat y Fases)
Si se solicita ejecutar o continuar el flujo de generación del CRUD, **DEBES seguir estrictamente este orden** (automatizado en `run-ai-crud.bat`):
1. `01-plan-and-tasks.md` -> Analizar specs y actualizar `plans/IMPLEMENTATION_PLAN.md` y `plans/TASKS.md`.
2. `02-domain.md` -> Implementar capa de dominio puro.
3. `03-application.md` -> Implementar puertos, casos de uso y tests JUnit 5.
4. `04-adapters-and-tests.md` -> Implementar adaptadores REST/JPA, tests JUnit 5 por endpoint, Cucumber BDD y ArchUnit.
5. `05-e2e-verification.md` -> Ejecutar `mvnw.cmd clean verify` y validar cumplimiento total.

*Nota para ejecución en Windows:* El script principal `run-ai-crud.bat` solicita primero la **carpeta destino** del proyecto, prepara el entorno en dicha ruta y orquesta estas 5 fases en orden con el modelo `claude-3-5-sonnet-20241022` (configurable vía variable `MODEL`). No saltes fases ni ejecutes fases posteriores si la anterior falló.

## Comandos de Compilación, Calidad y Test
- Build & Full Verify: `mvnw.cmd clean verify` (o `./mvnw clean verify`)
- Tests Unitarios (JUnit 5): `mvnw.cmd test`
- Tests de Aceptación (Cucumber BDD): `mvnw.cmd verify -Dspring.profiles.active=test`
- Análisis Estático: `mvnw.cmd checkstyle:check`

## Reglas Estrictas de Arquitectura y Código
1. **Arquitectura Hexagonal (Ports & Adapters):**
   - `domain`: Puro Java. **CERO** anotaciones de Spring, JPA o Lombok (salvo `@Getter` si es imprescindible; preferir Java `record` o POJOs inmutables).
   - `application`: Casos de uso (`UseCase`) y puertos (`ports.in`, `ports.out`). Orquestan la lógica de negocio.
   - `infrastructure.adapters.in.web`: Controladores REST, DTOs de entrada/salida, validación con `jakarta.validation`.
   - `infrastructure.adapters.out.persistence`: Entidades JPA separadas del dominio, mappers explícitos y repositorios Spring Data.
2. **Inyección de Dependencias:**
   - **SIEMPRE** inyección por constructor (`private final`). **PROHIBIDO** `@Autowired` en campos.
3. **Cobertura Obligatoria por Endpoint:**
   - Cada endpoint definido en `specs/crud-spec.yaml` DEBE tener:
     1. Test unitario en JUnit 5 + Mockito para su Caso de Uso y Controlador.
     2. Escenario Cucumber BDD (`.feature` + Step Definitions) validando el flujo completo (happy path y errores).
4. **Planificación y Tareas:**
   - Antes de escribir código, actualiza `plans/IMPLEMENTATION_PLAN.md` y `plans/TASKS.md` marcando el progreso.
5. **Verificación Final:**
   - Ninguna tarea se considera terminada hasta que `mvnw.cmd clean verify` pase sin errores de Checkstyle, ArchUnit, JUnit ni Cucumber.
