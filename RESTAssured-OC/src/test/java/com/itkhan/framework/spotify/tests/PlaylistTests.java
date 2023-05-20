package com.itkhan.framework.spotify.tests;

import com.itkhan.framework.spotify.api.applicationApi.PlaylistApi;
import com.itkhan.framework.spotify.pojo.Error;
import com.itkhan.framework.spotify.pojo.Playlist;
import com.itkhan.framework.spotify.utils.DataLoader;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/*This class contains the tests for Spotify Playlist API */
public class PlaylistTests {

    /**
     * Creates a spotify Playlist with given payload
     * https://developer.spotify.com/documentation/web-api/reference/create-playlist
     */
    @Test
    public void shouldBeAbleToCreateAPlaylist() {
        Playlist requestPlaylist = new Playlist()
                .setName("New Playlist")
                .setDescription("New playlist description")
                .setPublic(false)
                ;

        Response response = PlaylistApi.post(requestPlaylist);
        assertThat(response.statusCode(),equalTo(201));

        Playlist responsePlaylist = response.as(Playlist.class);

        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.getPublic(), equalTo(requestPlaylist.getPublic()));
    }

    /**
     * Retrieves a spotify playlist with given playlist ID
     * https://developer.spotify.com/documentation/web-api/reference/get-playlist
     */
    @Test
    public void shouldBeAbleToGetAPlaylist() {
        Response response = PlaylistApi.get(DataLoader.getInstance().getGetPlaylistId());
        assertThat(response.statusCode(),equalTo(200));

        Playlist responsePlaylist = response.as(Playlist.class);

        assertThat(responsePlaylist.getName(), equalTo("New Playlist"));
        assertThat(responsePlaylist.getDescription(), equalTo("New playlist description"));
        assertThat(responsePlaylist.getPublic(), equalTo(false));
    }

    /**
     * Updates Playlist's name, description and public properties as per the given payload
     * https://developer.spotify.com/documentation/web-api/reference/change-playlist-details
     */
    @Test
    public void shouldBeAbleToUpdateAPlaylist() {
        Playlist requestPlaylist = new Playlist()
                .setName("Updated Playlist Name")
                .setDescription("Updated playlist description")
                .setPublic(false)
                ;

        Response response = PlaylistApi.update(DataLoader.getInstance().getUpdatePlaylistId(), requestPlaylist);
        assertThat(response.statusCode(),equalTo(200));
    }

    /**
     * Validates that creating the playlist with empty name throws bad request error
     * https://developer.spotify.com/documentation/web-api/reference/create-playlist
     */
    @Test
    public void shouldNotBeAbleToCreateAPlaylistWithoutName() {
        Playlist requestPlaylist = new Playlist()
                .setName("")
                .setDescription("New playlist description")
                .setPublic(false)
                ;

        Response response = PlaylistApi.post(requestPlaylist);
        assertThat(response.statusCode(),equalTo(400));

        Error error = response.as(Error.class);

        assertThat(error.getError().getStatus(), equalTo(400));
        assertThat(error.getError().getMessage(),equalTo("Missing required field: name"));
    }

    /**
     * Validates that creating the playlist with expired or invalid access token throws unauthorized request error
     * https://developer.spotify.com/documentation/web-api/reference/create-playlist
     */
    @Test
    public void shouldNotBeAbleToCreateAPlaylistWithExpiredToken() {
        Playlist requestPlaylist = new Playlist()
                .setName("New Playlist")
                .setDescription("New playlist description")
                .setPublic(false)
                ;

        String invalid_access_token = "12345";

        Response response = PlaylistApi.post(requestPlaylist, invalid_access_token);
        assertThat(response.statusCode(),equalTo(401));

        Error error = response.as(Error.class);

        assertThat(error.getError().getStatus(), equalTo(401));
        assertThat(error.getError().getMessage(),equalTo("Invalid access token"));
    }
}
