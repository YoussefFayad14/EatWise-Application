package com.example.foodapp.ui.search.presenter;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.foodapp.data.remote.MealApi.MealResponse;
import com.example.foodapp.data.remote.model.Area;
import com.example.foodapp.data.remote.model.Category;
import com.example.foodapp.data.remote.model.Ingredient;
import com.example.foodapp.data.remote.model.Meal;
import com.example.foodapp.data.repository.MealsRepository;
import com.example.foodapp.ui.search.view.SearchView;
import com.example.foodapp.utils.CachedList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;

public class SearchPresenter {
    private SearchView view;
    private MealsRepository mealsRepository;
    private List<Meal> cachedMeals = new ArrayList<>();
    private CachedList cachedList = CachedList.getInstance();
    private String selectedCategory = null;
    private String selectedCountry = null;
    private String selectedIngredient = null;

    public SearchPresenter(SearchView view, MealsRepository mealsRepository) {
        this.view = view;
        this.mealsRepository = mealsRepository;
    }

    private Single<Meal> fetchMealDetails(Meal meal) {
        return mealsRepository.fetchMealDetailsFromAPI(meal.getIdMeal())
                .map(MealResponse::getMeals)
                .map(mealDetails -> mealDetails.isEmpty() ? meal : mealDetails.get(0))
                .onErrorReturnItem(meal);
    }

    @SuppressLint("CheckResult")
    public void loadFilterOptions() {
        Observable.zip(
                        Observable.fromCallable(() -> cachedList.getCategories().stream().map(Category::getStrCategory).collect(Collectors.toList())),
                        Observable.fromCallable(() -> cachedList.getCountries().stream().map(Area::getName).collect(Collectors.toList())),
                        Observable.fromCallable(() -> cachedList.getIngredients().stream().map(Ingredient::getName).collect(Collectors.toList())),
                        (categories, countries, ingredients) -> new Object[]{categories, countries, ingredients}
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        results -> {
                            List<String> categories = (List<String>) results[0];
                            List<String> countries = (List<String>) results[1];
                            List<String> ingredients = (List<String>) results[2];

                            view.updateFilterOptions("categories", categories);
                            view.updateFilterOptions("countries", countries);
                            view.updateFilterOptions("ingredients", ingredients);
                        },
                        throwable -> Log.e("Error", "Error loading filter options", throwable)
                );
    }

    @SuppressLint("CheckResult")
    public void filterBy(String filterType, String value) {
        if ("All".equals(value)) {
            clearAllFilters();
            return;
        }

        selectedCategory = null;
        selectedCountry = null;
        selectedIngredient = null;

        switch (filterType) {
            case "categories":
                selectedCategory = value;
                break;
            case "countries":
                selectedCountry = value;
                break;
            case "ingredients":
                selectedIngredient = value;
                break;
        }

        applyFilters();
    }

    @SuppressLint("CheckResult")
    private void applyFilters() {
        view.showLoading();

        Observable<List<Meal>> source = null;
        if (selectedCategory != null) {
            source = fetchMealsByCategory(selectedCategory);
        } else if (selectedCountry != null) {
            source = fetchMealsByCountry(selectedCountry);
        } else if (selectedIngredient != null) {
            source = fetchMealsByIngredient(selectedIngredient);
        }

        if (source != null) {
            source.flatMapIterable(meals -> meals)
                    .distinct(Meal::getIdMeal)
                    .flatMapSingle(this::fetchMealDetails)
                    .toList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            meals -> {
                                cachedMeals.clear();
                                cachedMeals.addAll(meals);
                                view.hideLoading();
                                view.clearSearchResults();
                                view.updateSearchResults(cachedMeals);
                            },
                            throwable -> {
                                view.hideLoading();
                                view.showAlert("Error loading meals", true);
                            }
                    );
        }
    }

    private Observable<List<Meal>> fetchMealsByCategory(String category) {
        return mealsRepository.fetchMealsByCategory(category)
                .map(MealResponse::getMeals)
                .onErrorReturnItem(Collections.emptyList());
    }

    private Observable<List<Meal>> fetchMealsByCountry(String country) {
        return mealsRepository.fetchMealsByArea(country)
                .map(MealResponse::getMeals)
                .onErrorReturnItem(Collections.emptyList());
    }

    private Observable<List<Meal>> fetchMealsByIngredient(String ingredient) {
        return mealsRepository.fetchMealsByIngredient(ingredient)
                .map(MealResponse::getMeals)
                .onErrorReturnItem(Collections.emptyList());
    }

    @SuppressLint("CheckResult")
    public void searchQueryChanged(String query) {
        Observable.just(query)
                .debounce(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> selectSearchType(result),
                        throwable -> view.showAlert("Error fetching meals", true)
                );
    }

    private void selectSearchType(String query) {
        if (query == null || query.trim().isEmpty()) {
            view.clearSearchResults();
            return;
        }

        if (selectedCategory == null && selectedCountry == null && selectedIngredient == null) {
            if (query.length() == 1) {
                fetchMealsByFirstLetter(query);
            } else {
                filterMeals(query);
            }
        } else {
            filterMeals(query);
        }
    }

    @SuppressLint("CheckResult")
    public void fetchMealsByFirstLetter(String letter) {
        view.showLoading();
        mealsRepository.listMealsByFirstLetter(letter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            cachedMeals.clear();
                            cachedMeals = response.getMeals() != null ? response.getMeals() : new ArrayList<>();
                            view.updateSearchResults(cachedMeals);
                            view.hideLoading();
                        },
                        throwable -> {
                            view.showAlert("Error fetching meals", true);
                            view.hideLoading();
                        }
                );
    }

    private void filterMeals(String query) {
        List<Meal> filteredMeals = cachedMeals.stream()
                .filter(meal -> meal.getMealName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        if (filteredMeals.isEmpty()) {
            view.showAlert("No meals found", true);
        }
        view.updateSearchResults(filteredMeals);
    }

    public void clearAllFilters() {
        selectedCategory = null;
        selectedCountry = null;
        selectedIngredient = null;
        cachedMeals.clear();

        view.clearSearchResults();
        view.updateSearchResults(cachedMeals);
    }
}
