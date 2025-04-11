package psi.jfrog.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import psi.jfrog.config.Config;

import java.time.Duration;
import java.util.List;

public class ViolationsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Constructor
    public ViolationsPage(WebDriver driver) {
        this.driver = driver;
        this.wait= new WebDriverWait(driver, Duration.ofSeconds(120));
    }

    
	// Methods
	public void goToScanDescendants(String baseUrl, String repo) {
		String url = Config.getBaseUrl() + "/ui/scans-list/repositories/" + Config.getRepoName() + "/scan-descendants";
		driver.get(url);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[text()='Artifacts']")));

	}

	public void NavigateToRepo(String dockerimage) {
		driver.findElement(By.xpath("(//span[text()='" + dockerimage + "'])[1]")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='Vulnerabilities']")));
	}

	public void NavigateToPolicyViolations() {
		driver.findElement(By.xpath("//span[@id='menuItemText' and normalize-space(text())='Policy Violations']"))
				.click();

		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//*[normalize-space(text())='Last Scan Status']")));

	}

	public void ValidateSeverityPolicy(String sev) {
		int Criticalcount = driver.findElements(By.xpath("//*[@id='icon_critical']")).size();
		int Highcount = driver.findElements(By.xpath("//*[@id='icon_high']")).size();
		int TotalCount = Integer.parseInt(
				driver.findElement(By.xpath("//*[contains(@class,'align-items-center')]/p")).getText().trim());
		if (TotalCount == (Highcount + Criticalcount)) {
			System.out.println("pass");

		}else {
			System.out.println("fail");
		}
	}
    
}