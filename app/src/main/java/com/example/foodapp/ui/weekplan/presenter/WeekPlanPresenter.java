package com.example.foodapp.ui.weekplan.presenter;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.foodapp.data.local.model.CalendarDay;
import com.example.foodapp.data.local.model.FavoriteMeal;
import com.example.foodapp.data.local.model.MealPlan;
import com.example.foodapp.data.remote.model.Meal;
import com.example.foodapp.data.repository.FavoriteMealRepository;
import com.example.foodapp.data.repository.MealPlanRepository;
import com.example.foodapp.ui.favorite.presenter.FavoritePresenter;
import com.example.foodapp.ui.weekplan.view.OnMealClickListener;
import com.example.foodapp.ui.weekplan.view.WeekPlanView;
import com.example.foodapp.utils.Converters;

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
                .subscribe();
    }

    @SuppressLint("CheckResult")
    public void removeMealFromPlan(MealPlan mealPlan) {
        mealPlanRepository.removeMeal(mealPlan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @SuppressLint("CheckResult")
    public Completable clearAllMealPlans() {
        return mealPlanRepository.removeAllMealPlans()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public void loadMealsForDay(String day) {
        mealPlanRepository.getMealsForDay(day)
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
        return mealPlanRepository.syncMealPlansFromFirebase()
                .flatMapIterable(mealPlans -> mealPlans)
                .flatMapCompletable(mealPlan -> mealPlanRepository.resetMeal(mealPlan))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
