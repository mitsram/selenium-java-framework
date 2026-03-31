@api @soap
Feature: Country Info SOAP Service

  Scenario: Get capital city by country code
    When I send a SOAP request to get the capital city of "US"
    Then the SOAP response status should be 200
    And the SOAP response should contain "Washington"

  Scenario: Get country name by ISO code
    When I send a SOAP request to get the country name of "DE"
    Then the SOAP response status should be 200
    And the SOAP response should contain "Germany"

  Scenario: Get currency information by country code
    When I send a SOAP request to get the currency of "GB"
    Then the SOAP response status should be 200
    And the SOAP response should contain "GBP"

  Scenario: Get country flag URL
    When I send a SOAP request to get the flag of "FR"
    Then the SOAP response status should be 200
    And the SOAP response should contain "CountryFlagResult"

  @contract
  Scenario: Capital city response conforms to schema
    When I send a SOAP request to get the capital city of "US"
    Then the SOAP response status should be 200
    And the SOAP response should conform to the schema for "CapitalCityResponse"

  @contract
  Scenario: Country name response conforms to schema
    When I send a SOAP request to get the country name of "JP"
    Then the SOAP response status should be 200
    And the SOAP response should conform to the schema for "CountryNameResponse"
