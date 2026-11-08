package com.example.studyplanservice.bdd;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.studyplanservice.infrastructure.adapter.out.persistence.AiPlanJpaRepository;
import com.example.studyplanservice.infrastructure.adapter.out.persistence.GoalJpaRepository;
import com.example.studyplanservice.infrastructure.adapter.out.persistence.ReportJpaRepository;
import com.example.studyplanservice.infrastructure.adapter.out.persistence.SessionJpaRepository;
import com.example.studyplanservice.infrastructure.adapter.out.persistence.StudyPlanJpaRepository;
import io.cucumber.java.Before;
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
public class StudyPlanStepDefinitions {

    @LocalServerPort
    private int port;

    @Autowired
    private StudyPlanJpaRepository planRepository;
    @Autowired
    private AiPlanJpaRepository aiPlanRepository;
    @Autowired
    private GoalJpaRepository goalRepository;
    @Autowired
    private SessionJpaRepository sessionRepository;
    @Autowired
    private ReportJpaRepository reportRepository;

    private Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        sessionRepository.deleteAll();
        reportRepository.deleteAll();
        aiPlanRepository.deleteAll();
        goalRepository.deleteAll();
        planRepository.deleteAll();
        response = null;
    }

    private String base(final String userId) {
        return "/v1/study-plan/users/" + userId;
    }

    @When("I get the plan of user {string}")
    public void iGetPlan(final String userId) {
        response = given().when().get(base(userId) + "/plan");
    }

    @When("I upsert the plan of user {string}")
    public void iUpsertPlan(final String userId) {
        response = given().contentType(ContentType.JSON)
                .body("{\"minutosDia\":30,\"diasSemana\":[1,2],\"prioridad\":\"AUTO\",\"recordatorioActivo\":false}")
                .when().put(base(userId) + "/plan");
    }

    @When("I upsert the ai-plan of user {string} with weeks {int}")
    public void iUpsertAiPlan(final String userId, final int weeks) {
        response = given().contentType(ContentType.JSON)
                .body("{\"semanas\":" + weeks + ",\"plan\":{\"a\":1}}")
                .when().put(base(userId) + "/ai-plan");
    }

    @When("I upsert the goal of user {string} tipo {string} cantidad {int}")
    public void iUpsertGoal(final String userId, final String tipo, final int cantidad) {
        response = given().contentType(ContentType.JSON)
                .body("{\"tipo\":\"" + tipo + "\",\"cantidad\":" + cantidad + "}")
                .when().put(base(userId) + "/goal");
    }

    @When("I add a session of user {string} tarea {string}")
    public void iAddSession(final String userId, final String tarea) {
        response = given().contentType(ContentType.JSON)
                .body("{\"tarea\":\"" + tarea + "\"}")
                .when().post(base(userId) + "/sessions");
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
