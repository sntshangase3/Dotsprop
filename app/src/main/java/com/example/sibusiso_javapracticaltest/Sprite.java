package com.example.sibusiso_javapracticaltest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sibusiso
 */

public class Sprite {

    @SerializedName("name")
    private String name;

    @SerializedName("resource_uri")
    private String resourceUri;

    public Sprite(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceUri() {
        return resourceUri.substring(1);
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }
}
