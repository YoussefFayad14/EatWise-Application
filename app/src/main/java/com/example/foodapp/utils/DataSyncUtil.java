package com.example.foodapp.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.foodapp.data.repository.FavoriteMealRepository;
import com.example.foodapp.data.repository.MealPlanRepository;
import com.example.foodapp.ui.favorite.presenter.FavoritePresenter;
import com.example.foodapp.ui.weekplan.presenter.WeekPlanPresenter;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class DataSyncUtil {

    private FavoritePresenter favoritePresenter;
    private WeekPlanPresenter weekPlanPresenter;

    public DataSyncUtil(FavoriteMealRepository favoriteMealRepository, MealPlanRepository mealPlanRepository){
        this.favoritePresenter = new FavoritePresenter(null, favoriteMealRepository);
        this.weekPlanPresenter = new WeekPlanPresenter(null, mealPlanRepository, favoriteMealRepository);
    }
    @SuppressLint("CheckResult")
    public void syncUserData() {
        Completable.concatArray(
                        favoritePresenter.clearAllFavoriteMeals(),
                        weekPlanPresenter.clearAllMealPlans(),
                        favoritePresenter.syncFavorites(),
                        weekPlanPresenter.syncMealPlans()
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(()->Log.d("DataSyncUtil", "User data synced successfully"))
                .subscribe(
                        () ->{},
                        throwable -> Log.e("DataSyncUtil", "Failed to sync user data", throwable)
                );
    }


}
