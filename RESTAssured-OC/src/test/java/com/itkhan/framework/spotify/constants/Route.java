package com.itkhan.framework.spotify.constants;

/**
 * Stores the end points or Paths for different Spotify API Calls as constants
 * These are re-used in RequestSpecifications, and RestResource and Playlist API calls
 */
public class Route {
    public static final String BASE_URI = "https://api.spotify.com";
    public static final String ACCOUNTS_BASE_URI = "https://accounts.spotify.com";
    public static final String BASE_PATH = "/v1";
    public static final String API = "/api";
    public static final String TOKEN = "/token";
    public static final String USERS = "/users";
    public static final String PLAYLISTS = "/playlists";
}
