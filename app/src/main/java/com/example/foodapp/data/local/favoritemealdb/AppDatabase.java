package com.example.foodapp.data.local.favoritemealdb;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {FavoriteMeal.class}, version = 3, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE = null;
    public abstract FavoriteMealDao favoriteMealDao();
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
