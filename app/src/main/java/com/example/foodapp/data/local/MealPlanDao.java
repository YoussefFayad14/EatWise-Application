package com.example.foodapp.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foodapp.data.local.model.MealPlan;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

@Dao
public interface MealPlanDao {
    @Insert
    Completable insertMeal(MealPlan mealPlan);

    @Delete
    Completable deleteMeal(MealPlan mealPlan);

    @Query("SELECT * FROM meal_plan WHERE dayOfWeek = :day")
    Observable<List<MealPlan>> getMealsForDay(String day);
}
