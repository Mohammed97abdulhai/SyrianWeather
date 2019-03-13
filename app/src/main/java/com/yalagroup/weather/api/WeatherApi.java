package com.yalagroup.weather.api;

import com.yalagroup.weather.models.ForecastResponse;
import com.yalagroup.weather.models.WeatherResponse;

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
