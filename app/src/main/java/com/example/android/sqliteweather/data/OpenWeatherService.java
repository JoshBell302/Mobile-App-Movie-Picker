package com.example.android.sqliteweather.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherService {
    @GET("forecast")
    Call<FiveDayForecast> fetchForecast(
            @Query("q") String location,
            @Query("units") String units,
            @Query("appid") String apiKey
    );
}
