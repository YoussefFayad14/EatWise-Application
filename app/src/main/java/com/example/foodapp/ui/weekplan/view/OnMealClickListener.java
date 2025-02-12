package com.example.foodapp.ui.weekplan.view;

import com.example.foodapp.data.local.weekplandb.MealPlan;

public interface OnMealClickListener {
    void onMealRemove(MealPlan mealPlan);
    void onMealClick(MealPlan mealPlan);
}
