package com.example.foodapp.data.remote.IPApi;

import com.google.gson.annotations.SerializedName;

public class IpApiResponse {
    @SerializedName("country")
    private String country;

    public String getCountry() {
        return country;
    }
}
