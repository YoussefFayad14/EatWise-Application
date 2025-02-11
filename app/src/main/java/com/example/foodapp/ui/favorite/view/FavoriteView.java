package com.example.foodapp.ui.favorite.view;

import androidx.lifecycle.LiveData;

import com.example.foodapp.data.local.FavoriteMeal;
import java.util.List;

public interface FavoriteView {
    void showFavoriteMeals(LiveData<List<FavoriteMeal>> favoriteMeals);
}
