package com.itkhan.practice.serializeDeserialize;

import com.itkhan.practice.pojo.Address;
import com.itkhan.practice.pojo.Geo;
import com.itkhan.practice.pojo.User;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
public class AssignmentPojoTest {
    private static String BASEURI = "https://jsonplaceholder.typicode.com";
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void beforeClass() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder()
                .setBaseUri(BASEURI)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL);

        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder()
                .expectStatusCode(201)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                ;
        responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void assignment_serialize_deserialize_test() {

        Geo geo = new Geo("-37.3159", "81.1496");
        Address address = new Address("Kulas Light","Apt. 556","Gwenborough","92998-3874", geo);
        User user = new User("Leanne Graham","Bret","Sincere@april.biz", address);

        User deserializedUser =
                given()
                        .body(user)
                .when()
                        .post("/users")
                .then()
                        .spec(responseSpecification)
                        .extract().response().as(User.class);

        assertThat(deserializedUser.getId(), not(is(emptyOrNullString())));
    }
}
