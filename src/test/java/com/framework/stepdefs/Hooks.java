package com.framework.stepdefs;

import com.framework.context.TestContext;
import com.framework.factory.BrowserFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {

    private static final Logger log = LoggerFactory.getLogger(Hooks.class);
    private final TestContext ctx;

    public Hooks(TestContext ctx) {
        this.ctx = ctx;
    }

    @Before("@web")
    public void launchBrowser(Scenario scenario) {
        log.info(">>> Starting browser for: {}", scenario.getName());
        ctx.setDriver(BrowserFactory.createDriver());
    }

    @After("@web")
    public void tearDownBrowser(Scenario scenario) {
        if (ctx.getDriver() != null) {
            if (scenario.isFailed()) {
                log.error("<<< Scenario FAILED: {}", scenario.getName());
            }
            ctx.getDriver().quit();
        }
    }
}
