package com.example.lab4_20212591;

import com.example.lab4_20212591.model.Location;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {

    @GET("search.json")
    Call<List<Location>> buscarUbicaciones(
            @Query("key") String apiKey,
            @Query("q") String location
    );
    @GET("forecast.json")
    Call<ForecastResponse> getForecast(
            @Query("key") String apiKey,
            @Query("q") String location,
            @Query("days") int days
    );
}