package practice;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import practice.files.Payload;
import practice.utils.ParseUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

/**
 * Section 07: Handling Dynamic JSON payload with parameterization
 * Dynamically build json payload with external data inputs
 * Parameterize the API tests with multiple data sets
 * How to send static json files (payload) directly into POST method of REST Assured
 * Feed json payload from Using Excel using Hashmap
 * ***********************************************************************************
 * Library API
 * Addbook (Bookname, authorname, ISBN, aisle), ID will be combination of ISBN+aisle in response
 */
public class Library {
    @BeforeTest
    public void setup() {
        RestAssured.baseURI = "http://216.10.245.166";
    }

    //Parameterization of API Tests with multiple data sets
    @Test(dataProvider = "BooksData")
    public void test_AddBook(String aisle, String isbn) {
        String responseAddBook = addBook(aisle, isbn);
        JsonPath parsedAddBookResponse = ParseUtils.rawToJson(responseAddBook);
        String bookID = parsedAddBookResponse.getString("ID");
        System.out.println(bookID);
    }

    @DataProvider(name="BooksData")
    public Object[][] getData() {
        //multidimensional array of aisle and isbn
        return new Object[][] {
                {"dsae", "4585"},
                {"dfrde", "7891"},
                {"dewq", "3215"}
        };
    }

    public static String addBook(String aisle, String isbn) {
        String response = given()
                            //.log().all()
                            .header("content-Type", "application/json")
                            .body(Payload.addBook(aisle, isbn))
                        .when()
                            .post("Library/Addbook.php")
                        .then()
                            //.log().all()
                            .assertThat()
                            .statusCode(200)
                            .extract().response().asString()
                ;
        return response;

    }

    //How to handle API tests with static JSON payloads
    @Test
    public void test_AddBookStatic() throws IOException {
        //Convert the content of addBook.json file to String -> content of file convert into Byte -> Byte data to String
        //This conversion is necessary because body() method only accepts String
        String responseAddBook = addBookStatic();
        JsonPath parsedAddBookResponse = ParseUtils.rawToJson(responseAddBook);
        String bookID = parsedAddBookResponse.getString("ID");
        System.out.println(bookID);
    }

    public static String addBookStatic() throws IOException {
        String response = given()
                            .log().all()
                            .header("content-Type", "application/json")
                            //.body(Payload.addBook(aisle, isbn))
                             .body(new String(Files.readAllBytes(Paths.get("src/test/java/practice/files/addBook.json"))))
                        .when()
                            .post("Library/Addbook.php")
                        .then()
                            .log().all()
                            .assertThat()
                                .statusCode(200)
                            .extract().response().asString()
                ;
        return response;

    }

}
