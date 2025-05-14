package com.example.lab4_20212591.model;

public class Location {
    //Para evitar problemas y como no se usan operaciones se usa String para la ID
    private String id;
    private String name;
    private String region;
    private String country;
    private double lat;
    private double lon;
    private String url;

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getUrl() {
        return url;
    }
}
