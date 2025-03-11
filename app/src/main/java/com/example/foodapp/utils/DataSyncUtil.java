package com.example.foodapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.foodapp.data.repository.FavoriteMealsRepository;
import com.example.foodapp.data.repository.WeekPlanMealsRepository;
import com.example.foodapp.ui.favorite.presenter.FavoritePresenter;
import com.example.foodapp.ui.weekplan.presenter.WeekPlanPresenter;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class DataSyncUtil {

    private FavoritePresenter favoritePresenter;
    private WeekPlanPresenter weekPlanPresenter;

    public DataSyncUtil(Context context, FavoriteMealsRepository favoriteMealsRepository, WeekPlanMealsRepository weekPlanMealsRepository){
        this.favoritePresenter = new FavoritePresenter(null, favoriteMealsRepository);
        this.weekPlanPresenter = new WeekPlanPresenter(context,null, weekPlanMealsRepository, favoriteMealsRepository);
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
