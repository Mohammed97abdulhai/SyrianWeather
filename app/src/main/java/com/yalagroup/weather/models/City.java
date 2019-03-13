package com.yalagroup.weather.models;

public class City {

    private long id;
    private String name;

    public City(String name, long id) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
