package com.yalagroup.weather.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {

    private long dt;

    private Coordination coord;

    private List<WeatherInfo> weather;

    @SerializedName("main")
    private DetailedWeather detailedweather;

    private long id;

    private String name;

    private int cod;

    private Wind wind;

    public Wind getWind()
    {
        return wind;
    }

    public long getDt()
    {
        return dt;
    }

    public Coordination getCoord() {
        return coord;
    }

    public List<WeatherInfo> getWeather() {
        return weather;
    }

    public DetailedWeather getDetailedWeather() {
        return detailedweather;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCod() {
        return cod;
    }

}
