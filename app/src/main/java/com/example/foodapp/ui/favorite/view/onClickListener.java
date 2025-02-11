package com.example.foodapp.ui.favorite.view;

import com.example.foodapp.data.local.FavoriteMeal;

public interface onClickListener {
    void onMealClick(FavoriteMeal meal);
    void AddToPlanClick(FavoriteMeal meal);
    void RemoveFromFavouritesClick(FavoriteMeal meal);
}
