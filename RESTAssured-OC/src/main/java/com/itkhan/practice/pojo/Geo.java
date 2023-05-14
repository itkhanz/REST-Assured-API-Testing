package com.itkhan.practice.pojo;

public class Geo {
    private String lat;
    private String lng;

    public Geo() {

    }

    public Geo(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }
    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

}
