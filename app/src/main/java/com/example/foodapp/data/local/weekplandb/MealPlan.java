package com.example.foodapp.data.local.weekplandb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "meal_plan")
public class MealPlan {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int mealId;
    private String mealName;
    private String mealArea;
    private String mealImage;
    private String dayOfWeek;
    private String mealType;

    public MealPlan(int mealId, String mealName,String mealArea ,String mealImage, String dayOfWeek, String mealType) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.mealArea = mealArea;
        this.mealImage = mealImage;
        this.dayOfWeek = dayOfWeek;
        this.mealType = mealType;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMealId() { return mealId; }
    public String getMealName() { return mealName; }
    public String getMealArea() { return mealArea; }
    public String getMealImage() { return mealImage; }
    public String getDayOfWeek() { return dayOfWeek; }
    public String getMealType() { return mealType; }
}
