package com.example.foodapp.data.local.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.foodapp.utils.Converters;

import java.util.List;

@Entity(tableName = "favorite_meals")
public class FavoriteMeal {
    @NonNull
    @PrimaryKey
    private String mealId;
    private String mealName;
    private String mealCategory;
    private String mealArea;
    private String mealImage;
    @TypeConverters(Converters.class)
    private List<String> ingredients;
    @TypeConverters(Converters.class)
    private List<String> measures;
    private String instructions;
    private String youtubeLink;

    public FavoriteMeal() {}

    public FavoriteMeal(String mealId, String mealName, String mealCategory,String mealArea ,String mealImage,
                        List<String> ingredients, List<String> measures,
                        String instructions, String youtubeLink) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.mealCategory = mealCategory;
        this.mealArea = mealArea;
        this.mealImage = mealImage;
        this.ingredients = ingredients;
        this.measures = measures;
        this.instructions = instructions;
        this.youtubeLink = youtubeLink;
    }

    public String getMealId() { return mealId; }
    public void setMealId(String mealId) { this.mealId = mealId; }

    public String getMealName() { return mealName; }
    public void setMealName(String mealName) { this.mealName = mealName; }

    public String getMealCategory() { return mealCategory; }
    public void setMealCategory(String mealCategory) { this.mealCategory = mealCategory; }

    public String getMealArea() { return mealArea; }
    public void setMealArea(String mealArea) { this.mealArea = mealArea; }

    public String getMealImage() { return mealImage; }
    public void setMealImage(String mealImage) { this.mealImage = mealImage; }

    public List<String> getIngredients() { return ingredients; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }

    public List<String> getMeasures() { return measures; }
    public void setMeasures(List<String> measures) { this.measures = measures; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getYoutubeLink() { return youtubeLink; }
    public void setYoutubeLink(String youtubeLink) { this.youtubeLink = youtubeLink; }

}
