"""Ficheros Java identicos en todos los servicios (solo cambia el paquete)."""
from __future__ import annotations

from .model import Service

MAIN = {
    "domain/model/PageResult.java": """package {pkg}.domain.model;

/**
 * Pagina de resultados de un listado.
 *
 * @param <T> tipo de los elementos
 */
public record PageResult<T>(java.util.List<T> items, long total, int page, int size) {
}
""",
    "domain/exception/ResourceNotFoundException.java": """package {pkg}.domain.exception;

/**
 * El recurso pedido no existe.
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
""",
    "domain/exception/ResourceAlreadyExistsException.java": """package {pkg}.domain.exception;

/**
 * Ya existe un recurso con la misma clave.
 */
public class ResourceAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceAlreadyExistsException(final String message) {
        super(message);
    }
}
""",
    "infrastructure/adapter/in/web/dto/PageResponse.java": """package {pkg}.infrastructure.adapter.in.web.dto;

/**
 * Respuesta paginada de un listado.
 *
 * @param <T> tipo de los elementos
 */
public record PageResponse<T>(java.util.List<T> items, long total, int page, int size) {
}
""",
    "infrastructure/adapter/in/web/dto/ErrorResponse.java": """package {pkg}.infrastructure.adapter.in.web.dto;

/**
 * Cuerpo de error comun: codigo y mensaje.
 */
public record ErrorResponse(String code, String message) {
}
""",
    "infrastructure/adapter/in/web/JsonValues.java": """package {pkg}.infrastructure.adapter.in.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * Conversion entre el JSON de la API y el texto JSON que guarda el dominio (columnas jsonb).
 */
public final class JsonValues {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonValues() {
    }

    public static String toText(final JsonNode node) {
        return node == null || node.isNull() ? null : node.toString();
    }

    public static JsonNode toNode(final String text) {
        if (text == null) {
            return null;
        }
        try {
            return MAPPER.readTree(text);
        } catch (JsonProcessingException ex) {
            return TextNode.valueOf(text);
        }
    }
}
""",
    "infrastructure/adapter/in/web/GlobalExceptionHandler.java": """package {pkg}.infrastructure.adapter.in.web;

import {pkg}.domain.exception.ResourceAlreadyExistsException;
import {pkg}.domain.exception.ResourceNotFoundException;
import {pkg}.infrastructure.adapter.in.web.dto.ErrorResponse;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Traduce los errores a respuestas {@code {code, message}}.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String BAD_REQUEST = "BAD_REQUEST";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(final ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleConflict(final ResourceAlreadyExistsException ex) {
        return build(HttpStatus.CONFLICT, "RESOURCE_ALREADY_EXISTS", ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleIntegrity(final DataIntegrityViolationException ex) {
        return build(HttpStatus.CONFLICT, "DATA_INTEGRITY_VIOLATION",
                "La operacion incumple una restriccion de la base de datos");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBodyValidation(final MethodArgumentNotValidException ex) {
        final String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return build(HttpStatus.BAD_REQUEST, BAD_REQUEST, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(final HttpMessageNotReadableException ex) {
        return build(HttpStatus.BAD_REQUEST, BAD_REQUEST, "Cuerpo de la peticion mal formado");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(final MethodArgumentTypeMismatchException ex) {
        return build(HttpStatus.BAD_REQUEST, BAD_REQUEST, "Valor no valido para el parametro: " + ex.getName());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(final Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Error inesperado");
    }

    private ResponseEntity<ErrorResponse> build(final HttpStatus status, final String code, final String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(code, message));
    }
}
""",
    "infrastructure/adapter/out/persistence/FilterSpecification.java": """package {pkg}.infrastructure.adapter.out.persistence;

import jakarta.persistence.criteria.Predicate;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filtro por igualdad sobre los atributos indicados (clave = nombre del atributo de la entidad).
 */
public final class FilterSpecification {

    private FilterSpecification() {
    }

    public static <T> Specification<T> of(final Map<String, Object> filters) {
        return (root, query, builder) -> builder.and(filters.entrySet().stream()
                .map(entry -> builder.equal(root.get(entry.getKey()), entry.getValue()))
                .toArray(Predicate[]::new));
    }
}
""",
}

TEST = {
    "architecture/HexagonalArchitectureTest.java": """package {pkg}.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Reglas ArchUnit que garantizan el aislamiento hexagonal.
 */
@AnalyzeClasses(packages = "{pkg}", importOptions = ImportOption.DoNotIncludeTests.class)
class HexagonalArchitectureTest {

    @ArchTest
    static final ArchRule DOMAIN_IS_INDEPENDENT = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..application..", "..infrastructure..", "org.springframework..",
                    "jakarta.persistence..", "org.hibernate..");

    @ArchTest
    static final ArchRule APPLICATION_IS_FRAMEWORK_FREE = noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..infrastructure..", "org.springframework..", "jakarta.persistence..",
                    "org.hibernate..");
}
""",
    "bdd/CucumberRunnerTest.java": """package {pkg}.bdd;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Suite de JUnit Platform que ejecuta las features de Cucumber.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "{pkg}.bdd")
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "pretty, html:target/cucumber-reports/cucumber.html, json:target/cucumber-reports/cucumber.json")
class CucumberRunnerTest {
}
""",
    "bdd/CucumberSpringConfiguration.java": """package {pkg}.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * Arranca el contexto completo (puerto aleatorio, perfil test, PostgreSQL embebido) para Cucumber.
 */
@CucumberContextConfiguration
@ActiveProfiles("test")
@Import(EmbeddedPostgresConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberSpringConfiguration {
}
""",
    "bdd/EmbeddedPostgresConfiguration.java": """package {pkg}.bdd;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import java.io.IOException;
import javax.sql.DataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * PostgreSQL real embebido para las pruebas: no necesita Docker ni una base externa.
 */
@TestConfiguration(proxyBeanMethods = false)
public class EmbeddedPostgresConfiguration {

    @Bean(destroyMethod = "close")
    public EmbeddedPostgres embeddedPostgres() throws IOException {
        return EmbeddedPostgres.start();
    }

    @Bean
    public DataSource dataSource(final EmbeddedPostgres postgres) {
        return postgres.getPostgresDatabase();
    }
}
""",
    "bdd/CrudStepDefinitions.java": """package {pkg}.bdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * Pasos genericos: llaman a la API real por HTTP. Los marcadores {@code {campo}} de una ruta se
 * sustituyen por el valor de ese campo en el ultimo recurso creado (respuesta 201).
 */
public class CrudStepDefinitions {

    private static final Pattern PLACEHOLDER = Pattern.compile("\\\\{(\\\\w+)}");

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();

    @LocalServerPort
    private int port;

    private HttpResponse<String> response;
    private JsonNode created;

    @When("envio {word} a {string}")
    public void send(final String method, final String path) throws IOException, InterruptedException {
        execute(method, path, null);
    }

    @When("envio {word} a {string} con el cuerpo")
    public void sendWithBody(final String method, final String path, final String body)
            throws IOException, InterruptedException {
        execute(method, path, body);
    }

    @Then("la respuesta tiene estado {int}")
    public void responseHasStatus(final int expected) {
        assertEquals(expected, response.statusCode(), response.body());
    }

    @Then("la respuesta contiene el campo {string}")
    public void responseHasField(final String field) throws IOException {
        assertTrue(mapper.readTree(response.body()).has(field), response.body());
    }

    private void execute(final String method, final String path, final String body)
            throws IOException, InterruptedException {
        final HttpRequest.BodyPublisher publisher = body == null
                ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(body);
        final HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + resolve(path)))
                .header("Content-Type", "application/json")
                .method(method, publisher)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 201) {
            created = mapper.readTree(response.body());
        }
    }

    private String resolve(final String path) {
        final Matcher matcher = PLACEHOLDER.matcher(path);
        final StringBuilder resolved = new StringBuilder();
        while (matcher.find()) {
            final String value = created.get(matcher.group(1)).asText();
            matcher.appendReplacement(resolved,
                    Matcher.quoteReplacement(URLEncoder.encode(value, StandardCharsets.UTF_8)));
        }
        matcher.appendTail(resolved);
        return resolved.toString();
    }
}
""",
}


def fixed_sources(s: Service) -> dict[str, str]:
    pkg = s.package
    base = pkg.replace(".", "/")
    files = {f"src/main/java/{base}/{path}": content.replace("{pkg}", pkg) for path, content in MAIN.items()}
    files.update({f"src/test/java/{base}/{path}": content.replace("{pkg}", pkg) for path, content in TEST.items()})
    files[f"src/main/java/{base}/{s.app_class}.java"] = f"""package {pkg};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de {s.name}.
 */
@SpringBootApplication
public class {s.app_class} {{

    public static void main(final String[] args) {{
        SpringApplication.run({s.app_class}.class, args);
    }}
}}
"""
    return files
