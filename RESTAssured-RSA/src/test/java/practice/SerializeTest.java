package practice;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import practice.pojo.AddPlace;
import practice.pojo.Location;
import practice.utils.ParseUtils;

import java.util.Arrays;

import static io.restassured.RestAssured.given;

public class SerializeTest {

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
    public void test_SerializationMapsApi() {
        //Create the object of POJO class for request body and fill in values
        //These values come from addPlace.json sample request body
        AddPlace ap = new AddPlace();
        ap.setAccuracy(50);
        ap.setAddress("29, side layout, cohen 09");
        ap.setLanguage("French-IN");
        ap.setPhone_number("(+91) 983 893 3937");
        ap.setWebsite("http://google.com");
        ap.setName("Frontline house");
        ap.setTypes(Arrays.asList("shoe park", "shop"));
        Location loc = new Location();
        loc.setLat(-38.383494);
        loc.setLng(33.427362);
        ap.setLocation(loc);

        String responseAddPlace = addPlace(ap);
        JsonPath parsedAddPlaceResponse = ParseUtils.rawToJson(responseAddPlace);
        String placeID = parsedAddPlaceResponse.getString("place_id");
        System.out.println("Place ID: " + placeID);
    }

    public static String addPlace(AddPlace reqBody) {
        //Read the API Contract in resources/files/doc/ADD-DeleteAPIs.docx
        //Here in the request body we are passing POJO Class object
        String response = given()
                            .log().all()
                            .queryParam("key", "qaclick123")
                            .header("Content-Type","application/json")
                            .body(reqBody)
                        .when()
                            .post("/maps/api/place/add/json")
                        .then()
                            .log().all()
                            .assertThat()
                                .statusCode(200)
                        .extract().response().asString()
                ;
        return response;
    }
}
