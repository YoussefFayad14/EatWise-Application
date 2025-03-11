package com.example.foodapp.ui.meal.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.foodapp.R;
import com.example.foodapp.data.local.model.FavoriteMeal;
import com.example.foodapp.data.remote.model.Meal;
import com.example.foodapp.data.repository.FavoriteMealsRepository;
import com.example.foodapp.ui.favorite.presenter.FavoritePresenter;
import com.example.foodapp.ui.meal.view.MealDetailsView;
import com.example.foodapp.utils.UserPreferences;

public class MealDetailsPresenter{
    private final Context context;
    private final MealDetailsView  view;
    private final Meal meal;
    private final UserPreferences userPreferences;
    private final FavoritePresenter favoritePresenter;

    public MealDetailsPresenter(Context context, MealDetailsView view, Meal meal, FavoriteMealsRepository favoriteMealsRepository) {
        this.context = context;
        this.view = view;
        this.meal = meal;
        favoritePresenter = new FavoritePresenter(null, favoriteMealsRepository);
        userPreferences = new UserPreferences(context);
    }

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
    public void addToFavorites(FavoriteMeal meal) {
        if(userPreferences.isGuest()){
            view.showMessage(context.getString(R.string.login_as_user_to_add_to_favorites), true);
        }
        else {
            favoritePresenter.addToFavorites(meal);
            view.showMessage(context.getString(R.string.meal_added_to_favorites), false);
        }
    }


    public void onBackPressed() {
        view.handleBackNavigation();
    }
}
