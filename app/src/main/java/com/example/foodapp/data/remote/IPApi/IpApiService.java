package com.example.foodapp.data.remote.IPApi;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IpApiService {
    @GET("json/")
    Call<IpApiResponse> getIpInfo();
}
