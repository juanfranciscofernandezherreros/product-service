# Arquitectura y código (reglas estrictas)

## Stack tecnológico obligatorio
- **Lenguaje:** **Java 21** (`<java.version>21</java.version>` en `pom.xml`). Aprovecha `record` para DTOs y Value Objects, `switch` pattern matching donde aplique y APIs modernas de Java 21.
- **Framework:** Spring Boot 3.x (Web, Data JPA, Validation).
- **Base de Datos:** PostgreSQL levantada mediante `docker-compose.yml` en local.

## Arquitectura Hexagonal (Ports & Adapters)
- `domain`: Puro Java 21. **CERO** anotaciones de Spring, JPA o Lombok (preferir Java `record` o POJOs inmutables).
- `application`: Casos de uso (`UseCase`) y puertos (`ports.in`, `ports.out`). Orquestan la lógica de negocio.
- `infrastructure.adapters.in.web`: Controladores REST, DTOs de entrada/salida (`record`), validación con `jakarta.validation`.
- `infrastructure.adapters.out.persistence`: Entidades JPA separadas del dominio, mappers explícitos y repositorios Spring Data.

## Inyección de dependencias
- **SIEMPRE** inyección por constructor (`private final`). **PROHIBIDO** `@Autowired` en campos.

## Planificación y tareas
- Antes de escribir código, actualiza `plans/IMPLEMENTATION_PLAN.md` y `plans/TASKS.md` marcando el progreso.
