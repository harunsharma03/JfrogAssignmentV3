package psi.jfrog.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.testng.Assert;
import psi.jfrog.base.BaseTest;
import psi.jfrog.config.Config;
import psi.jfrog.pages.LoginPage;
import psi.jfrog.pages.ViolationsPage;
import psi.jfrog.pages.RepositoryClient;
import psi.jfrog.pages.ScanStatusClient;
import psi.jfrog.pages.ViolationClient;
import psi.jfrog.pages.PolicyClient;
import psi.jfrog.pages.WatchClient;
import psi.jfrog.utilities.DockerImageUploader;

/**
 * Step definitions for uploading and scanning a Docker image in JFrog
 * Artifactory. This connects to the Gherkin steps in your feature file.
 */
public class UploadAndScanSteps extends BaseTest {

	//Scenario-1(API): Create a new Docker repository
	@Given("Create a Docker repository with name {string} or check if existing")
	public void ensureDockerRepoExists(String repoKey) {
		repoKey= repoKey.startsWith("config.") ? Config.getRepoName():repoKey;
	    System.out.println(">>> Ensuring Docker repo exists: " + repoKey);
	    RepositoryClient repoClient = new RepositoryClient();
	    repoClient.createDockerRepository(repoKey);
	}
	
	

	@Given("Ensure a new security policy {string} is created or already exists")
	public void createorvalidateexistingPolicy(String policyName) {
		policyName=policyName.startsWith("config.")?Config.getSecurityPolicy():policyName;
	    PolicyClient policyClient = new PolicyClient();
	    policyClient.createPolicy(policyName);
	}
	
	
	@Given("A watch {string} is created for repo {string} and policy {string}")
	public void createWatchForRepoAndPolicy(String watch, String repo, String policy) {
		watch= watch.startsWith("config.") ? Config.getWatchName():watch;
		repo= repo.startsWith("config.") ? Config.getRepoName():repo;
		policy= policy.startsWith("config.") ? Config.getSecurityPolicy():policy;
	    WatchClient watchClient = new WatchClient();
	    watchClient.createWatch(watch, repo, policy);
	}

	@When("I apply watch {string} from {string} to {string}")
	public void applyWatchToExistingArtifacts(String watchNameToapply, String start, String end) {
		watchNameToapply= watchNameToapply.startsWith("config.") ? Config.getWatchNameToApply():watchNameToapply;
		start= start.startsWith("config.") ? Config.getWatchStartDate():start;
		end= end.startsWith("config.") ? Config.getWatchEndDate():end;
		
	    WatchClient client = new WatchClient();
	    boolean result = client.applyWatchOnExistingContent(watchNameToapply, start, end);
	    Assert.assertTrue(result, "Watch was not applied correctly.");
	}

	@Given("I push the docker image {string} to repo {string} with name {string} and tag {string}")
	public void pushDockerImage(String baseImage, String repo, String customImage, String tag) {
		baseImage = baseImage.startsWith("config.") ? Config.getBaseImage() : baseImage;
		repo = repo.startsWith("config.") ? Config.getRepoName() : repo;

		customImage = customImage.startsWith("config.") ? Config.getCustomImage() : customImage;
		tag = tag.startsWith("config.") ? Config.gettag() : tag;
		boolean result = DockerImageUploader.pushDockerImageWithScript(Config.getDockerPath(),
				"trial5tdi8g.jfrog.io/" + repo, Config.getUsername(), Config.getPassword(), baseImage, customImage,
				tag);
		Assert.assertTrue(result, "Docker image push failed.");
	}

	@Then("Verify the scan status for repo {string} that was created")
	public void verifyScanStatus(String repo) {
		repo= repo.startsWith("config.") ? Config.getRepoName():repo;
		String path= Config.getCustomImage()+"/"+Config.gettag()+"/manifest.json";
	    ScanStatusClient client = new ScanStatusClient();
	    boolean result = client.verifyScanStatus(repo, path);
	    Assert.assertTrue(result, "Scan status check failed.");
	}


	
	@Then("Verify violations for watch and repo created above")
	public void verifyTheViolationsOnTheDockerImage() {
		String result = ViolationClient.verifyViolations();
		System.out.println(">>> Violation Check Result:\n" + result);
		Assert.assertTrue(result.contains("Total:") && result.contains("Details:"),
				"Violation verification failed or no response.");
	}
    
/*	@Then("I should see high or critical policy violations in the UI")
	public void verifyViolationsInUI() {
		LoginPage loginPage = new LoginPage(driver);*/
		// ViolationsPage violationsPage = new ViolationsPage();
		// loginPage.login(null, null);
		// boolean hasViolations = violationsPage.checkCriticalOrHighViolations(); 
		// You’ll implement this
		// Assert.assertTrue(hasViolations, "No high/critical violations found in UI.");
	//}
}