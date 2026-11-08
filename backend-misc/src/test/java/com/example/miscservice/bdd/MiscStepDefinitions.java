package com.example.miscservice.bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions exercising the Misc REST API over real HTTP.
 */
public class MiscStepDefinitions {

    @LocalServerPort
    private int port;

    @Autowired
    private org.springframework.context.ApplicationContext context;

    private Response response;
    private String userId;
    private Long suggestionId;

    @Given("the misc service is running")
    public void theServiceIsRunning() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        assertThat(context).isNotNull();
        userId = UUID.randomUUID().toString();
    }

    @When("I create a suggestion with title {string} and description {string}")
    public void iCreateSuggestion(final String titulo, final String descripcion) {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body("{\"titulo\":\"" + titulo + "\",\"descripcion\":\"" + descripcion + "\"}")
                .post("/v1/suggestions");
        if (response.getStatusCode() == 201) {
            suggestionId = response.jsonPath().getLong("id");
        }
    }

    @When("I publish that suggestion")
    public void iPublishThatSuggestion() {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body("{\"estado\":\"publicada\"}")
                .patch("/v1/suggestions/" + suggestionId + "/status");
    }

    @When("I list suggestions")
    public void iListSuggestions() {
        response = RestAssured.given().get("/v1/suggestions");
    }

    @When("I create a logbook category named {string}")
    public void iCreateCategory(final String nombre) {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body("{\"nombre\":\"" + nombre + "\"}")
                .post("/v1/users/" + userId + "/logbook/categories");
    }

    @When("I create a logbook note with content {string}")
    public void iCreateNote(final String contenido) {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body("{\"contenido\":\"" + contenido + "\"}")
                .post("/v1/users/" + userId + "/logbook/notes");
    }

    @When("I create a writing draft of type {string}")
    public void iCreateDraft(final String tipo) {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body("{\"tipo\":\"" + tipo + "\"}")
                .post("/v1/users/" + userId + "/writing-drafts");
    }

    @When("I list writing drafts")
    public void iListDrafts() {
        response = RestAssured.given().get("/v1/users/" + userId + "/writing-drafts");
    }

    @Then("the response status is {int}")
    public void theResponseStatusIs(final int status) {
        assertThat(response.getStatusCode()).isEqualTo(status);
    }

    @Then("the response status field is {string}")
    public void theResponseStatusFieldIs(final String estado) {
        assertThat(response.jsonPath().getString("estado")).isEqualTo(estado);
    }
}
