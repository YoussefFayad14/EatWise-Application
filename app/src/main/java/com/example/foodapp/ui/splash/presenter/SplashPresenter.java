package com.example.foodapp.ui.splash.presenter;

import android.content.Context;

import com.example.foodapp.ui.splash.view.SplashView;
import com.example.foodapp.utils.UserPreferences;
import com.google.firebase.auth.FirebaseAuth;

public class SplashPresenter{
    private final SplashView view;
    private final UserPreferences userPreferences;

    private final Context context;

    public SplashPresenter(SplashView view, Context context) {
        this.view = view;
        this.context = context;
        userPreferences = new UserPreferences(context);
    }

    public void checkUserLogin() {
        if (userPreferences.isLoggedIn() && FirebaseAuth.getInstance().getCurrentUser() != null) {
            view.navigateToMain();
        } else if (userPreferences.isGuest()) {
            view.navigateToMain();
        } else {
            view.navigateToLogin();
        }
    }
}
