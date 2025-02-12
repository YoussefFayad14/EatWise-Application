package com.example.foodapp.data.repository;

import androidx.lifecycle.LiveData;
import com.example.foodapp.data.local.favoritemealdb.FavoriteMeal;
import com.example.foodapp.data.local.favoritemealdb.FavoriteMealDao;
import java.util.List;

public class FavoriteMealRepository {
    private FavoriteMealDao favoriteMealDao;

    public FavoriteMealRepository(FavoriteMealDao favoriteMealDao) {
        this.favoriteMealDao = favoriteMealDao;
    }

    public void insert(FavoriteMeal meal) {
        new Thread(() -> favoriteMealDao.insert(meal)).start();
    }

    public void delete(FavoriteMeal meal) {
        new Thread(() -> favoriteMealDao.delete(meal)).start();
    }

    public LiveData<List<FavoriteMeal>> getAllFavoriteMeals() {
        return favoriteMealDao.getAllFavoriteMeals();
    }
    public LiveData<FavoriteMeal> getMealById(String mealId) {
        return favoriteMealDao.getMealById(mealId);
    }
}
