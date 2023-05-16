package com.itkhan.practice.codingChallenges;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class Exercises {

    public static List<String> allKeysList = new ArrayList<String>();
    public static List<Object> allValuesList = new ArrayList<Object>();

    /**
     * Given below JSON array, fetch all nested keys and values.
     *
     * Store the keys in an array list.
     * Store the values in an array list.
     *
     * Final output should be,
     * keys = [k, k10, k11, k121, k120, k12, k14, k1, k221, k22]
     * values = [[1, 3, 5], 4, [4, 7, 9], v121, 6, v122]
     * This should work even if the number of nested objects vary.
     * @throws JsonProcessingException
     */
    @Test
    public void exe3_fetch_nested_keys_values() throws IOException {

        File jsonFile = new File("src\\test\\java\\com\\itkhan\\practice\\codingChallenges\\assignment3.json");

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Map<String, Object>> jsonArr = objectMapper.readValue(jsonFile,
                new TypeReference<ArrayList<Map<String, Object>>>(){}
        );

        for (Map<String, Object> jsonMap : jsonArr) {
            iterate_over(jsonMap);
        }

        System.out.println(allKeysList.toString()); //[k, k1, k10, k11, k12, k120, k121, k14, k22, k221]
        System.out.println(allValuesList.toString()); //[[1, 3, 5], 4, [4, 7, 9], v121, 6, v122]
    }

    public void iterate_over(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            //System.out.println("Key is: " + entry.getKey());
            allKeysList.add(entry.getKey());
            if (entry.getValue() instanceof Map) {
                //System.out.println("Map found, digging further");
                iterate_over((Map<String, Object>) entry.getValue());
            } else {
                //System.out.println("Leaf found, value is: " + entry.getValue());
                allValuesList.add(entry.getValue());
            }
        }
    }

    @Test
    public void exe4() {
        File jsonFile = new File("src\\test\\java\\com\\itkhan\\practice\\codingChallenges\\assignment4.json");

        JsonPath jsonPath = new JsonPath(jsonFile);
        List<String> namesList = jsonPath.get("names.language.name");
        System.out.println(namesList.toString());   //[ja-Hrkt, ko, fr, de, es, it, en]

        List<String> urlList = jsonPath.get("names.language.url");
        System.out.println(urlList.toString()); //[https://pokeapi.co/api/v2/language/1/, https://pokeapi.co/api/v2/language/3/, https://pokeapi.co/api/v2/language/5/, https://pokeapi.co/api/v2/language/6/, https://pokeapi.co/api/v2/language/7/, https://pokeapi.co/api/v2/language/8/, https://pokeapi.co/api/v2/language/9/]

        List<String> langNamesList = jsonPath.get("names.name");
        System.out.println(langNamesList.toString());   //[金, 골드, Or, Gold, Oro, Oro, Gold]

        //Alternatively you can find it using groovy
        /*import groovy.json.JsonSlurper

        def object = new JsonSlurper().parseText(
                '''
        {INSERT_YOUR_JSON_HERE}
        '''
        )

        println(object.names.language.findAll{it.name}.name)
        //[ja-Hrkt, ko, fr, de, es, it, en]

        println(object.names.language.findAll{it.url}.url)
        //[https://pokeapi.co/api/v2/language/1/, https://pokeapi.co/api/v2/language/3/, https://pokeapi.co/api/v2/language/5/, https://pokeapi.co/api/v2/language/6/, https://pokeapi.co/api/v2/language/7/, https://pokeapi.co/api/v2/language/8/, https://pokeapi.co/api/v2/language/9/]

        println(object.names.name)
        //[金, 골드, Or, Gold, Oro, Oro, Gold]*/

    }



}
