package com.example.foodapp.ui.home.view;


import com.example.foodapp.data.model.CategoryItem;
import com.example.foodapp.data.model.CountryMeal;

public interface onClickListener {
    <T> void onSectionClick(T item);
    <T> void onItemClick(T meal);

}
