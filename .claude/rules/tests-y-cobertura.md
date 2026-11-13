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
Los tests (`backend/src/test/resources/application-test.yml`) usan un **PostgreSQL real embebido** (`io.zonky.test:embedded-postgres`), no H2: el esquema de Supabase usa `jsonb` y arrays, que H2 no soporta. No necesita Docker ni una base externa, así que `mvnw.cmd clean verify` es autónomo. Flyway aplica `db/testmigration/V1__init.sql` (mismas columnas, tipos, PK y UNIQUE que el DDL, sin CHECK ni FK) y JPA lo valida. Detalle en `base-de-datos-sdd.md`.
