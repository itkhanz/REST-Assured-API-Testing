package com.itkhan.practice.postmanAPI;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
public class AutomateDelete {
    private static String API_KEY = ""; //generate your own Postman API key
    private static String BASEURI = "https://api.postman.com";

    @BeforeClass
    public void beforeClass() {

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(BASEURI);
        requestSpecBuilder.addHeader("X-Api-Key", API_KEY);
        requestSpecBuilder.log(LogDetail.ALL);

        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                ;
        RestAssured.responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void validate_delete_request_bdd_style(){
        String workspaceId = "cb56bd0e-97d1-4b86-8fbf-79b8cc8776c5";
        given().
                pathParam("workspaceId", workspaceId).
        when().
                delete("/workspaces/{workspaceId}").
        then().
                log().all().
                assertThat().
                body("workspace.id", matchesPattern("^[a-z0-9-]{36}$"),
                        "workspace.id", equalTo(workspaceId));;
    }
}
