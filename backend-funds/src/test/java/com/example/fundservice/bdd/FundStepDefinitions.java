package com.example.fundservice.bdd;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.fundservice.infrastructure.adapter.out.persistence.FundJpaRepository;
import com.example.fundservice.infrastructure.adapter.out.persistence.NavPointJpaRepository;
import com.example.fundservice.infrastructure.adapter.out.persistence.OrderJpaRepository;
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
 * Step definitions driving real HTTP calls against the running application.
 */
public class FundStepDefinitions {

    @LocalServerPort
    private int port;

    @Autowired
    private FundJpaRepository fundRepository;
    @Autowired
    private NavPointJpaRepository navRepository;
    @Autowired
    private OrderJpaRepository orderRepository;

    private Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        orderRepository.deleteAll();
        navRepository.deleteAll();
        fundRepository.deleteAll();
        response = null;
    }

    private Response createFund(final String isin, final String name, final String category,
                                final String currency, final int riskLevel) {
        return given().contentType(ContentType.JSON)
                .body("{\"isin\":\"" + isin + "\",\"name\":\"" + name + "\",\"category\":\"" + category
                        + "\",\"currency\":\"" + currency + "\",\"riskLevel\":" + riskLevel + "}")
                .when().post("/v1/funds");
    }

    @When("I create a fund with isin {string} name {string} category {string} currency {string} riskLevel {int}")
    public void iCreateAFund(final String isin, final String name, final String category,
                             final String currency, final int riskLevel) {
        response = createFund(isin, name, category, currency, riskLevel);
    }

    @Given("an existing fund with isin {string}")
    public void anExistingFund(final String isin) {
        final Response created = createFund(isin, "Fund " + isin, "RV", "EUR", 5);
        assertThat(created.statusCode()).isEqualTo(201);
    }

    @Given("an active order for fund {string}")
    public void anActiveOrderForFund(final String isin) {
        final Response created = given().contentType(ContentType.JSON)
                .body("{\"accountId\":\"ACC-1\",\"isin\":\"" + isin
                        + "\",\"operationType\":\"SUBSCRIPTION\",\"amount\":1000.00,\"currency\":\"EUR\"}")
                .when().post("/v1/orders");
        assertThat(created.statusCode()).isEqualTo(201);
    }

    @When("I get the fund by isin {string}")
    public void iGetTheFund(final String isin) {
        response = given().when().get("/v1/funds/" + isin);
    }

    @When("I update the fund {string} with name {string}")
    public void iUpdateTheFund(final String isin, final String name) {
        response = given().contentType(ContentType.JSON)
                .body("{\"name\":\"" + name + "\"}")
                .when().put("/v1/funds/" + isin);
    }

    @When("I delete the fund {string}")
    public void iDeleteTheFund(final String isin) {
        response = given().when().delete("/v1/funds/" + isin);
    }

    @When("I list funds with currency {string} limit {int} offset {int}")
    public void iListFunds(final String currency, final int limit, final int offset) {
        response = given().when().get("/v1/funds?currency=" + currency + "&limit=" + limit + "&offset=" + offset);
    }

    @When("I get the nav history for {string}")
    public void iGetNavHistory(final String isin) {
        response = given().when().get("/v1/funds/" + isin + "/nav");
    }

    @When("I place an order for fund {string} amount {double}")
    public void iPlaceAnOrder(final String isin, final double amount) {
        response = given().contentType(ContentType.JSON)
                .body("{\"accountId\":\"ACC-1\",\"isin\":\"" + isin
                        + "\",\"operationType\":\"SUBSCRIPTION\",\"amount\":" + amount + ",\"currency\":\"EUR\"}")
                .when().post("/v1/orders");
    }

    @Then("the response status is {int}")
    public void theResponseStatusIs(final int statusCode) {
        assertThat(response.statusCode()).isEqualTo(statusCode);
    }

    @Then("the response field {string} equals {string}")
    public void theResponseFieldEquals(final String field, final String value) {
        assertThat(response.jsonPath().getString(field)).isEqualTo(value);
    }

    @Then("the list total is at least {int}")
    public void theListTotalIsAtLeast(final int min) {
        assertThat(response.jsonPath().getLong("total")).isGreaterThanOrEqualTo(min);
    }
}
