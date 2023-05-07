package com.itkhan.practice;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * This class is used to automate POSTMAN getWorkspaces API.
 * The code snippets are based on my own collections and workspaces.
 * Therefore you must use your own API KEY and change the assertions according to your collections.
 */
public class AutomateGet {
    private static String API_KEY = ""; //generate your own Postman API key
    private static String BASEURI = "https://api.postman.com";

    @Test
    public void validate_status_code(){
        given().
                baseUri(BASEURI).
                header("X-Api-Key", API_KEY).
        when().
                get("/workspaces").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    public void validate_response_body() {
        given().
                baseUri(BASEURI)
                .header("X-Api-Key", API_KEY)
        .when()
                .get("/workspaces")
        .then()
                .log().all()
                .assertThat()
                    .statusCode(200)
                    .body("workspaces.name", hasItems("My Workspace", "RestAssured-OC"),
                            "workspaces.type", hasItems("personal"),
                            "workspaces.type", not(hasItem("team")),
                            "workspaces[0].name", is(equalTo("My Workspace")),
                            "workspaces[0].name", equalTo("My Workspace"),
                            "workspaces.size()", equalTo(2),
                            "workspaces.name", hasItem("My Workspace")
                    );
    }

    @Test
    public void extract_response(){
        Response res = given().
                baseUri(BASEURI).
                header("X-Api-Key", API_KEY).
        when().
                get("/workspaces").
        then().
                log().all().
                assertThat().
                statusCode(200).
                extract().
                response()
        ;

        System.out.println("Response = " + res.asString());

        //first way
        System.out.println("Workspace name = " + JsonPath.from(res.asString()).getString("workspaces[0].name"));

        //second way
        System.out.println("Workspace name = " + res.path("workspaces[0].name"));

        //third way
        JsonPath jpath = new JsonPath(res.asString());
        System.out.println("Workspace name = " + jpath.getString("workspaces[0].name"));
    }

    @Test
    public void extract_single_value_from_response(){
        String name = given().
                baseUri(BASEURI).
                header("X-Api-Key", API_KEY).
        when().
                get("/workspaces").
        then().
                log().all().
                assertThat().
                statusCode(200).
                extract().
                response().path("workspaces[0].name");
        ;

        System.out.println("Workspace name = " + name);
    }

    @Test
    public void hamcrest_assert_on_extracted_response(){
        String name = given().
                baseUri(BASEURI).
                header("X-Api-Key", API_KEY).
        when().
                get("/workspaces").
        then().
                log().all().
                assertThat().
                statusCode(200).
                extract().
                response().path("workspaces[0].name");
        ;

        System.out.println("Workspace name = " + name);

        //Hamcrest assertion
        assertThat(name, equalTo("My Workspace"));

        //TestNG assertion
        Assert.assertEquals(name, "My Workspace");
    }

    @Test
    public void validate_response_body_hamcrest_learnings() {
        given().
                baseUri(BASEURI)
                .header("X-Api-Key", API_KEY)
        .when()
                .get("/workspaces")
        .then()
                .log().all()
                .assertThat()
                    .statusCode(200)
                    .body("workspaces.name", containsInAnyOrder("RestAssured-OC", "My Workspace"),
                            "workspaces.name", is(not(emptyArray())),
                            "workspaces.name", hasSize(2),
                            "workspaces.name", not(everyItem((startsWith("My")))),
                            "workspaces[0]", hasKey("id"),
                            "workspaces[1]", hasValue("RestAssured-OC"),
                            "workspaces[0]", hasEntry("type", "personal"),
                            "workspaces[0]", not(equalTo(Collections.EMPTY_MAP)),
                            "workspaces[0].name", allOf(startsWith("My"), containsString("Workspace"))
                    );
    }




}
