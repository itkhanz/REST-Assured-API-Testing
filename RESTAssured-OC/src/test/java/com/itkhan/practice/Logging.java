package com.itkhan.practice;
import io.restassured.config.LogConfig;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Logging {

    private static String API_KEY = ""; //generate your own Postman API key
    private static String BASEURI = "https://api.postman.com";

    @Test
    public void request_response_logging(){
        given().
                baseUri(BASEURI).
                header("X-Api-Key", API_KEY).
                log().all().
        when().
                get("/workspaces").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    public void log_only_if_error(){
        given().
                baseUri(BASEURI).
                header("X-Api-Key", API_KEY).
                log().all().
        when().
                get("/workspaces").
        then().
                log().ifError().
                assertThat().
                statusCode(200);
    }

    @Test
    public void log_only_if_validation_fails(){
        given().
                baseUri(BASEURI).
                header("X-Api-Key", API_KEY).
                config(config.logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails())).
                log().all().
        when().
                get("/workspaces").
        then().
                log().ifValidationFails().
                assertThat().
                statusCode(200);
    }

    @Test
    public void logs_blacklist_header(){
        Set<String> headers = new HashSet<String>();
        headers.add("X-Api-Key");
        headers.add("Accept");

        given().
                baseUri(BASEURI).
                header("X-Api-Key", API_KEY).
                config(config.logConfig(LogConfig.logConfig().blacklistHeaders(headers))).
                log().all().
        when().
                get("/workspaces").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }
}
