Lee `.ai/project-context.txt` para obtener `SERVICE_NAME` y `REMOTE_URL`.
Ejecuta `mvnw.cmd clean verify` en `backend/`.
Comprueba obligatoriamente:
1. El proyecto compila con Java 21 y Checkstyle pasa sin errores.
2. ArchUnit pasa sin errores.
3. Todos los tests de JUnit 5 y Cucumber BDD pasan.
4. Se ha generado el reporte HTML de Cucumber en `backend/target/cucumber-reports/cucumber.html`.
5. Existen `backend/README.md` y `backend/CLAUDE.md` bien documentados.
6. Audita el código generado frente a `specs/crud-spec.yaml` y `specs/acceptance-criteria.md`: confirma endpoint por endpoint, esquema por esquema y código HTTP por código HTTP que todo lo especificado ha sido implementado y probado.
7. Genera `backend/docs/index.html` como **Documentación Funcional Oficial** autocontenida (CSS embebido profesional, lista para publicar en GitHub/GitLab Pages) explicando de forma funcional el servicio `SERVICE_NAME`, reglas de negocio, catálogo de endpoints con ejemplos, modelo de datos, entornos (`local` Docker / `dev`) y resumen de cobertura de pruebas.
8. Inicializa Git en el proyecto (`git init`), añade el repositorio remoto `REMOTE_URL` (`git remote add origin <REMOTE_URL>`), realiza `git add .`, `git commit -m "feat: complete java 21 hexagonal crud microservice <SERVICE_NAME>"` y `git push -u origin main`.
Si falta algo o hay algún fallo, corrígelo y vuelve a ejecutar hasta que todo pase en verde y el push sea exitoso.
Actualiza `plans/TASKS.md` marcando todas las tareas completadas.
