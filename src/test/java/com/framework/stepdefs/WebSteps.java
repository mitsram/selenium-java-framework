package com.framework.stepdefs;

import com.framework.config.ConfigManager;
import com.framework.context.TestContext;
import com.framework.pages.LoginPage;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.*;

public class WebSteps {

    private final TestContext ctx;
    private LoginPage loginPage;

    public WebSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    @Given("I open the login page")
    public void openLoginPage() {
        ctx.getDriver().get(ConfigManager.get("base.url") + "/login");
        loginPage = new LoginPage(ctx.getDriver());
    }

    @When("I login with username {string} and password {string}")
    public void login(String user, String pass) {
        loginPage.enterUsername(user);
        loginPage.enterPassword(pass);
        loginPage.clickLogin();
    }

    @Then("I should see a flash message containing {string}")
    public void verifyFlashMessage(String expected) {
        String actual = loginPage.getFlashMessage();
        assertTrue(actual.contains(expected),
                "Expected flash to contain '%s' but got '%s'".formatted(expected, actual));
    }
}
