package com.example.foodapp.data.repository;

import com.example.foodapp.data.remote.MealApi.ApiClient;
import com.example.foodapp.data.remote.MealApi.ApiService;
import com.example.foodapp.data.remote.MealApi.AreasResponse;
import com.example.foodapp.data.remote.MealApi.CategoriesResponse;
import com.example.foodapp.data.remote.MealApi.IngredientResponse;
import com.example.foodapp.data.remote.MealApi.MealResponse;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class MealsRepository {

    private final ApiService apiService;

    public MealsRepository() {
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    public Single<MealResponse> fetchMealDetailsFromAPI(String mealId) {
        return apiService.getMealsById(mealId);
    }

    public Single<MealResponse> fetchRandomMealFromAPI() {
        return apiService.getRandomMeal();
    }

    public Observable<MealResponse> fetchMealsByArea(String country) {
        return apiService.getMealsByArea(country);
    }
    public Observable<MealResponse> fetchMealsByCategory(String category) {
        return apiService.getMealsByCategory(category);
    }
    public Observable<MealResponse> fetchMealsByIngredient(String ingredient) {
        return apiService.getMealsByIngredient(ingredient);
    }

    public Observable<CategoriesResponse> fetchCategoriesFromAPI() {
        return apiService.getCategories();
    }

    public Observable<CategoriesResponse> fetchAllCategoriesFromAPI() {
        return apiService.getAllCategories();
    }

    public Observable<AreasResponse> fetchCountriesFromAPI() {
        return apiService.getAreas();
    }

    public Observable<IngredientResponse> fetchIngredientsFromAPI() {
        return apiService.getIngredients();
    }
    public Single<MealResponse> searchMealByName(String mealName) {
        return apiService.searchMealByName(mealName);
    }
    public Observable<MealResponse> listMealsByFirstLetter(String firstLetter) {
        return apiService.listMealsByFirstLetter(firstLetter);
    }
}
