package com.example.foodapp.ui.home.presenter;

import android.util.Log;

import com.example.foodapp.data.remote.model.CountryMapper;
import com.example.foodapp.data.remote.ApiCallback;
import com.example.foodapp.data.remote.IPApi.IpApiClient;
import com.example.foodapp.data.remote.IPApi.IpApiResponse;
import com.example.foodapp.data.remote.MealApi.AreasResponse;
import com.example.foodapp.data.remote.MealApi.CategoriesResponse;
import com.example.foodapp.data.remote.MealApi.IngredientResponse;
import com.example.foodapp.data.remote.MealApi.MealResponse;
import com.example.foodapp.data.remote.MealApi.MealsCountryResponse;
import com.example.foodapp.data.repository.HomeRepository;
import com.example.foodapp.data.repository.LocationRepository;
import com.example.foodapp.ui.home.view.HomeView;

import java.util.Collections;

public class HomePresenter {
    private final HomeView view;
    private final HomeRepository homeRepository;
    private final LocationRepository locationRepository;

    public HomePresenter(HomeView view, HomeRepository homeRepository, LocationRepository locationRepository) {
        this.view = view;
        this.homeRepository = homeRepository;
        this.locationRepository = locationRepository;
    }
    public void loadSelectedMeal(String mealId) {
        homeRepository.fetchMealDetailsFromAPI(mealId,new ApiCallback<MealResponse>(){
            @Override
            public void onSuccess(MealResponse response) {
                view.showMealDetails(response.getMeals().get(0));
            }
            @Override
            public void onFailure(String errorMessage) {
                view.showMealDetails(null);
                Log.e("HomePresenter", "Error fetching meal details: " + errorMessage);
            }
        });
    }
    public void loadRandomMeal() {
        homeRepository.fetchRandomMealFromAPI(new ApiCallback<MealResponse>() {
            @Override
            public void onSuccess(MealResponse response) {
                if (response != null && response.getMeals() != null && !response.getMeals().isEmpty()) {
                    view.showRandomMeal(response.getMeals().get(0));
                } else {
                    view.showRandomMeal(null);
                }
            }
            @Override
            public void onFailure(String errorMessage) {
                Log.e("HomePresenter", "Error fetching random meal: " + errorMessage);
            }
        });
    }

    public void loadPopularMeals() {
        locationRepository.fetchLocationFromAPI(new ApiCallback<IpApiResponse>() {
            @Override
            public void onSuccess(IpApiResponse result) {
                String country = IpApiClient.formatCountryForMealDB(result.getCountry());
                homeRepository.fetchPopularMealsFromAPI(country, new ApiCallback<MealsCountryResponse>() {
                    @Override
                    public void onSuccess(MealsCountryResponse response) {
                        if (response != null && response.getMeals() != null) {
                            view.showPopularMeals(response.getMeals(),country);
                        } else {
                            view.showPopularMeals(Collections.emptyList(),"empty");
                        }
                    }
                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e("HomePresenter", "Error fetching popular meals: " + errorMessage);
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("HomePresenter", "Error fetching location: " + errorMessage);
            }
        });
    }

    public void loadCountries() {
        homeRepository.fetchCountriesFromAPI(new ApiCallback<AreasResponse>() {
            @Override
            public void onSuccess(AreasResponse response) {
                if (response != null && response.getAreas() != null) {
                    view.showCountries(CountryMapper.getCountriesByMappedValues(response.getAreas()));
                } else {
                    view.showCountries(Collections.emptyList());
                }
            }
            @Override
            public void onFailure(String errorMessage) {
                Log.e("HomePresenter", "Error fetching countries: " + errorMessage);
            }
        });
    }

    public void loadCategories() {
        homeRepository.fetchCategoriesFromAPI(new ApiCallback<CategoriesResponse>() {
            @Override
            public void onSuccess(CategoriesResponse response) {
                if (response != null && response.getCategories() != null) {
                    view.showCategories(response.getCategories());
                } else {
                    view.showCategories(Collections.emptyList());
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("HomePresenter", "Error fetching categories: " + errorMessage);
            }
        });
    }

    public void loadIngredients() {
        homeRepository.fetchIngredientsFromAPI(new ApiCallback<IngredientResponse>() {
            @Override
            public void onSuccess(IngredientResponse result) {
                if (result != null && result.getIngredients() != null) {
                    view.showIngredients(result.getIngredients());
                } else {
                    view.showIngredients(Collections.emptyList());
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("HomePresenter", "Error fetching ingredients: " + errorMessage);
            }
        });
    }







}
