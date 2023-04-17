package framework.hooks;

import framework.stepDefinitions.Stepdefs;
import io.cucumber.java.Before;

import java.io.IOException;

public class Hooks {

    @Before("@DeletePlace")
    public void beforeScenario() throws IOException {
        //execute this code only when place id is null
        //write a code that will give you place id
        Stepdefs stepdefs = new Stepdefs();
        if (Stepdefs.place_id == null) {
            stepdefs.add_place_payload_with("Shetty", "French", "Asia");
            stepdefs.user_calls_with_http_request("AddPlaceAPI", "POST");
            stepdefs.verify_place_id_created_maps_to_using("Shetty", "getPlaceAPI");
        }
    }
}
