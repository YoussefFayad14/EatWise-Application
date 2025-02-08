package com.example.foodapp.ui.register;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface RegisterContract {
    interface View {
        void showErrorMessage(String message);
        void navigateToMain();
        void setRegisterButtonEnabled(boolean enabled);
    }

    interface Presenter {
        void registerUser(String username,String email, String password);
        void signInWithGoogle(GoogleSignInAccount account);
    }
}
