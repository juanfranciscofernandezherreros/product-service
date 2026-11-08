package com.example.examsservice.bdd;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.examsservice.infrastructure.adapter.out.persistence.DiagnosticJpaRepository;
import com.example.examsservice.infrastructure.adapter.out.persistence.ExamJpaRepository;
import com.example.examsservice.infrastructure.adapter.out.persistence.QuestionJpaRepository;
import com.example.examsservice.infrastructure.adapter.out.persistence.ResultJpaRepository;
import com.example.examsservice.infrastructure.adapter.out.persistence.SimulationJpaRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * Step definitions driving real HTTP calls.
 */
public class ExamsStepDefinitions {

    @LocalServerPort
    private int port;

    @Autowired
    private ExamJpaRepository examRepository;
    @Autowired
    private QuestionJpaRepository questionRepository;
    @Autowired
    private ResultJpaRepository resultRepository;
    @Autowired
    private DiagnosticJpaRepository diagnosticRepository;
    @Autowired
    private SimulationJpaRepository simulationRepository;

    private Response response;
    private Long examId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        resultRepository.deleteAll();
        questionRepository.deleteAll();
        diagnosticRepository.deleteAll();
        simulationRepository.deleteAll();
        examRepository.deleteAll();
        response = null;
        examId = null;
    }

    private Response createExam(final String codigo, final String titulo) {
        return given().contentType(ContentType.JSON)
                .body("{\"codigo\":\"" + codigo + "\",\"titulo\":\"" + titulo + "\",\"publicado\":true}")
                .when().post("/v1/exams");
    }

    @When("I create an exam with codigo {string} titulo {string}")
    public void iCreateExam(final String codigo, final String titulo) {
        response = createExam(codigo, titulo);
        if (response.statusCode() == 201) {
            examId = response.jsonPath().getLong("id");
        }
    }

    @Given("an existing exam with codigo {string} titulo {string}")
    public void anExistingExam(final String codigo, final String titulo) {
        final Response created = createExam(codigo, titulo);
        assertThat(created.statusCode()).isEqualTo(201);
        examId = created.jsonPath().getLong("id");
    }

    @When("I get the exam with id {long}")
    public void iGetExam(final long id) {
        response = given().when().get("/v1/exams/" + id);
    }

    @When("I add a question with orden {int} to the exam")
    public void iAddQuestion(final int orden) {
        response = given().contentType(ContentType.JSON)
                .body("{\"orden\":" + orden + ",\"enunciado\":\"q\",\"opciones\":[\"a\",\"b\",\"c\"]}")
                .when().post("/v1/exams/" + examId + "/questions");
    }

    @When("I register a result for user {string} on the exam")
    public void iRegisterResult(final String userId) {
        response = given().contentType(ContentType.JSON)
                .body("{\"usuarioId\":\"" + userId + "\",\"examenId\":" + examId
                        + ",\"respuestas\":{\"q1\":\"a\"},\"aciertos\":5,\"total\":10,\"nota\":50.0}")
                .when().post("/v1/exams/results");
    }

    @When("I register a diagnostic for user {string}")
    public void iRegisterDiagnostic(final String userId) {
        response = given().contentType(ContentType.JSON)
                .body("{\"usuarioId\":\"" + userId + "\",\"aciertos\":30,\"total\":40,\"desglose\":{\"x\":1}}")
                .when().post("/v1/exams/diagnostics");
    }

    @When("I register a simulation for user {string}")
    public void iRegisterSimulation(final String userId) {
        response = given().contentType(ContentType.JSON)
                .body("{\"usuarioId\":\"" + userId + "\",\"modalidad\":\"LISTENING\",\"aciertos\":20,"
                        + "\"total\":40,\"respuestas\":{\"q1\":\"a\"},\"duracionSegundos\":600}")
                .when().post("/v1/exams/simulations");
    }

    @Then("the status is {int}")
    public void theStatusIs(final int status) {
        assertThat(response.statusCode()).isEqualTo(status);
    }

    @Then("the field {string} equals {string}")
    public void theFieldEquals(final String field, final String value) {
        assertThat(response.jsonPath().getString(field)).isEqualTo(value);
    }
}
