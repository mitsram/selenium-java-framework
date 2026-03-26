package com.framework.stepdefs;

import com.framework.api.OpenApiValidator;
import com.framework.api.RestClient;
import com.framework.config.ConfigManager;
import com.framework.context.TestContext;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.*;

public class RestApiSteps {

    private final TestContext ctx;
    private Response response;

    public RestApiSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    // ── Basic REST steps ─────────────────────────────────────

    @When("I send a GET request to {string}")
    public void sendGet(String path) {
        response = ctx.rest().get(path);
    }

    @When("I send a POST request to {string} with body:")
    public void sendPost(String path, String body) {
        response = ctx.rest().post(path, body);
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int expected) {
        assertEquals(expected, response.getStatusCode());
    }

    @Then("the response body should contain {string}")
    public void verifyBodyContains(String expected) {
        assertTrue(response.getBody().asString().contains(expected));
    }

    @Then("the response JSON path {string} should be {string}")
    public void verifyJsonPath(String jsonPath, String expected) {
        String actual = response.jsonPath().getString(jsonPath);
        assertEquals(expected, actual);
    }

    // ── OpenAPI-validated REST step ──────────────────────────

    @When("I send a validated GET request to {string}")
    public void sendValidatedGet(String path) {
        response = RestAssured.given()
                .baseUri(ConfigManager.get("api.base.url"))
                .filter(OpenApiValidator.filter())
                .accept("application/json")
                .when()
                .get(path);
    }

    @When("I send a validated POST request to {string} with body:")
    public void sendValidatedPost(String path, String body) {
        response = RestAssured.given()
                .baseUri(ConfigManager.get("api.base.url"))
                .filter(OpenApiValidator.filter())
                .contentType("application/json")
                .accept("application/json")
                .body(body)
                .when()
                .post(path);
    }
}
