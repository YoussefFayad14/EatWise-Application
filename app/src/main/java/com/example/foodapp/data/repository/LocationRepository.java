package com.example.foodapp.data.repository;

import com.example.foodapp.data.remote.IPApi.IpApiClient;
import com.example.foodapp.data.remote.IPApi.IpApiResponse;
import com.example.foodapp.data.remote.IPApi.IpApiService;
import com.example.foodapp.data.remote.ApiCallback;
import retrofit2.Call;

public class LocationRepository {
    private final IpApiService ipApiService;
    public LocationRepository() {
        this.ipApiService = IpApiClient.getClient().create(IpApiService.class);
    }

    public void fetchLocationFromAPI(ApiCallback<IpApiResponse> callback) {
        Call<IpApiResponse> call = ipApiService.getIpInfo();
        IpApiClient.makeNetworkCall(call, callback);
    }
}
