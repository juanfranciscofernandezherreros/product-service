# Plan de Implementación

## 1. Resumen de la Especificación
- **Entidad principal:** `Product` (id: UUID, sku: String [min 3], name: String [min 2], price: BigDecimal [>= 0.01]).
- **Endpoints a implementar** (`specs/crud-spec.yaml`, base `/api/v1/products`):
  - `POST   /api/v1/products`      → 201 Created / 400 Bad Request
  - `GET    /api/v1/products`      → 200 OK (lista)
  - `GET    /api/v1/products/{id}` → 200 OK / 404 Not Found
  - `PUT    /api/v1/products/{id}` → 200 OK / 400 / 404
  - `DELETE /api/v1/products/{id}` → 204 No Content / 404

## 2. Diseño por Capas (Hexagonal)
- **Domain** (`com.example.productcrud.domain`): Java puro, sin Spring/JPA/Lombok.
  - `model.Product` (inmutable, validación invariante en el constructor).
  - `exception.ProductNotFoundException`, `exception.InvalidProductException`.
- **Application** (`com.example.productcrud.application`):
  - `port.in`: `CreateProductUseCase`, `GetProductUseCase`, `ListProductsUseCase`, `UpdateProductUseCase`, `DeleteProductUseCase` + comandos.
  - `port.out`: `ProductRepositoryPort`.
  - `usecase`: implementaciones con inyección por constructor (`private final`).
- **Infrastructure** (`com.example.productcrud.infrastructure`):
  - `adapter.in.web`: `ProductController`, `dto.ProductRequest`/`ProductResponse`, `ProductWebMapper`, `GlobalExceptionHandler`.
  - `adapter.out.persistence`: `ProductJpaEntity`, `ProductJpaRepository` (Spring Data), `ProductPersistenceAdapter`, `ProductPersistenceMapper`.
  - `config.BeanConfiguration`: wiring explícito (`@Bean`) de los casos de uso.

## 3. Estrategia de Testing
- **ArchUnit:** `domain` no depende de `application`/`infrastructure`/Spring; puertos `out` no dependen de `infrastructure`.
- **JUnit 5 + Mockito:** un test por cada caso de uso y un `@WebMvcTest` por el controlador (happy path + errores por endpoint).
- **Cucumber BDD:** `products.feature` con un escenario por endpoint (crear, obtener, actualizar, eliminar + errores 400/404), ejecutado contra la app real (`@SpringBootTest` + H2 + RestAssured).

## 4. Stack
- Spring Boot 3.3.x, Java 17, H2 (runtime + test), Bean Validation (jakarta), Checkstyle.
