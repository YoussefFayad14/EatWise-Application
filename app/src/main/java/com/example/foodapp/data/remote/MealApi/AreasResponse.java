package com.example.foodapp.data.remote.MealApi;

import com.example.foodapp.data.remote.model.Area;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AreasResponse {
    @SerializedName("meals")
    private List<Area> areas;

    public List<Area> getAreas() {
        return areas;
    }
}

