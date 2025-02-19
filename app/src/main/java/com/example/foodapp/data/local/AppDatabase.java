package com.example.foodapp.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.foodapp.utils.Converters;
import com.example.foodapp.data.local.model.FavoriteMeal;
import com.example.foodapp.data.local.model.MealPlan;

@Database(entities = {FavoriteMeal.class, MealPlan.class}, version = 5, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE = null;
    public abstract FavoriteMealDao favoriteMealDao();
    public abstract MealPlanDao mealPlanDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "food_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
