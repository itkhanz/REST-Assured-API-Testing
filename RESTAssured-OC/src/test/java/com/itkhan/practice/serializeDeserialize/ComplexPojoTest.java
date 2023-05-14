package com.itkhan.practice.serializeDeserialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itkhan.practice.pojo.collection.*;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONException;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.ValueMatcher;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ComplexPojoTest {
    private static String API_KEY = "PMAK-6455f9b49325834e17608e53-7e10f5eff1dd8519c16f8ad74c0b03d9bc"; //generate your own Postman API key
    private static String BASEURI = "https://api.postman.com";

    private static String WORKSPACE_ID = "f3c17300-836c-4a00-a16f-8bcaace3707e";
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void beforeClass() {

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder()
                .setBaseUri(BASEURI)
                .addHeader("X-Api-Key", API_KEY)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL);

        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                ;
        responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void complex_pojo_create_collection() throws JsonProcessingException, JSONException {
        //Initialize the POJO classes with a data
        Header header = new Header("Content-Type", "application/json");
        List<Header> headerList = new ArrayList<Header>();
        headerList.add(header);

        Body body = new Body("raw", "{\"data\": \"123\"}");

        RequestRequest request = new RequestRequest(
                "https://postman-echo.com/post",
                "POST",
                headerList,
                body,
                "This is a sample Request"
        );

        RequestRootRequest requestRoot = new RequestRootRequest("Sample POST Request", request);
        List<RequestRootRequest> requestRootList = new ArrayList<RequestRootRequest>();
        requestRootList.add(requestRoot);

        FolderRequest folder = new FolderRequest("This is a folder", requestRootList);
        List<FolderRequest> folderList = new ArrayList<FolderRequest>();
        folderList.add(folder);

        Info info = new Info(
                "Sample Collection1",
                "This is just a sample collection.",
                "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
        );

        CollectionRequest collection = new CollectionRequest(info, folderList);

        CollectionRootRequest collectionRoot = new CollectionRootRequest(collection);

        //Create a new Collection and retrieve the UID of the created collection
        String collectionUid = given()
                .body(collectionRoot)
                .queryParam("workspace", WORKSPACE_ID)
        .when()
                .post("/collections")
        .then()
                .spec(responseSpecification)
                .extract()
                .response().path("collection.uid")
        ;

        //Make a GET coll to the collections API with UID of the created collection and deserialize the POJO
        CollectionRootResponse deserializedCollectionRoot = given()
            .pathParam("collectionUid", collectionUid)
        .when()
            .get("/collections/{collectionUid}")
        .then()
            .spec(responseSpecification)
            .extract()
            .response().as(CollectionRootResponse.class)
        ;

        //converting the POJO to Json objects (strings) for the JSONAssert
        ObjectMapper objectMapper = new ObjectMapper();
        String collectionRootStr = objectMapper.writeValueAsString(collectionRoot);
        String deserializedCollectionRootStr = objectMapper.writeValueAsString(deserializedCollectionRoot);

        //writing custom Comparator for full JSON Body assertion excluding URL field
        //STRICT mode will also validate for order of the payload and response body, while LENIENT mode will ignore order
        JSONAssert.assertEquals(collectionRootStr, deserializedCollectionRootStr,
                new CustomComparator(JSONCompareMode.STRICT,
                            new Customization("collection.item[*].item[*].request.url", new ValueMatcher<Object>() {
                                public boolean equal(Object o1, Object o2) {
                                    return true;
                                }
                            })
                        )
        );


        //Now we will carry out the steps to perform assertion for URL
        List<String> urlRequestList = new ArrayList<>();
        List<String> urlResponseList = new ArrayList<>();

        //Get all the URLs for request payload
        for (RequestRootRequest requestRootRequest: requestRootList) {
            System.out.println("URL from Request Payload: " + requestRootRequest.getRequest().getUrl());
            urlRequestList.add(requestRootRequest.getRequest().getUrl());
        }

        //Get all the URLs for the response body
        List<FolderResponse> folderResponseList = deserializedCollectionRoot.getCollection().getItem();
        for (FolderResponse folderResponse: folderResponseList) {
            List<RequestRootResponse> requestRootResponseList = folderResponse.getItem();
            for (RequestRootResponse requestRootResponse: requestRootResponseList) {
                URL url = requestRootResponse.getRequest().getUrl();
                System.out.println("URL from Response Body: " + url.getRaw());
                urlResponseList.add(url.getRaw());
            }
        }

        //Check that the response URL is present in the request payload
        assertThat(urlResponseList, containsInAnyOrder(urlRequestList.toArray()));

    }

    @Test
    public void simple_pojo_create_collection() throws JsonProcessingException {
        List<FolderRequest> folderList = new ArrayList<FolderRequest>();

        Info info = new Info("Sample Collection2", "This is just a sample collection.",
                "https://schema.getpostman.com/json/collection/v2.1.0/collection.json");

        CollectionRequest collection = new CollectionRequest(info, folderList);
        CollectionRootRequest collectionRoot = new CollectionRootRequest(collection);

        String collectionUid =
                given().
                    body(collectionRoot).
                when().
                    post("/collections").
                then().spec(responseSpecification).
                    extract().
                    response().path("collection.uid");

        CollectionRootResponse deserializedCollectionRoot =
                given().
                    pathParam("collectionUid", collectionUid).
                when().
                    get("/collections/{collectionUid}").
                then().spec(responseSpecification).
                    extract().
                    response().
                    as(CollectionRootResponse.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String collectionRootStr = objectMapper.writeValueAsString(collectionRoot);
        String deserializedCollectionRootStr = objectMapper.writeValueAsString(deserializedCollectionRoot);

        assertThat(objectMapper.readTree(collectionRootStr),
                equalTo(objectMapper.readTree(deserializedCollectionRootStr)));
    }
}
