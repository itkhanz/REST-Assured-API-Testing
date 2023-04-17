package framework.utils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class JsonUtils {
    public static String getJsonPath (Response response, String key) {
        String resp = response.asString();
        JsonPath jpath = new JsonPath(resp);
        return jpath.get(key).toString();
    }
}
