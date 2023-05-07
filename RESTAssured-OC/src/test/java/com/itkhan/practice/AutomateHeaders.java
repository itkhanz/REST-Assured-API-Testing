package com.itkhan.practice;
import io.restassured.http.Header;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
public class AutomateHeaders {
    private static String BASEURI = "";
    @Test
    public void multiple_headers(){
        Header header = new Header("header", "value1");
        Header matchHeader = new Header("x-mock-match-request-headers", "header");
        given().
                baseUri(BASEURI).
                header(header).
                header(matchHeader).
        when().
                get("/get").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }
}
