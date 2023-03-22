package practice;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import practice.files.Payload;
import practice.utils.ParseUtils;

import java.io.File;

import static io.restassured.RestAssured.given;

/**
 * Setup JIRA Server locally
 * Step 1:Download and Install the latest JIRA software from URL: https://www.atlassian.com/software/jira/update
 * Step 2:Use Default settings during installation, and generate license key which is valid for 30 days.
 * Step 3:After installation is finished, it will open JIRA on http://localhost:8080/. It takes few minutes before the page can load and finish the server setup.
 * Step 4:Create a Scrum Project and save its KEY which is later needed in API requests to distinguish among projects
 * Step 5: Read the JIRA Server API Documentation: https://docs.atlassian.com/software/jira/docs/api/REST/7.6.1/
 * Step 6: Use Cookie based authentication for login: https://developer.atlassian.com/server/jira/platform/cookie-based-authentication/
 * Step 9: In each of the API request, the session cookie must be sent as API header to authenticate the user
 * *************************************************************************************************************************************************
 * *************************************************************************************************************************************************
 * Steps to Automate in JIRA Application
 * 1. Login to Jira to Create Session using Login API
 * 2. Add a comment to existing Issue using Add comment API
 * 3. Add an attachment to existing Issue using Add Attachment API
 * 4. Get Issue details and verify if added comment and attachment exists using Get issue API
 * *************************************************************************************************************************************************
 * *************************************************************************************************************************************************
 * Topics Covered
 * How to create session filter for authentication in RESTAssured automation
 * Introducing path parameters and query parameters together in single test
 * Sending files as attachments using RESTAssured with multipart method
 * Parsing complex JSON and limiting JSON response through query parameters
 * Handling HTTPS certification validation through automated code
 * *************************************************************************************************************************************************
 * https://jsoneditoronline.org/ An online JSON editor to see JSON in clean nodes form
 * *************************************************************************************************************************************************
 */
public class Jira {
    private static String USERNAME_JIRA = ""; //add your username for local JIRA server created during installation
    private static String PASSWORD_JIRA = "";   //add your password for local JIRA server created during installation
    private static String PROJECT_KEY = "";  //project key generated during creation of Scrum software project the beginning
    private static String sessionID;    //This session ID is used for cookie based authentication in each API Request and sent as header
    private static SessionFilter sessionFilter; //alternative to using sessionID for session based authentication

    @BeforeTest
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.useRelaxedHTTPSValidation();    //handles HTTPS certificate validation

        //This sessionFilter object remembers the response of new Session created
        //and can be used as filter in all the subsequent requests for authentication.
        //This object must be passed in filter() method of given() before when()
        //This filter() method is an alternative to using sessionID and extracting and parsing the response manually and sending as header.
        sessionFilter = new SessionFilter();

        // 1. Login to Jira to Create Session using Login API
        String responseSessionJira = createSession();
        JsonPath parsedJIRASessionResponse = ParseUtils.rawToJson(responseSessionJira);
        sessionID = parsedJIRASessionResponse.getString("session.value");
        System.out.println("Session ID: " + sessionID);
    }

    @Test
    public void test_Jira() {
        //Create an Issue
        String responseIssueCreated = createIssue("test issue for jira api");
        JsonPath parsedResponseIssueCreated = ParseUtils.rawToJson(responseIssueCreated);
        String issueID = parsedResponseIssueCreated.getString("id");
        String issueKey = parsedResponseIssueCreated.getString("key");
        System.out.println("Issue ID: " + issueID);
        System.out.println("Issue Key: " + issueKey);

        //2. Add a comment to existing Issue using Add comment API
        String comment = "This is my first comment";
        String responseCommentAdded = addComment(comment, issueID);
        JsonPath parsedResponseCommentAdded = ParseUtils.rawToJson(responseCommentAdded);
        String commentID = parsedResponseCommentAdded.getString("id");
        String commentBody = parsedResponseCommentAdded.getString("body");
        System.out.println("Comment ID: " + commentID);
        System.out.println("Comment Body: " + commentBody);

        //Please do below mandatory setting and make attachments ON
        //From Jira page, on top right, Select Settings Icon > System > Attachments (under Advanced): Set Allow Attachments to ON
        //3. Add an attachment to existing Issue using Add Attachment API
        String attachmentPath = System.getProperty("user.dir") + "/src/test/java/practice/files/jira.txt";
        String responseAttachmentAdded = addAttachment(issueID, attachmentPath);

        //4. Get Issue details and verify if added comment and attachment exists using Get issue API
        String responseIssue = getIssue(issueID);
        JsonPath parsedResponseIssue = ParseUtils.rawToJson(responseIssue);
        int commentsCount = parsedResponseIssue.getInt("fields.comment.comments.size()");
        for (int i = 0; i < commentsCount; i++) {
            String commentIssueID = parsedResponseIssue.get("fields.comment.comments[" + i + "].id").toString();
            if (commentIssueID.equals(commentID)) {
                String actualComment = parsedResponseIssue.get("fields.comment.comments[" + i + "].body").toString();
                System.out.println("Actual Comment: " + actualComment);
                Assert.assertEquals(actualComment, comment);
            }
        }



    }

    public static String createIssue(String issueSummary) {
        //https://docs.atlassian.com/software/jira/docs/api/REST/7.6.1/#api/2/issue-createIssue
        String response = given()
                            //.log().all()
                            .header("content-Type", "application/json")
                            //.header("Cookie", "JSESSIONID="+sessionID)
                            .filter(sessionFilter)
                            .body(Payload.createIssueJIRA(PROJECT_KEY, issueSummary))
                        .when()
                            .post("rest/api/2/issue")
                        .then()
                            .statusCode(201)
                            .extract().response().asString()
                            ;
        return response;
    }

    public static String createSession() {
        //https://developer.atlassian.com/server/jira/platform/cookie-based-authentication/
        String response = given()
                            .header("content-Type", "application/json")
                            .body(Payload.createSessionJIRA(USERNAME_JIRA, PASSWORD_JIRA))
                            .filter(sessionFilter)
                        .when()
                            .post("rest/auth/1/session")
                        .then()
                            .statusCode(200)
                            .extract().response().asString()
                            ;
        return response;
    }

    public static String addComment(String comment, String issueID) {
        //https://docs.atlassian.com/software/jira/docs/api/REST/7.6.1/#api/2/issue-addComment
        String response = given()
                            .pathParam("issueID", issueID)
                            .header("content-Type", "application/json")
                            //.header("Cookie", "JSESSIONID="+sessionID)
                            .filter(sessionFilter)
                            .body(Payload.addCommentJIRA(comment))
                        .when()
                            .post("rest/api/2/issue/{issueID}/comment")
                        .then()
                            .statusCode(201)
                            .extract().response().asString()
                        ;
        return response;
    }

    public static String addAttachment(String issueID, String attachmentPath) {
        //https://docs.atlassian.com/software/jira/docs/api/REST/7.6.1/#api/2/issue/{issueIdOrKey}/attachments-addAttachment
        String response = given()
                            .pathParam("issueID", issueID)
                            .header("X-Atlassian-Token", "no-check")
                            .header("Content-Type", "multipart/form-data")
                            .multiPart("file", new File(attachmentPath))
                            .filter(sessionFilter)
                        .when()
                            .post("rest/api/2/issue/{issueID}/attachments")
                        .then()
                            .statusCode(200)
                            .extract().response().asString();
        return response;
    }

    public static String getIssue(String issueID) {
        //https://docs.atlassian.com/software/jira/docs/api/REST/7.6.1/#api/2/issue-getIssue
        String response = given()
                            .filter(sessionFilter)
                            .pathParam("issueID", issueID)
                            .queryParam("fields", "comment")
                        .when()
                            .get("rest/api/2/issue/{issueID}")
                        .then()
                            .log().all()
                            .statusCode(200)
                            .extract().response().asString();
        return response;
    }


}
