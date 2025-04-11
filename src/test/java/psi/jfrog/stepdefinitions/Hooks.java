package psi.jfrog.stepdefinitions;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import psi.jfrog.base.BaseTest;
import psi.jfrog.utilities.ExtentManager;
import psi.jfrog.utilities.ScreenshotUtil;

import java.io.File;
import java.io.IOException;

public class Hooks extends BaseTest {

    @Before(order = 1)
    public void beforeScenario(Scenario scenario) throws IOException {
        // Create and link scenario to ExtentReport
        ExtentTest test = ExtentManager.getExtent().createTest(scenario.getName());
        ExtentManager.setTest(test);

        test.log(Status.INFO, ">>> Starting scenario: " + scenario.getName());

        setupRestAssured();
        setupWebDriver();
    }

    @After(order = 1)
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            ExtentManager.getTest().fail("Scenario failed: " + scenario.getName());

            // Attach screenshot
            byte[] screenshot = ScreenshotUtil.captureScreenshotAsBytes(driver);
            scenario.attach(screenshot, "image/png", "Failure Screenshot");
            ScreenshotUtil.saveScreenshotToFile(driver, scenario.getName());

            // Attach docker log if exists
            File dockerLog = new File("target/docker-log.txt");
            if (dockerLog.exists()) {
                ExtentManager.getTest().info("Docker CLI logs available in docker-log.txt");
            }
        } else {
            ExtentManager.getTest().pass("Scenario passed: " + scenario.getName());
        }

        tearDownWebDriver();
        ExtentManager.flush();
    }
}