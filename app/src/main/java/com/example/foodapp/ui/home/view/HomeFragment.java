package com.example.foodapp.ui.home.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.foodapp.R;
import com.example.foodapp.data.remote.model.Area;
import com.example.foodapp.data.remote.model.CategoryItem;
import com.example.foodapp.data.remote.model.Ingredient;
import com.example.foodapp.data.remote.model.Meal;
import com.example.foodapp.data.repository.HomeRepository;
import com.example.foodapp.data.repository.LocationRepository;
import com.example.foodapp.ui.home.presenter.HomePresenter;

import java.util.List;

public class HomeFragment extends Fragment implements onClickListener, HomeView {

    private ImageView imgMealDay;
    private TextView tvMealDay,tvCountry;
    private Button btnCheckNow;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3, recyclerView4;
    private HomePresenter presenter;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        presenter = new HomePresenter(this, new HomeRepository(), new LocationRepository());

        imgMealDay = view.findViewById(R.id.imageMealDay);
        tvMealDay = view.findViewById(R.id.tvMealDay);
        tvCountry = view.findViewById(R.id.tvCountry);
        btnCheckNow = view.findViewById(R.id.btnCheckNow);
        recyclerView1 = view.findViewById(R.id.recyclerView1);
        recyclerView2 = view.findViewById(R.id.recyclerView2);
        recyclerView3 = view.findViewById(R.id.recyclerView3);
        recyclerView4 = view.findViewById(R.id.recyclerView4);

        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView3.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView4.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        presenter.loadPopularMeals();
        presenter.loadCategories();
        presenter.loadCountries();
        presenter.loadIngredients();

        View.OnClickListener mealDayListener = click -> {
            presenter.loadRandomMeal();
        };

        imgMealDay.setOnClickListener(mealDayListener);
        tvMealDay.setOnClickListener(mealDayListener);
        btnCheckNow.setOnClickListener(mealDayListener);

        return view;
    }

    @Override
    public void showMealDetails(Meal meal) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("meal", meal);
        Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_mealDetailsFragment, bundle);
    }

    @Override
    public void showRandomMeal(Meal meal) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("meal", meal);
        Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_mealDetailsFragment, bundle);
    }

    @Override
    public void showPopularMeals(List<Meal> popularMeals, String country) {
        tvCountry.setText(country);
        recyclerView1.setAdapter(new PopularAdapter(getContext(), popularMeals,this));
    }

    @Override
    public void showCountries(List<Area> countries) {
        recyclerView2.setAdapter(new CountryAdapter(getContext(), countries, this));
    }

    @Override
    public void showCategories(List<CategoryItem> categories) {
        recyclerView3.setAdapter(new CategoryAdapter(getContext(), categories, this));
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        recyclerView4.setAdapter(new IngredientAdapter(getContext(), ingredients, this));
    }

    @Override
    public <T> void onSectionClick(T item) {
        Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_searchFragment);
    }

    @Override
    public void onMealClick(Meal meal) {
        presenter.loadSelectedMeal(meal.getIdMeal());
    }
}
