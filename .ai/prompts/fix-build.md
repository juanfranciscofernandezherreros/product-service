El build de este microservicio (`backend/`, `mvnw clean verify`) ha fallado. Debajo tienes el final del registro.

El código está generado por plantillas a partir de `specs/database-spec.sql`. Corrige solo lo necesario para que el build pase:

1. Lee el error y abre únicamente los ficheros que nombra. No recorras el proyecto entero.
2. Aplica el cambio mínimo. No reescribas clases que no fallan ni cambies el estilo del código.
3. No modifiques `specs/database-spec.sql`, las migraciones de `db/migration` ni el umbral de cobertura: el esquema es la fuente de verdad.
4. No ejecutes el build: lo vuelve a lanzar el script que te ha llamado.
5. Termina con una línea que diga qué fichero has cambiado y por qué. Si el fallo viene del generador y se repetirá en otros servicios, dilo.

Registro del build:
