package com.example.weatherapp.api;

import com.example.weatherapp.models.ForecastResponse;
import com.example.weatherapp.models.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("weather")
    Call<WeatherResponse> getWeatherByCityID(@Query("id") long id,
                                             @Query("APPID") String key);

    @GET("forecast")
    Call<ForecastResponse> getForecastByCityId(@Query("id") long id,
                                               @Query("APPID") String key);
}
