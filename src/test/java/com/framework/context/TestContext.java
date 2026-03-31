package com.framework.context;

import com.framework.api.RestClient;
import com.framework.api.SoapClient;
import io.restassured.response.Response;
import org.openqa.selenium.WebDriver;

/**
 * Shared state across step-definition classes within a single scenario.
 * PicoContainer (cucumber-picocontainer) injects this automatically.
 */
public class TestContext {

    private WebDriver driver;
    private final RestClient restClient = new RestClient();
    private final SoapClient soapClient = new SoapClient();
    private Response lastResponse;

    public WebDriver getDriver()        { return driver; }
    public void setDriver(WebDriver d)  { this.driver = d; }

    public RestClient rest()  { return restClient; }
    public SoapClient soap()  { return soapClient; }

    public Response getLastResponse()           { return lastResponse; }
    public void setLastResponse(Response r)     { this.lastResponse = r; }
}
