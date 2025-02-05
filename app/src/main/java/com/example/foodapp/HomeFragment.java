package com.example.foodapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FrameLayout frameMealDay;
    private ImageView imgMealDay;
    private TextView tvMealDay;
    private Button btnCheckNow;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        frameMealDay = view.findViewById(R.id.frameMealDay);
        imgMealDay = view.findViewById(R.id.imageMealDay);
        tvMealDay = view.findViewById(R.id.tvMealDay);
        btnCheckNow = view.findViewById(R.id.btnCheckNow);
        recyclerView1 = view.findViewById(R.id.recyclerView1);
        recyclerView2 = view.findViewById(R.id.recyclerView2);
        recyclerView3 = view.findViewById(R.id.recyclerView3);

        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView3.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerView1.setAdapter(new CategoryAdapter(getContext(), getSampleCategories(),getParentFragment()));
        recyclerView2.setAdapter(new CountryAdapter(getContext(), getSampleCountries(),getParentFragment()));
        recyclerView3.setAdapter(new PopularAdapter(getContext(), getSamplePopularMeals(),getParentFragment()));


        View.OnClickListener clickListener = click -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(getParentFragment().getId(), new MealDetailsFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        };

        frameMealDay.setOnClickListener(clickListener);
        imgMealDay.setOnClickListener(clickListener);
        tvMealDay.setOnClickListener(clickListener);
        btnCheckNow.setOnClickListener(clickListener);


        return view;
    }

    private List<String> getSampleCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("Vegetarian");
        categories.add("Vegan");
        categories.add("Gluten-Free");
        categories.add("Paleo");
        categories.add("Keto");
        categories.add("Paleo");
        categories.add("Keto");
        categories.add("Paleo");
        categories.add("Keto");
        categories.add("Paleo");
        return categories;
    }

    private List<String> getSampleCountries() {
        List<String> countries = new ArrayList<>();
        countries.add("Italy");
        countries.add("Mexico");
        countries.add("India");
        countries.add("Germany");
        countries.add("France");
        countries.add("Spain");
        countries.add("China");
        countries.add("Japan");
        countries.add("Brazil");
        countries.add("Russia");
        return countries;
    }

    private List<String> getSamplePopularMeals() {
        List<String> popularMeals = new ArrayList<>();
        popularMeals.add("Pizza");
        popularMeals.add("Tacos");
        popularMeals.add("Sushi");
        popularMeals.add("Pasta");
        popularMeals.add("Salad");
        popularMeals.add("Burger");
        popularMeals.add("Steak");
        popularMeals.add("Biryani");
        return popularMeals;
    }
}
