package com.example.foodapp.data.repository;

import com.example.foodapp.data.local.model.MealPlan;
import com.example.foodapp.data.local.MealPlanDao;
import com.example.foodapp.data.remote.FirebaseService;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WeekPlanMealsRepository {
    private MealPlanDao mealPlanDao;
    private FirebaseService firebaseService = new FirebaseService();


    public WeekPlanMealsRepository(MealPlanDao mealPlanDao) {
        this.mealPlanDao = mealPlanDao;
    }

    public Single<Long> addMeal(MealPlan mealPlan) {
        return mealPlanDao.insertMeal(mealPlan)
                .doOnSuccess(rowId -> firebaseService.addPlanToFirestore(mealPlan))
                .subscribeOn(Schedulers.io());
    }

    public Completable resetMeal(MealPlan mealPlan) {
        return mealPlanDao.insertMeal(mealPlan)
                .ignoreElement()
                .subscribeOn(Schedulers.io());
    }

    public Completable removeMeal(MealPlan mealPlan) {
        return mealPlanDao.deleteMeal(mealPlan)
                .doOnComplete(() -> firebaseService.removePlanFromFirestore(mealPlan));
    }

    public Completable removeAllMealPlans() {
        return mealPlanDao.deleteAllMealPlans();
    }

    public Completable removeMealsForYesterday(String yesterday) {
        return mealPlanDao.deleteMealsForYesterday(yesterday);
    }

    public Observable<List<MealPlan>> getMealsForDay(String day) {
        return mealPlanDao.getMealsForDay(day);
    }
    public @NonNull Observable<List<MealPlan>> syncMealPlansFromFirebase() {
        return firebaseService.fetchMealPlansFromFirebase();
    }
}
