package com.framework.factory;

import com.framework.config.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public final class BrowserFactory {

    public enum BrowserType { CHROME, FIREFOX, EDGE }

    private BrowserFactory() {}

    public static WebDriver createDriver() {
        String name = ConfigManager.get("browser", "chrome").toUpperCase();
        BrowserType type = BrowserType.valueOf(name);
        boolean headless = ConfigManager.getBoolean("headless", false);

        WebDriver driver = switch (type) {
            case CHROME -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions opts = new ChromeOptions();
                if (headless) opts.addArguments("--headless=new");
                opts.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                yield new ChromeDriver(opts);
            }
            case FIREFOX -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions opts = new FirefoxOptions();
                if (headless) opts.addArguments("-headless");
                yield new FirefoxDriver(opts);
            }
            case EDGE -> {
                WebDriverManager.edgedriver().setup();
                EdgeOptions opts = new EdgeOptions();
                if (headless) opts.addArguments("--headless=new");
                yield new EdgeDriver(opts);
            }
        };

        int implicitWait = ConfigManager.getInt("implicit.wait", 10);
        int pageLoad = ConfigManager.getInt("page.load.timeout", 30);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoad));
        driver.manage().window().maximize();

        return driver;
    }
}
