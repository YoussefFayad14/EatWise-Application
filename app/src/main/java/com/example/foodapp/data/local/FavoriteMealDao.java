package com.example.foodapp.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodapp.data.local.model.FavoriteMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface FavoriteMealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insert(FavoriteMeal meal);
    @Delete
    Completable delete(FavoriteMeal meal);
    @Query("DELETE FROM favorite_meals")
    Completable deleteAllFavoriteMeals();

    @Query("SELECT * FROM favorite_meals")
    Observable<List<FavoriteMeal>> getAllFavoriteMeals();

    @Query("SELECT * FROM favorite_meals WHERE mealId = :mealId LIMIT 1")
    Observable<FavoriteMeal> getMealById(String mealId);
}
