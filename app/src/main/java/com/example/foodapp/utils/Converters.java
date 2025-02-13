package com.example.foodapp.utils;

import androidx.room.TypeConverter;

import com.example.foodapp.data.local.model.FavoriteMeal;
import com.example.foodapp.data.remote.model.Meal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static String fromList(List<String> list) {
        return list != null ? gson.toJson(list) : null;
    }

    @TypeConverter
    public static List<String> toList(String data) {
        if (data == null) return null;
        Type listType = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    public Meal fromFavoriteMeal(FavoriteMeal favoriteMeal) {
        Meal meal = new Meal();
        meal.setIdMeal(favoriteMeal.getMealId());
        meal.setMealName(favoriteMeal.getMealName());
        meal.setCategory(favoriteMeal.getMealCategory());
        meal.setArea(favoriteMeal.getMealArea());
        meal.setMealThumb(favoriteMeal.getMealImage());
        meal.setInstructions(favoriteMeal.getInstructions());
        meal.setYoutubeLink(favoriteMeal.getYoutubeLink());
        meal.getIngredients().addAll(favoriteMeal.getIngredients());
        meal.getMeasures().addAll(favoriteMeal.getMeasures());
        return meal;
    }
}
