package practice.utils;

import io.restassured.path.json.JsonPath;

public class ParseUtils {
    public static JsonPath rawToJson(String response) {
        JsonPath parsedResponse = new JsonPath(response); //parse JSON
        return parsedResponse;
    }
}
