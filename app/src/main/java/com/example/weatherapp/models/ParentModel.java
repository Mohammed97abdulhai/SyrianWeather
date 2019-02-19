package com.example.weatherapp.models;

import java.io.Serializable;

public class ParentModel implements Serializable {

    public String temparature;
    public String hour;
    public String image;
    public long hummidity;
    public double windspeed;

    public String getTemparature() {
        return temparature;
    }

    public String getHour() {
        return hour;
    }

    public String getImage() {
        return image;
    }

    public long getHummidity() {
        return hummidity;
    }

    public double getWindspeed() {
        return windspeed;
    }
}
