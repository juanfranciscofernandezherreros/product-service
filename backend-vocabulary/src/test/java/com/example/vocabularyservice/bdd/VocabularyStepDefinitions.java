package com.example.vocabularyservice.bdd;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.vocabularyservice.infrastructure.adapter.out.persistence.VocabularyItemJpaRepository;
import com.example.vocabularyservice.infrastructure.adapter.out.persistence.VocabularyProgressJpaRepository;
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
public class VocabularyStepDefinitions {

    @LocalServerPort
    private int port;

    @Autowired
    private VocabularyItemJpaRepository itemRepository;
    @Autowired
    private VocabularyProgressJpaRepository progressRepository;

    private Response response;
    private Long currentId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        progressRepository.deleteAll();
        itemRepository.deleteAll();
        response = null;
        currentId = null;
    }

    private Response createItem(final String en, final String es, final String level) {
        return given().contentType(ContentType.JSON)
                .body("{\"en\":\"" + en + "\",\"es\":\"" + es + "\",\"level\":\"" + level + "\"}")
                .when().post("/v1/vocabulary/items");
    }

    @When("I create an item with en {string} es {string} level {string}")
    public void iCreateAnItem(final String en, final String es, final String level) {
        response = createItem(en, es, level);
        if (response.statusCode() == 201) {
            currentId = response.jsonPath().getLong("id");
        }
    }

    @Given("an existing item with en {string} es {string} level {string}")
    public void anExistingItem(final String en, final String es, final String level) {
        final Response created = createItem(en, es, level);
        assertThat(created.statusCode()).isEqualTo(201);
        currentId = created.jsonPath().getLong("id");
    }

    @When("I get the item by its id")
    public void iGetItemById() {
        response = given().when().get("/v1/vocabulary/items/" + currentId);
    }

    @When("I get the item with id {long}")
    public void iGetItemWithId(final long id) {
        response = given().when().get("/v1/vocabulary/items/" + id);
    }

    @When("I update the item with es {string} level {string}")
    public void iUpdateItem(final String es, final String level) {
        response = given().contentType(ContentType.JSON)
                .body("{\"es\":\"" + es + "\",\"level\":\"" + level + "\"}")
                .when().put("/v1/vocabulary/items/" + currentId);
    }

    @When("I delete the item by its id")
    public void iDeleteItem() {
        response = given().when().delete("/v1/vocabulary/items/" + currentId);
    }

    @When("I list items with level {string}")
    public void iListItems(final String level) {
        response = given().when().get("/v1/vocabulary/items?level=" + level);
    }

    @When("I upsert progress for user {string} with lastGrade {int} streak {int}")
    public void iUpsertProgress(final String userId, final int lastGrade, final int streak) {
        response = given().contentType(ContentType.JSON)
                .body("{\"itemId\":" + currentId + ",\"lastGrade\":" + lastGrade + ",\"streak\":" + streak + "}")
                .when().put("/v1/vocabulary/users/" + userId + "/progress");
    }

    @Then("the status is {int}")
    public void theStatusIs(final int status) {
        assertThat(response.statusCode()).isEqualTo(status);
    }

    @Then("the field {string} equals {string}")
    public void theFieldEquals(final String field, final String value) {
        assertThat(response.jsonPath().getString(field)).isEqualTo(value);
    }

    @Then("the list total is at least {int}")
    public void theListTotalIsAtLeast(final int min) {
        assertThat(response.jsonPath().getLong("total")).isGreaterThanOrEqualTo(min);
    }
}
