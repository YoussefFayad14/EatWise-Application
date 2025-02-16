package com.example.foodapp.data.repository;

import android.util.Log;

import com.example.foodapp.data.local.model.FavoriteMeal;
import com.example.foodapp.data.local.FavoriteMealDao;
import com.example.foodapp.data.local.model.MealPlan;
import com.example.foodapp.data.remote.FirebaseService;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoriteMealRepository {
    private FavoriteMealDao favoriteMealDao;
    private FirebaseService firebaseService = new FirebaseService();

    public FavoriteMealRepository(FavoriteMealDao favoriteMealDao) {
        this.favoriteMealDao = favoriteMealDao;
    }

    public Single<Long> insert(FavoriteMeal meal) {
        return favoriteMealDao.insert(meal)
                .doOnSuccess(rowId -> firebaseService.addMealToFirestore(meal))
                .subscribeOn(Schedulers.io());
    }

    public Completable resetMeal(FavoriteMeal meal) {
        return favoriteMealDao.insert(meal)
                .ignoreElement()
                .subscribeOn(Schedulers.io());
    }

    public Completable delete(FavoriteMeal meal) {
        return favoriteMealDao.delete(meal)
                .doOnComplete(() -> firebaseService.removeMealFromFirestore(meal));
    }
    public Completable deleteAllFavoriteMeals() {
        return favoriteMealDao.deleteAllFavoriteMeals();
    }

    public Observable<List<FavoriteMeal>> getAllFavoriteMeals() {
        return favoriteMealDao.getAllFavoriteMeals();
    }
    public Observable<FavoriteMeal> getMealById(String mealId) {
        return favoriteMealDao.getMealById(mealId);
    }

    public @NonNull Observable<List<FavoriteMeal>> syncFavoritesFromFirebase() {
        return firebaseService.fetchFavoritesFromFirebase();
    }


}
