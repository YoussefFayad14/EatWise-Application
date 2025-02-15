package com.example.foodapp.ui.search.presenter;

import android.annotation.SuppressLint;
import android.util.Log;

import com.airbnb.lottie.L;
import com.example.foodapp.data.remote.MealApi.MealResponse;
import com.example.foodapp.data.remote.MealApi.MealsCountryResponse;
import com.example.foodapp.data.remote.model.Area;
import com.example.foodapp.data.remote.model.Category;
import com.example.foodapp.data.remote.model.Ingredient;
import com.example.foodapp.data.remote.model.Meal;
import com.example.foodapp.data.repository.HomeRepository;
import com.example.foodapp.ui.search.view.SearchView;
import com.example.foodapp.utils.CachedList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;

public class SearchPresenter {
    private SearchView view;
    private HomeRepository homeRepository;
    private List<Meal> cachedMeals = new ArrayList<>();
    private CachedList cachedList = CachedList.getInstance();
    private List<String> selectedCategories = new ArrayList<>();
    private List<String> selectedCountries = new ArrayList<>();
    private List<String> selectedIngredients = new ArrayList<>();

    public SearchPresenter(SearchView view, HomeRepository homeRepository) {
        this.view = view;
        this.homeRepository = homeRepository;
    }

    @SuppressLint("CheckResult")
    public void loadMeals() {
        view.showLoading();

        List<Observable<List<Meal>>> categoryRequests = selectedCategories.stream()
                .map(category -> homeRepository.fetchMealsByCategory(category)
                        .map(MealResponse::getMeals)
                        .onErrorReturnItem(Collections.emptyList()))
                .collect(Collectors.toList());

        List<Observable<List<Meal>>> countryRequests = selectedCountries.stream()
                .map(country -> homeRepository.fetchMealsByArea(country)
                        .map(MealResponse::getMeals)
                        .onErrorReturnItem(Collections.emptyList()))
                .collect(Collectors.toList());

        List<Observable<List<Meal>>> ingredientRequests = selectedIngredients.stream()
                .map(ingredient -> homeRepository.fetchMealsByIngredient(ingredient)
                        .map(MealResponse::getMeals)
                        .onErrorReturnItem(Collections.emptyList()))
                .collect(Collectors.toList());

        List<Observable<List<Meal>>> allRequests = new ArrayList<>();
        allRequests.addAll(categoryRequests);
        allRequests.addAll(countryRequests);
        allRequests.addAll(ingredientRequests);

        Observable.zip(allRequests, results -> {
                    List<Meal> combinedMeals = new ArrayList<>();
                    for (Object result : results) {
                        combinedMeals.addAll((List<Meal>) result);
                    }
                    return combinedMeals;
                })
                .flatMapIterable(meals -> meals)
                .distinct(Meal::getIdMeal)
                .flatMapSingle(meal -> homeRepository.fetchMealDetailsFromAPI(meal.getIdMeal())
                        .map(MealResponse::getMeals)
                        .map(mealDetails -> mealDetails.isEmpty() ? meal : mealDetails.get(0))
                        .onErrorReturnItem(meal)
                )
                .toList()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {
                            Log.d("Debug", "Meals loaded: " + meals.size());
                            cachedMeals.clear();
                            cachedMeals.addAll(meals);

                            view.hideLoading();
                            view.updateSearchResults(cachedMeals);
                        },
                        throwable -> {
                            Log.e("Debug", "Error loading meals", throwable);
                            view.showAlert("Error fetching meals", true);
                            view.hideLoading();
                        }
                );
    }

    @SuppressLint("CheckResult")
    public void loadFilterOptions() {
        view.updateFilterOptions("categories", cachedList.getCategories().stream().map(Category::getStrCategory).collect(Collectors.toList()));
        view.updateFilterOptions("countries", cachedList.getCountries().stream().map(Area::getName).collect(Collectors.toList()));
        view.updateFilterOptions("ingredients", cachedList.getIngredients().stream().map(Ingredient::getName).collect(Collectors.toList()));
    }

    public void filterBy(String filterType, String value){
        switch (filterType){
            case "categories":
                selectedCategories.clear();
                selectedCategories.add(value);
                break;
            case "countries":
                selectedCategories.clear();
                selectedCountries.add(value);
                break;
            case "ingredients":
                selectedCategories.clear();
                selectedIngredients.add(value);
                break;
    }
        loadMeals();
    }

    public void setFilters(List<String> categoryFilter, List<String> countryFilter, List<String> ingredientFilter) {
        this.selectedCategories = categoryFilter;
        this.selectedCountries = countryFilter;
        this.selectedIngredients = ingredientFilter;
        loadMeals();
    }



    @SuppressLint("CheckResult")
    public void searchQueryChanged(String query) {
        Observable.just(query)
            .debounce(200, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    result -> selectSearchType(result),
                    throwable -> view.showAlert("Error fetching meals", true)
            );
    }

    private void selectSearchType(String query) {
        if (!query.isEmpty()) {
            filterMeals(query);
        } else {
            selectedCategories.clear();
            selectedCountries.clear();
            selectedIngredients.clear();
            view.clearSearchResults();
        }
    }


    @SuppressLint("CheckResult")
    public void fetchMealsByFirstLetter(String letter) {
        homeRepository.listMealsByFirstLetter(letter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            cachedMeals = response.getMeals() != null ? response.getMeals() : new ArrayList<>();
                            view.updateSearchResults(cachedMeals);
                            view.hideLoading();
                        },
                        throwable -> {
                            view.showAlert("Error fetching meals",true);
                            view.hideLoading();
                        }
                );
    }

    public void filterMeals(String query) {
        List<Meal> filteredMeals = new ArrayList<>();
        for (Meal meal : cachedMeals) {
            boolean matchesCategory = selectedCategories.contains(meal.getCategory().toLowerCase());
            boolean matchesCountry = selectedCountries.contains(meal.getArea());
            boolean matchesIngredient = meal.getIngredients().stream().anyMatch(selectedIngredients::contains);

            if (matchesCategory || matchesCountry || matchesIngredient) {
                filteredMeals.add(meal);
            }
        }

        filteredMeals = filteredMeals.stream()
                .filter(meal -> meal.getMealName() != null && meal.getMealName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());


        if (filteredMeals.isEmpty()){
            view.showAlert("No meals found",true);
            view.updateSearchResults(cachedMeals);
            return;
        }
        view.updateSearchResults(filteredMeals);
    }



}
