package com.itkhan.framework.spotify.tests;

import com.itkhan.framework.spotify.api.applicationApi.PlaylistApi;
import com.itkhan.framework.spotify.pojo.Error;
import com.itkhan.framework.spotify.pojo.Playlist;
import com.itkhan.framework.spotify.utils.DataLoader;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.itkhan.framework.spotify.utils.AssertionUtils.*;
import static com.itkhan.framework.spotify.utils.BuilderUtils.playlistBuilder;

/*This class contains the tests for Spotify Playlist API */
public class PlaylistTests {

    /**
     * Creates a spotify Playlist with given payload
     * https://developer.spotify.com/documentation/web-api/reference/create-playlist
     */
    @Test
    public void shouldBeAbleToCreateAPlaylist() {
        Playlist requestPlaylist = playlistBuilder("New Playlist", "New playlist description", false);
        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.statusCode(),201);
        assertPlaylistEqual(response.as(Playlist.class),requestPlaylist);
    }

    /**
     * Retrieves a spotify playlist with given playlist ID
     * https://developer.spotify.com/documentation/web-api/reference/get-playlist
     */
    @Test
    public void shouldBeAbleToGetAPlaylist() {
        Playlist requestPlaylist = playlistBuilder("New Playlist", "New playlist description", false);
        Response response = PlaylistApi.get(DataLoader.getInstance().getGetPlaylistId());
        assertStatusCode(response.statusCode(),200);
        assertPlaylistEqual(response.as(Playlist.class),requestPlaylist);
    }

    /**
     * Updates Playlist's name, description and public properties as per the given payload
     * https://developer.spotify.com/documentation/web-api/reference/change-playlist-details
     */
    @Test
    public void shouldBeAbleToUpdateAPlaylist() {
        Playlist requestPlaylist = playlistBuilder("Updated Playlist Name", "Updated playlist description", false);
        Response response = PlaylistApi.update(DataLoader.getInstance().getUpdatePlaylistId(), requestPlaylist);
        assertStatusCode(response.statusCode(),200);
    }

    /**
     * Validates that creating the playlist with empty name throws bad request error
     * https://developer.spotify.com/documentation/web-api/reference/create-playlist
     */
    @Test
    public void shouldNotBeAbleToCreateAPlaylistWithoutName() {
        Playlist requestPlaylist = playlistBuilder("", "New Playlist", false);
        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.statusCode(),400);
        assertError(response.as(Error.class), 400, "Missing required field: name");
    }

    /**
     * Validates that creating the playlist with expired or invalid access token throws unauthorized request error
     * https://developer.spotify.com/documentation/web-api/reference/create-playlist
     */
    @Test
    public void shouldNotBeAbleToCreateAPlaylistWithExpiredToken() {
        Playlist requestPlaylist = playlistBuilder("", "New Playlist", false);
        String invalid_access_token = "12345";
        Response response = PlaylistApi.post(requestPlaylist, invalid_access_token);
        assertStatusCode(response.statusCode(),401);
        assertError(response.as(Error.class), 401, "Invalid access token");
    }

}
