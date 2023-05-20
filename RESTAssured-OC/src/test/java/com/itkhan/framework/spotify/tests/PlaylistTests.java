package com.itkhan.framework.spotify.tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistTests {
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;
    private static String access_token = "";    //get access token via Postman

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
        String payload = "{\n" +
                "    \"name\": \"New Playlist\",\n" +
                "    \"description\": \"New playlist description\",\n" +
                "    \"public\": false\n" +
                "}";

        given(requestSpecification)
                .pathParam("user_id", "31ere62g3sbz2lsr27qcc5w4fsae")
                .body(payload)
        .when()
                .post("/users/{user_id}/playlists")
        .then().spec(responseSpecification)
                .assertThat()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("name", equalTo("New Playlist"),
                        "description", equalTo("New playlist description"),
                        "public",equalTo(false)
                );
    }

    @Test
    public void shouldBeAbleToGetAPlaylist() {

        given(requestSpecification)
                .pathParam("playlist_id", "3sjJgjSUDIXQNggx0SdnKY")
        .when()
                .get("/playlists/{playlist_id}")
        .then().spec(responseSpecification)
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("name", equalTo("New Playlist"),
                        "description", equalTo("New playlist description"),
                        "public",equalTo(false)
                );
    }

    @Test
    public void shouldBeAbleToUpdateAPlaylist() {
        String payload = "{\n" +
                "    \"name\": \"Updated Playlist Name\",\n" +
                "    \"description\": \"Updated playlist description\",\n" +
                "    \"public\": false\n" +
                "}";

        given(requestSpecification)
                .pathParam("playlist_id", "3sjJgjSUDIXQNggx0SdnKY")
                .body(payload)
        .when()
                .put("/playlists/{playlist_id}")
        .then().spec(responseSpecification)
                .assertThat()
                .statusCode(200)
                ;
    }

    @Test
    public void shouldNotBeAbleToCreateAPlaylistWithoutName() {
        String payload = "{\n" +
                "    \"name\": \"\",\n" +
                "    \"description\": \"New playlist description\",\n" +
                "    \"public\": false\n" +
                "}";

        given(requestSpecification)
                .pathParam("user_id", "31ere62g3sbz2lsr27qcc5w4fsae")
                .body(payload)
        .when()
                .post("/users/{user_id}/playlists")
        .then().spec(responseSpecification)
                .assertThat()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("error.status", equalTo(400),
                        "error.message", equalTo("Missing required field: name")
                );
    }

    @Test
    public void shouldNotBeAbleToCreateAPlaylistWithExpiredToken() {
        String payload = "{\n" +
                "    \"name\": \"\",\n" +
                "    \"description\": \"New playlist description\",\n" +
                "    \"public\": false\n" +
                "}";

        given()
                .baseUri("https://api.spotify.com")
                .basePath("/v1")
                .header("Authorization", "Bearer " + "12345")
                .contentType(ContentType.JSON)
                .log().all()
                .pathParam("user_id", "31ere62g3sbz2lsr27qcc5w4fsae")
                .body(payload)
        .when()
                .post("/users/{user_id}/playlists")
        .then().spec(responseSpecification)
                .assertThat()
                .statusCode(401)
                .contentType(ContentType.JSON)
                .body("error.status", equalTo(401),
                        "error.message", equalTo("Invalid access token")
                );
    }
}
