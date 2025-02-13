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
    public String getMealName() { return mealName; }
    public String getMealCategory() { return mealCategory; }
    public String getMealArea() { return mealArea; }
    public String getMealImage() { return mealImage; }
    public List<String> getIngredients() { return ingredients; }
    public List<String> getMeasures() { return measures; }
    public String getInstructions() { return instructions; }
    public String getYoutubeLink() { return youtubeLink; }

}
