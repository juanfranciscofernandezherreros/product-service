package com.example.accessservice.bdd;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.accessservice.infrastructure.adapter.out.persistence.PermissionJpaRepository;
import com.example.accessservice.infrastructure.adapter.out.persistence.RoleJpaRepository;
import com.example.accessservice.infrastructure.adapter.out.persistence.RolePermissionJpaRepository;
import com.example.accessservice.infrastructure.adapter.out.persistence.UserRoleJpaRepository;
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
public class AccessStepDefinitions {

    @LocalServerPort
    private int port;

    @Autowired
    private RoleJpaRepository roleRepository;
    @Autowired
    private PermissionJpaRepository permissionRepository;
    @Autowired
    private RolePermissionJpaRepository rolePermissionRepository;
    @Autowired
    private UserRoleJpaRepository userRoleRepository;

    private Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        userRoleRepository.deleteAll();
        rolePermissionRepository.deleteAll();
        permissionRepository.deleteAll();
        roleRepository.deleteAll();
        response = null;
    }

    private Response createRole(final String codigo, final String descripcion) {
        return given().contentType(ContentType.JSON)
                .body("{\"codigo\":\"" + codigo + "\",\"descripcion\":\"" + descripcion + "\"}")
                .when().post("/v1/access/roles");
    }

    @When("I create a role {string} {string}")
    public void iCreateRole(final String codigo, final String descripcion) {
        response = createRole(codigo, descripcion);
    }

    @Given("an existing role {string} {string}")
    public void anExistingRole(final String codigo, final String descripcion) {
        assertThat(createRole(codigo, descripcion).statusCode()).isEqualTo(201);
    }

    @Given("an existing permission {string} {string}")
    public void anExistingPermission(final String codigo, final String descripcion) {
        final Response created = given().contentType(ContentType.JSON)
                .body("{\"codigo\":\"" + codigo + "\",\"descripcion\":\"" + descripcion + "\"}")
                .when().post("/v1/access/permissions");
        assertThat(created.statusCode()).isEqualTo(201);
    }

    @When("I get the role {string}")
    public void iGetRole(final String codigo) {
        response = given().when().get("/v1/access/roles/" + codigo);
    }

    @When("I delete the role {string}")
    public void iDeleteRole(final String codigo) {
        response = given().when().delete("/v1/access/roles/" + codigo);
    }

    @When("I add permission {string} to role {string}")
    public void iAddPermission(final String permiso, final String rol) {
        response = given().contentType(ContentType.JSON)
                .body("{\"permiso\":\"" + permiso + "\"}")
                .when().post("/v1/access/roles/" + rol + "/permissions");
    }

    @When("I list permissions of role {string}")
    public void iListPermissions(final String rol) {
        response = given().when().get("/v1/access/roles/" + rol + "/permissions");
    }

    @When("I assign role {string} to user {string}")
    public void iAssignRole(final String rol, final String userId) {
        response = given().contentType(ContentType.JSON)
                .body("{\"rol\":\"" + rol + "\"}")
                .when().post("/v1/access/users/" + userId + "/roles");
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
