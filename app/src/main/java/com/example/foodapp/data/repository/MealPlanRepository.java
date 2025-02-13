package com.example.foodapp.data.repository;

import com.example.foodapp.data.local.model.MealPlan;
import com.example.foodapp.data.local.MealPlanDao;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public class MealPlanRepository {
    private MealPlanDao mealPlanDao;

    public MealPlanRepository(MealPlanDao mealPlanDao) {
        this.mealPlanDao = mealPlanDao;
    }

    public Completable addMeal(MealPlan mealPlan) {
        return mealPlanDao.insertMeal(mealPlan);
    }

    public Completable removeMeal(MealPlan mealPlan) {
        return mealPlanDao.deleteMeal(mealPlan);
    }

    public Observable<List<MealPlan>> getMealsForDay(String day) {
        return mealPlanDao.getMealsForDay(day);
    }
}
