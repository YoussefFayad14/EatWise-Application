package com.example.foodapp.ui.favorite.presenter;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import com.example.foodapp.data.local.model.FavoriteMeal;
import com.example.foodapp.data.remote.model.Meal;
import com.example.foodapp.data.repository.FavoriteMealRepository;
import com.example.foodapp.ui.favorite.view.FavoriteView;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePresenter {
    private FavoriteView view;
    private FavoriteMealRepository repository;
    public FavoritePresenter(FavoriteView view, FavoriteMealRepository repository) {
        this.view = view;
        this.repository = repository;
    }
    public void addToFavorites(FavoriteMeal meal) {
        repository.insert(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @SuppressLint("CheckResult")
    public void getFavoriteProducts() {
        repository.getAllFavoriteMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> view.showFavoriteMeals(list));
    }
    public void removeFromFavorites(FavoriteMeal meal) {
        repository.delete(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }




}
