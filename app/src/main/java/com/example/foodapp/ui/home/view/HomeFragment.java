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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.example.foodapp.R;
import com.example.foodapp.data.remote.model.Area;
import com.example.foodapp.data.remote.model.Category;
import com.example.foodapp.data.remote.model.Ingredient;
import com.example.foodapp.data.remote.model.Meal;
import com.example.foodapp.data.repository.HomeRepository;
import com.example.foodapp.data.repository.LocationRepository;
import com.example.foodapp.ui.PopupSnackbar;
import com.example.foodapp.ui.RetryDialog;
import com.example.foodapp.ui.home.presenter.HomePresenter;
import com.example.foodapp.utils.NetworkUtil;

import java.util.List;

public class HomeFragment extends Fragment implements onClickListener, HomeView {

    private ImageView imgMealDay;
    private TextView tvMealDay,tvCountry;
    private Button btnCheckNow,retryButton;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3, recyclerView4;
    private ScrollView contentView;
    private LinearLayout retryLayout;
    private HomePresenter presenter;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        presenter = new HomePresenter(this, new HomeRepository(), new LocationRepository());

        retryLayout = view.findViewById(R.id.retryLayout);
        contentView = view.findViewById(R.id.contentScrollView);
        imgMealDay = view.findViewById(R.id.imageMealDay);
        tvMealDay = view.findViewById(R.id.tvMealDay);
        tvCountry = view.findViewById(R.id.tvCountry);
        btnCheckNow = view.findViewById(R.id.btnCheckNow);
        retryButton = view.findViewById(R.id.retryButton);
        recyclerView1 = view.findViewById(R.id.recyclerView1);
        recyclerView2 = view.findViewById(R.id.recyclerView2);
        recyclerView3 = view.findViewById(R.id.recyclerView3);
        recyclerView4 = view.findViewById(R.id.recyclerView4);

        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView3.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView4.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        loadData();

        View.OnClickListener mealDayListener = click -> {
            if (NetworkUtil.isNetworkAvailable(getContext())) {
                presenter.loadRandomMeal();
            }else {
                showRetryDialog();
            }
        };

        imgMealDay.setOnClickListener(mealDayListener);
        tvMealDay.setOnClickListener(mealDayListener);
        btnCheckNow.setOnClickListener(mealDayListener);

        return view;
    }

    private void loadData() {
        if (!NetworkUtil.isNetworkAvailable(requireContext())) {
            retryButton.setOnClickListener(view1 -> {
                showRetryDialog();
            });
        }else{
            presenter.loadPopularMeals();
            presenter.loadCategories();
            presenter.loadCountries();
            presenter.loadIngredients();
            retryLayout.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
        }
    }

    private void showRetryDialog() {
        RetryDialog retryDialog = new RetryDialog(() -> {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                presenter.loadPopularMeals();
                presenter.loadCategories();
                presenter.loadCountries();
                presenter.loadIngredients();
                retryLayout.setVisibility(View.GONE);
                contentView.setVisibility(View.VISIBLE);
            } else {
                retryLayout.setVisibility(View.VISIBLE);
                contentView.setVisibility(View.GONE);
            }
        });
        retryDialog.show(getChildFragmentManager(), "RetryDialog");
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
    public void showCategories(List<Category> categories) {
        recyclerView3.setAdapter(new CategoryAdapter(getContext(), categories, this));
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        recyclerView4.setAdapter(new IngredientAdapter(getContext(), ingredients, this));
    }

    @Override
    public void showAlert(String message, boolean flag) {
        new PopupSnackbar(getContext()).showMessage(message,flag);
    }

    @Override
    public void onSectionClick(String item, String sectionType) {
        if(NetworkUtil.isNetworkAvailable(getContext())){
            Bundle bundle = new Bundle();
            bundle.putString("filterType",sectionType);
            bundle.putString("value",item);
            Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_searchFragment,bundle);
        }else {
            showRetryDialog();
        }

    }

    @Override
    public void onMealClick(Meal meal) {
        if(NetworkUtil.isNetworkAvailable(getContext())) {
            presenter.loadSelectedMeal(meal.getIdMeal());
        }else {
            showRetryDialog();
        }
    }
}
