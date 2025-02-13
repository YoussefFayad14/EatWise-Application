package com.example.foodapp.ui.home.presenter;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.foodapp.data.remote.model.CountryMapper;
import com.example.foodapp.data.remote.IPApi.IpApiClient;
import com.example.foodapp.data.repository.HomeRepository;
import com.example.foodapp.data.repository.LocationRepository;
import com.example.foodapp.ui.home.view.HomeView;

import java.util.Collections;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter {
    private final HomeView view;
    private final HomeRepository homeRepository;
    private final LocationRepository locationRepository;

    public HomePresenter(HomeView view, HomeRepository homeRepository, LocationRepository locationRepository) {
        this.view = view;
        this.homeRepository = homeRepository;
        this.locationRepository = locationRepository;
    }
    @SuppressLint("CheckResult")
    public void loadSelectedMeal(String mealId) {
        homeRepository.fetchMealDetailsFromAPI(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showMealDetails(response.getMeals().get(0)),
                        throwable -> {
                            view.showMealDetails(null);
                            view.showAlert("Error fetching meal details",true);
                        }
                );
    }
    @SuppressLint("CheckResult")
    public void loadRandomMeal() {
        homeRepository.fetchRandomMealFromAPI()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response != null && response.getMeals() != null && !response.getMeals().isEmpty()) {
                                view.showRandomMeal(response.getMeals().get(0));
                            } else {
                                view.showRandomMeal(null);
                            }
                        },
                        throwable -> view.showAlert("Error fetching random meal: ",true)
                );
    }

    @SuppressLint("CheckResult")
    public void loadPopularMeals() {
        var ref = new Object() {
            String country = "";
        };
        locationRepository.fetchLocationFromAPI()
                .flatMapObservable(ipApiResponse -> {
                    ref.country = CountryMapper.getMappedCountry(ipApiResponse.getCountry());
                    return homeRepository.fetchPopularMealsFromAPI(ref.country);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealsCountryResponse -> {
                            if (mealsCountryResponse != null && mealsCountryResponse.getMeals() != null) {
                                view.showPopularMeals(mealsCountryResponse.getMeals(), ref.country);
                            } else {
                                view.showPopularMeals(Collections.emptyList(), "");
                            }
                        },
                        throwable -> {
                            view.showAlert("Error fetching data",true);
                            view.showPopularMeals(Collections.emptyList(), "");
                        }
                );

    }


    @SuppressLint("CheckResult")
    public void loadCountries() {
        homeRepository.fetchCountriesFromAPI()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response != null && response.getAreas() != null) {
                                view.showCountries(CountryMapper.getCountriesByMappedValues(response.getAreas()));
                            } else {
                                view.showCountries(Collections.emptyList());
                            }
                        },
                        throwable -> view.showAlert("Error fetching countries",true)
                );
    }

    @SuppressLint("CheckResult")
    public void loadCategories() {
        homeRepository.fetchCategoriesFromAPI()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response != null && response.getCategories() != null) {
                                view.showCategories(response.getCategories());
                            } else {
                                view.showCategories(Collections.emptyList());
                                }
                        },
                        throwable -> view.showAlert("Error fetching categories",true)
                );
    }

    @SuppressLint("CheckResult")
    public void loadIngredients() {
        homeRepository.fetchIngredientsFromAPI()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response != null && response.getIngredients() != null) {
                                view.showIngredients(response.getIngredients());
                            } else {
                                view.showIngredients(Collections.emptyList());
                            }
                        },
                        throwable -> view.showAlert("Error fetching ingredients",true)
                );
    }







}
