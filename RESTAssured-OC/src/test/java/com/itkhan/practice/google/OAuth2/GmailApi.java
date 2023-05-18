package com.itkhan.practice.google.OAuth2;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GmailApi {
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;
    private static String access_token = "";    //get access token via Postman
    
    @BeforeClass
    public void beforeClass() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder()
                .setBaseUri("https://gmail.googleapis.com")
                .addHeader("Authorization", "Bearer " + access_token)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL);

        requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                ;
        responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void getUserProfile() {
        given()
                .spec(requestSpecification)
                .basePath("/gmail/v1")
                .pathParam("userid", "test@gmail.com")
        .when()
                .get("/users/{userid}/profile")
        .then()
                .spec(responseSpecification)
        ;
    }

    @Test
    public void sendMessage() {
        String msg = "From: test@gmail.com\n" +
                "To: example@gmail.com\n" +
                "Subject: Test Email\n" +
                "\n" +
                "Sending from Gmail API";

        String base64UrlEncodedMsg = Base64.getUrlEncoder().encodeToString(msg.getBytes());

        HashMap<String, String> payload = new HashMap<>();
        payload.put("raw", base64UrlEncodedMsg);

        given()
                .spec(requestSpecification)
                .basePath("/gmail/v1")
                .pathParam("userid", "test@gmail.com")
                .body(payload)
        .when()
                .post("/users/{userid}/messages/send")
        .then()
                .spec(responseSpecification)
                .assertThat()
                .body("labelIds", hasItem("SENT"))
        ;
    }

    @Test
    public void getMessagesList() {
        given()
                .spec(requestSpecification)
                .basePath("/gmail/v1")
                .pathParam("userid", "test@gmail.com")
        .when()
                .get("/users/{userid}/messages")
        .then()
                .spec(responseSpecification)
                .assertThat()
                .body("resultSizeEstimate", greaterThan(0))
        ;
    }

    @Test
    public void getMessage() {
        Response res = given()
                .spec(requestSpecification)
                .basePath("/gmail/v1")
                .pathParam("userid", "test@gmail.com")
                .pathParam("id", "1882afb7a24626f1")
        .when()
                .get("/users/{userid}/messages/{id}")
        .then()
                .spec(responseSpecification)
                .extract().response()
        ;
        res.prettyPrint();
    }

    @Test
    public void getLabelsList() {
        given()
                .spec(requestSpecification)
                .basePath("/gmail/v1")
                .pathParam("userid", "test@gmail.com")
        .when()
                .get("/users/{userid}/labels")
        .then()
                .spec(responseSpecification)
                .assertThat()
                .body("labels", not(emptyArray()))
        ;
    }

    @Test
    public void modifyMessage() {
        HashMap<String, List<String>> payload = new HashMap<>();

        List<String> addLabels = new ArrayList<String>();
        addLabels.add("CATEGORY_SOCIAL");
        List<String> removeLabels = new ArrayList<String>();
        removeLabels.add("CATEGORY_PROMOTIONS");

        payload.put("addLabelIds", addLabels);
        payload.put("removeLabelIds", removeLabels);

        given()
                .spec(requestSpecification)
                .basePath("/gmail/v1")
                .pathParam("userid", "test@gmail.com")
                .pathParam("id", "1882d8fcc8b4e7d4")
                .body(payload)
        .when()
                .post("/users/{userid}/messages/{id}/modify")
        .then()
                .spec(responseSpecification)
                .assertThat()
                .body("labelIds", hasItem("CATEGORY_SOCIAL"),
                        "labelIds", not(hasItem("CATEGORY_PROMOTIONS")))
        ;
    }

    @Test
    public void deleteMessage() {
        Response res = given()
                .spec(requestSpecification)
                .basePath("/gmail/v1")
                .pathParam("userid", "test@gmail.com")
                .pathParam("id", "1882d8fcc8b4e7d4")
        .when()
                .delete("/users/{userid}/messages/{id}")
        .then()
                .log().all()
                .statusCode(204)
                .extract().response()
        ;

        if(!(res.body().asString().equals("null")))
        {
           Assert.assertFalse(false);
        } else {
            Assert.fail("response body not empty");
        }
    }


}
