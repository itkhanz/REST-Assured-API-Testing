package com.itkhan.framework.spotify.utils;

import com.itkhan.framework.spotify.pojo.Error;
import com.itkhan.framework.spotify.pojo.Playlist;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/*performs Hamcrest assertions for Tests */
public class AssertionUtils {
    /**
     * This method performs Hamcrest assertions on equality of Request and Response playlist name, description, and public properties
     * @param responsePlaylist Playlist POJO for request Payload
     * @param requestPlaylist Playlist POJO for response body
     */
    public static void assertPlaylistEqual(Playlist responsePlaylist, Playlist requestPlaylist) {
        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.get_public(), equalTo(requestPlaylist.get_public()));
    }

    /**
     * This method performs assertions for the Error Json returned as response body
     * @param responseErr ErrorRoot POJO object
     * @param expectedStatusCode expected error status code
     * @param expectedMsg expected error message
     */
    public static void assertError(Error responseErr, int expectedStatusCode, String expectedMsg){
        assertThat(responseErr.getError().getStatus(), equalTo(expectedStatusCode));
        assertThat(responseErr.getError().getMessage(), equalTo(expectedMsg));
    }

    /**
     * This method performs assertions for validation of status code
     * @param actualStatusCode
     * @param expectedStatusCode
     */
    public static void assertStatusCode(int actualStatusCode, int expectedStatusCode) {
        assertThat(actualStatusCode, equalTo(expectedStatusCode));
    }
}
