package com.example.foodapp.ui.meal.view;

import com.example.foodapp.data.local.model.FavoriteMeal;

import java.util.List;
public interface MealDetailsView {
        void showMealDetails(String name, String category, String country, String instructions, List<String> ingredients, String measures, String youtubeLink, String mealThumb);

        void playMealVideo(String videoUrl);

        void showMessage(String message, boolean isError);

        void handleBackNavigation();
}
