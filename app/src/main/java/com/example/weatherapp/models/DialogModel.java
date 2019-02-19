package com.example.weatherapp.models;

import java.io.Serializable;

public class DialogModel implements Serializable {

    public int humidity;
    public int windSpeed;
    public String temp;

    public int getHumidity() {
        return humidity;
    }

    public int  getWindSpeed() {
        return windSpeed;
    }

    public String getTemp() {
        return temp;
    }
}
