package com.example.foodapp.ui.home.view;


import com.example.foodapp.data.model.Meal;

public interface onClickListener {
    <T> void onSectionClick(T item);
    void onMealClick(Meal meal);
}
