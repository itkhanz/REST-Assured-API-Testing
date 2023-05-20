package com.itkhan.framework.spotify.utils;

import java.util.Objects;
import java.util.Properties;

/*Singleton class for loading the data properties for API tests */
public class DataLoader {
    private final Properties properties;
    private static DataLoader dataLoader;

    private DataLoader(){
        properties = PropertyUtils.propertyLoader("src/test/resources/data.properties");
    }

    public static DataLoader getInstance(){
        if(dataLoader == null){
            dataLoader = new DataLoader();
        }
        return dataLoader;
    }

    public String getGetPlaylistId(){
        return Objects.requireNonNull(properties.getProperty("get_playlist_id"), "property get_playlist_id is not specified in the data.properties file");
    }

    public String getUpdatePlaylistId(){
        return Objects.requireNonNull(properties.getProperty("update_playlist_id"), "property update_playlist_id is not specified in the data.properties file");
    }
}
