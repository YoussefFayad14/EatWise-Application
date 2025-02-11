package com.example.foodapp.ui.favorite.presenter;

import androidx.lifecycle.LiveData;
import com.example.foodapp.data.local.FavoriteMeal;
import com.example.foodapp.data.remote.model.Meal;
import com.example.foodapp.data.repository.FavoriteMealRepository;
import com.example.foodapp.ui.favorite.view.FavoriteView;
import java.util.List;

public class FavoritePresenter {
    private FavoriteView view;
    private FavoriteMealRepository repository;
    public FavoritePresenter(FavoriteView view, FavoriteMealRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    public LiveData<List<FavoriteMeal>> getFavoriteProducts() {
        return repository.getAllFavoriteMeals();
    }
    public void removeFromFavorites(FavoriteMeal meal) {
        repository.delete(meal);
    }

    public Meal fromFavoriteMeal(FavoriteMeal favoriteMeal) {
        Meal meal = new Meal();
        meal.setIdMeal(favoriteMeal.getMealId());
        meal.setMealName(favoriteMeal.getMealName());
        meal.setCategory(favoriteMeal.getMealCategory());
        meal.setArea(favoriteMeal.getMealArea());
        meal.setMealThumb(favoriteMeal.getMealImage());
        meal.setInstructions(favoriteMeal.getInstructions());
        meal.setYoutubeLink(favoriteMeal.getYoutubeLink());
        meal.getIngredients().addAll(favoriteMeal.getIngredients());
        meal.getMeasures().addAll(favoriteMeal.getMeasures());
        return meal;
    }


}
