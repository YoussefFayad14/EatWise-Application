package com.example.foodapp.ui.login.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Patterns;

import com.example.foodapp.ui.login.LoginContract;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginPresenter implements LoginContract.Presenter {
    private final LoginContract.View view;
    private final FirebaseAuth auth;
    private final SharedPreferences sharedPreferences;

    public LoginPresenter(LoginContract.View view, Context context) {
        this.view = view;
        this.auth = FirebaseAuth.getInstance();
        this.sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
    }

    @Override
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
                        saveUserLoginState(user.getUid());
                        view.navigateToMain();
                    } else {
                        view.showErrorMessage("Email not verified. Please check your email.");
                    }
                })
                .addOnFailureListener(e -> view.showErrorMessage("Login failed: " + e.getMessage()));
    }

    @Override
    public void loginWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    saveUserLoginState(user.getUid());
                    view.navigateToMain();
                }
            } else {
                view.showErrorMessage("Google login failed: " + authTask.getException().getMessage());
            }
        });
    }

    @Override
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

    private void saveUserLoginState(String uid) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USER_UID", uid);
        editor.putBoolean("IS_LOGGED_IN", true);
        editor.apply();
    }
}