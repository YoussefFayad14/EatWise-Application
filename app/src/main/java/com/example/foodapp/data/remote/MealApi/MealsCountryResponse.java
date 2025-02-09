package com.example.foodapp.data.remote.MealApi;

import com.example.foodapp.data.model.CountryMeal;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MealsCountryResponse {
    @SerializedName("meals")
    private List<CountryMeal> meals;

    public List<CountryMeal> getMeals() {
        return meals;
    }
}
