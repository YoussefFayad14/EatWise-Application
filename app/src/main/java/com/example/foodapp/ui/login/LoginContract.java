package com.example.foodapp.ui.login;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface LoginContract {
    interface View {
        void showErrorMessage(String message);
        void navigateToMain();
        void clearFields();
    }

    interface Presenter {
        void loginWithEmail(String email, String password);
        void loginWithGoogle(GoogleSignInAccount account);
        void resetPassword(String email);
    }
}