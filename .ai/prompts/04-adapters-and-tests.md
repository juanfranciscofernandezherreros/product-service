Implementa los adaptadores de salida (Spring Data JPA con entidades separadas del dominio y mappers) y los adaptadores de entrada (Controladores REST con `jakarta.validation`).
OBLIGATORIO: Por cada endpoint de `specs/crud-spec.yaml`, crea:
1. Test unitario/WebMvcTest en JUnit 5.
2. Escenario en Cucumber BDD (`.feature` + Step Definitions) basado en `specs/acceptance-criteria.md`.
3. Test de ArchUnit verificando que `domain` no depende de `infrastructure`.
Actualiza `plans/TASKS.md` al terminar.
