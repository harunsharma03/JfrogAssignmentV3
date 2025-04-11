package psi.jfrog.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import psi.jfrog.pages.ViolationsPage;
import psi.jfrog.base.BaseTest;
import psi.jfrog.config.Config;

public class ViolationUISteps extends BaseTest {

    ViolationsPage violationsPage;

    @Then("I navigate to scan page for repository {string}")
    public void navigateToScanDescendants(String repo) {
    	repo= repo.startsWith("config.")?Config.getRepoName():repo;
        violationsPage = new ViolationsPage(driver);
        violationsPage.goToScanDescendants(Config.getBaseUrl(), repo);
    }

    @And("I click on the docker image that was created by the API")
    public void clickOnDockerImage() {
    	String imageName= Config.getCustomImage()+"/"+Config.gettag();
        violationsPage.NavigateToRepo(imageName);
    }

    @And("I navigate to the Policy Violations tab")
    public void goToPolicyViolations() {
        violationsPage.NavigateToPolicyViolations();
    }

    @Then("I validate that only {string} severity violations are listed")
    public void validateViolationSeverity(String severity) {
        violationsPage.ValidateSeverityPolicy(severity);
    }
}