# Criterios de Aceptación (BDD)

## Feature: Gestión de Fondos de Inversión (CRUD completo + NAV + Órdenes)

### Escenario 1: Crear un fondo válido (POST /funds)
- **Dado** que envío un fondo con ISIN "ES0123456789", nombre, categoría, divisa "EUR" y riskLevel 5
- **Cuando** invoco `POST /funds`
- **Entonces** recibo HTTP 201 Created con el detalle del fondo.

### Escenario 2: Rechazar fondo duplicado (POST /funds)
- **Dado** que ya existe un fondo con ISIN "ES0123456789"
- **Cuando** invoco `POST /funds` con el mismo ISIN
- **Entonces** recibo HTTP 409 Conflict con error `{ code, message }` y code "RESOURCE_ALREADY_EXISTS".

### Escenario 3: Rechazar fondo inválido (POST /funds)
- **Dado** que envío un fondo con riskLevel 9 (fuera de 1..7)
- **Cuando** invoco `POST /funds`
- **Entonces** recibo HTTP 400 Bad Request con error `{ code, message }`.

### Escenario 4: Obtener detalle de un fondo (GET /funds/{isin})
- **Dado** que existe un fondo registrado
- **Cuando** invoco `GET /funds/{isin}`
- **Entonces** recibo HTTP 200 OK con su detalle.

### Escenario 5: Fondo inexistente (GET /funds/{isin})
- **Cuando** invoco `GET /funds/{isin}` con un ISIN que no existe
- **Entonces** recibo HTTP 404 Not Found con error `{ code, message }`.

### Escenario 6: Actualizar un fondo (PUT /funds/{isin})
- **Dado** que existe un fondo registrado
- **Cuando** invoco `PUT /funds/{isin}` con nuevos datos válidos
- **Entonces** recibo HTTP 200 OK con el detalle actualizado.

### Escenario 7: Eliminar un fondo (DELETE /funds/{isin})
- **Dado** que existe un fondo sin posiciones activas
- **Cuando** invoco `DELETE /funds/{isin}`
- **Entonces** recibo HTTP 204 No Content.

### Escenario 8: Eliminar un fondo inexistente (DELETE /funds/{isin})
- **Cuando** invoco `DELETE /funds/{isin}` con un ISIN que no existe
- **Entonces** recibo HTTP 404 Not Found.

### Escenario 9: No eliminar un fondo con posiciones activas (DELETE /funds/{isin})
- **Dado** que existe un fondo con una orden de suscripción activa (no rechazada)
- **Cuando** invoco `DELETE /funds/{isin}`
- **Entonces** recibo HTTP 422 Unprocessable Entity con error `{ code, message }`.

### Escenario 10: Listar fondos con paginación y filtro (GET /funds)
- **Dado** que existen varios fondos en distintas divisas
- **Cuando** invoco `GET /funds?currency=EUR&limit=2&offset=0`
- **Entonces** recibo HTTP 200 OK con `total` e `items` (máximo 2) y todos con currency "EUR".

### Escenario 11: Consultar histórico NAV (GET /funds/{isin}/nav)
- **Dado** que existe un fondo registrado
- **Cuando** invoco `GET /funds/{isin}/nav`
- **Entonces** recibo HTTP 200 OK con `isin` y un array `history`.

### Escenario 12: Emitir una orden (POST /orders)
- **Dado** que existe un fondo registrado
- **Cuando** invoco `POST /orders` con accountId, ISIN, operationType "SUBSCRIPTION" y amount 1000.00
- **Entonces** recibo HTTP 201 Created con `orderId`, `status` "PENDING" y `createdAt`.

> **Nota de interpretación:** el contrato no define un recurso de "posiciones"; se interpreta que un fondo tiene **posiciones activas** cuando existe al menos una orden suya con estado distinto de `REJECTED`. El valor liquidativo (`currentNav`) y el histórico `NAV` se derivan de los `NavPoint` registrados (el contrato solo expone su lectura).
