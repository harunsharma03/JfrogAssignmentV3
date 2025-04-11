package psi.jfrog.base;

import io.restassured.RestAssured;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import psi.jfrog.config.Config;

/**
 * BaseTest sets up the environment for both UI and API tests.
 * Provides multi-browser support and centralized API config.
 */
public class BaseTest {

    // Shared WebDriver instance accessible to step definitions
    protected static WebDriver driver;

    /**
     * Initializes RestAssured base URI and basic auth from config.properties.
     */
    public void setupRestAssured() {
        RestAssured.baseURI = Config.getBaseUrl();
        RestAssured.authentication = RestAssured.preemptive()
                .basic(Config.getUsername(), Config.getPassword());
        System.out.println(">>> RestAssured configured with base URI: " + Config.getBaseUrl());
    }

    /**
     * Initializes WebDriver based on passed system property `-Dbrowser`.
     * Supported: chrome (default), firefox, edge.
     */
    public void setupWebDriver() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();

        switch (browser) {
            case "firefox":
                FirefoxOptions ffOptions = new FirefoxOptions();
                driver = new FirefoxDriver(ffOptions);
                System.out.println(">>> Launched Firefox WebDriver");
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                driver = new EdgeDriver(edgeOptions);
                System.out.println(">>> Launched Edge WebDriver");
                break;

            case "chrome":
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(chromeOptions);
                System.out.println(">>> Launched Chrome WebDriver");
                break;
        }

        driver.manage().window().maximize();
    }

    /**
     * Quits the WebDriver instance after scenario execution.
     */
    public void tearDownWebDriver() {
        if (driver != null) {
            driver.quit();
            System.out.println(">>> WebDriver session closed");
        }
    }
}