package com.example.sibusiso_javapracticaltest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sibuiso
 */

public class PokeType {

    @SerializedName("name")
    private String name;

    public PokeType(String name) {
        this.name = name;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
