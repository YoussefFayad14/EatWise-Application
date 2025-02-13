package com.example.foodapp.data.repository;

import com.example.foodapp.data.remote.MealApi.ApiClient;
import com.example.foodapp.data.remote.MealApi.ApiService;
import com.example.foodapp.data.remote.MealApi.AreasResponse;
import com.example.foodapp.data.remote.MealApi.CategoriesResponse;
import com.example.foodapp.data.remote.MealApi.IngredientResponse;
import com.example.foodapp.data.remote.MealApi.MealResponse;
import com.example.foodapp.data.remote.MealApi.MealsCountryResponse;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class HomeRepository {

    private final ApiService apiService;

    public HomeRepository() {
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    public Single<MealResponse> fetchMealDetailsFromAPI(String mealId) {
        return apiService.getMealsById(mealId);
    }

    public Single<MealResponse> fetchRandomMealFromAPI() {
        return apiService.getRandomMeal();
    }

    public Observable<MealsCountryResponse> fetchPopularMealsFromAPI(String country) {
        return apiService.getMealsByArea(country);
    }

    public Observable<CategoriesResponse> fetchCategoriesFromAPI() {
        return apiService.getCategories();
    }

    public Observable<AreasResponse> fetchCountriesFromAPI() {
        return apiService.getAreas();
    }

    public Observable<IngredientResponse> fetchIngredientsFromAPI() {
        return apiService.getIngredients();
    }

}
