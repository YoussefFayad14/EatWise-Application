package com.example.foodapp.ui.home.view;


import com.example.foodapp.data.remote.model.Meal;

public interface onClickListener {
    void onSectionClick(String item,String sectionType);
    void onMealClick(Meal meal);
}
