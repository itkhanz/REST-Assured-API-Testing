package framework.stepDefinitions;

import framework.constants.APIResources;
import framework.utils.JsonUtils;
import framework.utils.SpecBuilder;
import framework.utils.TestDataBuild;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class Stepdefs {
    private RequestSpecification reqSpec;
    private ResponseSpecification resSpec;
    private Response response;
    private TestDataBuild data;
    private SpecBuilder specBuilder;
    public static String place_id;

    public Stepdefs() {
        data = new TestDataBuild();
        specBuilder = new SpecBuilder();
    }


    @Given("Add Place Payload with {string}  {string} {string}")
    public void add_place_payload_with(String name, String language, String address) throws IOException {
        reqSpec = given()
                .spec(specBuilder.getRequestSpecification())
                .body(data.addPlacePayLoad(name, language, address));
    }

    @When("user calls {string} with {string} http request")
    public void user_calls_with_http_request(String resource, String method) {
        resSpec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();

        if (method.equalsIgnoreCase("POST"))
            response = reqSpec.when().post(APIResources.valueOf(resource).getResource());
        else if (method.equalsIgnoreCase("GET")) {
            response = reqSpec.when().get(APIResources.valueOf(resource).getResource());
        }
    }

    @Then("the API call got success with status code {int}")
    public void the_api_call_got_success_with_status_code(int statusCode) {
        Assert.assertEquals(statusCode, response.getStatusCode());
    }

    @Then("{string} in response body is {string}")
    public void in_response_body_is(String fieldKey, String fieldValue) {
        Assert.assertEquals(fieldValue, JsonUtils.getJsonPath(response, fieldKey));
    }

    @Then("verify place_Id created maps to {string} using {string}")
    public void verify_place_id_created_maps_to_using(String placeName, String apiResource) throws IOException {
        place_id = JsonUtils.getJsonPath(response, "place_id");

        reqSpec = given().spec(specBuilder.getRequestSpecification().queryParam("place_id", place_id));
        user_calls_with_http_request(apiResource, "GET");
        String actualPlace = JsonUtils.getJsonPath(response, "name");

        Assert.assertEquals(placeName, actualPlace);
    }

    @Given("DeletePlace Payload")
    public void delete_place_payload() throws IOException {
        reqSpec = given().spec(specBuilder.getRequestSpecification()).body(data.deletePlacePayload(place_id));
    }
}
