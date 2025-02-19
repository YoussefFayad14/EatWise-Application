package com.example.foodapp.ui.login.view;

public interface LoginView {
        void showErrorMessage(String message);
        void navigateToMain();
        void clearFields();
}