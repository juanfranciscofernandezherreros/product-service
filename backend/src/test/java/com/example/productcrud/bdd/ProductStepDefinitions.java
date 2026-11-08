package com.example.productcrud.bdd;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.productcrud.infrastructure.adapter.out.persistence.ProductJpaRepository;
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
public class ProductStepDefinitions {

    @LocalServerPort
    private int port;

    @Autowired
    private ProductJpaRepository repository;

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

    private String body(final String sku, final String name, final String price) {
        return "{\"sku\":\"" + sku + "\",\"name\":\"" + name + "\",\"price\":" + price + "}";
    }

    @When("I create a product with sku {string} name {string} and price {double}")
    public void iCreateAProduct(final String sku, final String name, final double price) {
        response = given().contentType(ContentType.JSON)
                .body(body(sku, name, String.valueOf(price)))
                .when().post("/api/v1/products");
        if (response.statusCode() == 201) {
            currentId = response.jsonPath().getString("id");
        }
    }

    @Given("an existing product with sku {string} name {string} and price {double}")
    public void anExistingProduct(final String sku, final String name, final double price) {
        final Response created = given().contentType(ContentType.JSON)
                .body(body(sku, name, String.valueOf(price)))
                .when().post("/api/v1/products");
        assertThat(created.statusCode()).isEqualTo(201);
        currentId = created.jsonPath().getString("id");
    }

    @When("I get the product by its id")
    public void iGetTheProductByItsId() {
        response = given().when().get("/api/v1/products/" + currentId);
    }

    @When("I get a product with a random id")
    public void iGetAProductWithARandomId() {
        response = given().when().get("/api/v1/products/" + UUID.randomUUID());
    }

    @When("I update the product with sku {string} name {string} and price {double}")
    public void iUpdateTheProduct(final String sku, final String name, final double price) {
        response = given().contentType(ContentType.JSON)
                .body(body(sku, name, String.valueOf(price)))
                .when().put("/api/v1/products/" + currentId);
    }

    @When("I update a random product with sku {string} name {string} and price {double}")
    public void iUpdateARandomProduct(final String sku, final String name, final double price) {
        response = given().contentType(ContentType.JSON)
                .body(body(sku, name, String.valueOf(price)))
                .when().put("/api/v1/products/" + UUID.randomUUID());
    }

    @When("I delete the product by its id")
    public void iDeleteTheProductByItsId() {
        response = given().when().delete("/api/v1/products/" + currentId);
    }

    @When("I delete a product with a random id")
    public void iDeleteAProductWithARandomId() {
        response = given().when().delete("/api/v1/products/" + UUID.randomUUID());
    }

    @Then("the response status is {int}")
    public void theResponseStatusIs(final int status) {
        assertThat(response.statusCode()).isEqualTo(status);
    }

    @Then("the response contains a generated id")
    public void theResponseContainsAGeneratedId() {
        assertThat(response.jsonPath().getString("id")).isNotNull();
    }

    @Then("the response field {string} equals {string}")
    public void theResponseFieldEquals(final String field, final String value) {
        assertThat(response.jsonPath().getString(field)).isEqualTo(value);
    }

    @Then("getting the product by its id returns 404")
    public void gettingTheProductReturns404() {
        final Response check = given().when().get("/api/v1/products/" + currentId);
        assertThat(check.statusCode()).isEqualTo(404);
    }
}
