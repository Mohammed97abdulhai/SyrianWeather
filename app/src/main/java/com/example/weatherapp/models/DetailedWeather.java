package com.example.weatherapp.models;

public class DetailedWeather {

    double temp;
    double pressure;
    long humidity;
    double temp_min;
    double temp_max;
    double sea_level;
    double grnd_level;

    public double getSea_level() {
        return sea_level;
    }

    public double getGrnd_level() {
        return grnd_level;
    }

    public double getTemp() {
        return temp;
    }

    public double getPressure() {
        return pressure;
    }

    public long getHumidity() {
        return humidity;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }
}
