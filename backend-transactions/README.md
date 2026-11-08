# Transaction Service

Microservicio REST para la gestión de **transacciones financieras**, construido con
**Spring Boot 3.3 / Java 21** y **arquitectura hexagonal** (Ports & Adapters), con
persistencia en **PostgreSQL** y pruebas unitarias (JUnit 5), de arquitectura (ArchUnit)
y de aceptación (Cucumber BDD).

## Requisitos
- **JDK 21**
- **Maven** (wrapper `mvnw` / `mvnw.cmd`)
- **PostgreSQL** (perfiles `local`/`dev`; los tests usan H2 en memoria)

## Estructura (hexagonal)
```
com.example.transactionservice
├── domain                → Transaction, enums, invariantes (Java puro)
├── application           → puertos (in/out) + casos de uso
└── infrastructure
    ├── adapter.in.web    → REST controllers, DTOs, mappers, errores {code,message}
    ├── adapter.out.persistence → entidad JPA, repositorio, adapter, paginación offset
    └── config            → wiring explícito de beans
```

## Perfiles
| Perfil  | Base de datos                            | ddl-auto     | Uso                          |
|---------|------------------------------------------|--------------|------------------------------|
| `local` | PostgreSQL `localhost:5432/transaction_db` | `update`     | Desarrollo local (por defecto) |
| `dev`   | PostgreSQL (env `DB_HOST_DEV`, `DB_NAME_DEV`…) | `validate`   | Entorno compartido           |
| `test`  | H2 en memoria (modo PostgreSQL)          | `create-drop`| Tests automatizados          |

## Arranque
```bash
cd backend-transactions
docker compose up -d
./mvnw spring-boot:run
```
La API escucha en `http://localhost:8081`.

## Endpoints (base `/v1`)
| Método | Ruta | Descripción | Respuestas |
|--------|------|-------------|------------|
| POST | `/v1/transactions` | Crear transacción | 201 / 400 / 500 |
| GET | `/v1/transactions` | Listar (`limit`, `offset`, `status`) | 200 / 400 / 500 |
| GET | `/v1/transactions/{id}` | Obtener por UUID | 200 / 400 / 404 / 500 |

### Ejemplo
```bash
curl -X POST http://localhost:8081/v1/transactions \
  -H "Content-Type: application/json" \
  -d '{"amount":150.50,"currency":"EUR","type":"debit","account_id":"f47ac10b-58cc-4372-a567-0e02b2c3d479"}'
```

## Tests
```bash
cd backend-transactions
./mvnw clean verify
```
Checkstyle, ArchUnit, JUnit 5 + Mockito y Cucumber BDD.
Reporte HTML: `backend-transactions/target/cucumber-reports/cucumber.html`.

## Documentación funcional
Documento autocontenido en [`docs/index.html`](docs/index.html).
