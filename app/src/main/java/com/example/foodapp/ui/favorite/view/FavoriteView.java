package com.example.foodapp.ui.favorite.view;

import com.example.foodapp.data.local.FavoriteMeal;
import java.util.List;

public interface FavoriteView {
    void onMealAdded();
    void onMealRemoved();
    void onMealIsFavorite(FavoriteMeal meal);
    void onMealIsNotFavorite();
    void showFavoriteMeals(List<FavoriteMeal> meals);
    void onError(String message);
}
