package com.example.foodapp.ui.login.presenter;

import android.content.Context;
import android.util.Patterns;

import com.example.foodapp.data.repository.FavoriteMealRepository;
import com.example.foodapp.data.repository.WeekPlanRepository;
import com.example.foodapp.ui.login.view.LoginFragment;
import com.example.foodapp.ui.login.view.LoginView;
import com.example.foodapp.utils.DataSyncUtil;
import com.example.foodapp.utils.UserPreferences;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginPresenter{
    private final LoginView view;
    private final FirebaseAuth auth;
    private final UserPreferences userPreferences;
    private DataSyncUtil dataSyncUtil;

    public LoginPresenter(LoginFragment view, Context context, FavoriteMealRepository favoriteMealRepository, WeekPlanRepository weekPlanRepository) {
        this.view = view;
        this.auth = FirebaseAuth.getInstance();
        this.userPreferences = new UserPreferences(context);
        this.dataSyncUtil = new DataSyncUtil(context,favoriteMealRepository, weekPlanRepository);
    }

    public void loginWithEmail(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            view.showErrorMessage("Enter email and password.");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showErrorMessage("Invalid email format.");
            return;
        }
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null && user.isEmailVerified()) {
                        userPreferences.saveUserLogin(user.getDisplayName(), user.getEmail());
                        dataSyncUtil.syncUserData();
                        view.navigateToMain();
                    } else {
                        view.showErrorMessage("Email not verified. Please check your email.");
                    }
                })
                .addOnFailureListener(e -> view.showErrorMessage("Login failed: " + e.getMessage()));
    }

    public void loginWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    userPreferences.saveUserLogin(user.getDisplayName(), user.getEmail());
                    dataSyncUtil.syncUserData();
                    view.navigateToMain();
                }
            } else {
                view.showErrorMessage("Google login failed: " + authTask.getException().getMessage());
            }
        });
    }

    public void resetPassword(String email) {
        if (email.isEmpty()) {
            view.showErrorMessage("Enter your email to reset password");
            return;
        }
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(aVoid -> {
                    view.showErrorMessage("Reset link sent to your email");
                    view.clearFields();
                })
                .addOnFailureListener(e -> view.showErrorMessage("Failed to send reset email: " + e.getMessage()));
    }
    public void loginAsGuest() {
        userPreferences.saveGuestMode();
        view.navigateToMain();
    }
}