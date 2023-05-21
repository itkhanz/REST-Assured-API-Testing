package com.itkhan.framework.spotify.utils;

import java.util.Objects;
import java.util.Properties;

/**
 * This method is based on Singleton design pattern and creates object of this class only once.
 * It also ensures that the properties are initialized only once during the execution of the program.
 */
public class ConfigLoader {
    private final Properties properties;
    private static ConfigLoader configLoader;

    private ConfigLoader(){
        //loads the properties from config.properties that has all the secret Keys
        //Since config.properties is not pushed to GitHub so jenkins cannot access it
        //so we need a workaround to pass the secret properties as system properties in jenkins
        if (Objects.equals(System.getProperty("agent"), "localhost")) {
            properties = PropertyUtils.propertyLoader("src/test/resources/config.properties");
        }else {
            properties = PropertyUtils.propertyLoader("src/test/resources/public.properties");
        }
    }

    public static ConfigLoader getInstance(){
        if(configLoader == null){
            configLoader = new ConfigLoader();
        }
        return configLoader;
    }

    public String getClientId(){
        /*String prop = properties.getProperty("client_id");
        if(prop != null) return prop;
        else throw new RuntimeException("property client_id is not specified in the config.properties file");*/

        //Better optimized way
        String clientID = System.getProperty("client_id") != null ?
                System.getProperty("client_id")
                :
                Objects.requireNonNull(properties.getProperty("client_id"), "property client_id is not specified in the config.properties file");
        return clientID;
    }

    public String getClientSecret(){
        String clientSecret = System.getProperty("client_secret") != null ?
                System.getProperty("client_secret")
                :
                Objects.requireNonNull(properties.getProperty("client_secret"), "property client_secret is not specified in the config.properties file");
        return clientSecret;
    }

    public String getGrantType(){
        String grantType = System.getProperty("grant_type") != null ?
                System.getProperty("grant_type")
                :
                Objects.requireNonNull(properties.getProperty("grant_type"), "property grant_type is not specified in the config.properties file");
        return grantType;
    }

    public String getRefreshToken(){
        String refreshToken = System.getProperty("refresh_token") != null ?
                System.getProperty("refresh_token")
                :
                Objects.requireNonNull(properties.getProperty("refresh_token"), "property refresh_token is not specified in the config.properties file");
        return refreshToken;
    }

    public String getUser(){
        String userID = System.getProperty("user_id") != null ?
                System.getProperty("user_id")
                :
                Objects.requireNonNull(properties.getProperty("user_id"), "property user_id is not specified in the config.properties file");
        return userID;
    }
}
