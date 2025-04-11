package psi.jfrog.pages;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.http.ContentType;
import psi.jfrog.config.Config;
import psi.jfrog.utilities.ApiService;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import org.testng.Assert;

import static io.restassured.RestAssured.given;

public class RepositoryClient {

	/**
	 * Creates a local Docker repository in Artifactory with Xray indexing enabled.
	 * 
	 * @param repoKey The repository key to create (e.g. "docker-local")
	 * @return true if repo created successfully or already exists, false otherwise
	 * 
	 * 
	 * 
	 */
	
	ApiService apiservice= new ApiService();

	public boolean repositoryExists(String repoKey) {
		String baseUrl = Config.getBaseUrl();
		String endpoint = baseUrl + "/artifactory/api/repositories/" + repoKey;
		Response response = ApiService.apivalidations("", endpoint, "get");

		return response.getStatusCode() == 200;
	}

	public boolean createDockerRepository(String repoKey) {
		try {
			// 1. Check if repo already exists
			if (repositoryExists(repoKey)) {
				Assert.assertTrue(true, "Repository '" + repoKey + "' already exists — skipping creation.");
				return true;
			}

			// 2. Build creation payload
			String body = String.format(
					"{\n" + "  \"key\": \"%s\",\n" + "  \"projectKey\": \"\",\n" + "  \"packageType\": \"docker\",\n"
							+ "  \"rclass\": \"local\",\n" + "  \"xrayIndex\": true\n" + "}",
					repoKey);

			String baseUrl = Config.getBaseUrl();
			String endpoint = baseUrl + "/artifactory/api/repositories/" + repoKey;

			// 3. Attempt repo creation

			Response createResponse = ApiService.apivalidations(body, endpoint, "put"); 
	
			int status = createResponse.getStatusCode();

			if (status == 200 || status == 201) {
				// 4. Verify again using GET
				boolean created = repositoryExists(repoKey);
				Assert.assertTrue(created, "Repository was created successfully.");
				return true;
			} else {
				Assert.fail("Repository creation failed. Status: " + status + "\n" + createResponse.asPrettyString());
				return false;
			}

		} catch (Exception e) {
			Assert.fail("Exception during repo creation: " + e.getMessage());
			return false;
		}
	}

}
