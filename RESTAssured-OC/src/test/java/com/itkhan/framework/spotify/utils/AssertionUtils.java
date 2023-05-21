package com.itkhan.framework.spotify.utils;

import com.itkhan.framework.spotify.constants.StatusCode;
import com.itkhan.framework.spotify.pojo.Error;
import com.itkhan.framework.spotify.pojo.Playlist;
import io.qameta.allure.Step;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/*performs Hamcrest assertions for Tests */
public class AssertionUtils {
    /**
     * This method performs Hamcrest assertions on equality of Request and Response playlist name, description, and public properties
     * @param responsePlaylist Playlist POJO for request Payload
     * @param requestPlaylist Playlist POJO for response body
     */
    @Step
    public static void assertPlaylistEqual(Playlist responsePlaylist, Playlist requestPlaylist) {
        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.get_public(), equalTo(requestPlaylist.get_public()));
    }

    /**
     * This method performs assertions for the Error Json returned as response body
     * @param responseErr ErrorRoot POJO object
     * @param statusCode StatusCode ENUM constant that stores code and message
     */
    @Step
    public static void assertError(Error responseErr, StatusCode statusCode){
        assertThat(responseErr.getError().getStatus(), equalTo(statusCode.code));
        assertThat(responseErr.getError().getMessage(), equalTo(statusCode.msg));
    }

    /**
     * This method performs assertions for validation of status code
     * @param actualStatusCode
     * @param statusCode StatusCode ENUM constant that stores code and message
     */
    @Step
    public static void assertStatusCode(int actualStatusCode, StatusCode statusCode) {
        assertThat(actualStatusCode, equalTo(statusCode.code));
    }
}
