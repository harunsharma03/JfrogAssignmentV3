
package psi.jfrog.utilities;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Utility class for capturing and saving screenshots.
 */
public class ScreenshotUtil {

	// Capture screenshot as byte array to attach to report
	public static byte[] captureScreenshotAsBytes(WebDriver driver) {
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
	}

	// Save screenshot to a file (e.g. /target/screenshots/)
	public static void saveScreenshotToFile(WebDriver driver, String scenarioName) {
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String sanitized = scenarioName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
		File dest = new File("target/screenshots/" + sanitized + ".png");

		try {
			Files.createDirectories(dest.getParentFile().toPath());
			Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.out.println("Failed to save screenshot: " + e.getMessage());
		}
	}
}
