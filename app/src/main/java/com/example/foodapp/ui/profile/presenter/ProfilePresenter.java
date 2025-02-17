package com.example.foodapp.ui.profile.presenter;

import android.content.Context;

import com.example.foodapp.ui.profile.view.ProfileView;
import com.example.foodapp.utils.UserPreferences;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

public class ProfilePresenter {
    private ProfileView view;
    private UserPreferences userPreferences;
    private GoogleSignInClient googleSignInClient;

    public ProfilePresenter(Context context,GoogleSignInClient googleSignInClient, ProfileView view) {
        this.view = view;
        this.googleSignInClient = googleSignInClient;
        userPreferences = new UserPreferences(context);
    }

    public void loadUserData() {
        String username = userPreferences.getUsername();
        String email = userPreferences.getEmail();
        view.displayUserData(username, email);
    }

    public void logOut() {
        userPreferences.logout();
        FirebaseAuth.getInstance().signOut();
        googleSignInClient.revokeAccess().addOnCompleteListener(task -> view.navigateToLogin());
    }

}
