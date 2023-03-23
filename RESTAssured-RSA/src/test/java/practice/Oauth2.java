package practice;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import practice.pojo.Api;
import practice.pojo.GetCourse;
import practice.pojo.WebAutomation;
import practice.utils.ParseUtils;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * Learning Objectives:
 * What is Oauth2.0
 * OAuth 2.0 comes with multiple grant types
 * Authorization code and Client credentials are the most commonly used Grant types for OAuth
 * Understand the flow of OAuth (Authorization code) Grant type with Real world example
 * Backend implementation of Authorization code with different layers of security
 * Plan for generating Access token using APIs in Postman for complex flow of authorization code OAuth 2.0
 * API testing with generated access token
 * Automate complete OAuth2.0 flow using Rest Assured
 * ****************************************************************************************************************
 * Deserialization of complex JSON response using POJO classes is also covered in this test class
 * TC_01: Get the price of SoapUI Webservices testing course (Dynamically scan all the course titles and extract the course price for matching course title)
 * TC_02: Get all the course titles of webAutomation
 *
 */
public class Oauth2 {
    /*public WebDriver driver;

    @BeforeClass
    public void setup() {
        *//*ChromeOptions co = new ChromeOptions();
        co.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(co);*//*

        driver = new ChromeDriver();
    }

    @AfterClass
    public void tearDown() {
        if (driver!=null) driver.quit();
    }*/

    @Test
    public void test_Oauth2() throws InterruptedException {
        String email = "";     //your GMAIL email ID
        String password = "";       //your password for GMAIL email ID

        //Step 1: Get the authorization code (perform this step manually in browser)
        //User signs in to google by hitting google authorization server and get code
        //https://accounts.google.com/o/oauth2/v2/auth
        String code = getAuthorizationCode(email, password);
        System.out.println("Auth Code: " + code);

        //Step 2: Get the access token
        //Application will use the auth code to hit google resource server in backend to get (access token, email, firstname, lastname)
        //https://www.googleapis.com/oauth2/v4/token
        String tokenResponse = getAccessToken(code);
        JsonPath parsedTokenResponse = ParseUtils.rawToJson(tokenResponse);
        String accessToken = parsedTokenResponse.getString("access_token");
        System.out.println("Access Token: " + accessToken);

        //Step 3: Hit the actual request with access token
        //Application grants access to User by validating access token
        //https://rahulshettyacademy.com/getCourse.php
        GetCourse gc = getCourses(accessToken);
        System.out.println("Linkedin: " + gc.getLinkedIn());
        System.out.println("Instructor: " + gc.getInstructor());

        //Step 4: Get the price of SoapUI Webservices testing course
        List<Api> apiCourses = gc.getCourses().getApi();
        String coursePrice = apiCourses
                                .stream()
                                .filter(apiCourse -> apiCourse.getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
                                .findAny()
                                .map(Api::getPrice)
                                .orElse(null);
        Assert.assertEquals(coursePrice, "40");

        //Step 5: Get all the course titles of webAutomation
        List<String> expectedCourseTitles = Arrays.asList("Selenium Webdriver Java", "Cypress", "Protractor");
        List<WebAutomation> webAutomationCourses = gc.getCourses().getWebAutomation();
        List<String> actualCourseTitles = webAutomationCourses.stream().map(WebAutomation::getCourseTitle).toList();
        webAutomationCourses.forEach(course -> System.out.println(course.getCourseTitle()));
        Assert.assertEquals(actualCourseTitles, expectedCourseTitles);


    }

    private String getAuthorizationCode(String email, String password) throws InterruptedException {
        //Due to security issues, Google no longer supports sign in from automated browsers, so perform this step manually and copy the code from url
        //Alternatively ask the developer to extend the expiry of code to higher no of days so tests can run once you get the code at start of the day
        /*//navigate to url
        driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");

        //enter username and password
        driver.findElement(By.cssSelector("input[type='email']")).sendKeys(email);
        driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
        Thread.sleep(5000);
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys(password);
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Keys.ENTER);
        Thread.sleep(5000);

        //extract the code from url
        String url = driver.getCurrentUrl();
        System.out.println("Code Url: " + url);
        String code = url
                        .split("code=")[1]
                        .split("&scope")[0];*/

        //Get this code manually by hitting the above URL and extract the code
        String code = "4%2F0AVHEtk4gZYfLY2atUybPFMtriCv74LCVgJE9Ag9q4-A2bR5Ea64MVkCJ3OLExyupA4VYAQ";
        return code;
    }

    public static String getAccessToken(String authCode) {
        //Rest Assured encodes special characters like % to numeric values, so we need to disable encoding to be able to use exact code
        //Client ID and Client Secret can be asked from developer who integrated Oauth in application
        //Here authorization_code grant type is used which is the most complex of grant type mechanism in Oauth
        //Another grant type: client_credentials is used where application requests for its own data and therefore does not need authorization code
        //Client ID: ID that identifies client.
        //Client Secret: Application registers this ID with Google
        //Resource owner: Human
        //Resource/Authorization server: Google
        String response = given()
                            .urlEncodingEnabled(false)
                            .queryParam("code", authCode)
                            .queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                            .queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                            .queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
                            .queryParam("grant_type", "authorization_code")
                        .when()
                            .post("https://www.googleapis.com/oauth2/v4/token")
                        .asString();
        return response;
    }

    /*public static String getCourses(String accessToken) {
        String response = given()
                            .queryParam("access_token", accessToken)
                        .when()
                            .get("https://rahulshettyacademy.com/getCourse.php")
                        .asString();
        return response;
    }*/

    public static GetCourse getCourses(String accessToken) {
        //Deserialization of the response is achieved using POJO classes
        //RestAssured will convert the JSON response with the help of POJO classes and insert the values into objects.
        //src/test/java/practice/files/oauth.json contains the mock response of API
        //If the response header has Content-Type: application/json, then no need to add expect() Parser.JSON
        GetCourse response = given()
                                .queryParam("access_token", accessToken)
                            .expect()
                                .defaultParser(Parser.JSON)
                            .when()
                                .get("https://rahulshettyacademy.com/getCourse.php")
                            .as(GetCourse.class)
                            ;
        return response;
    }
}
