package com.example.foodapp.ui.meal.presenter;

import com.example.foodapp.data.model.Meal;
import com.example.foodapp.ui.meal.MealDetailsContract;
import java.util.List;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {
    private final MealDetailsContract.View view;
    private final Meal meal;

    public MealDetailsPresenter(MealDetailsContract.View view, Meal meal) {
        this.view = view;
        this.meal = meal;
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
            view.showError("Meal details not available");
        }
    }

    @Override
    public void onBackPressed() {
        view.handleBackNavigation();
    }
}
