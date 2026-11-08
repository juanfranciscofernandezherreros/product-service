# Criterios de Aceptación (BDD)

## Feature: Gestión de Transacciones Financieras

### Escenario 1: Crear una transacción válida (POST /v1/transactions)
- **Dado** que envío una petición con amount 150.50, currency "EUR", type "debit" y account_id válido
- **Cuando** invoco el endpoint `POST /v1/transactions`
- **Entonces** recibo un código HTTP 201 Created, con su UUID generado, `status` "pending" y `created_at` informado.

### Escenario 2: Rechazar una transacción inválida (POST /v1/transactions)
- **Dado** que envío una petición con amount 0 y currency "E"
- **Cuando** invoco el endpoint `POST /v1/transactions`
- **Entonces** recibo un código HTTP 400 Bad Request con un error `{ code, message }`.

### Escenario 3: Obtener una transacción por ID (GET /v1/transactions/{id})
- **Dado** que existe una transacción registrada con un ID específico
- **Cuando** invoco `GET /v1/transactions/{id}`
- **Entonces** recibo HTTP 200 OK con los datos de la transacción.

### Escenario 4: Obtener una transacción inexistente (GET /v1/transactions/{id})
- **Dado** un identificador aleatorio que no corresponde a ninguna transacción
- **Cuando** invoco `GET /v1/transactions/{id}`
- **Entonces** recibo HTTP 404 Not Found con un error `{ code, message }`.

### Escenario 5: Listar transacciones con paginación (GET /v1/transactions)
- **Dado** que existen varias transacciones registradas
- **Cuando** invoco `GET /v1/transactions?limit=2&offset=0`
- **Entonces** recibo HTTP 200 OK con `total`, `limit`, `offset` y un array `data` de como máximo 2 elementos.

### Escenario 6: Filtrar transacciones por estado (GET /v1/transactions?status=completed)
- **Dado** que existen transacciones en distintos estados
- **Cuando** invoco `GET /v1/transactions?status=completed`
- **Entonces** recibo HTTP 200 OK y todas las transacciones devueltas tienen `status` "completed".
