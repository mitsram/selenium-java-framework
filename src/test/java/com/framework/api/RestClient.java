package com.framework.api;

import com.framework.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

/**
 * Thin wrapper around Rest Assured for REST calls.
 * Each test gets its own instance via TestContext (PicoContainer).
 */
public class RestClient {

    private final RequestSpecification spec;
    private Response lastResponse;

    public RestClient() {
        spec = RestAssured.given()
                .baseUri(ConfigManager.get("api.base.url"))
                .contentType("application/json")
                .accept("application/json");
    }

    public Response get(String path) {
        lastResponse = spec.get(path);
        return lastResponse;
    }

    public Response get(String path, Map<String, ?> queryParams) {
        lastResponse = spec.queryParams(queryParams).get(path);
        return lastResponse;
    }

    public Response post(String path, Object body) {
        lastResponse = spec.body(body).post(path);
        return lastResponse;
    }

    public Response put(String path, Object body) {
        lastResponse = spec.body(body).put(path);
        return lastResponse;
    }

    public Response delete(String path) {
        lastResponse = spec.delete(path);
        return lastResponse;
    }

    public Response getLastResponse() {
        return lastResponse;
    }
}
