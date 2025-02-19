package com.example.foodapp.data.repository;

import com.example.foodapp.data.remote.IPApi.IpApiClient;
import com.example.foodapp.data.remote.IPApi.IpApiResponse;
import com.example.foodapp.data.remote.IPApi.IpApiService;

import io.reactivex.rxjava3.core.Single;

public class LocationRepository {
    private final IpApiService ipApiService;
    public LocationRepository() {
        this.ipApiService = IpApiClient.getClient().create(IpApiService.class);
    }

    public Single<IpApiResponse> fetchLocationFromAPI() {
        return ipApiService.getIpInfo();
    }
}
