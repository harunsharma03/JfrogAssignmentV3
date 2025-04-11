package psi.jfrog.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import psi.jfrog.base.BaseTest;
import psi.jfrog.config.Config;
import psi.jfrog.pages.LoginPage;

public class LoginSteps extends BaseTest {


	LoginPage loginPage= new LoginPage(driver);

	@Given("I open the JFrog login page")
	public void openLoginPage() {
		loginPage = new LoginPage(driver);
		loginPage.open();
	}

	@Then("I login to JFrog UI with username {string} and password {string}")
	public void loginWithCredentials(String username, String password) throws InterruptedException {
		username = username.startsWith("config.") ? Config.getUsername() : username;
		password = password.startsWith("config.") ? Config.getPassword() : password;
		loginPage.login(username, password);
	}

	@Then("I should land on the JFrog dashboard")
	public void verifyLoginSuccess() {
		Assert.assertTrue(loginPage.isLoginSuccessful(), "Login failed — dashboard not loaded.");
	}
}