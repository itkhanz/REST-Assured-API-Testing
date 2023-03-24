package practice;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;
import practice.pojo.AddPlace;
import practice.pojo.Location;
import practice.utils.ParseUtils;

import java.util.Arrays;

import static io.restassured.RestAssured.given;

public class SpecBuilderTest {

    /*@BeforeTest
    public void setup() {
        //Validate if the Add Place API is working as expected
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        RestAssured.useRelaxedHTTPSValidation();
    }*/
    private static RequestSpecification getReqSpec() {
        RequestSpecification reqSpec = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addQueryParam("key", "qaclick123")
                .setContentType(ContentType.JSON)
                .build();
        return reqSpec;
    }

    private static ResponseSpecification getResSpec() {
        ResponseSpecification resSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();
        return resSpec;
    }

    @Test
    public void test_AddPlace() {
        //Validate if the Add Place API is working as expected
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
        System.out.println(responseAddPlace);
        JsonPath parsedAddPlaceResponse = ParseUtils.rawToJson(responseAddPlace);
        String placeID = parsedAddPlaceResponse.getString("place_id");
        System.out.println("Place ID: " + placeID);
    }

    public static String addPlace(AddPlace reqBody) {
        //Read the API Contract in resources/files/doc/ADD-DeleteAPIs.docx
        //Here in the request body we are passing POJO Class object
        //Here we are using RequestSpecification and ResponseSpecification SpecBuilders, so we can reuse them in all the API requests with same req and res
        //https://github.com/rest-assured/rest-assured/wiki/Usage#specification-re-use
        RequestSpecification req = given()
                                    .spec(getReqSpec())
                                    .body(reqBody);
        Response res = req.
                        when()
                            .post("/maps/api/place/add/json")
                        .then()
                            .spec(getResSpec())
                            .extract()
                            .response();

        String responseStr = res.asString();

        return responseStr;
    }


}
