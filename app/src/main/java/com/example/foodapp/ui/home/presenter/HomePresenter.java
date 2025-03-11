package com.example.foodapp.ui.home.presenter;

import android.annotation.SuppressLint;

import com.example.foodapp.data.remote.model.CountryMapper;
import com.example.foodapp.data.repository.MealsRepository;
import com.example.foodapp.data.repository.LocationRepository;
import com.example.foodapp.ui.home.view.HomeView;
import com.example.foodapp.utils.CachedList;

import java.util.Collections;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter {
    private final HomeView view;
    private final MealsRepository mealsRepository;
    private final LocationRepository locationRepository;
    private final CachedList cachedList = CachedList.getInstance();

    public HomePresenter(HomeView view, MealsRepository mealsRepository, LocationRepository locationRepository) {
        this.view = view;
        this.mealsRepository = mealsRepository;
        this.locationRepository = locationRepository;
    }
    @SuppressLint("CheckResult")
    public void loadSelectedMeal(String mealId) {
        mealsRepository.fetchMealDetailsFromAPI(mealId)
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
        mealsRepository.fetchRandomMealFromAPI()
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
                    return mealsRepository.fetchMealsByArea(ref.country);
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
        if (cachedList.getCountries() != null) {
            view.showCountries(cachedList.getCountries());
            return;
        }
        mealsRepository.fetchCountriesFromAPI()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response != null && response.getAreas() != null) {
                                cachedList.saveCache("countries", response.getAreas());
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
        if (cachedList.getCategories() != null) {
            view.showCategories(cachedList.getCategories());
            return;
        }
        mealsRepository.fetchCategoriesFromAPI()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response != null && response.getCategories() != null) {
                                cachedList.saveCache("categories", response.getCategories());
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
        if (cachedList.getIngredients() != null) {
            view.showIngredients(cachedList.getIngredients());
            return;
        }
        mealsRepository.fetchIngredientsFromAPI()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response != null && response.getIngredients() != null) {
                                cachedList.saveCache("ingredients", response.getIngredients());
                                view.showIngredients(response.getIngredients());
                            } else {
                                view.showIngredients(Collections.emptyList());
                            }
                        },
                        throwable -> view.showAlert("Error fetching ingredients",true)
                );
    }

}
