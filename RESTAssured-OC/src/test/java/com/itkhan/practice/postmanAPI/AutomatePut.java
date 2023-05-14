package com.itkhan.practice.postmanAPI;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;

import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
public class AutomatePut {
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
    public void validate_put_request_bdd_style(){
        String workspaceId = "c13e4c59-b0ca-4df5-bc74-22e9cf8031e2";
        String payload = "{\n" +
                "    \"workspace\": {\n" +
                "        \"name\": \"newWorkspaceName\",\n" +
                "        \"type\": \"personal\",\n" +
                "        \"description\": \"this is updated by Rest Assured\"\n" +
                "    }\n" +
                "}";

        //BDD Style
        given().
                body(payload).
                pathParam("workspaceId", workspaceId).
        when().
                put("/workspaces/{workspaceId}").
        then().
                log().all().
                assertThat().
                body("workspace.name", equalTo("newWorkspaceName"),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"),
                        "workspace.id", equalTo(workspaceId));
    }

    @Test
    public void validate_put_request_non_bdd_style(){
        String workspaceId = "08aff5a4-e315-48db-8472-2f8106f343d5";

        String payload = "{\n" +
                "    \"workspace\": {\n" +
                "        \"name\": \"myFirstWorkspaceGotUpdated\",\n" +
                "        \"type\": \"personal\",\n" +
                "        \"description\": \"Rest Assured updated this\"\n" +
                "    }\n" +
                "}";

        //Non-BDD Style
        Response response = with()
                .body(payload)
                .pathParam("workspaceId", workspaceId)
                .put("/workspaces/{workspaceId}");


        assertThat(response.path("workspace.name"), equalTo("myFirstWorkspaceGotUpdated"));
        assertThat(response.path("workspace.id"), matchesPattern("^[a-z0-9-]{36}$"));
        assertThat(response.path("workspace.id"), equalTo(workspaceId));
    }
}
