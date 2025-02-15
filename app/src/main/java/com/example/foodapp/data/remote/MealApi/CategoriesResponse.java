package com.example.foodapp.data.remote.MealApi;

import java.util.List;

import com.example.foodapp.data.remote.model.Category;
import com.google.gson.annotations.SerializedName;

public class CategoriesResponse{
	@SerializedName("meals")
	private List<Category> meals;
	@SerializedName("categories")
	private List<Category> categories;

	public List<Category> getCategories(){
		return categories != null ? categories : meals;
	}
}