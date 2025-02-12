package com.example.foodapp.data.local.favoritemealdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface FavoriteMealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteMeal meal);
    @Delete
    void delete(FavoriteMeal meal);
    @Query("SELECT * FROM favorite_meals")
    LiveData<List<FavoriteMeal>> getAllFavoriteMeals();

    @Query("SELECT * FROM favorite_meals WHERE mealId = :mealId LIMIT 1")
    LiveData<FavoriteMeal> getMealById(String mealId);
}
