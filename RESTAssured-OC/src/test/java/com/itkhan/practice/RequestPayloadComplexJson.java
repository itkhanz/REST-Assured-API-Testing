package com.itkhan.practice;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RequestPayloadComplexJson {
    private static String BASEURI = "";
    ResponseSpecification customResponseSpecification;

    @BeforeClass
    public void beforeClass() {

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder()
                .setBaseUri(BASEURI)
                .addHeader("x-mock-match-request-body", "true")
                .setContentType("application/json;charset=utf-8")
                //.setConfig(config.encoderConfig(EncoderConfig.encoderConfig()
                //        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                //.setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                ;
        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                ;
        customResponseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void validate_post_request_payload_complex_json(){

        //Refer to the complexJson.json in src/test/resources/payload folder to view JSON
        List<Integer> idArrayList = new ArrayList<Integer>();
        idArrayList.add(5);
        idArrayList.add(9);

        HashMap<String, Object> batterHashMap2 = new HashMap<String, Object>();
        batterHashMap2.put("id", idArrayList);
        batterHashMap2.put("type", "Chocolate");

        HashMap<String, Object> batterHashMap1 = new HashMap<String, Object>();
        batterHashMap1.put("id", "1001");
        batterHashMap1.put("type", "Regular");

        List<HashMap<String, Object>> batterArrayList = new ArrayList<HashMap<String, Object>>();
        batterArrayList.add(batterHashMap1);
        batterArrayList.add(batterHashMap2);

        HashMap<String, List<HashMap<String, Object>>> battersHashMap
                = new HashMap<String, List<HashMap<String, Object>>>();
        battersHashMap.put("batter", batterArrayList);

        List<String> typeArrayList = new ArrayList<String>();
        typeArrayList.add("test1");
        typeArrayList.add("test2");

        HashMap<String, Object> toppingHashMap2 = new HashMap<String, Object>();
        toppingHashMap2.put("id", "5002");
        toppingHashMap2.put("type", typeArrayList);

        HashMap<String, Object> toppingHashMap1 = new HashMap<String, Object>();
        toppingHashMap1.put("id", "5001");
        toppingHashMap1.put("type", "None");

        List<HashMap<String, Object>> toppingArrayList = new ArrayList<HashMap<String, Object>>();
        toppingArrayList.add(toppingHashMap1);
        toppingArrayList.add(toppingHashMap2);

        HashMap<String, Object> mainHashMap = new HashMap<String, Object>();
        mainHashMap.put("id", "0001");
        mainHashMap.put("type", "donut");
        mainHashMap.put("name", "Cake");
        mainHashMap.put("ppu", 0.55);
        mainHashMap.put("batters", battersHashMap);
        mainHashMap.put("topping", toppingArrayList);

        given().
                body(mainHashMap).
        when().
                post("/postComplexJson").
        then().spec(customResponseSpecification).
                assertThat().
                body("msg", equalTo("Success"));
    }

    @Test
    public void validate_post_request_payload_complex_json_assignment(){

        //Refer to the complexJsonAssignment.json in src/test/resources/payload folder to view JSON

        List<Integer> rgbaValuesColor1 = new ArrayList<Integer>();
        rgbaValuesColor1.add(255);
        rgbaValuesColor1.add(255);
        rgbaValuesColor1.add(255);
        rgbaValuesColor1.add(1);

        HashMap<String, Object> codeColor1 = new HashMap<String, Object>();
        codeColor1.put("rgba", rgbaValuesColor1);
        codeColor1.put("hex", "#000");

        HashMap<String, Object> mainColor1 = new HashMap<String, Object>();
        mainColor1.put("color", "black");
        mainColor1.put("category", "hue");
        mainColor1.put("type", "primary");
        mainColor1.put("code", codeColor1);


        List<Integer> rgbaValuesColor2 = new ArrayList<Integer>();
        rgbaValuesColor2.add(0);
        rgbaValuesColor2.add(0);
        rgbaValuesColor2.add(0);
        rgbaValuesColor2.add(1);

        HashMap<String, Object> codeColor2 = new HashMap<String, Object>();
        codeColor2.put("rgba", rgbaValuesColor2);
        codeColor2.put("hex", "#FFF");

        HashMap<String, Object> mainColor2 = new HashMap<String, Object>();
        mainColor2.put("color", "white");
        mainColor2.put("category", "value");
        mainColor2.put("code", codeColor2);

        List<HashMap<String, Object>> mainArrList = new ArrayList<HashMap<String, Object>>();
        mainArrList.add(mainColor1);
        mainArrList.add(mainColor2);

        HashMap<String, List<HashMap<String, Object>>> mainHashMap = new HashMap<String, List<HashMap<String, Object>>>();
        mainHashMap.put("colors", mainArrList);

        given().
                body(mainHashMap).
        when().
                post("/postComplexJsonExample").
        then().spec(customResponseSpecification).
                assertThat().
                body("msg", equalTo("Success"));
    }
}
