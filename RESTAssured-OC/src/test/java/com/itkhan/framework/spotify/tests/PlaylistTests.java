package com.itkhan.framework.spotify.tests;

import com.itkhan.framework.spotify.pojo.Error;
import com.itkhan.framework.spotify.pojo.Playlist;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistTests {
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;
    private static String access_token = "";
    @BeforeClass
    public void beforeClass() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder()
                .setBaseUri("https://api.spotify.com")
                .setBasePath("/v1")
                .addHeader("Authorization", "Bearer " + access_token)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL);
        requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder()
                .log(LogDetail.ALL);
        responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void shouldBeAbleToCreateAPlaylist() {
        Playlist requestPlaylist = new Playlist()
                .setName("New Playlist")
                .setDescription("New playlist description")
                .setPublic(false)
                ;

        Playlist responsePlaylist = given(requestSpecification)
                .pathParam("user_id", "31ere62g3sbz2lsr27qcc5w4fsae")
                .body(requestPlaylist)
        .when()
                .post("/users/{user_id}/playlists")
        .then().spec(responseSpecification)
                .assertThat()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract().response().as(Playlist.class);

        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.getPublic(), equalTo(requestPlaylist.getPublic()));
    }

    @Test
    public void shouldBeAbleToGetAPlaylist() {

        Playlist responsePlaylist = given(requestSpecification)
                .pathParam("playlist_id", "6794ECQRd7OBRkcpMyyo40")
        .when()
                .get("/playlists/{playlist_id}")
        .then().spec(responseSpecification)
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response().as(Playlist.class);

        assertThat(responsePlaylist.getName(), equalTo("New Playlist"));
        assertThat(responsePlaylist.getDescription(), equalTo("New playlist description"));
        assertThat(responsePlaylist.getPublic(), equalTo(false));
    }

    @Test
    public void shouldBeAbleToUpdateAPlaylist() {
        Playlist requestPlaylist = new Playlist()
                .setName("Updated Playlist Name")
                .setDescription("Updated playlist description")
                .setPublic(false)
                ;

        given(requestSpecification)
                .pathParam("playlist_id", "3sjJgjSUDIXQNggx0SdnKY")
                .body(requestPlaylist)
        .when()
                .put("/playlists/{playlist_id}")
        .then().spec(responseSpecification)
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void shouldNotBeAbleToCreateAPlaylistWithoutName() {
        Playlist requestPlaylist = new Playlist()
                .setName("")
                .setDescription("New playlist description")
                .setPublic(false)
                ;

        Error error = given(requestSpecification)
                .pathParam("user_id", "31ere62g3sbz2lsr27qcc5w4fsae")
                .body(requestPlaylist)
        .when()
                .post("/users/{user_id}/playlists")
        .then().spec(responseSpecification)
                .assertThat()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .extract().response().as(Error.class);

        assertThat(error.getError().getStatus(), equalTo(400));
        assertThat(error.getError().getMessage(),equalTo("Missing required field: name"));
    }

    @Test
    public void shouldNotBeAbleToCreateAPlaylistWithExpiredToken() {
        Playlist requestPlaylist = new Playlist()
                .setName("New Playlist")
                .setDescription("New playlist description")
                .setPublic(false)
                ;

        Error error = given()
                .baseUri("https://api.spotify.com")
                .basePath("/v1")
                .header("Authorization", "Bearer " + "12345")
                .contentType(ContentType.JSON)
                .log().all()
                .pathParam("user_id", "31ere62g3sbz2lsr27qcc5w4fsae")
                .body(requestPlaylist)
        .when()
                .post("/users/{user_id}/playlists")
        .then().spec(responseSpecification)
                .assertThat()
                .statusCode(401)
                .contentType(ContentType.JSON)
                .extract().response().as(Error.class);

        assertThat(error.getError().getStatus(), equalTo(401));
        assertThat(error.getError().getMessage(),equalTo("Invalid access token"));
    }
}
