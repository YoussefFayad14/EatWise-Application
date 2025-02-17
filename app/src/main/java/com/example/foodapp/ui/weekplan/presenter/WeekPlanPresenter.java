package com.example.foodapp.ui.weekplan.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.foodapp.data.local.model.CalendarDay;
import com.example.foodapp.data.local.model.MealPlan;
import com.example.foodapp.data.repository.FavoriteMealRepository;
import com.example.foodapp.data.repository.WeekPlanRepository;
import com.example.foodapp.ui.weekplan.view.OnMealClickListener;
import com.example.foodapp.ui.weekplan.view.WeekPlanView;
import com.example.foodapp.utils.Converters;
import com.example.foodapp.utils.UserPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WeekPlanPresenter {
    private WeekPlanView view;
    private WeekPlanRepository weekPlanRepository;
    private FavoriteMealRepository favoriteMealRepository;
    private UserPreferences userPreferences;

    public WeekPlanPresenter(Context context, WeekPlanView view, WeekPlanRepository weekPlanRepository, FavoriteMealRepository favoriteMealRepository) {
        this.view = view;
        this.weekPlanRepository = weekPlanRepository;
        this.favoriteMealRepository = favoriteMealRepository;
        this.userPreferences = new UserPreferences(context);
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

        removeMealsForYesterday();
        view.showCalendarDays(weekDays);
    }

    @SuppressLint("CheckResult")
    public void addMealToPlan(MealPlan mealPlan) {
        weekPlanRepository.addMeal(mealPlan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @SuppressLint("CheckResult")
    public void removeMealFromPlan(MealPlan mealPlan) {
        weekPlanRepository.removeMeal(mealPlan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @SuppressLint("CheckResult")
    public void removeMealsForYesterday() {
        Calendar calendar = Calendar.getInstance();
        String today = new SimpleDateFormat("EEEE", Locale.getDefault()).format(Calendar.getInstance().getTime());
        if (!userPreferences.getLastCleanupDate().equals(today)){
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            String yesterday = new SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.getTime());
            weekPlanRepository.removeMealsForYesterday(yesterday)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> userPreferences.setLastCleanupDate(today));
        }
    }

    @SuppressLint("CheckResult")
    public Completable clearAllMealPlans() {
        return weekPlanRepository.removeAllMealPlans()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public void loadMealsForDay(String day) {
        weekPlanRepository.getMealsForDay(day)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealPlans -> {
                            view.showMealPlan(mealPlans);
                        },
                        throwable -> view.showMessage("Error loading meals: " + throwable.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    public void getMealDetails(MealPlan mealPlan, OnMealClickListener listener) {
        favoriteMealRepository.getMealById(mealPlan.getMealId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(meal -> listener.onMealLoaded(new Converters().fromFavoriteMeal(meal)));
    }
    @SuppressLint("CheckResult")
    public Completable syncMealPlans() {
        return weekPlanRepository.syncMealPlansFromFirebase()
                .flatMapIterable(mealPlans -> mealPlans)
                .flatMapCompletable(mealPlan -> weekPlanRepository.resetMeal(mealPlan))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
