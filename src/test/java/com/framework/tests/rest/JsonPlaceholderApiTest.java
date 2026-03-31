package com.framework.tests.rest;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import com.framework.api.RestClient;
import com.framework.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonPlaceholderApiTest {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static OpenApiValidationFilter specFilter;
    private RestClient client;

    @BeforeAll
    static void loadSpec() {
        String specPath = ConfigManager.get("jsonplaceholder.spec.path");
        specFilter = new OpenApiValidationFilter(specPath);
    }

    @BeforeEach
    void setUp() {
        client = new RestClient(BASE_URL);
    }

    // ── Basic CRUD tests ────────────────────────────────────

    @Test
    void shouldListAllPosts() {
        Response response = client.get("/posts");

        assertEquals(200, response.getStatusCode());
        assertTrue(response.jsonPath().getList("$").size() > 0, "Posts list should not be empty");
    }

    @Test
    void shouldGetPostById() {
        Response response = client.get("/posts/1");

        assertEquals(200, response.getStatusCode());
        assertEquals(1, response.jsonPath().getInt("id"));
        assertEquals(1, response.jsonPath().getInt("userId"));
        assertNotNull(response.jsonPath().getString("title"));
        assertNotNull(response.jsonPath().getString("body"));
    }

    @Test
    void shouldCreatePost() {
        String body = """
                {
                  "title": "Test Post",
                  "body": "This is a test post body",
                  "userId": 1
                }""";

        Response response = client.post("/posts", body);

        assertEquals(201, response.getStatusCode());
        assertEquals("Test Post", response.jsonPath().getString("title"));
        assertEquals(1, response.jsonPath().getInt("userId"));
        assertNotNull(response.jsonPath().getInt("id"));
    }

    @Test
    void shouldUpdatePost() {
        String body = """
                {
                  "id": 1,
                  "title": "Updated Title",
                  "body": "Updated body content",
                  "userId": 1
                }""";

        Response response = client.put("/posts/1", body);

        assertEquals(200, response.getStatusCode());
        assertEquals("Updated Title", response.jsonPath().getString("title"));
    }

    @Test
    void shouldDeletePost() {
        Response response = client.delete("/posts/1");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void shouldGetCommentsForPost() {
        Response response = client.get("/posts/1/comments");

        assertEquals(200, response.getStatusCode());
        assertTrue(response.jsonPath().getList("$").size() > 0, "Comments list should not be empty");
        assertEquals(1, response.jsonPath().getInt("[0].postId"));
    }

    @Test
    void shouldListUsers() {
        Response response = client.get("/users");

        assertEquals(200, response.getStatusCode());
        assertEquals(10, response.jsonPath().getList("$").size());
    }

    @Test
    void shouldGetUserById() {
        Response response = client.get("/users/1");

        assertEquals(200, response.getStatusCode());
        assertEquals("Leanne Graham", response.jsonPath().getString("name"));
        assertEquals("Bret", response.jsonPath().getString("username"));
    }

    // ── OpenAPI contract-validated tests ─────────────────────

    @Test
    void shouldValidateListPostsAgainstSpec() {
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .filter(specFilter)
                .accept("application/json")
                .when()
                .get("/posts");

        assertEquals(200, response.getStatusCode());
    }

    @Test
    void shouldValidateGetPostAgainstSpec() {
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .filter(specFilter)
                .accept("application/json")
                .when()
                .get("/posts/1");

        assertEquals(200, response.getStatusCode());
    }

    @Test
    void shouldValidateCreatePostAgainstSpec() {
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .filter(specFilter)
                .contentType("application/json")
                .accept("application/json")
                .body("""
                        {
                          "title": "Spec Validated Post",
                          "body": "Validated body",
                          "userId": 1
                        }""")
                .when()
                .post("/posts");

        assertEquals(201, response.getStatusCode());
    }

    @Test
    void shouldValidateListUsersAgainstSpec() {
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .filter(specFilter)
                .accept("application/json")
                .when()
                .get("/users");

        assertEquals(200, response.getStatusCode());
    }

    @Test
    void shouldValidateGetCommentsAgainstSpec() {
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .filter(specFilter)
                .accept("application/json")
                .when()
                .get("/posts/1/comments");

        assertEquals(200, response.getStatusCode());
    }
}
