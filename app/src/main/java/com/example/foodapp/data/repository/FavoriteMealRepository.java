package com.example.foodapp.data.repository;

import com.example.foodapp.data.local.model.FavoriteMeal;
import com.example.foodapp.data.local.FavoriteMealDao;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public class FavoriteMealRepository {
    private FavoriteMealDao favoriteMealDao;

    public FavoriteMealRepository(FavoriteMealDao favoriteMealDao) {
        this.favoriteMealDao = favoriteMealDao;
    }

    public Completable insert(FavoriteMeal meal) {
        return favoriteMealDao.insert(meal);
    }

    public Completable delete(FavoriteMeal meal) {
        return favoriteMealDao.delete(meal);
    }

    public Observable<List<FavoriteMeal>> getAllFavoriteMeals() {
        return favoriteMealDao.getAllFavoriteMeals();
    }
    public Observable<FavoriteMeal> getMealById(String mealId) {
        return favoriteMealDao.getMealById(mealId);
    }
}
