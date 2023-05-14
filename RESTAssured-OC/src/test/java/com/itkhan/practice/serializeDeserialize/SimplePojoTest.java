package com.itkhan.practice.serializeDeserialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itkhan.practice.pojo.simple.SimplePojo;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
public class SimplePojoTest {
    private static String BASEURI = "";
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void beforeClass() {

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder()
                .setBaseUri(BASEURI)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                ;
        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                ;
        responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void simple_pojo_test() throws JsonProcessingException {
        /*String payload = "{\n" +
                "    \"key1\": \"value1\",\n" +
                "    \"key2\": \"value2\"\n" +
                "}";

        given()
                .body(payload)
        .when()
                .post("/postSimplejson")
        .then()
                .spec(responseSpecification)
                .assertThat()
                .body("key1", equalTo("value1"),
                        "key2", equalTo("value2"));*/

        SimplePojo simplePojo = new SimplePojo("value1", "value2");

        /*given()
            .body(simplePojo)
        .when()
            .post("/postSimplejson")
        .then()
            .spec(responseSpecification)
            .assertThat()
            .body("key1", equalTo(simplePojo.getKey1()),
    "key2", equalTo(simplePojo.getKey2()));*/

        SimplePojo deserializePojo =
                given()
                    .body(simplePojo)
                .when()
                    .post("/postSimplejson")
                .then().spec(responseSpecification)
                    .extract().response().as(SimplePojo.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String deserialzedPojoStr = objectMapper.writeValueAsString(deserializePojo);
        String simplePojoStr = objectMapper.writeValueAsString(simplePojo);
        assertThat(objectMapper.readTree(deserialzedPojoStr), equalTo(objectMapper.readTree(simplePojoStr)));

    }
}
