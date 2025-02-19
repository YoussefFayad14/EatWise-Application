package com.example.foodapp.data.local.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "meal_plan")
public class MealPlan {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String mealId;
    private String mealName;
    private String mealArea;
    private String mealImage;
    private String dayOfWeek;
    private String mealType;

    public MealPlan() {}

    public MealPlan(String mealId, String mealName,String mealArea ,String mealImage, String dayOfWeek, String mealType) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.mealArea = mealArea;
        this.mealImage = mealImage;
        this.dayOfWeek = dayOfWeek;
        this.mealType = mealType;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMealId() { return mealId; }
    public void setMealId(String mealId) { this.mealId = mealId; }

    public String getMealName() { return mealName; }
    public void setMealName(String mealName) { this.mealName = mealName; }

    public String getMealArea() { return mealArea; }
    public void setMealArea(String mealArea) { this.mealArea = mealArea; }

    public String getMealImage() { return mealImage; }
    public void setMealImage(String mealImage) { this.mealImage = mealImage; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
}
