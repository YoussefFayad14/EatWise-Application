package com.example.foodapp.data.model;

import com.google.gson.annotations.SerializedName;

public class Area {
    @SerializedName("strArea")
    private String name;

    public Area(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}

