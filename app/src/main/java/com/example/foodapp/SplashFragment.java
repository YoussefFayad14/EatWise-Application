package com.example.foodapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

public class SplashFragment extends Fragment {

    public SplashFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (checkUserLogin(view)) {
            return;
        }

        new Handler().postDelayed(() -> {
            Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_loginFragment);
        }, 3000);
    }

    private boolean checkUserLogin(View view) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);

        if (isLoggedIn && FirebaseAuth.getInstance().getCurrentUser() != null) {
            Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_mainFragment);
            return true;
        }
        return false;
    }
}
