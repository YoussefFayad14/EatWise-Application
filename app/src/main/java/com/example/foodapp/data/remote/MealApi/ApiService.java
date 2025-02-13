package com.example.foodapp.data.remote.MealApi;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("categories.php")   // Endpoint for fetching all categories
    Observable<CategoriesResponse> getCategories();

    @GET("list.php?a=list")  // Endpoint for fetching all areas
    Observable<AreasResponse> getAreas();

    @GET("list.php?i=list")  // Endpoint for fetching all ingredients
    Observable<IngredientResponse> getIngredients();

    @GET("filter.php?") // Endpoint for fetching meals by area
    Observable<MealsCountryResponse> getMealsByArea(@Query("a") String area);

    @GET("random.php") // Endpoint for fetching a random meal
    Single<MealResponse> getRandomMeal();

    @GET("lookup.php?") // Endpoint for fetching meal by mealId
    Single<MealResponse> getMealsById(@Query("i") String mealId);


}

