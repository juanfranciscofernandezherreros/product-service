# content-service

Microservicio hexagonal (Java 21 + Spring Boot 3) para el dominio **Content & Blog**: artículos, comentarios, categorías, páginas, colecciones de material, recursos descargables y frases de subtítulos. Database-First (Flyway crea el esquema, JPA valida con `ddl-auto: validate`).

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET/POST | `/v1/content/articles` | Listar (filtro categoría/nivel, paginado) / crear artículo |
| GET/PUT/DELETE | `/v1/content/articles/{slug}` | Obtener / actualizar / eliminar artículo |
| GET/POST | `/v1/content/articles/{slug}/comments` | Comentarios de un artículo |
| GET/POST | `/v1/content/categories` | Categorías de blog |
| GET/POST | `/v1/content/pages` | Páginas de blog |
| GET/POST | `/v1/content/collections` | Colecciones de material |
| GET/POST | `/v1/content/downloadables` | Recursos descargables (filtro por categoría) |
| GET/POST | `/v1/content/movie-phrases` | Frases de subtítulos (filtro por película) |

## Ejecución

```bash
docker compose up -d        # PostgreSQL local (content_db)
mvn spring-boot:run         # perfil local, puerto 8090
```

## Calidad

```bash
mvn clean verify            # JUnit 5 + Mockito, ArchUnit, Cucumber BDD, Checkstyle, JaCoCo (≥80%)
```

- Reporte Cucumber: `target/cucumber-reports/cucumber.html`
- Reporte JaCoCo: `target/site/jacoco/index.html` (cobertura actual **89%**)
- Documentación funcional: [`docs/index.html`](docs/index.html)

## Perfiles

- `local`: PostgreSQL vía Docker (`jdbc:postgresql://localhost:5432/content_db`).
- `dev`: PostgreSQL parametrizado por variables de entorno.
- `test`: H2 en memoria (modo PostgreSQL) con Flyway habilitado.
