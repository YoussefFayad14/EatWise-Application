package com.example.foodapp.ui.weekplan.view;

import com.example.foodapp.data.local.model.MealPlan;
import com.example.foodapp.data.remote.model.Meal;

public interface OnMealClickListener {
    void onMealRemove(MealPlan mealPlan);
    void onMealClick(MealPlan mealPlan);
    void onMealLoaded(Meal meal);

}
