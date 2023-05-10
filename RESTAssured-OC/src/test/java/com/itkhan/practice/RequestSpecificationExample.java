package com.itkhan.practice;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RequestSpecificationExample {
    private static String API_KEY = ""; //generate your own Postman API key
    private static String BASEURI = "https://api.postman.com";
    RequestSpecification requestSpecification;

    @BeforeClass
    public void beforeClass() {
        /*requestSpecification = with()
                .baseUri(BASEURI)
                .header("X-Api-Key", API_KEY)
                .log().all()
        ;*/

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(BASEURI);
        requestSpecBuilder.addHeader("X-Api-Key", API_KEY);
        requestSpecBuilder.log(LogDetail.ALL);

        RestAssured.requestSpecification = requestSpecBuilder.build();
    }

    @Test
    public void queryTest() {
        QueryableRequestSpecification queryableRequestSpecification = SpecificationQuerier
                .query(RestAssured.requestSpecification);
        System.out.println(queryableRequestSpecification.getBaseUri());
        System.out.println(queryableRequestSpecification.getHeaders());
    }

    @Test
    public void validate_status_code() {

        //Method 1: BDD Style
        /*given()
                .spec(requestSpecification).
        when()
                .get("/workspaces")
        .then()
                .log().all()
                .assertThat().statusCode(200)
        ;*/

        //Method 2: Non BDD Style
        /*Response response = requestSpecification.get("/workspaces").then().log().all().extract().response();
        assertThat(response.statusCode(), is(equalTo(200)));*/

        //Method 3: non BDD Style using Spec Builder
        Response response = get("/workspaces").then().log().all().extract().response();
        assertThat(response.statusCode(), is(equalTo(200)));

    }

    @Test
    public void validate_response_body() {
        //Response response = requestSpecification.get("/workspaces").then().log().all().extract().response();

        Response response = get("/workspaces").then().log().all().extract().response();
        assertThat(response.path("workspaces[0].name").toString(), equalTo("My Workspace"));
    }
}
