package com.example.foodapp.data.remote.MealApi;

import java.util.List;

import com.example.foodapp.data.model.CategoryItem;
import com.google.gson.annotations.SerializedName;

public class CategoriesResponse{

	@SerializedName("categories")
	private List<CategoryItem> categories;

	public List<CategoryItem> getCategories(){
		return categories;
	}
}