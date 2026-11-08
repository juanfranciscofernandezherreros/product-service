Ejecuta `mvnw.cmd clean verify` en `backend/`.
Comprueba que:
1. Checkstyle pasa sin errores.
2. ArchUnit pasa sin errores.
3. Todos los tests de JUnit 5 y Cucumber BDD pasan.
4. El software implementado cumple al 100% con todos los endpoints, códigos HTTP y validaciones de `specs/crud-spec.yaml` y `specs/acceptance-criteria.md`.
Si hay algún fallo, corrígelo y vuelve a ejecutar `mvnw.cmd clean verify` hasta que pase en verde.
