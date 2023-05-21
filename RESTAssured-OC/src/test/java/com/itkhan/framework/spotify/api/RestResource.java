package com.itkhan.framework.spotify.api;

import io.restassured.response.Response;

import java.util.HashMap;

import static com.itkhan.framework.spotify.constants.Route.API;
import static com.itkhan.framework.spotify.constants.Route.TOKEN;
import static com.itkhan.framework.spotify.api.SpecBuilder.*;
import static io.restassured.RestAssured.given;

/* Reusable methods for all the APIs of Spotify */
public class RestResource {

    /**
     * This method makes POST call to fetch new access_token for the Spotify API
     * @param formParams URL Encoded form parameter has Hashmap i.e. client_id, client_secret, refresh_token, grant_type
     * @return Response object from REST Assured
     */
    public static Response postAccount(HashMap<String, String> formParams) {
        return given(getAccountRequestSpec())
                    .formParams(formParams)
                .when()
                    .post(API + TOKEN)
                .then()
                    .spec(getResponseSpec())
                    .extract().response()
                ;
    }

    /**
     * This method makes a POST request to Spotify API
     * @param path complete Path or endpoint of the API excluding baseURL
     * @param requestPlaylist POJO Object
     * @return Response object from the REST Assured
     */
    public static Response post(String path, Object requestPlaylist) {
        return given(getRequestSpec())
                    .body(requestPlaylist)
                .when()
                    .post(path)
                .then()
                    .spec(getResponseSpec())
                    .extract().response()
                ;
    }

    /**
     * This overloaded method makes a POST request to Spotify API with access_token provided as parameter from Tests
     * @param path complete Path or endpoint of the API excluding baseURL
     * @param requestPlaylist POJO Object
     * @param accessToken invalid or dummy access token from test case to validate negative scenarios
     * @return Response object from the REST Assured
     */
    public static Response post(String path, Object requestPlaylist, String accessToken) {
        return given(getRequestSpec().auth().oauth2(accessToken))
                    .body(requestPlaylist)
                .when()
                    .post(path)
                .then()
                    .spec(getResponseSpec())
                    .extract().response()
                ;
    }


    /**
     * This method makes a GET request to the Spotify API
     * @param path complete Path or endpoint of the API excluding baseURL
     * @return Response object from the REST Assured
     */
    public static Response get(String path) {
        return given(getRequestSpec())
                .when()
                    .get(path)
                .then()
                    .spec(getResponseSpec())
                    .extract().response()
                ;
    }

    /**
     * This method makes a PUT request to the Spotify API
     * @param path complete Path or endpoint of the API excluding baseURL
     * @param requestPlaylist POJO Object
     * @return Response object from the REST Assured
     */
    public static Response update(String path, Object requestPlaylist) {
        return given(getRequestSpec())
                    .body(requestPlaylist)
                .when()
                    .put(path)
                .then()
                    .spec(getResponseSpec())
                    .extract().response()
                ;
    }
}
