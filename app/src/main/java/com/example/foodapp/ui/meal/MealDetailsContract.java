package com.example.foodapp.ui.meal;

import java.util.List;
public interface MealDetailsContract {
    interface View {
        void showMealDetails(String name, String category, String country, String instructions, List<String> ingredients,String measures ,String youtubeLink, String mealThumb);
        void playMealVideo(String videoUrl);
        void showError(String message);
        void handleBackNavigation();
    }

    interface Presenter {
        void loadMealDetails();
        void onBackPressed();
    }
}
