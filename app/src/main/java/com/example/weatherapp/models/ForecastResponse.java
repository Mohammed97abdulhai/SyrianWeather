package com.example.weatherapp.models;

import java.util.List;

public class ForecastResponse {

    private List<WeatherResponse> list;

    public List<WeatherResponse> getResponse() {
        return list;
    }
}
