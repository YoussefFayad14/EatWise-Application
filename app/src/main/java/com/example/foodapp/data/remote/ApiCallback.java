package com.example.foodapp.data.remote;

public interface ApiCallback<T> {
    void onSuccess(T result);
    void onFailure(String errorMessage);
}
