package com.itkhan.practice.pojo.collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestResponse extends RequestBase{
    private URL url;

    public RequestResponse() {

    }

    public RequestResponse(URL url, String method, List<Header> header, Body body, String description) {
        super(method, header, body, description);
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

}
