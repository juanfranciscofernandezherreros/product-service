# Criterios de Aceptación (BDD)

## Feature: Gestión de Productos (CRUD)

### Escenario 1: Crear un producto válido (POST /api/v1/products)
- **Dado** que envío una petición con SKU "PROD-001", nombre "Teclado Mecánico" y precio 99.99
- **Cuando** invoco el endpoint `POST /api/v1/products`
- **Entonces** recibo un código HTTP 201 Created y el producto con su UUID generado.

### Escenario 2: Obtener producto por ID (GET /api/v1/products/{id})
- **Dado** que existe un producto registrado con un ID específico
- **Cuando** invoco `GET /api/v1/products/{id}`
- **Entonces** recibo HTTP 200 OK con los datos del producto.

### Escenario 3: Actualizar producto (PUT /api/v1/products/{id})
- **Dado** que existe un producto registrado
- **Cuando** invoco `PUT /api/v1/products/{id}` con nuevos datos válidos
- **Entonces** recibo HTTP 200 OK con los datos actualizados.

### Escenario 4: Eliminar producto (DELETE /api/v1/products/{id})
- **Dado** que existe un producto registrado
- **Cuando** invoco `DELETE /api/v1/products/{id}`
- **Entonces** recibo HTTP 204 No Content y el producto ya no existe en base de datos.
