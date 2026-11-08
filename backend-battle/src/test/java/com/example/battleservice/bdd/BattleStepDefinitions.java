package com.example.battleservice.bdd;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.battleservice.infrastructure.adapter.out.persistence.PlayerJpaRepository;
import com.example.battleservice.infrastructure.adapter.out.persistence.QuestionJpaRepository;
import com.example.battleservice.infrastructure.adapter.out.persistence.RoomJpaRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * Step definitions driving real HTTP calls.
 */
public class BattleStepDefinitions {

    @LocalServerPort
    private int port;

    @Autowired
    private QuestionJpaRepository questionRepository;
    @Autowired
    private RoomJpaRepository roomRepository;
    @Autowired
    private PlayerJpaRepository playerRepository;

    private Response response;
    private Long questionId;
    private String roomId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        playerRepository.deleteAll();
        roomRepository.deleteAll();
        questionRepository.deleteAll();
        response = null;
        questionId = null;
        roomId = null;
    }

    private Response createQuestion(final String level, final String topic, final String prompt) {
        return given().contentType(ContentType.JSON)
                .body("{\"level\":\"" + level + "\",\"topic\":\"" + topic + "\",\"prompt\":\"" + prompt
                        + "\",\"options\":[\"a\",\"b\",\"c\",\"d\"],\"correctIndex\":1,\"explanation\":\"x\"}")
                .when().post("/v1/battle/questions");
    }

    @When("I create a question level {string} topic {string} prompt {string}")
    public void iCreateQuestion(final String level, final String topic, final String prompt) {
        response = createQuestion(level, topic, prompt);
        if (response.statusCode() == 201) {
            questionId = response.jsonPath().getLong("id");
        }
    }

    @Given("an existing question level {string} topic {string} prompt {string}")
    public void anExistingQuestion(final String level, final String topic, final String prompt) {
        final Response created = createQuestion(level, topic, prompt);
        assertThat(created.statusCode()).isEqualTo(201);
        questionId = created.jsonPath().getLong("id");
    }

    @When("I create an invalid question")
    public void iCreateInvalidQuestion() {
        response = given().contentType(ContentType.JSON)
                .body("{\"level\":\"A1\",\"topic\":\"DAILY\",\"prompt\":\"p\","
                        + "\"options\":[\"a\",\"b\"],\"correctIndex\":0,\"explanation\":\"x\"}")
                .when().post("/v1/battle/questions");
    }

    @When("I get the question by its id")
    public void iGetQuestionById() {
        response = given().when().get("/v1/battle/questions/" + questionId);
    }

    @When("I get the question with id {long}")
    public void iGetQuestionWithId(final long id) {
        response = given().when().get("/v1/battle/questions/" + id);
    }

    @When("I create a room level {string} topic {string} duration {int} capacity {int}")
    public void iCreateRoom(final String level, final String topic, final int duration, final int capacity) {
        response = given().contentType(ContentType.JSON)
                .body("{\"hostId\":\"" + UUID.randomUUID() + "\",\"level\":\"" + level + "\",\"topic\":\"" + topic
                        + "\",\"durationSeconds\":" + duration + ",\"capacity\":" + capacity + "}")
                .when().post("/v1/battle/rooms");
        if (response.statusCode() == 201) {
            roomId = response.jsonPath().getString("id");
        }
    }

    @When("I join the room with alias {string}")
    public void iJoinRoom(final String alias) {
        response = given().contentType(ContentType.JSON)
                .body("{\"userId\":\"" + UUID.randomUUID() + "\",\"alias\":\"" + alias + "\"}")
                .when().post("/v1/battle/rooms/" + roomId + "/players");
    }

    @When("I list players of the room")
    public void iListPlayers() {
        response = given().when().get("/v1/battle/rooms/" + roomId + "/players");
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
