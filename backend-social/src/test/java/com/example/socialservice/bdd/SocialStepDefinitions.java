package com.example.socialservice.bdd;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.socialservice.infrastructure.adapter.out.persistence.FollowJpaRepository;
import com.example.socialservice.infrastructure.adapter.out.persistence.MessageJpaRepository;
import com.example.socialservice.infrastructure.adapter.out.persistence.ProfileJpaRepository;
import com.example.socialservice.infrastructure.adapter.out.persistence.RequestJpaRepository;
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
public class SocialStepDefinitions {

    @LocalServerPort
    private int port;

    @Autowired
    private ProfileJpaRepository profileRepository;
    @Autowired
    private FollowJpaRepository followRepository;
    @Autowired
    private MessageJpaRepository messageRepository;
    @Autowired
    private RequestJpaRepository requestRepository;

    private Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        requestRepository.deleteAll();
        messageRepository.deleteAll();
        followRepository.deleteAll();
        profileRepository.deleteAll();
        response = null;
    }

    private Response createProfile(final String userId, final String alias) {
        return given().contentType(ContentType.JSON)
                .body("{\"userId\":\"" + userId + "\",\"alias\":\"" + alias + "\"}")
                .when().post("/v1/social/public-profiles");
    }

    @When("I create a profile for user {string} alias {string}")
    public void iCreateProfile(final String userId, final String alias) {
        response = createProfile(userId, alias);
    }

    @Given("an existing profile for user {string} alias {string}")
    public void anExistingProfile(final String userId, final String alias) {
        assertThat(createProfile(userId, alias).statusCode()).isEqualTo(201);
    }

    @When("I get the profile of user {string}")
    public void iGetProfile(final String userId) {
        response = given().when().get("/v1/social/public-profiles/" + userId);
    }

    @When("user {string} follows {string}")
    public void userFollows(final String userId, final String seguido) {
        response = given().contentType(ContentType.JSON)
                .body("{\"seguido\":\"" + seguido + "\"}")
                .when().post("/v1/social/users/" + userId + "/following");
    }

    @When("user {string} sends a message to {string}")
    public void userSendsMessage(final String remitente, final String destinatario) {
        response = given().contentType(ContentType.JSON)
                .body("{\"remitente\":\"" + remitente + "\",\"destinatario\":\"" + destinatario
                        + "\",\"contenido\":\"hi\"}")
                .when().post("/v1/social/messages");
    }

    @When("user {string} requests to message {string}")
    public void userRequests(final String remitente, final String destinatario) {
        response = given().contentType(ContentType.JSON)
                .body("{\"remitente\":\"" + remitente + "\",\"destinatario\":\"" + destinatario
                        + "\",\"mensajeInicial\":\"hi\"}")
                .when().post("/v1/social/message-requests");
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
