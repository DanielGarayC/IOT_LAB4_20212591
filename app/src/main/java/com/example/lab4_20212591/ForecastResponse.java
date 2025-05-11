package com.example.lab4_20212591;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastResponse {
    private Forecast forecast;

    public Forecast getForecast() {
        return forecast;
    }

    public static class Forecast {
        @SerializedName("forecastday")
        private List<ForecastDay> forecastday;

        public List<ForecastDay> getForecastday() {
            return forecastday;
        }
    }
}