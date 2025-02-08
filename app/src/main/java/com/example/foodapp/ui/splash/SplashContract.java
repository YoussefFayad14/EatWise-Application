package com.example.foodapp.ui.splash;

public interface SplashContract {
    interface View {
        void navigateToMain();
        void navigateToLogin();
    }

    interface Presenter {
        void checkUserLogin();
    }
}
