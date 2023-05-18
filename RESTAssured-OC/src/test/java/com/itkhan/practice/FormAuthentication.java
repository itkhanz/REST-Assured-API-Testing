package com.itkhan.practice;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.session.SessionFilter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.specification.ProxySpecification.auth;
import static org.hamcrest.Matchers.equalTo;

public class FormAuthentication {
    @BeforeClass
    public void beforeClass(){
        RestAssured.requestSpecification = new RequestSpecBuilder().
                setRelaxedHTTPSValidation().
                setBaseUri("https://localhost:8081").
                build();
    }

    @Test
    public void form_authentication_using_csrf_token(){

        // /signin is a POST call.
        // /login is a GET call that gives us the login form which has the csrf token. So we are telling Rest Assured to find the csrf there.

        SessionFilter filter = new SessionFilter();
        given().
                csrf("/login").
                auth().form("dan", "dan123", new FormAuthConfig("/signin", "txtUsername", "txtPassword")).
                filter(filter).
                log().all().
        when().
                get("/login").
        then().
                log().all().
                assertThat().
                statusCode(200);

        System.out.println("Session id = " + filter.getSessionId());

        given().
                sessionId(filter.getSessionId()).
                log().all().
        when().
                get("/profile/index").
        then().
                log().all().
                assertThat().
                statusCode(200).
                body("html.body.div.p", equalTo("This is User Profile\\Index. Only authenticated people can see this"));
    }

}
