package com.example.projekt.db;

public class CountryJson {
    public CountryJson(String name, String continent) {
        this.name = name;
        this.continent = continent;
    }

    public String getName() {
        return name;
    }

    public String getContinent() {
        return continent;
    }

    private final String name;
    private final String continent;



}
