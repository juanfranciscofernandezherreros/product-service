package com.example.coursesservice.bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions exercising the Courses REST API over real HTTP.
 */
public class CoursesStepDefinitions {

    @LocalServerPort
    private int port;

    @Autowired
    private ApplicationContext context;

    private Response response;
    private Long courseId;
    private Long activityId;

    @Given("the courses service is running")
    public void theServiceIsRunning() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        assertThat(context).isNotNull();
    }

    @When("I create a course named {string}")
    public void iCreateCourse(final String nombre) {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body("{\"nombre\":\"" + nombre + "\"}")
                .post("/v1/courses");
        if (response.getStatusCode() == 201) {
            courseId = response.jsonPath().getLong("id");
        }
    }

    @When("I get the course")
    public void iGetTheCourse() {
        response = RestAssured.given().get("/v1/courses/" + courseId);
    }

    @When("I delete the course")
    public void iDeleteTheCourse() {
        response = RestAssured.given().delete("/v1/courses/" + courseId);
    }

    @When("I create a unit with slug {string} and level {string}")
    public void iCreateUnit(final String slug, final String nivel) {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body("{\"slug\":\"" + slug + "\",\"titulo\":\"Unidad\",\"nivel\":\"" + nivel + "\"}")
                .post("/v1/courses/units");
    }

    @When("I create an activity with slug {string} and skill {string}")
    public void iCreateActivity(final String slug, final String destreza) {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body("{\"slug\":\"" + slug + "\",\"titulo\":\"Actividad\",\"destreza\":\""
                        + destreza + "\"}")
                .post("/v1/courses/activities");
        if (response.getStatusCode() == 201) {
            activityId = response.jsonPath().getLong("id");
        }
    }

    @When("I list activities")
    public void iListActivities() {
        response = RestAssured.given().get("/v1/courses/activities");
    }

    @When("I add a question with order {int} to the activity")
    public void iAddQuestion(final int orden) {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body("{\"orden\":" + orden + ",\"tipo\":\"seleccion\",\"enunciado\":\"Pregunta?\","
                        + "\"configuracion\":{\"opciones\":[\"a\",\"b\"]}}")
                .post("/v1/courses/activities/" + activityId + "/questions");
    }

    @When("I register an attempt on the activity")
    public void iRegisterAttempt() {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body("{\"usuarioId\":\"" + UUID.randomUUID() + "\",\"respuestas\":{\"1\":\"a\"},"
                        + "\"detalle\":{\"1\":true},\"aciertos\":1,\"evaluables\":1,"
                        + "\"pendientesRevision\":0}")
                .post("/v1/courses/activities/" + activityId + "/attempts");
    }

    @Then("the response status is {int}")
    public void theResponseStatusIs(final int status) {
        assertThat(response.getStatusCode()).isEqualTo(status);
    }

    @Then("the course active field is true")
    public void theCourseActiveFieldIsTrue() {
        assertThat(response.jsonPath().getBoolean("activo")).isTrue();
    }
}
