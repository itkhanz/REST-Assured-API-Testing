package com.itkhan.framework.spotify.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.HeaderConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static com.itkhan.framework.spotify.api.Route.BASE_PATH;
import static com.itkhan.framework.spotify.api.TokenManager.getToken;
import static io.restassured.RestAssured.config;

/*generic Request and Response Specifications for the Spotify API */
public class SpecBuilder {
    static String access_token = getToken();

    /*Authorization Header is configured to be overwritten because for negative scenarios we need to pass the different access_token than default */
    public static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri("https://api.spotify.com")
                .setBasePath(BASE_PATH)
                .addHeader("Authorization", "Bearer " + access_token)
                .setContentType(ContentType.JSON)
                .build()
                .config(config().headerConfig(HeaderConfig.headerConfig().overwriteHeadersWithName("Authorization")))
                ;
    }

    /* API calls such as for renewal of access_token has different baseURL so need separate RequestSpec*/
    public static RequestSpecification getAccountRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri("https://accounts.spotify.com")
                .setContentType(ContentType.URLENC)
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
