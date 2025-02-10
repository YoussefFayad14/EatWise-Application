package com.example.foodapp.data.remote.MealApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("categories.php")   // Endpoint for fetching all categories
    Call<CategoriesResponse> getCategories();

    @GET("list.php?a=list")  // Endpoint for fetching all areas
    Call<AreasResponse> getAreas();

    @GET("list.php?i=list")  // Endpoint for fetching all ingredients
    Call<IngredientResponse> getIngredients();

    @GET("filter.php?") // Endpoint for fetching meals by area
    Call<MealsCountryResponse> getMealsByArea(@Query("a") String area);

    @GET("random.php") // Endpoint for fetching a random meal
    Call<MealResponse> getRandomMeal();


}

