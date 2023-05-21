package com.itkhan.framework.spotify.api.applicationApi;

import com.itkhan.framework.spotify.api.RestResource;
import com.itkhan.framework.spotify.pojo.Playlist;
import com.itkhan.framework.spotify.utils.ConfigLoader;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.itkhan.framework.spotify.api.Route.PLAYLISTS;
import static com.itkhan.framework.spotify.api.Route.USERS;

/*Reusable methods for Spotify Playlist API */
public class PlaylistApi {
    /**
     * This method makes a POST request to Spotify Playlist API for creating Playlist
     * @param requestPlaylist POJO for the playlist object
     * @return Response object from the REST Assured
     */
    @Step
    public static Response post(Playlist requestPlaylist) {
        String user_id = ConfigLoader.getInstance().getUser();
        String path = USERS + "/" + user_id + PLAYLISTS;
        return RestResource.post(path, requestPlaylist);
    }

    /**
     * This overloaded method makes a POST request to Spotify Playlist API for creating Playlist and accept the access token as extra parameter
     * accessToken passed here will overwrite the access token from the SpecBuilder
     * @param requestPlaylist POJO for the playlist object
     * @param accessToken dummy access token
     * @return Response object from the REST Assured
     */
    @Step
    public static Response post(Playlist requestPlaylist, String accessToken) {
        String user_id = ConfigLoader.getInstance().getUser();
        String path = USERS + "/" + user_id + PLAYLISTS;
        return RestResource.post(path, requestPlaylist, accessToken);
    }

    /**
     * This method makes a GET request to the Spotify Playlist API for getting playlist
     * @param playlist_id ID of the playlist (can be obtained in response of createPlaylist or getAllPlaylists APIs)
     * @return Response object from the REST Assured
     */
    @Step
    public static Response get(String playlist_id) {
        String path = PLAYLISTS + "/" + playlist_id;
        return RestResource.get(path);
    }

    /**
     * This method makes a PUT request to the Spotify Playlist API for updating the playlist
     * @param playlist_id ID of the playlist (can be obtained in response of createPlaylist or getAllPlaylists APIs)
     * @param requestPlaylist POJO for the playlist object
     * @return Response object from the REST Assured
     */
    @Step
    public static Response update(String playlist_id, Playlist requestPlaylist) {
        String path = PLAYLISTS + "/" + playlist_id;
        return  RestResource.update(path, requestPlaylist);
    }
}
