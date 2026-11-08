package com.example.contentservice.bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions exercising the Content REST API over real HTTP.
 */
public class ContentStepDefinitions {

    @LocalServerPort
    private int port;

    @Autowired
    private ApplicationContext context;

    private Response response;
    private String articleSlug;

    @Given("the content service is running")
    public void theServiceIsRunning() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        assertThat(context).isNotNull();
    }

    @When("I create an article with slug {string}")
    public void iCreateArticle(final String slug) {
        articleSlug = slug;
        final String body = "{\"ruta\":\"post/" + slug + ".html\",\"slug\":\"" + slug
                + "\",\"titulo\":\"Titulo\",\"descripcion\":\"desc\",\"html\":\"<p>hola</p>\","
                + "\"urlPublica\":\"https://x/" + slug + "\"}";
        response = RestAssured.given().contentType(ContentType.JSON).body(body)
                .post("/v1/content/articles");
    }

    @When("I get the article")
    public void iGetTheArticle() {
        response = RestAssured.given().get("/v1/content/articles/" + articleSlug);
    }

    @When("I list articles")
    public void iListArticles() {
        response = RestAssured.given().get("/v1/content/articles");
    }

    @When("I delete the article")
    public void iDeleteTheArticle() {
        response = RestAssured.given().delete("/v1/content/articles/" + articleSlug);
    }

    @When("I create a comment on the article with content {string}")
    public void iCreateComment(final String content) {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body("{\"contenido\":\"" + content + "\"}")
                .post("/v1/content/articles/" + articleSlug + "/comments");
    }

    @When("I create a category with slug {string} and name {string}")
    public void iCreateCategory(final String slug, final String name) {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body("{\"slug\":\"" + slug + "\",\"nombre\":\"" + name + "\"}")
                .post("/v1/content/categories");
    }

    @When("I create a page with path {string}")
    public void iCreatePage(final String ruta) {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body("{\"ruta\":\"" + ruta + "\",\"titulo\":\"T\",\"html\":\"<p>x</p>\","
                        + "\"urlPublica\":\"https://x\"}")
                .post("/v1/content/pages");
    }

    @When("I create a collection with slug {string} and file {string}")
    public void iCreateCollection(final String slug, final String file) {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body("{\"slug\":\"" + slug + "\",\"titulo\":\"T\",\"tipo\":\"articulos\","
                        + "\"nombreArchivo\":\"" + file + "\"}")
                .post("/v1/content/collections");
    }

    @When("I create a downloadable with slug {string}")
    public void iCreateDownloadable(final String slug) {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body("{\"slug\":\"" + slug + "\",\"titulo\":\"T\",\"urlArchivo\":\"https://f/x.pdf\"}")
                .post("/v1/content/downloadables");
    }

    @When("I create a movie phrase for movie {string} number {int}")
    public void iCreateMoviePhrase(final String movie, final int number) {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body("{\"movieSlug\":\"" + movie + "\",\"movieTitle\":\"Title\",\"subtitleNumber\":"
                        + number + ",\"timecode\":\"00:01\",\"phrase\":\"hello\"}")
                .post("/v1/content/movie-phrases");
    }

    @When("I list movie phrases for movie {string}")
    public void iListMoviePhrases(final String movie) {
        response = RestAssured.given().get("/v1/content/movie-phrases?movieSlug=" + movie);
    }

    @Then("the response status is {int}")
    public void theResponseStatusIs(final int status) {
        assertThat(response.getStatusCode()).isEqualTo(status);
    }

    @Then("the article slug field is {string}")
    public void theArticleSlugFieldIs(final String slug) {
        assertThat(response.jsonPath().getString("slug")).isEqualTo(slug);
    }
}
