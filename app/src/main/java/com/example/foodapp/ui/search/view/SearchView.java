package com.example.foodapp.ui.search.view;

import com.example.foodapp.data.remote.model.Meal;

import java.util.List;

public interface SearchView {
    void updateSearchResults(List<Meal> results);
    void updateFilterOptions(String filterType, List<String> options);
    void clearSearchResults();
    void showAlert(String message,boolean flag);
    void showLoading();
    void hideLoading();
}