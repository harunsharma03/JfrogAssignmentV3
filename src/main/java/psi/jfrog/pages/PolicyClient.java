package psi.jfrog.pages;

import java.util.Base64;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import psi.jfrog.config.Config;
import psi.jfrog.utilities.ApiService;
import psi.jfrog.utilities.ExtentManager;

public class PolicyClient {

	public boolean createPolicy(String policyName) {
		try {
			String baseUrl = Config.getBaseUrl(); // ends with /xray
			String endpoint = baseUrl + "/xray/api/v2/policies";


			String body = "{\n" + "  \"name\": \"" + policyName + "\",\n"
					+ "  \"description\": \"This is a specific CVEs security policy\",\n"
					+ "  \"type\": \"security\",\n" + "  \"rules\": [\n" + "    {\n"
					+ "      \"name\": \"some_rule\",\n" + "      \"criteria\": {\n"
					+ "        \"malicious_package\": false,\n" + "        \"fix_version_dependant\": false,\n"
					+ "        \"min_severity\": \"high\"\n" + "      },\n" + "      \"actions\": {\n"
					+ "        \"mails\": [],\n" + "        \"webhooks\": [],\n" + "        \"fail_build\": false,\n"
					+ "        \"block_release_bundle_distribution\": false,\n"
					+ "        \"block_release_bundle_promotion\": false,\n" + "        \"notify_deployer\": false,\n"
					+ "        \"notify_watch_recipients\": false,\n" + "        \"create_ticket_enabled\": false,\n"
					+ "        \"block_download\": {\n" + "          \"active\": false,\n"
					+ "          \"unscanned\": false\n" + "        }\n" + "      },\n" + "      \"priority\": 1\n"
					+ "    }\n" + "  ]\n" + "}";


			
			Response response = ApiService.apivalidations(body, endpoint, "post");

			int status = response.getStatusCode();
			String message = response.jsonPath().getString("error");

			if (status == 200 || status == 201) {
				ExtentManager.getTest().pass("Policy '" + policyName + "' created successfully.");
				Assert.assertTrue(true);
				return true;
			} else if ("Policy already exists".equalsIgnoreCase(message)) {
				ExtentManager.getTest().info("Policy '" + policyName + "' already existed.");
				Assert.assertTrue(true);
				return true;
			} else {
				ExtentManager.getTest()
						.fail("Policy creation failed. Status: " + status + "<br>" + response.asPrettyString());
				Assert.fail("Policy creation failed.");
				return false;
			}

		} catch (Exception e) {
			Assert.fail("Exception in createPolicy: " + e.getMessage());
			return false;
		}
	}

}
