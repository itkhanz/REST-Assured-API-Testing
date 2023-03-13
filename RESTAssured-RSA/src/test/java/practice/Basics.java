package practice;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import practice.files.Payload;
import practice.utils.ParseUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Basics {

    @BeforeTest
    public void setup() {
        //Validate if the Add Place API is working as expected
        //given - all input details
        //when - Submit the API resource http method - resource, HTTP method
        //then - validate the response
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        RestAssured.useRelaxedHTTPSValidation();

    }

    @Test
    public void basicTest() {
        //Auto-Complete import suggestions for static packages are not shown bz Intellij/Eclipse, therefore import them manually once at the top
        //E2E Scenario: Add Place -> Update place with New Address -> Get Place to validate if New Address is present in response

        //Step 1: Add Place and save place_id
        String responseAddPlace = addPlace();
        JsonPath parsedAddPlaceResponse = ParseUtils.rawToJson(responseAddPlace);
        String placeID = parsedAddPlaceResponse.getString("place_id");
        System.out.println(placeID);

        //Step 2: Update place with a new address
        String newAddress = "70 Summer walk, USA";
        String responseUpdatePlace = updatePlace(placeID, newAddress);

        //Step 3: Get place and perform assertion for updated address
        String responseGetPlace = getPlace(placeID);
        JsonPath parsedGetPlaceResponse = ParseUtils.rawToJson(responseGetPlace);
        String updatedAddress = parsedGetPlaceResponse.getString("address");
        System.out.println(updatedAddress);
        Assert.assertEquals(updatedAddress, newAddress);
    }

    public static String addPlace() {

        String response = given()
                            .log().all()
                            .queryParam("key", "qaclick123")
                            .header("Content-Type","application/json")
                            .body(Payload.addPlace())
                        .when()
                            .post("/maps/api/place/add/json")
                        .then()
                            //.log().all()
                            .assertThat()
                                .statusCode(200)
                                .body("scope", equalTo("APP"))
                                .header("server", "Apache/2.4.41 (Ubuntu)")
                            .extract().response().asString()
        ;
        return response;
    }

    public static String updatePlace(String placeID, String newAddress) {
        String response = given()
                            .log().all()
                            .queryParam("key", "qaclick123")
                            .header("Content-Type","application/json")
                            .body(Payload.updatePlace(placeID,newAddress))
                        .when()
                            .put("/maps/api/place/update/json")
                        .then()
                            .log().all()
                            .assertThat()
                                .statusCode(200)
                                .body("msg", equalTo("Address successfully updated"))
                            .extract().response().asString()
                ;
        return response;

    }

    public static String getPlace(String placeID) {
        String response = given()
                            .log().all()
                            .queryParam("key", "qaclick123")
                            .queryParam("place_id", placeID)
                        .when()
                            .get("/maps/api/place/get/json")
                        .then()
                            .log().all()
                            .assertThat()
                                .statusCode(200)
                        .extract().response().asString()
                ;
        return response;
    }
}
