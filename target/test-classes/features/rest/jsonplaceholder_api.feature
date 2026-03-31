@api @rest @jsonplaceholder
Feature: JSONPlaceholder REST API

  # ── Posts CRUD ──────────────────────────────────────────────

  Scenario: List all posts
    When I send a GET request to JSONPlaceholder "/posts"
    Then the response status code should be 200
    And the response body should contain "userId"

  Scenario: Get a single post by ID
    When I send a GET request to JSONPlaceholder "/posts/1"
    Then the response status code should be 200
    And the response JSON path "id" should be "1"

  Scenario: Create a new post
    When I send a POST request to JSONPlaceholder "/posts" with body:
      """
      {
        "title": "My New Post",
        "body": "This is the post body",
        "userId": 1
      }
      """
    Then the response status code should be 201
    And the response JSON path "title" should be "My New Post"

  Scenario: Update an existing post
    When I send a PUT request to JSONPlaceholder "/posts/1" with body:
      """
      {
        "id": 1,
        "title": "Updated Post Title",
        "body": "Updated content",
        "userId": 1
      }
      """
    Then the response status code should be 200
    And the response JSON path "title" should be "Updated Post Title"

  Scenario: Delete a post
    When I send a DELETE request to JSONPlaceholder "/posts/1"
    Then the response status code should be 200

  # ── Related resources ───────────────────────────────────────

  Scenario: Get comments for a post
    When I send a GET request to JSONPlaceholder "/posts/1/comments"
    Then the response status code should be 200
    And the response body should contain "email"

  Scenario: List all users
    When I send a GET request to JSONPlaceholder "/users"
    Then the response status code should be 200
    And the response body should contain "Leanne Graham"

  Scenario: Get a single user by ID
    When I send a GET request to JSONPlaceholder "/users/1"
    Then the response status code should be 200
    And the response JSON path "username" should be "Bret"

  # ── OpenAPI contract validation ─────────────────────────────

  @contract
  Scenario: List posts — validated against OpenAPI spec
    When I send a spec-validated GET request to JSONPlaceholder "/posts"
    Then the response status code should be 200

  @contract
  Scenario: Get post — validated against OpenAPI spec
    When I send a spec-validated GET request to JSONPlaceholder "/posts/1"
    Then the response status code should be 200

  @contract
  Scenario: Create post — validated against OpenAPI spec
    When I send a spec-validated POST request to JSONPlaceholder "/posts" with body:
      """
      {
        "title": "Contract Test Post",
        "body": "Validated by spec",
        "userId": 1
      }
      """
    Then the response status code should be 201
