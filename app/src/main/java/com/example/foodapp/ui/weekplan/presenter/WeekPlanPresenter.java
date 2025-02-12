package com.example.foodapp.ui.weekplan.presenter;

import android.annotation.SuppressLint;

import com.example.foodapp.data.local.weekplandb.CalendarDay;
import com.example.foodapp.data.local.weekplandb.MealPlan;
import com.example.foodapp.data.local.weekplandb.MealPlanDao;
import com.example.foodapp.data.repository.FavoriteMealRepository;
import com.example.foodapp.data.repository.MealPlanRepository;
import com.example.foodapp.ui.weekplan.view.WeekPlanView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WeekPlanPresenter {
    private WeekPlanView view;
    private MealPlanRepository mealPlanRepository;
    private FavoriteMealRepository favoriteMealRepository;

    public WeekPlanPresenter(WeekPlanView view, MealPlanRepository mealPlanRepository, FavoriteMealRepository favoriteMealRepository) {
        this.view = view;
        this.mealPlanRepository = mealPlanRepository;
        this.favoriteMealRepository = favoriteMealRepository;
    }

    public void loadCurrentWeek() {
        List<CalendarDay> weekDays = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();


        for (int i = 0; i < 7; i++) {
            String dayName = new SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.getTime());
            int dayNum = calendar.get(Calendar.DAY_OF_MONTH);
            String monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            weekDays.add(new CalendarDay(dayName, dayNum, monthName));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        view.showCalendarDays(weekDays);
    }

    @SuppressLint("CheckResult")
    public void addMealToPlan(MealPlan mealPlan) {
        mealPlanRepository.addMeal(mealPlan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.showMessage("Meal added successfully!"),
                        throwable -> view.showMessage("Error adding meal: " + throwable.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    public void removeMealFromPlan(MealPlan mealPlan) {
        mealPlanRepository.removeMeal(mealPlan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.showMessage("Meal removed from plan!"),
                        throwable -> view.showMessage("Error removing meal: " + throwable.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    public void loadMealsForDay(String day) {
        mealPlanRepository.getMealsForDay(day)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealPlans -> view.showMealPlan(mealPlans),
                        throwable -> view.showMessage("Error loading meals: " + throwable.getMessage())
                );
    }

    public void showMealDetails(MealPlan mealPlan) {
        String mealId = String.valueOf(mealPlan.getMealId());
    }
}
