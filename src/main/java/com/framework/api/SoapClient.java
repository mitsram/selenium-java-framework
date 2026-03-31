package com.framework.api;

import com.framework.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * Lightweight SOAP client built on Rest Assured.
 * Sends raw XML envelopes — no heavy WS-* libraries needed.
 */
public class SoapClient {

    private final String baseUrl;
    private Response lastResponse;

    public SoapClient() {
        this.baseUrl = ConfigManager.get("soap.base.url");
    }

    public SoapClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Response send(String soapAction, String xmlBody) {
        lastResponse = RestAssured.given()
                .baseUri(baseUrl)
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SOAPAction", soapAction)
                .body(xmlBody)
                .post();
        return lastResponse;
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    /** Helper to wrap a body fragment in a SOAP 1.1 envelope. */
    public static String envelope(String body) {
        return """
                <?xml version="1.0" encoding="utf-8"?>
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                  <soap:Body>
                    %s
                  </soap:Body>
                </soap:Envelope>""".formatted(body);
    }
}
