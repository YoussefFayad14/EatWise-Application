package com.example.foodapp.ui.register.ui;

public interface RegisterView {
        void showErrorMessage(String message);
        void navigateToMain();
        void setRegisterButtonEnabled(boolean enabled);
}
