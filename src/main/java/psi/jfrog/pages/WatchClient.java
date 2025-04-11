package psi.jfrog.pages;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import psi.jfrog.config.Config;
import psi.jfrog.utilities.ApiService;
import psi.jfrog.utilities.ExtentManager;
import com.aventstack.extentreports.Status;

import java.util.Base64;

public class WatchClient {

	public boolean createWatch(String watchName, String repoName, String policyName) {
		try {
			String baseUrl = Config.getBaseUrl(); // ends with /xray
			String endpoint = baseUrl + "/xray/api/v2/watches";

			String body = "{\n" + "  \"general_data\": {\n" + "    \"name\": \"" + watchName + "\",\n"
					+ "    \"description\": \"Watch linked to repo and policy\",\n" + "    \"active\": true\n"
					+ "  },\n" + "  \"project_resources\": {\n" + "    \"resources\": [\n" + "      {\n"
					+ "        \"type\": \"repository\",\n" + "        \"bin_mgr_id\": \"default\",\n"
					+ "        \"name\": \"" + repoName + "\",\n" + "        \"filters\": [\n" + "          {\n"
					+ "            \"type\": \"regex\",\n" + "            \"value\": \".*\"\n" + "          }\n"
					+ "        ]\n" + "      }\n" + "    ]\n" + "  },\n" + "  \"assigned_policies\": [\n" + "    {\n"
					+ "      \"name\": \"" + policyName + "\",\n" + "      \"type\": \"security\"\n" + "    }\n"
					+ "  ]\n" + "}";

			Response response = ApiService.apivalidations(body, endpoint, "post");

			int statusCode = response.getStatusCode();
			String message = response.jsonPath().getString("info");
			String error = response.jsonPath().getString("error");

			if (statusCode == 200 || statusCode == 201 && message.contains("successfully created")) {
				ExtentManager.getTest().log(Status.PASS, "Watch '" + watchName + "' created successfully.");
				return true;
			} else if (statusCode == 409 && "Watch already exists".equalsIgnoreCase(error)) {
				ExtentManager.getTest().log(Status.INFO, "Watch '" + watchName + "' already exists.");
				return true;
			} else {
				ExtentManager.getTest().log(Status.FAIL,
						"Watch creation failed. Status: " + statusCode + "<br>" + response.asPrettyString());
				Assert.fail("Watch creation failed.");
				return false;
			}

		} catch (Exception e) {
			ExtentManager.getTest().log(Status.FAIL, "Exception: " + e.getMessage());
			Assert.fail("Exception in createWatch: " + e.getMessage());
			return false;
		}
	}

	public boolean applyWatchOnExistingContent(String watchName, String startDate, String endDate) {
		try {
			String baseUrl = Config.getBaseUrl(); // ends with /xray
			String endpoint = baseUrl + "/xray/api/v1/applyWatch";

			String body = "{\n" + "  \"watch_names\": [\n" + "    \"" + watchName + "\"\n" + "  ],\n"
					+ "  \"date_range\": {\n" + "    \"start_date\": \"" + startDate + "\",\n" + "    \"end_date\": \""
					+ endDate + "\"\n" + "  }\n" + "}";

			Response response = ApiService.apivalidations(body, endpoint, "post");

			int status = response.getStatusCode();
			String message = response.jsonPath().getString("info");

			if (status == 202 && "History Scan is in progress".equalsIgnoreCase(message)) {
				ExtentManager.getTest().pass("Watch '" + watchName + "' applied successfully. Scan started.");
				return true;
			} else {
				ExtentManager.getTest()
						.fail("Failed to apply watch. Status: " + status + "<br>" + response.asPrettyString());
				Assert.fail("Failed to apply watch.");
				return false;
			}

		} catch (Exception e) {
			ExtentManager.getTest().fail("Exception in applyWatch: " + e.getMessage());
			Assert.fail("Exception in applyWatchOnExistingContent: " + e.getMessage());
			return false;
		}
	}

}
