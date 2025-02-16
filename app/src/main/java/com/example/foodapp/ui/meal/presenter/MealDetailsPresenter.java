package com.example.foodapp.ui.meal.presenter;

import android.annotation.SuppressLint;

import com.example.foodapp.data.local.model.FavoriteMeal;
import com.example.foodapp.data.remote.model.Meal;
import com.example.foodapp.data.repository.FavoriteMealRepository;
import com.example.foodapp.ui.favorite.presenter.FavoritePresenter;
import com.example.foodapp.ui.meal.MealDetailsContract;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {
    private final MealDetailsContract.View view;
    private final Meal meal;
    private final FavoritePresenter favoritePresenter;

    public MealDetailsPresenter(MealDetailsContract.View view, Meal meal, FavoriteMealRepository favoriteMealRepository) {
        this.view = view;
        this.meal = meal;
        favoritePresenter = new FavoritePresenter(null, favoriteMealRepository);
    }

    @Override
    public void loadMealDetails() {
        if (meal != null) {
            meal.addIngredientsAndMeasures();
            StringBuilder measuresBuilder = new StringBuilder();
            for (String measure : meal.getMeasures()) {
                if (measure != null && !measure.isEmpty()) {
                    measuresBuilder.append(measure).append("\n");
                }
            }

            view.showMealDetails(
                    meal.getMealName(),
                    meal.getCategory(),
                    meal.getArea(),
                    meal.getInstructions(),
                    meal.getIngredients(),
                    measuresBuilder.toString(),
                    meal.getYoutubeLink(),
                    meal.getMealThumb()
            );
        } else {
            view.showMessage("Meal details not available", true);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void addToFavorites(FavoriteMeal meal) {
        favoritePresenter.addToFavorites(meal);
        view.showMessage("Meal added to favorites", false);
    }

    @SuppressLint("CheckResult")
    @Override
    public void removeFromFavorites(FavoriteMeal meal) {
        favoritePresenter.removeFromFavorites(meal);
        view.showMessage("Meal removed from favorites", false);
    }

    @Override
    public void onBackPressed() {
        view.handleBackNavigation();
    }
}
