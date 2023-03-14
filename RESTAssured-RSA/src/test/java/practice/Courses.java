package practice;

import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import practice.files.Payload;
import practice.utils.ParseUtils;

/****************************************
 * Section 06: Diving in Depth-Automating REST APIs
 * Mock response of API is stored in the Payload.coursePrice(). It is helpful to test APIs before the API contract if fully developed.
 * 1. Print no of courses returned by API
 * 2. Print purchase amount
 * 3. Print title of the first course
 * 4. Print all course titles and their respective prices
 * 5. Print no of copies sold by RPA Course
 * 6. Verify if sum of all Course prices matches with purchase amount
 */
public class Courses {

    public static JsonPath parsedCoursesResponse = ParseUtils.rawToJson(Payload.coursePrice());

    @Test
    public void test_Courses() {
        //Print no of courses returned by API
        int count = parsedCoursesResponse.getInt("courses.size()");
        System.out.println(count);  //3

        //Print purchase amount
        int totalAmount = parsedCoursesResponse.getInt("dashboard.purchaseAmount");
        System.out.println(totalAmount);    //910

        //Print title of the first course
        String titleFirstCourse = parsedCoursesResponse.getString("courses[2].title");
        System.out.println(titleFirstCourse);   //RPA

        //Print all course titles and their respective prices
        for (int i = 0; i < count; i++) {
            String courseTitles = parsedCoursesResponse.get("courses[" + i + "].title");
            System.out.println("title: " + courseTitles);
            System.out.println("price: " + parsedCoursesResponse.get("courses[" + i + "].price").toString());
        }

        //Print no of copies sold by RPA Course
        for (int i = 0; i < count; i++) {
            String courseTitles = parsedCoursesResponse.get("courses[" + i + "].title");
            if (courseTitles.equalsIgnoreCase("RPA")) {
                System.out.println("copies sold by RPA: " + parsedCoursesResponse.get("courses[" + i + "].copies").toString()); //10
                break;
            }
        }

        //Verify if sum of all Course prices matches with purchase amount
        //(50*6)+(40*4)+(45*10) = 910
        int actualSum = 0;
        for (int i = 0; i < count; i++) {
            int coursePrice = parsedCoursesResponse.getInt("courses[" + i + "].price");
            int courseCopies = parsedCoursesResponse.getInt("courses[" + i + "].copies");
            actualSum = actualSum+(coursePrice*courseCopies);
        }
        System.out.println(actualSum);
        Assert.assertEquals(actualSum, totalAmount);
    }
}
