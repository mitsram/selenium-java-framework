package com.framework.tests.soap;

import com.framework.api.SoapClient;
import com.framework.api.WsdlSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryInfoSoapTest {

    private static final String NS = "http://www.oorsprong.org/websamples.countryinfo";
    private SoapClient soapClient;

    @BeforeEach
    void setUp() {
        soapClient = new SoapClient("http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso");
    }

    @Test
    void shouldGetCapitalCity() {
        String body = """
                <CapitalCity xmlns="%s">
                  <sCountryISOCode>US</sCountryISOCode>
                </CapitalCity>""".formatted(NS);

        Response response = soapClient.send(NS + "/CapitalCity", SoapClient.envelope(body));

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("Washington"));
    }

    @Test
    void shouldGetCountryName() {
        String body = """
                <CountryName xmlns="%s">
                  <sCountryISOCode>DE</sCountryISOCode>
                </CountryName>""".formatted(NS);

        Response response = soapClient.send(NS + "/CountryName", SoapClient.envelope(body));

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("Germany"));
    }

    @Test
    void shouldGetCurrencyInfo() {
        String body = """
                <CountryCurrency xmlns="%s">
                  <sCountryISOCode>GB</sCountryISOCode>
                </CountryCurrency>""".formatted(NS);

        Response response = soapClient.send(NS + "/CountryCurrency", SoapClient.envelope(body));

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("GBP"));
    }

    @Test
    void shouldGetCountryFlag() {
        String body = """
                <CountryFlag xmlns="%s">
                  <sCountryISOCode>FR</sCountryISOCode>
                </CountryFlag>""".formatted(NS);

        Response response = soapClient.send(NS + "/CountryFlag", SoapClient.envelope(body));

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("CountryFlagResult"));
    }

    @Test
    void shouldValidateCapitalCityResponseAgainstSchema() {
        String body = """
                <CapitalCity xmlns="%s">
                  <sCountryISOCode>US</sCountryISOCode>
                </CapitalCity>""".formatted(NS);

        Response response = soapClient.send(NS + "/CapitalCity", SoapClient.envelope(body));

        assertEquals(200, response.getStatusCode());
        WsdlSchemaValidator.validate(response.getBody().asString(), "CapitalCityResponse");
    }
}
