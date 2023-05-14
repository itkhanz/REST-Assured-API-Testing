package com.itkhan.practice.pojo;

import java.util.HashMap;

public class Workspace {
    private String id;
    private HashMap<String, String> myHashMap;
    private String name;
    private String type;
    private String description;

    public Workspace(){

    }

    public Workspace(String name, String type, String description){
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public HashMap<String, String> getMyHashMap() {
        return myHashMap;
    }

    public void setMyHashMap(HashMap<String, String> myHashMap) {
        this.myHashMap = myHashMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
