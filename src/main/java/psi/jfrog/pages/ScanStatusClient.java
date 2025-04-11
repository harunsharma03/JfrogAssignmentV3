package psi.jfrog.pages;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import psi.jfrog.config.Config;
import psi.jfrog.utilities.ApiService;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class ScanStatusClient {

    public boolean verifyScanStatus(String repo, String path) {
    	try {
        String scanUrl = Config.getBaseUrl() + "/xray/api/v1/artifact/status";

        String requestBody = "{\n" +
                "  \"repo\": \"" + repo + "\",\n" +
                "  \"path\": \"" + path + "\"\n" +
                "}";
       
        Response createResponse = ApiService.apivalidations(requestBody, scanUrl, "post"); 

        System.out.println("Scan status response: " + createResponse.asPrettyString());

        assertEquals(createResponse.statusCode(), 200, "Failed to get scan status");

        String status = createResponse.jsonPath().getString("overall.status");
        assertEquals(status, "DONE", "Scan is not complete yet");

        return true;
    	}catch(Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    }
}