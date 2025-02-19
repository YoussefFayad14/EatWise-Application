package com.example.foodapp.ui.favorite.view;


import com.example.foodapp.data.local.model.FavoriteMeal;
import java.util.List;

public interface FavoriteView {
    void showFavoriteMeals(List<FavoriteMeal> favoriteMeals);
}
