package com.example.foodapp.data.model;

import com.google.gson.annotations.SerializedName;

public class Meal {
    @SerializedName("idMeal")
    private String idMeal;

    @SerializedName("strMeal")
    private String mealName;

    @SerializedName("strCategory")
    private String category;

    @SerializedName("strArea")
    private String area;

    @SerializedName("strInstructions")
    private String instructions;

    @SerializedName("strMealThumb")
    private String mealThumb;

    @SerializedName("strYoutube")
    private String youtubeLink;

    public String getIdMeal() { return idMeal; }
    public String getMealName() { return mealName; }
    public String getCategory() { return category; }
    public String getArea() { return area; }
    public String getInstructions() { return instructions; }
    public String getMealThumb() { return mealThumb; }
    public String getYoutubeLink() { return youtubeLink; }
}
