package com.framework.stepdefs;

import com.framework.api.SoapClient;
import com.framework.api.WsdlSchemaValidator;
import com.framework.context.TestContext;
import io.cucumber.java.en.*;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.*;

public class SoapApiSteps {

    private final TestContext ctx;
    private Response response;

    public SoapApiSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    @When("I send a SOAP {string} request adding {int} and {int}")
    public void sendAddRequest(String action, int a, int b) {
        String body = """
                <Add xmlns="http://tempuri.org/">
                  <intA>%d</intA>
                  <intB>%d</intB>
                </Add>""".formatted(a, b);

        response = ctx.soap().send(action, SoapClient.envelope(body));
    }

    @Then("the SOAP response status should be {int}")
    public void verifySoapStatus(int expected) {
        assertEquals(expected, response.getStatusCode());
    }

    @Then("the SOAP response should contain {string}")
    public void verifySoapBody(String expected) {
        assertTrue(response.getBody().asString().contains(expected));
    }

    @Then("the SOAP response should conform to the schema for {string}")
    public void verifySoapContract(String expectedElement) {
        WsdlSchemaValidator.validate(response.getBody().asString(), expectedElement);
    }
}
