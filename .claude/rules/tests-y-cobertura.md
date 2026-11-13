# Tests y cobertura

## Cobertura obligatoria por endpoint
Cada endpoint definido en `specs/crud-spec.yaml` DEBE tener:
1. Test unitario en JUnit 5 + Mockito para su Caso de Uso y Controlador.
2. Escenario Cucumber BDD (`.feature` + Step Definitions) validando el flujo completo (happy path y errores).

## Cobertura global mínima (OBLIGATORIO)
El microservicio completo debe superar el **80% de cobertura** (líneas/instrucciones) medido con **JaCoCo**. Configura el plugin `jacoco-maven-plugin` con `report` y una regla `check` (`BUNDLE`, `LINE` ≥ 0.80) enlazada a `verify`, de modo que `mvnw.cmd clean verify` falle si no se alcanza. El informe HTML queda en `target/site/jacoco/index.html`.

## Reporte HTML de Cucumber BDD
- Configura el runner de Cucumber con el plugin:
  `plugin = {"pretty", "html:target/cucumber-reports/cucumber.html", "json:target/cucumber-reports/cucumber.json"}`
- El reporte HTML debe quedar generado tras ejecutar `mvnw.cmd clean verify` en `backend/target/cucumber-reports/cucumber.html`.

## Perfil de test
Para tests automatizados (`backend/src/test/resources/application-test.yml`): usa H2 en memoria en modo PostgreSQL (`jdbc:h2:mem:testdb;MODE=PostgreSQL`) o Testcontainers para que `mvnw.cmd clean verify` funcione sin depender de contenedores activos. Detalle del esquema en tests: ver `base-de-datos-sdd.md`.
