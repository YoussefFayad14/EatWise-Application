package com.example.foodapp.utils;

import com.example.foodapp.data.remote.model.Area;
import com.example.foodapp.data.remote.model.Category;
import com.example.foodapp.data.remote.model.Ingredient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachedList {

    private static CachedList instance;

    private final Map<String, List<?>> cache;

    private CachedList() {
        cache = new HashMap<>();
    }

    public static synchronized CachedList getInstance() {
        if (instance == null) {
            instance = new CachedList();
        }
        return instance;
    }

    public void saveCache(String key, List<?> data) {
        cache.put(key, data);
    }

    public List<Category> getCategories() {
        return (List<Category>) cache.getOrDefault("categories", null);
    }

    public List<Area> getCountries() {
        return (List<Area>) cache.getOrDefault("countries", null);
    }

    public List<Ingredient> getIngredients() {
        return (List<Ingredient>) cache.getOrDefault("ingredients", null);
    }
}
