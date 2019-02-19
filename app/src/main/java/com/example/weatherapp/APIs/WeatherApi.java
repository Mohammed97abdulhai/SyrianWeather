package com.example.weatherapp.APIs;

import com.example.weatherapp.models.ForecastResponse;
import com.example.weatherapp.models.WeatherResponse;

import java.lang.annotation.Target;

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
