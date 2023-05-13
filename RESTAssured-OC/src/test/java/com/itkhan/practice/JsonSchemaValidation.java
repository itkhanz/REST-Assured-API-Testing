package com.itkhan.practice;

import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public class JsonSchemaValidation {
    @Test
    public void validateJsonSchema() {
        given().
                baseUri("https://postman-echo.com").
                log().all().
        when().
                get("/get").
        then().
                log().all().
                assertThat().
                statusCode(200).
                body(matchesJsonSchema(new File("src/test/resources/schema/EchoGet.json")));
//                body(matchesJsonSchemaInClasspath("EchoGet.json"));
    }

}
