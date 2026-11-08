package com.example.transactionservice.bdd;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.transactionservice.infrastructure.adapter.out.persistence.TransactionJpaRepository;
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
 * Step definitions driving real HTTP calls against the running application.
 */
public class TransactionStepDefinitions {

    @LocalServerPort
    private int port;

    @Autowired
    private TransactionJpaRepository repository;

    private Response response;
    private String currentId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        repository.deleteAll();
        response = null;
        currentId = null;
    }

    private String body(final String amount, final String currency, final String type) {
        return "{\"amount\":" + amount + ",\"currency\":\"" + currency + "\",\"type\":\"" + type + "\","
                + "\"account_id\":\"" + UUID.randomUUID() + "\"}";
    }

    private Response create(final double amount, final String currency, final String type) {
        return given().contentType(ContentType.JSON)
                .body(body(String.valueOf(amount), currency, type))
                .when().post("/v1/transactions");
    }

    @When("I create a transaction with amount {double} currency {string} type {string}")
    public void iCreateATransaction(final double amount, final String currency, final String type) {
        response = create(amount, currency, type);
        if (response.statusCode() == 201) {
            currentId = response.jsonPath().getString("id");
        }
    }

    @Given("an existing transaction with amount {double} currency {string} type {string}")
    public void anExistingTransaction(final double amount, final String currency, final String type) {
        final Response created = create(amount, currency, type);
        assertThat(created.statusCode()).isEqualTo(201);
        currentId = created.jsonPath().getString("id");
    }

    @Given("{int} existing transactions")
    public void existingTransactions(final int count) {
        for (int i = 0; i < count; i++) {
            final Response created = create(10.00 + i, "EUR", "credit");
            assertThat(created.statusCode()).isEqualTo(201);
        }
    }

    @When("I get the transaction by its id")
    public void iGetTheTransactionByItsId() {
        response = given().when().get("/v1/transactions/" + currentId);
    }

    @When("I get a transaction with a random id")
    public void iGetARandomTransaction() {
        response = given().when().get("/v1/transactions/" + UUID.randomUUID());
    }

    @When("I list transactions with limit {int} and offset {int}")
    public void iListTransactions(final int limit, final int offset) {
        response = given().when().get("/v1/transactions?limit=" + limit + "&offset=" + offset);
    }

    @Then("the response status is {int}")
    public void theResponseStatusIs(final int statusCode) {
        assertThat(response.statusCode()).isEqualTo(statusCode);
    }

    @Then("the response contains a generated id")
    public void theResponseContainsAGeneratedId() {
        assertThat(response.jsonPath().getString("id")).isNotNull();
    }

    @Then("the response field {string} equals {string}")
    public void theResponseFieldEquals(final String field, final String value) {
        assertThat(response.jsonPath().getString(field)).isEqualTo(value);
    }

    @Then("the paged response has total {int} and at most {int} items")
    public void thePagedResponseHasTotal(final int total, final int maxItems) {
        assertThat(response.jsonPath().getLong("total")).isEqualTo(total);
        assertThat(response.jsonPath().getList("data").size()).isLessThanOrEqualTo(maxItems);
    }
}
