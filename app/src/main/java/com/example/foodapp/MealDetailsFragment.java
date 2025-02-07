package com.example.foodapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;


public class MealDetailsFragment extends Fragment {
    private TextView tvMealName, tvCategory, tvCountry, tvIngredient, tvInstructions;
    private VideoView videoView;
    private ImageButton backButton;
    private Button btnAddFavourite;
    private String mealVideoUrl;

    public MealDetailsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_details, container, false);
        tvMealName = view.findViewById(R.id.tv_mealName);
        tvCategory = view.findViewById(R.id.tv_category);
        tvCountry = view.findViewById(R.id.tv_country);
        tvIngredient = view.findViewById(R.id.tv_ingredient);
        tvInstructions = view.findViewById(R.id.tv_instructions);
        videoView = view.findViewById(R.id.video);
        backButton = view.findViewById(R.id.back_Button2);
        btnAddFavourite = view.findViewById(R.id.btn_add_meal_favourite);

        backButton.setOnClickListener(view1 -> {
            navigateToMainFragment();

        });
        return view;
    }

    private void navigateToMainFragment() {
        Navigation.findNavController(getView()).navigate(R.id.action_mealDetailsFragment_to_mainFragment);
    }
}