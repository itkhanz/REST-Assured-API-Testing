package com.itkhan.framework.spotify.api;

import com.itkhan.framework.spotify.utils.ConfigLoader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.time.Instant;
import java.util.HashMap;

import static com.itkhan.framework.spotify.api.SpecBuilder.getResponseSpec;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.responseSpecification;

/* Retrieves the access_token and renew it if it is expired */
public class TokenManager {

    public static String access_token;
    public static Instant expiry_time;  //Duration for which access_token is valid

    /**
     * This method retrieves the access_token if it is already generated and not expired.
     * If the token is null or expired, then it makes call to fetch new access_token and return the updated access_token
     * @return access_token
     */
    public static String getToken() {
        try {
            if (access_token == null || Instant.now().isAfter(expiry_time)) {
                System.out.println("Renewing token.....");
                Response response = renewToken();
                access_token = response.path("access_token");
                int expiryDurationInSeconds = response.path("expires_in");
                expiry_time = Instant.now().plusSeconds(expiryDurationInSeconds - 300); //add a buffer of 300 seconds before actual expiry duration
            } else {
                System.out.println("Token is good to use");
            }
        } catch (Exception e) {
            throw new RuntimeException("ABORT!!! Failed to get token");
        }
        return access_token;
    }

    /**
     * This method makes a POST call to renew the access_token for Spotify API
     * It needs the client_id and client_secret to make this request as URL Encoded form parameters
     * https://developer.spotify.com/documentation/web-api/tutorials/code-flow
     * @return Response from call to renew token
     */
    private static Response renewToken() {
        HashMap<String, String> formParams = new HashMap<String, String>();
        formParams.put("client_id", ConfigLoader.getInstance().getClientId());
        formParams.put("client_secret", ConfigLoader.getInstance().getClientSecret());
        formParams.put("refresh_token", ConfigLoader.getInstance().getRefreshToken());
        formParams.put("grant_type", ConfigLoader.getInstance().getGrantType());

        Response response = RestResource.postAccount(formParams);

        if (response.statusCode() != 200) {
            throw new RuntimeException("ABORT!!! Renew Access Token failed");
        }
        return response;
    }
}
