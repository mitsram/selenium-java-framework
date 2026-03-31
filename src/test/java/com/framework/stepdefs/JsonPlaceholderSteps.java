package com.framework.stepdefs;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import com.framework.api.RestClient;
import com.framework.config.ConfigManager;
import com.framework.context.TestContext;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;

public class JsonPlaceholderSteps {

    private static final String BASE_URL = ConfigManager.get("jsonplaceholder.base.url");
    private static final OpenApiValidationFilter SPEC_FILTER =
            new OpenApiValidationFilter(ConfigManager.get("jsonplaceholder.spec.path"));

    private final RestClient client;
    private final TestContext ctx;

    public JsonPlaceholderSteps(TestContext ctx) {
        this.ctx = ctx;
        this.client = new RestClient(BASE_URL);
    }

    // ── Basic CRUD steps ─────────────────────────────────────

    @When("I send a GET request to JSONPlaceholder {string}")
    public void sendGet(String path) {
        ctx.setLastResponse(client.get(path));
    }

    @When("I send a POST request to JSONPlaceholder {string} with body:")
    public void sendPost(String path, String body) {
        ctx.setLastResponse(client.post(path, body));
    }

    @When("I send a PUT request to JSONPlaceholder {string} with body:")
    public void sendPut(String path, String body) {
        ctx.setLastResponse(client.put(path, body));
    }

    @When("I send a DELETE request to JSONPlaceholder {string}")
    public void sendDelete(String path) {
        ctx.setLastResponse(client.delete(path));
    }

    // ── OpenAPI-validated steps ──────────────────────────────

    @When("I send a spec-validated GET request to JSONPlaceholder {string}")
    public void sendValidatedGet(String path) {
        ctx.setLastResponse(RestAssured.given()
                .baseUri(BASE_URL)
                .filter(SPEC_FILTER)
                .accept("application/json")
                .when()
                .get(path));
    }

    @When("I send a spec-validated POST request to JSONPlaceholder {string} with body:")
    public void sendValidatedPost(String path, String body) {
        ctx.setLastResponse(RestAssured.given()
                .baseUri(BASE_URL)
                .filter(SPEC_FILTER)
                .contentType("application/json")
                .accept("application/json")
                .body(body)
                .when()
                .post(path));
    }
}
