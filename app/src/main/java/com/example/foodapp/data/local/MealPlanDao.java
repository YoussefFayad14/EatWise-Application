package com.example.foodapp.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foodapp.data.local.model.MealPlan;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MealPlanDao {
    @Insert
    Single<Long> insertMeal(MealPlan mealPlan);

    @Delete
    Completable deleteMeal(MealPlan mealPlan);

    @Query("DELETE FROM meal_plan WHERE dayOfWeek = :yesterday")
    Completable deleteMealsForYesterday(String yesterday);

    @Query("DELETE FROM meal_plan")
    Completable deleteAllMealPlans();

    @Query("SELECT * FROM meal_plan WHERE dayOfWeek = :day")
    Observable<List<MealPlan>> getMealsForDay(String day);
}
