package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME  = By.id("username");
    private static final By PASSWORD  = By.id("password");
    private static final By LOGIN_BTN = By.cssSelector("button[type='submit']");
    private static final By FLASH_MSG = By.id("flash");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void enterUsername(String username) {
        type(USERNAME, username);
    }

    public void enterPassword(String password) {
        type(PASSWORD, password);
    }

    public void clickLogin() {
        click(LOGIN_BTN);
    }

    public String getFlashMessage() {
        return getText(FLASH_MSG);
    }
}
