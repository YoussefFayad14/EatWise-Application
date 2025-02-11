package com.example.foodapp.data.remote.IPApi;

import com.example.foodapp.data.remote.model.CountryMapper;
import com.example.foodapp.data.remote.ApiCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IpApiClient {
    private static final String BASE_URL = "http://ip-api.com/";
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static <T> void makeNetworkCall(Call<T> call, ApiCallback<T> callback) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Error: Response is null or unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.onFailure("Network Error: " + t.getMessage());
            }
        });
    }

    public static String formatCountryForMealDB(String country) {
        return CountryMapper.getMappedCountry(country);
    }
}
