package com.framework.stepdefs;

import com.framework.api.SoapClient;
import com.framework.api.WsdlSchemaValidator;
import com.framework.context.TestContext;
import io.cucumber.java.en.*;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.*;

public class SoapApiSteps {

    private static final String NS = "http://www.oorsprong.org/websamples.countryinfo";
    private final TestContext ctx;
    private Response response;

    public SoapApiSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    @When("I send a SOAP request to get the capital city of {string}")
    public void sendCapitalCityRequest(String countryCode) {
        String body = """
                <CapitalCity xmlns="%s">
                  <sCountryISOCode>%s</sCountryISOCode>
                </CapitalCity>""".formatted(NS, countryCode);

        response = ctx.soap().send(NS + "/CapitalCity", SoapClient.envelope(body));
    }

    @When("I send a SOAP request to get the country name of {string}")
    public void sendCountryNameRequest(String countryCode) {
        String body = """
                <CountryName xmlns="%s">
                  <sCountryISOCode>%s</sCountryISOCode>
                </CountryName>""".formatted(NS, countryCode);

        response = ctx.soap().send(NS + "/CountryName", SoapClient.envelope(body));
    }

    @When("I send a SOAP request to get the currency of {string}")
    public void sendCurrencyRequest(String countryCode) {
        String body = """
                <CountryCurrency xmlns="%s">
                  <sCountryISOCode>%s</sCountryISOCode>
                </CountryCurrency>""".formatted(NS, countryCode);

        response = ctx.soap().send(NS + "/CountryCurrency", SoapClient.envelope(body));
    }

    @When("I send a SOAP request to get the flag of {string}")
    public void sendCountryFlagRequest(String countryCode) {
        String body = """
                <CountryFlag xmlns="%s">
                  <sCountryISOCode>%s</sCountryISOCode>
                </CountryFlag>""".formatted(NS, countryCode);

        response = ctx.soap().send(NS + "/CountryFlag", SoapClient.envelope(body));
    }

    @Then("the SOAP response status should be {int}")
    public void verifySoapStatus(int expected) {
        assertEquals(expected, response.getStatusCode());
    }

    @Then("the SOAP response should contain {string}")
    public void verifySoapBody(String expected) {
        assertTrue(response.getBody().asString().contains(expected),
                "Expected SOAP response to contain '%s'".formatted(expected));
    }

    @Then("the SOAP response should conform to the schema for {string}")
    public void verifySoapContract(String expectedElement) {
        WsdlSchemaValidator.validate(response.getBody().asString(), expectedElement);
    }
}
