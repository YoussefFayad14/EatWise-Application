package com.example.foodapp.ui.favorite.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.data.local.AppDatabase;
import com.example.foodapp.data.local.model.FavoriteMeal;
import com.example.foodapp.data.repository.FavoriteMealsRepository;
import com.example.foodapp.data.repository.WeekPlanMealsRepository;
import com.example.foodapp.ui.favorite.presenter.FavoritePresenter;
import com.example.foodapp.ui.weekplan.presenter.WeekPlanPresenter;
import com.example.foodapp.utils.Converters;
import com.example.foodapp.utils.UserPreferences;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment implements onClickListener, FavoriteView {
    private RecyclerView recyclerView;
    private LinearLayout guestLayout;
    private Button btnRegister;
    private FavoriteAdapter adapter;
    private List <FavoriteMeal> FavMealList = new ArrayList<>();
    private FavoritePresenter presenter;
    private UserPreferences userPreferences;

    public FavouritesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPreferences = new UserPreferences(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        guestLayout = view.findViewById(R.id.guestLayout);
        btnRegister = view.findViewById(R.id.goToRegisterButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new FavoriteAdapter(getContext(), FavMealList, this);
        recyclerView.setAdapter(adapter);
        presenter = new FavoritePresenter(this, new FavoriteMealsRepository(AppDatabase.getInstance(getContext()).favoriteMealDao()));

        btnRegister.setOnClickListener(v -> {
            Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_registerFragment);
        });

        if(userPreferences.isGuest()){
            guestLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            presenter.getFavoriteMeals();
            guestLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        onBack();
        return view;
    }

public void showFavoriteMeals(List<FavoriteMeal> favoriteMeals) {
        FavMealList.clear();
        FavMealList.addAll(favoriteMeals);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMealClick(FavoriteMeal meal) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("meal", new Converters().fromFavoriteMeal(meal));
        Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_mealDetailsFragment, bundle);
    }

    @Override
    public void AddToPlanClick(FavoriteMeal meal) {
        WeekPlanPresenter weekPlanPresenter = new WeekPlanPresenter(
                getContext(),
                null,
                new WeekPlanMealsRepository(AppDatabase.getInstance(getContext()).mealPlanDao()),
                new FavoriteMealsRepository(AppDatabase.getInstance(getContext()).favoriteMealDao())
        );
        MealPlanDialogSheet bottomSheet = new MealPlanDialogSheet(meal, weekPlanPresenter);
        bottomSheet.show(getParentFragmentManager(), "MealPlanBottomSheet");
    }

    @Override
    public void RemoveFromFavouritesClick(FavoriteMeal meal) {
        presenter.removeFromFavorites(meal);
    }
    private void onBack(){
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        });
    }
}
