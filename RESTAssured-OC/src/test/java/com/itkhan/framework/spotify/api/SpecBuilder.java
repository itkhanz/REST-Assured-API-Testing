package com.itkhan.framework.spotify.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static com.itkhan.framework.spotify.constants.Route.*;
import static com.itkhan.framework.spotify.api.TokenManager.getToken;
import static io.restassured.RestAssured.config;

/*generic Request and Response Specifications for the Spotify API */
public class SpecBuilder {
    //static String access_token = getToken();

    /*Authorization Header is configured to be overwritten because for negative scenarios we need to pass the different access_token than default */
    public static RequestSpecification getRequestSpec() {
        String baseUri = System.getProperty("BASE_URI") != null ? System.getProperty("BASE_URI"): BASE_URI;
        System.out.println(baseUri);
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                //.setBaseUri("https://api.spotify.com")
                .setBasePath(BASE_PATH)
                //.addHeader("Authorization", "Bearer " + access_token)
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build()
                //.config(config().headerConfig(HeaderConfig.headerConfig().overwriteHeadersWithName("Authorization")))
                .auth().oauth2(getToken())
                ;
    }

    /* API calls such as for renewal of access_token has different baseURL so need separate RequestSpec*/
    public static RequestSpecification getAccountRequestSpec() {
        String accountsBaseUri = System.getProperty("ACCOUNTS_BASE_URI") != null ? System.getProperty("ACCOUNTS_BASE_URI"): ACCOUNTS_BASE_URI;

        return new RequestSpecBuilder()
                .setBaseUri(accountsBaseUri)
                //.setBaseUri("https://accounts.spotify.com")
                .setContentType(ContentType.URLENC)
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build()
                ;
    }

    public static ResponseSpecification getResponseSpec() {
        return new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
    }
}
