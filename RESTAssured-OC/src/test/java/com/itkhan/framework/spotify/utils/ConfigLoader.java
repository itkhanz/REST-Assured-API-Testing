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
        properties = PropertyUtils.propertyLoader("src/test/resources/config.properties");
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
        return Objects.requireNonNull(properties.getProperty("client_id"), "property client_id is not specified in the config.properties file");
    }

    public String getClientSecret(){
        return Objects.requireNonNull(properties.getProperty("client_secret"), "property client_secret is not specified in the config.properties file");
    }

    public String getGrantType(){
        return Objects.requireNonNull(properties.getProperty("grant_type"), "property grant_type is not specified in the config.properties file");
    }

    public String getRefreshToken(){
        return Objects.requireNonNull(properties.getProperty("refresh_token"), "property refresh_token is not specified in the config.properties file");
    }

    public String getUser(){
        return Objects.requireNonNull(properties.getProperty("user_id"), "property user_id is not specified in the config.properties file");
    }
}
