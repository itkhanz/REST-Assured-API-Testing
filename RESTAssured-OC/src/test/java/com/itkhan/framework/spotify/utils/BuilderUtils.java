package com.itkhan.framework.spotify.utils;

import com.itkhan.framework.spotify.pojo.Playlist;

public class BuilderUtils {
    /**
     * Creates the Playlist Object for Playlist POJO for serialization of API Payload
     * @param name name of playlist
     * @param description description of playlist
     * @param _public privacy status
     * @return Playlist Object
     */
    public static Playlist playlistBuilder(String name, String description, boolean _public) {
        //With lombok Builder pattern
        return Playlist.builder()
                .name(name)
                .description(description)
                ._public(_public)
                .build();

        //Without Builder pattern
        /*Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setDescription(description);
        playlist.set_public(_public);
        return  playlist;*/

        //With normal Builder pattern
        /*return new Playlist()
                .setName(name)
                .setDescription(description)
                .setPublic(_public)
                ;*/
    }
}
