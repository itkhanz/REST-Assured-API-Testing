package com.itkhan.framework.spotify.utils;

import com.github.javafaker.Faker;

/*Generates fake test date for Playlist Name and Description based on provided regex of permitted characters and length */
public class FakerUtils {
    public static String generateName(){
        Faker faker = new Faker();
        return "Playlist " + faker.regexify("[A-Za-z0-9 ,_-]{10}");
    }

    public static String generateDescription(){
        Faker faker = new Faker();
        return "Description " + faker.regexify("[ A-Za-z0-9_@./#&+-]{50}");
    }
}
