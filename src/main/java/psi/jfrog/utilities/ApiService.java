package psi.jfrog.utilities;

import java.util.Base64;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import psi.jfrog.config.Config;

public class ApiService {
	


	public static Response apivalidations(String body, String url, String apimethod) {
	    RestAssured.useRelaxedHTTPSValidation();
	    String auth = Config.getUsername() + ":" + Config.getPassword();
		String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

	    switch (apimethod.toLowerCase()) {
	        case "put":
	            return RestAssured.given()
	                .auth().preemptive().basic(Config.getUsername(), Config.getPassword())
	                .contentType(ContentType.JSON)
	                .body(body)
	                .put(url);

	        case "get":
	            return RestAssured.given().header("Authorization", "Basic " + encodedAuth)
	                .get(url);

	        case "post":
	        default:
	            return RestAssured.given()
	                .auth().preemptive().basic(Config.getUsername(), Config.getPassword())
	                .contentType(ContentType.JSON)
	                .body(body)
	                .post(url);
	    }
	}


	}

