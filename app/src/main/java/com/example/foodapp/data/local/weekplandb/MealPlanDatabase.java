package com.example.foodapp.data.local.weekplandb;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MealPlan.class}, version = 1, exportSchema = false)
public abstract class MealPlanDatabase extends RoomDatabase {
    private static MealPlanDatabase instance;
    public abstract MealPlanDao mealPlanDao();

    public static synchronized MealPlanDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MealPlanDatabase.class, "meal_plan_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
