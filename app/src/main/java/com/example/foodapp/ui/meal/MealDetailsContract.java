package com.example.foodapp.ui.meal;

import com.example.foodapp.data.local.FavoriteMeal;

import java.util.List;
public interface MealDetailsContract {
    interface View {
        void showMealDetails(String name, String category, String country, String instructions, List<String> ingredients,String measures ,String youtubeLink, String mealThumb);
        void playMealVideo(String videoUrl);
        void showMessage(String message, boolean isError);
        void handleBackNavigation();
    }

    interface Presenter {
        void loadMealDetails();

        void addToFavorites(FavoriteMeal meal);
        void removeFromFavorites(FavoriteMeal meal);

        void onBackPressed();
    }
}
