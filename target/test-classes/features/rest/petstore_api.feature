@api @rest
Feature: Pet Store REST API

  Scenario: Find pet by status
    When I send a GET request to "/pet/findByStatus?status=available"
    Then the response status code should be 200

  Scenario: Get pet by ID
    When I send a GET request to "/pet/1"
    Then the response status code should be 200

  Scenario: Find pet by status — validated against OpenAPI spec
    When I send a validated GET request to "/pet/findByStatus?status=available"
    Then the response status code should be 200

  Scenario: Create a new pet — validated against OpenAPI spec
    When I send a validated POST request to "/pet" with body:
      """
      {
        "id": 77777,
        "name": "Buddy",
        "status": "available",
        "photoUrls": ["https://example.com/buddy.jpg"]
      }
      """
    Then the response status code should be 200
    And the response JSON path "name" should be "Buddy"
