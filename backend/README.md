# Product CRUD Microservice

Microservicio REST de gestión de productos construido con **Spring Boot 3.3 / Java 21** siguiendo
**arquitectura hexagonal** (Ports & Adapters), con persistencia en **PostgreSQL** y pruebas
unitarias (JUnit 5), de arquitectura (ArchUnit) y de aceptación (Cucumber BDD).

## Requisitos

- **JDK 21**
- **Maven** (incluido el wrapper `mvnw` / `mvnw.cmd`)
- **PostgreSQL** (para los perfiles `local` y `dev`; los tests usan H2 en memoria)

## Estructura (hexagonal)

```
com.example.productcrud
├── domain                → modelo e invariantes de negocio (Java puro)
├── application           → puertos (in/out) + casos de uso
└── infrastructure
    ├── adapter.in.web    → REST controllers, DTOs, mappers, manejo de errores
    ├── adapter.out.persistence → entidad JPA, repositorio, adapter, mapper
    └── config            → wiring explícito de beans
```

## Perfiles

| Perfil  | Base de datos                         | ddl-auto     | Uso                         |
|---------|---------------------------------------|--------------|-----------------------------|
| `local` | PostgreSQL `localhost:5432/product_db` | `update`     | Desarrollo local (por defecto) |
| `dev`   | PostgreSQL (vía env vars)              | `validate`   | Entorno de desarrollo compartido |
| `test`  | H2 en memoria (modo PostgreSQL)        | `create-drop`| Tests automatizados         |

Variables de entorno soportadas: `SPRING_PROFILES_ACTIVE`, `DB_USERNAME`, `DB_PASSWORD` (perfil `local`) y
`DB_HOST_DEV`, `DB_NAME_DEV`, `DB_USER_DEV`, `DB_PASS_DEV` (perfil `dev`).

## Arranque

### 1. Levantar PostgreSQL con Docker Compose

```bash
cd backend
docker compose up -d
```

Esto arranca PostgreSQL 16 (`product_db`, usuario/clave `postgres`) en `localhost:5432`.

### 2. Ejecutar la aplicación (perfil `local` por defecto)

```bash
cd backend
./mvnw spring-boot:run
```

En Windows:

```bash
mvnw.cmd spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

## Endpoints

| Método | Ruta                    | Descripción          | Respuestas       |
|--------|-------------------------|----------------------|------------------|
| POST   | `/api/v1/products`      | Crear producto       | 201 / 400        |
| GET    | `/api/v1/products`      | Listar productos     | 200              |
| GET    | `/api/v1/products/{id}` | Obtener por ID       | 200 / 404        |
| PUT    | `/api/v1/products/{id}` | Actualizar producto  | 200 / 400 / 404  |
| DELETE | `/api/v1/products/{id}` | Eliminar producto    | 204 / 404        |

### Ejemplo

```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{"sku":"PROD-001","name":"Mechanical Keyboard","price":99.99}'
```

## Tests

```bash
cd backend
./mvnw clean verify
```

Incluye: Checkstyle, ArchUnit, JUnit 5 + Mockito y Cucumber BDD.

### Reporte HTML de Cucumber

Tras `verify`, el reporte se genera en:

```
backend/target/cucumber-reports/cucumber.html
```

Ábrelo en el navegador para ver el resultado de cada escenario (happy path y errores).

## Documentación funcional

La documentación funcional oficial (autocontenida, publicable en GitHub/GitLab Pages) está en
[`docs/index.html`](docs/index.html): propósito, reglas de negocio, catálogo de endpoints con
ejemplos, modelo de datos, entornos y evidencias de calidad.
