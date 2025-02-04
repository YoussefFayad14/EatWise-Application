package com.example.foodapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainFragment extends Fragment {
    private BottomNavigationView bottomNavigationView;
    private TextView tvSearchToolbar;


    public MainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        tvSearchToolbar = view.findViewById(R.id.tv_search_toolbar);

        replaceFragment(new HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    return true;
                case R.id.favourites:
                    replaceFragment(new FavouritesFragment());
                    return true;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    return true;
                case R.id.calendar:
                    replaceFragment(new MealPlanFragment());
                    return true;
                default:
                    return false;
            }
        });

        tvSearchToolbar.setOnClickListener(v -> {
            navigateToSearchFragment();
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void navigateToSearchFragment() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(this.getId(), new SearchFragment());
        transaction.addToBackStack(null);
        transaction.commit();
        bottomNavigationView.setSelectedItemId(R.id.home);
    }
}
