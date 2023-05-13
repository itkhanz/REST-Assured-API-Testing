package com.itkhan.practice;

import org.testng.annotations.Test;

import java.io.*;

import static io.restassured.RestAssured.given;

public class FileUploadDownload {
    @Test
    public void upload_file_multipart_form_data(){
        String attributes = "{\"name\":\"temp.txt\",\"parent\":{\"id\":\"123456\"}}";
        given().
                baseUri("https://postman-echo.com").
                multiPart("file", new File("src/test/resources/payload/temp.txt")).
                multiPart("attributes", attributes, "application/json").
                log().all().
        when().
                post("/post").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    public void download_file() throws IOException {
        //InputStream is = given()
        byte[] bytes = given()
                .baseUri("https://raw.githubusercontent.com")
                .log().all().
        when().
                get("/appium-boneyard/tutorial/master/projects/ruby_ios/appium.txt").
        then().
                log().all()
                .extract()
                .response()
                //.asInputStream();
                .asByteArray();

        OutputStream os = new FileOutputStream(new File("downloaded-file.txt"));
        //byte[] bytes = new byte[is.available()];  //creating a new object of byte[] with sufficient buffer
        //is.read(bytes);   //we need to convert inputStream to the bytes arr
        os.write(bytes);
        os.close();
    }
}
