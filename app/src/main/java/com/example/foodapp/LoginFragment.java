package com.example.foodapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class LoginFragment extends Fragment {
    TextView tvCreateAccount;
    Button btnLoginAsGuest;
    Button btnLoginAsUser;

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvCreateAccount = view.findViewById(R.id.tv_CreateAccount);
        btnLoginAsGuest = view.findViewById(R.id.btn_loginAsGuest);
        btnLoginAsUser = view.findViewById(R.id.btn_loginAsUser);

        tvCreateAccount.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
        });

        btnLoginAsGuest.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment);
        });

        btnLoginAsUser.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment);
        });

    }
}