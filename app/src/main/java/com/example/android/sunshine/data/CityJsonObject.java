package com.example.android.sunshine.data;


public class CityJsonObject {

    public long id;

    public String name;

    public CoordObject coord;

    public String country;

    public int population;

    public static class CoordObject {
        public double lon;

        public double lat;
    }
}
