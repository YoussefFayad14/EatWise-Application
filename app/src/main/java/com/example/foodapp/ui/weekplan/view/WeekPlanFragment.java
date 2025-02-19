package com.example.foodapp.ui.weekplan.view;

import android.icu.util.Calendar;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import com.example.foodapp.R;
import com.example.foodapp.data.local.AppDatabase;
import com.example.foodapp.data.local.model.CalendarDay;
import com.example.foodapp.data.local.model.MealPlan;
import com.example.foodapp.data.remote.model.Meal;
import com.example.foodapp.data.repository.FavoriteMealRepository;
import com.example.foodapp.data.repository.WeekPlanRepository;
import com.example.foodapp.ui.favorite.view.FavouritesFragment;
import com.example.foodapp.ui.weekplan.presenter.WeekPlanPresenter;
import com.example.foodapp.ui.dialogs.PopupSnackbar;
import com.example.foodapp.utils.UserPreferences;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WeekPlanFragment extends Fragment implements OnDayClickListener,OnMealClickListener,WeekPlanView{
    private ScrollView planScrollView;
    private RecyclerView recyclerViewCalendar, recyclerViewBreakfast, recyclerViewLunch, recyclerViewDinner;
    private LinearLayout guestLayout;
    private TextView dayNumMonth;
    private Button btnRegister;
    private ImageButton btnAddNewMeal;
    private WeekPlanPresenter presenter;
    private UserPreferences userPreferences;
    private CalendarAdapter calendarAdapter;
    private WeekPlanAdapter breakfastAdapter, lunchAdapter, dinnerAdapter;
    private List<CalendarDay> weekDays = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPreferences = new UserPreferences(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week_plan, container, false);

        presenter = new WeekPlanPresenter(
                getContext(),
                this,
                new WeekPlanRepository(AppDatabase.getInstance(getContext()).mealPlanDao()),
                new FavoriteMealRepository(AppDatabase.getInstance(getContext()).favoriteMealDao())
        );

        recyclerViewCalendar = view.findViewById(R.id.recyclerViewCalendar);
        recyclerViewBreakfast = view.findViewById(R.id.recyclerView_Breakfast);
        recyclerViewLunch = view.findViewById(R.id.recyclerView_Lunch);
        recyclerViewDinner = view.findViewById(R.id.recyclerView_Dinner);
        planScrollView = view.findViewById(R.id.planScrollView);
        guestLayout = view.findViewById(R.id.guestLayout);
        dayNumMonth = view.findViewById(R.id.day_num_month);
        btnRegister = view.findViewById(R.id.goToRegisterButton);
        btnAddNewMeal = view.findViewById(R.id.btn_addNewMeal);


        calendarAdapter = new CalendarAdapter(getContext(),weekDays,dayNumMonth,this);
        breakfastAdapter = new WeekPlanAdapter(getContext(),this);
        lunchAdapter = new WeekPlanAdapter(getContext(),this);
        dinnerAdapter = new WeekPlanAdapter(getContext(),this);

        recyclerViewCalendar.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewBreakfast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewLunch.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewDinner.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        recyclerViewCalendar.setAdapter(calendarAdapter);
        recyclerViewBreakfast.setAdapter(breakfastAdapter);
        recyclerViewLunch.setAdapter(lunchAdapter);
        recyclerViewDinner.setAdapter(dinnerAdapter);

        if (userPreferences.isGuest()){
            guestLayout.setVisibility(View.VISIBLE);
            planScrollView.setVisibility(View.GONE);
        }else {
            guestLayout.setVisibility(View.GONE);
            planScrollView.setVisibility(View.VISIBLE);
            presenter.loadCurrentWeek();
            presenter.loadMealsForDay(new SimpleDateFormat("EEEE", Locale.getDefault()).format(Calendar.getInstance().getTime()));
        }

        btnRegister.setOnClickListener(v -> Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_registerFragment));
        btnAddNewMeal.setOnClickListener(v -> addNewMeal());

        return view;
    }

    @Override
    public void showCalendarDays(List<CalendarDay> days) {
        weekDays.clear();
        weekDays.addAll(days);
        calendarAdapter.setDays(weekDays);
        dayNumMonth.setText(new SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault()).format(Calendar.getInstance().getTime()));
    }

    @Override
    public void showMealPlan(List<MealPlan> mealPlans) {
        breakfastAdapter.updateList(filterMeals(mealPlans, "Breakfast"));
        lunchAdapter.updateList(filterMeals(mealPlans, "Lunch"));
        dinnerAdapter.updateList(filterMeals(mealPlans, "Dinner"));
    }

    private List<MealPlan> filterMeals(List<MealPlan> meals, String mealType) {
        List<MealPlan> filteredMeals = new ArrayList<>();
        for (MealPlan meal : meals) {
            if (meal.getMealType().equals(mealType)) {
                filteredMeals.add(meal);
            }
        }
        return filteredMeals;
    }


    @Override
    public void showMessage(String message) {
        new PopupSnackbar(requireContext()).showMessage(message, true);
    }

    private void addNewMeal(){
        FragmentManager fragmentManager = getParentFragment().getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new FavouritesFragment())
                .addToBackStack(null)
                .commit();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.favourites);
    }

    @Override
    public void onMealRemove(MealPlan mealPlan) {
        presenter.removeMealFromPlan(mealPlan);
    }

    @Override
    public void onMealClick(MealPlan mealPlan) {
        presenter.getMealDetails(mealPlan,this);
    }
    @Override
    public void onMealLoaded(Meal meal) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("meal", meal);
        Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_mealDetailsFragment, bundle);
    }

    @Override
    public void onDayClick(String day) {
        presenter.loadMealsForDay(day);
    }


}