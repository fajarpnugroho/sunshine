package com.example.android.sunshine.data;


import java.util.List;

public class WeatherItem {
    public String dt;

    public TempObject temp;

    public double pressure;

    public double humidity;

    public List<WeatherObject> weather;

    public double speed;

    public int deg;

    public int cloud;

    public static class TempObject {
        public double day;

        public double min;

        public double max;

        public double night;

        public double eve;

        public double morn;
    }

    public static class WeatherObject {
        public long id;

        public String main;

        public String description;

        public String icon;
    }
}
