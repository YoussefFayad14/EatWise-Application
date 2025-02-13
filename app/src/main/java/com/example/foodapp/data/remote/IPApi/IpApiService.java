package com.example.foodapp.data.remote.IPApi;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IpApiService {
    @GET("json/")
    Single<IpApiResponse> getIpInfo();
}
