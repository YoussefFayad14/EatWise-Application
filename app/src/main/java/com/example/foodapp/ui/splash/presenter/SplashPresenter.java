package com.example.foodapp.ui.splash.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.foodapp.ui.splash.SplashContract;
import com.google.firebase.auth.FirebaseAuth;

public class SplashPresenter implements SplashContract.Presenter {
    private final SplashContract.View view;
    private final Context context;

    public SplashPresenter(SplashContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void checkUserLogin() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);

        if (isLoggedIn && FirebaseAuth.getInstance().getCurrentUser() != null) {
            view.navigateToMain();
        } else {
            view.navigateToLogin();
        }
    }
}
