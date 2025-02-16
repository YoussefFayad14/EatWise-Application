package com.example.foodapp.data.remote;

import android.annotation.SuppressLint;
import android.util.Log;
import com.example.foodapp.data.local.model.FavoriteMeal;
import com.example.foodapp.data.local.model.MealPlan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.List;
import java.util.ArrayList;

public class FirebaseService {
    private DatabaseReference db;
    private FirebaseAuth auth;

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public FirebaseService() {
        db = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
    }

    public void addMealToFirestore(FavoriteMeal meal) {
        if (auth.getCurrentUser() == null) return;

        String userId = auth.getCurrentUser().getUid();
        db.child("users").child(userId).child("favoriteMeals").child(meal.getMealId())
                .setValue(meal)
                .addOnSuccessListener(result -> Log.d("RealtimeDB", "Meal added successfully"))
                .addOnFailureListener(error -> Log.e("RealtimeDB", "Failed to sync meal", error));
    }

    public void removeMealFromFirestore(FavoriteMeal meal) {
        if (auth.getCurrentUser() == null) return;

        String userId = auth.getCurrentUser().getUid();
        db.child("users").child(userId).child("favoriteMeals").child(meal.getMealId())
                .removeValue()
                .addOnSuccessListener(result -> Log.d("RealtimeDB", "Meal removed successfully"))
                .addOnFailureListener(error -> Log.e("RealtimeDB", "Failed to delete meal", error));
    }
    public void addPlanToFirestore(MealPlan mealPlan) {
        if (auth.getCurrentUser() == null) return;

        String userId = auth.getCurrentUser().getUid();
        db.child("users").child(userId).child("mealPlans").child(mealPlan.getMealId())
                .setValue(mealPlan)
                .addOnSuccessListener(result -> Log.d("RealtimeDB", "Meal plan added successfully"))
                .addOnFailureListener(error -> Log.e("RealtimeDB", "Failed to sync meal plan", error));
    }

    public void removePlanFromFirestore(MealPlan mealPlan) {
        if (auth.getCurrentUser() == null) return;

        String userId = auth.getCurrentUser().getUid();
        db.child("users").child(userId).child("mealPlans").child(mealPlan.getMealId())
                .removeValue()
                .addOnSuccessListener(result -> Log.d("RealtimeDB", "Meal plan removed successfully"))
                .addOnFailureListener(error -> Log.e("RealtimeDB", "Failed to delete meal plan", error));
    }

    @SuppressLint("CheckResult")
    public @NonNull Observable<List<FavoriteMeal>> fetchFavoritesFromFirebase() {
        return Observable.<List<FavoriteMeal>>create(emitter -> {
            String userId = auth.getCurrentUser().getUid();
            db.child("users").child(userId).child("favoriteMeals")
                    .get()
                    .addOnSuccessListener(snapshot -> {
                        List<FavoriteMeal> favoriteMeals = new ArrayList<>();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            FavoriteMeal meal = data.getValue(FavoriteMeal.class);
                            if (meal != null) {
                                favoriteMeals.add(meal);
                            }
                        }
                        emitter.onNext(favoriteMeals);
                        emitter.onComplete();
                    })
                    .addOnFailureListener(error -> {
                        emitter.onError(error);
                    });
            }
        ).subscribeOn(Schedulers.io());
    }

    @SuppressLint("CheckResult")
    public @NonNull Observable<List<MealPlan>> fetchMealPlansFromFirebase() {
        return Observable.<List<MealPlan>>create(emitter -> {
            String userId = auth.getCurrentUser().getUid();
            db.child("users").child(userId).child("mealPlans")
                    .get()
                    .addOnSuccessListener(snapshot -> {
                        List<MealPlan> mealPlans = new ArrayList<>();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            MealPlan mealPlan = data.getValue(MealPlan.class);
                            if (mealPlan != null) {
                                mealPlans.add(mealPlan);
                            }
                        }
                        emitter.onNext(mealPlans);
                        emitter.onComplete();
                    })
                    .addOnFailureListener( error -> {
                        emitter.onError(error);
                    });
        }).subscribeOn(Schedulers.io());
    }



}
