package com.example.foodapp.data.remote.MealApi;

import com.example.foodapp.data.model.Ingredient;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class IngredientResponse {
    @SerializedName("meals")
    private List<Ingredient> ingredients;

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
