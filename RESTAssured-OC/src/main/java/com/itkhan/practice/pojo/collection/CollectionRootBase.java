package com.itkhan.practice.pojo.collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class CollectionRootBase {
    public CollectionRootBase() {
    }

}
