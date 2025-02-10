package com.example.foodapp.ui.home.view;

import com.example.foodapp.data.model.Area;
import com.example.foodapp.data.model.CategoryItem;
import com.example.foodapp.data.model.Ingredient;
import com.example.foodapp.data.model.Meal;

import java.util.List;

public interface HomeView {
    void showPopularMeals(List<Meal> popularMeals, String country);
    void showCategories(List<CategoryItem> categories);
    void showCountries(List<Area> countries);
    void showIngredients(List<Ingredient> ingredients);
    void showRandomMeal(Meal meal);
    void showMealDetails(Meal meal);
}
