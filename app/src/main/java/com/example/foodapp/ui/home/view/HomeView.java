package com.example.foodapp.ui.home.view;

import com.example.foodapp.data.remote.model.Area;
import com.example.foodapp.data.remote.model.CategoryItem;
import com.example.foodapp.data.remote.model.Ingredient;
import com.example.foodapp.data.remote.model.Meal;

import java.util.List;

public interface HomeView {
    void showPopularMeals(List<Meal> popularMeals, String country);
    void showCategories(List<CategoryItem> categories);
    void showCountries(List<Area> countries);
    void showIngredients(List<Ingredient> ingredients);
    void showRandomMeal(Meal meal);
    void showMealDetails(Meal meal);
}
