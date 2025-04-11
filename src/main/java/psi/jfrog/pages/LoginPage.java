package psi.jfrog.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import psi.jfrog.config.Config;

public class LoginPage {

    private WebDriver driver;
	private WebDriverWait wait;

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait= new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    // Locators – update these based on actual JFrog UI
    private By usernameField = By.xpath("//input[@name='username']");
    private By passwordField = By.xpath("//input[@name='password']");
    private By loginButton   = By.xpath("//button[@type='submit']");
    private By dashboardMarker = By.xpath("//*[text()='Welcome to the JFrog Platform Trial']"); // some element visible only after login

	public void open() {
		driver.get(Config.getBaseUrl());
	}

	public void login(String username, String password) throws InterruptedException {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='username']")));
		driver.findElement(usernameField).sendKeys(username);
		driver.findElement(passwordField).sendKeys(password);
		driver.findElement(loginButton).click();
    }

	public boolean isLoginSuccessful() {
		try {
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//*[text()='Welcome to the JFrog Platform Trial']")));
			WebElement dashboard = driver.findElement(dashboardMarker);
            return dashboard.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}


