package com.example.foodapp.data.remote.MealApi;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("search.php")       // Endpoint for searching meals by first letter
    Observable<MealResponse> listMealsByFirstLetter(@Query("f") String firstLetter);

    @GET("categories.php")   // Endpoint for fetching all categories have images
    Observable<CategoriesResponse> getCategories();

    @GET("list.php?c=list")  // Endpoint for fetching all categories without images
    Observable<CategoriesResponse> getAllCategories();

    @GET("list.php?a=list")  // Endpoint for fetching all areas
    Observable<AreasResponse> getAreas();

    @GET("list.php?i=list")  // Endpoint for fetching all ingredients
    Observable<IngredientResponse> getIngredients();

    @GET("filter.php?") // Endpoint for fetching meals by category
    Observable<MealResponse> getMealsByCategory(@Query("c") String category);

    @GET("filter.php?") // Endpoint for fetching meals by area
    Observable<MealResponse> getMealsByArea(@Query("a") String area);

    @GET("filter.php?") // Endpoint for fetching meals by ingredient
    Observable<MealResponse> getMealsByIngredient(@Query("i") String ingredient);

    @GET("random.php") // Endpoint for fetching a random meal
    Single<MealResponse> getRandomMeal();

    @GET("lookup.php?") // Endpoint for fetching meal by mealId
    Single<MealResponse> getMealsById(@Query("i") String mealId);

    @GET("search.php") // Endpoint for searching meals by name
    Single<MealResponse> searchMealByName(@Query("s") String mealName);
}

