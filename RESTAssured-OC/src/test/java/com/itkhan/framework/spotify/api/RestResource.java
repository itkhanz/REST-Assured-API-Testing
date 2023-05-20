package com.itkhan.framework.spotify.api;

import com.itkhan.framework.spotify.pojo.Playlist;
import io.restassured.response.Response;

import static com.itkhan.framework.spotify.api.SpecBuilder.getRequestSpec;
import static com.itkhan.framework.spotify.api.SpecBuilder.getResponseSpec;
import static io.restassured.RestAssured.given;

/* Reusable methods for all the APIs of Spotify */
public class RestResource {
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
        return given(getRequestSpec().header("Authorization", "Bearer " + accessToken))
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
