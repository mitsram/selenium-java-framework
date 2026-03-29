@api @soap
Feature: Calculator SOAP Service

  Scenario: Add two numbers
    When I send a SOAP "http://tempuri.org/Add" request adding 5 and 3
    Then the SOAP response status should be 200
    And the SOAP response should contain "AddResult"

  @contract
  Scenario: Add — response conforms to WSDL schema
    When I send a SOAP "http://tempuri.org/Add" request adding 10 and 20
    Then the SOAP response status should be 200
    And the SOAP response should conform to the schema for "AddResponse"
