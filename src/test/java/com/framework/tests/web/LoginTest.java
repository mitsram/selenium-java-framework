package com.framework.tests.web;

import com.framework.factory.BrowserFactory;
import com.framework.config.ConfigManager;
import com.framework.pages.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeEach
    void setUp() {
        driver = BrowserFactory.createDriver();
        driver.get(ConfigManager.get("base.url") + "/login");
        loginPage = new LoginPage(driver);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void shouldLoginSuccessfully() {
        loginPage.enterUsername("tomsmith");
        loginPage.enterPassword("SuperSecretPassword!");
        loginPage.clickLogin();

        String flash = loginPage.getFlashMessage();
        assertTrue(flash.contains("You logged into a secure area!"),
                "Expected success message but got: " + flash);
    }

    @Test
    void shouldShowErrorOnInvalidLogin() {
        loginPage.enterUsername("invalid");
        loginPage.enterPassword("invalid");
        loginPage.clickLogin();

        String flash = loginPage.getFlashMessage();
        assertTrue(flash.contains("Your username is invalid!"),
                "Expected error message but got: " + flash);
    }
}
