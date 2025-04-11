package psi.jfrog.pages;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import psi.jfrog.config.Config;
import psi.jfrog.utilities.ApiService;

public class ViolationClient {

	public static String verifyViolations() {
		String url = Config.getBaseUrl() + "/xray/api/v1/violations";
		String watchName = Config.getWatchName();
		String repo = Config.getRepoName();
		String path = Config.getCustomImage() +"/"+ Config.gettag() +"/manifest.json";

		String body = "{\n" + "  \"filters\": {\n" + "    \"watch_name\": \"" + watchName + "\",\n"
				+ "    \"violation_type\": \"Security\",\n" + "    \"min_severity\": \"High\",\n"
				+ "    \"resources\": {\n" + "      \"artifacts\": [\n" + "        {\n" + "          \"repo\": \""
				+ repo + "\",\n" + "          \"path\": \"" + path + "\"\n" + "        }\n" + "      ]\n" + "    }\n"
				+ "  },\n" + "  \"pagination\": {\n" + "    \"order_by\": \"severity\",\n" + "    \"limit\": 50,\n"
				+ "    \"offset\": 0\n" + "  }\n" + "}";

		try {
			Response response = ApiService.apivalidations(body, url, "post");
			System.out.println("Violation API Response: " + response.asPrettyString());
			int totalViolations = response.jsonPath().getList("violations").size();
			String violations = response.jsonPath().getString("violations");

			System.out.println("Total Violations: " + totalViolations);
			return "Total: " + totalViolations + "\nDetails: " + violations;

		} catch (Exception e) {
			e.printStackTrace();
			return "Failed to retrieve violations.";
		}
	}
}