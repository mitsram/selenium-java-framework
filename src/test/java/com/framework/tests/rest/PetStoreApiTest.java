package com.framework.tests.rest;

import com.framework.api.OpenApiValidator;
import com.framework.api.RestClient;
import com.framework.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PetStoreApiTest {

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = new RestClient();
    }

    @Test
    void shouldFindPetsByStatus() {
        Response response = restClient.get("/pet/findByStatus?status=available");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void shouldGetPetById() {
        Response response = restClient.get("/pet/1");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void shouldCreatePet() {
        String body = """
                {
                  "id": 88888,
                  "name": "Rex",
                  "status": "available",
                  "photoUrls": ["https://example.com/rex.jpg"]
                }""";

        Response response = restClient.post("/pet", body);

        assertEquals(200, response.getStatusCode());
        assertEquals("Rex", response.jsonPath().getString("name"));
    }

    @Test
    void shouldValidateGetPetAgainstOpenApiSpec() {
        Response response = RestAssured.given()
                .baseUri(ConfigManager.get("api.base.url"))
                .filter(OpenApiValidator.filter())
                .accept("application/json")
                .when()
                .get("/pet/findByStatus?status=available");

        assertEquals(200, response.getStatusCode());
    }
}
