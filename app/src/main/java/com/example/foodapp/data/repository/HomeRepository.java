package com.example.foodapp.data.repository;

import com.example.foodapp.data.remote.ApiCallback;
import com.example.foodapp.data.remote.MealApi.ApiClient;
import com.example.foodapp.data.remote.MealApi.ApiService;
import com.example.foodapp.data.remote.MealApi.AreasResponse;
import com.example.foodapp.data.remote.MealApi.CategoriesResponse;
import com.example.foodapp.data.remote.MealApi.IngredientResponse;
import com.example.foodapp.data.remote.MealApi.MealResponse;
import com.example.foodapp.data.remote.MealApi.MealsCountryResponse;

import retrofit2.Call;

public class HomeRepository {

    private final ApiService apiService;

    public HomeRepository() {
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    public void fetchRandomMealFromAPI(ApiCallback callback) {
        Call<MealResponse> call = apiService.getRandomMeal();
        ApiClient.makeNetworkCall(call, callback);
    }

    public void fetchPopularMealsFromAPI(String country, ApiCallback callback) {
        Call<MealsCountryResponse> call = apiService.getMealsByArea(country);
        ApiClient.makeNetworkCall(call, callback);
    }

    public void fetchCategoriesFromAPI(ApiCallback callback) {
        Call<CategoriesResponse> call = apiService.getCategories();
        ApiClient.makeNetworkCall(call, callback);
    }

    public void fetchCountriesFromAPI(ApiCallback callback) {
        Call<AreasResponse> call = apiService.getAreas();
        ApiClient.makeNetworkCall(call, callback);
    }

    public void fetchIngredientsFromAPI(ApiCallback callback) {
        Call<IngredientResponse> call = apiService.getIngredients();
        ApiClient.makeNetworkCall(call, callback);
    }

}
