---
name: nuevo-esquema-database-first
description: Lleva un cambio del esquema de Supabase (tabla nueva, columna cambiada o servicio nuevo) hasta el microservicio generado. Úsala cuando cambie el esquema, se añada una tabla o un dominio, se quiera mover una tabla de servicio o los dominios no coincidan con Supabase.
---

# Del esquema de Supabase al microservicio (Database-First)

Supabase es el origen. Los DDL del generador se derivan del export y no se editan a mano; el código se deriva de los DDL. Todo el recorrido es determinista: no hace falta IA.

## Pasos (en el repositorio del generador)

1. **Exporta el esquema real.**
   ```
   python scripts/export_supabase_schema.py
   ```
   Necesita `psql` y la variable de entorno `SUPABASE_DB_URL` (secreto: nunca en el repositorio). Reescribe `specs/supabase-schema.sql`. Sin `psql`, pega `scripts/export-schema.sql` en el SQL Editor de Supabase.

2. **Reparte las tablas.** Edita `specs/domain-map.txt`: cada tabla del export debe estar en exactamente un dominio. Una tabla nueva se añade al dominio de las tablas que referencia; un servicio nuevo es una línea nueva `dominio: tabla1, tabla2`.

3. **Regenera los dominios y compruébalos.**
   ```
   python scripts/split_domains.py
   python scripts/check_domains.py
   ```
   `split_domains.py` copia cada tabla tal cual a `specs/domains/<dominio>.sql` y omite solo las claves foráneas hacia tablas de fuera del servicio (las lista en la cabecera). `check_domains.py` debe terminar con 0 diferencias y 0 tablas sin dominio.

4. **Si el dominio es nuevo**, añade su línea a `.ai/services.txt`, crea su repositorio remoto y añádelo a la matriz de `.github/workflows/validate.yml`:
   ```
   <dominio>-service;../<dominio>-service;<REMOTE_URL>;specs/openapi/<dominio>.yaml;specs/domains/<dominio>.sql
   ```

5. **Regenera los contratos y valida.**
   ```
   python scripts/generate.py --openapi
   python scripts/validate_specs.py
   ```

6. **Genera los servicios afectados.**
   ```
   python scripts/generate.py --build
   ```
   Solo se regeneran los servicios cuyo DDL ha cambiado. Con `--push` se suben a su remoto si el build pasa (skill `generar-crud`).

## Qué obtiene cada tabla

- `POST`, `GET` (lista paginada con filtros por claves foráneas), `GET` por clave, `PUT` y `DELETE` bajo `/v1/<tabla-con-guiones>`. Las tablas en las que todas las columnas son clave no tienen `PUT`.
- Clave generada por la base (identidad o UUID por defecto): no viaja en el alta. Clave aportada por el cliente: el alta duplicada devuelve 409.
- Tipos: `jsonb` es JSON libre en la API; los arrays son listas; `timestamptz` es fecha-hora ISO.
- Tests unitarios del caso de uso y del controlador, y una feature de Cucumber con el ciclo CRUD, duplicados, peticiones no válidas y recursos inexistentes.

## Límites que conviene conocer

- El generador exige clave primaria en todas las tablas.
- Las restricciones CHECK y las claves foráneas no se validan en el servicio: las aplica la base y el servicio responde 409. El esquema de pruebas las omite para poder insertar datos de ejemplo genéricos.
- Un export antiguo no guarda `GENERATED ... AS IDENTITY`; el generador asume identidad en las claves enteras de una sola columna sin valor por defecto. Vuelve a exportar para eliminar la suposición.
- Lo que no sea CRUD (reglas de negocio, endpoints compuestos) se describe en `specs/business-rules/<SERVICE_NAME>.md` y lo aplica la IA con `--ai-rules`.
