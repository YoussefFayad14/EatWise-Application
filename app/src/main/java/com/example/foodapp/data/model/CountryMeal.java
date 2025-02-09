package com.example.foodapp.data.model;

import com.google.gson.annotations.SerializedName;

public class CountryMeal {
    @SerializedName("strMeal")
    private String mealName;

    @SerializedName("strMealThumb")
    private String mealThumb;

    @SerializedName("idMeal")
    private String mealId;

    public String getMealName() {
        return mealName;
    }

    public String getMealThumb() {
        return mealThumb;
    }

    public String getMealId() {
        return mealId;
    }
}
