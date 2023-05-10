package com.itkhan.practice;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ResponseSpecificationExample {
    private static String API_KEY = ""; //generate your own Postman API key
    private static String BASEURI = "https://api.postman.com";
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void beforeClass() {
        //Using Request and Response Specification
        /*requestSpecification = with()
                .baseUri(BASEURI)
                .header("X-Api-Key", API_KEY)
                .log().all()
        ;*/

        /*responseSpecification = RestAssured
                .expect()
                .statusCode(200)
                .contentType(ContentType.JSON)
                ;*/


        //Using Request and Response Spec Builder
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
    public void validate_status_code() {
        //Using Response Specification instance variable
        //get("/workspaces").then().spec(responseSpecification).log().all();

        //Using Response Specification Builder
        get("/workspaces");

    }

    @Test
    public void validate_response_body() {
        //Response response = requestSpecification.get("/workspaces").then().log().all().extract().response();

        //Response response = get("/workspaces").then().log().all().extract().response();

        Response response = get("/workspaces").then().extract().response();
        assertThat(response.path("workspaces[0].name").toString(), equalTo("My Workspace"));
    }
}
