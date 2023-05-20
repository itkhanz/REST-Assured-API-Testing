package com.itkhan.framework.spotify.api.applicationApi;

import com.itkhan.framework.spotify.pojo.Playlist;
import io.restassured.response.Response;

import static com.itkhan.framework.spotify.api.SpecBuilder.getRequestSpec;
import static com.itkhan.framework.spotify.api.SpecBuilder.getResponseSpec;
import static io.restassured.RestAssured.given;

public class PlaylistApi {
    /**
     * This method makes a POST request to Spotify Playlist API for creating Playlist
     * @param requestPlaylist POJO for the playlist object
     * @return Response object from the REST Assured
     */
    public static Response post(Playlist requestPlaylist) {
        return given(getRequestSpec())
                    .pathParam("user_id", "31ere62g3sbz2lsr27qcc5w4fsae")
                    .body(requestPlaylist)
                .when()
                    .post("/users/{user_id}/playlists")
                .then()
                    .spec(getResponseSpec())
                    .extract().response()
                ;
    }

    /**
     * This overloaded method makes a POST request to Spotify Playlist API for creating Playlist and accept the access token as extra parameter
     * accessToken passed here will overwrite the access token from the SpecBuilder
     * @param requestPlaylist POJO for the playlist object
     * @param accessToken dummy access token
     * @return Response object from the REST Assured
     */
    public static Response post(Playlist requestPlaylist, String accessToken) {
        return given(getRequestSpec().header("Authorization", "Bearer " + accessToken))
                    .pathParam("user_id", "31ere62g3sbz2lsr27qcc5w4fsae")
                    .body(requestPlaylist)
                .when()
                    .post("/users/{user_id}/playlists")
                .then()
                    .spec(getResponseSpec())
                    .extract().response()
                ;
    }

    /**
     * This method makes a GET request to the Spotify Playlist API for getting playlist
     * @param playlist_id ID of the playlist (can be obtained in response of createPlaylist or getAllPlaylists APIs)
     * @return Response object from the REST Assured
     */
    public static Response get(String playlist_id) {
        return given(getRequestSpec())
                    .pathParam("playlist_id", playlist_id)
                .when()
                    .get("/playlists/{playlist_id}")
                .then()
                    .spec(getResponseSpec())
                    .extract().response()
                ;
    }

    /**
     * This method makes a PUT request to the Spotify Playlist API for updating the playlist
     * @param playlist_id ID of the playlist (can be obtained in response of createPlaylist or getAllPlaylists APIs)
     * @param requestPlaylist POJO for the playlist object
     * @return Response object from the REST Assured
     */
    public static Response update(String playlist_id, Playlist requestPlaylist) {
        return given(getRequestSpec())
                    .pathParam("playlist_id", playlist_id)
                    .body(requestPlaylist)
                .when()
                    .put("/playlists/{playlist_id}")
                .then()
                    .spec(getResponseSpec())
                    .extract().response()
                ;
    }
}
