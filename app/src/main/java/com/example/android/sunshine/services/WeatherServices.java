package com.example.android.sunshine.services;


import com.example.android.sunshine.data.WeatherResponse;
import com.example.android.sunshine.utilities.NetworkUtils;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherServices {

    @GET("weather")
    Call<WeatherResponse>
    getDynamicWeatherBYLocation(@Query(NetworkUtils.QUERY_PARAM) String location,
                                @Query(NetworkUtils.FORMAT_PARAM) String format,
                                @Query(NetworkUtils.UNITS_PARAM) String units,
                                @Query(NetworkUtils.DAYS_PARAM) int numDays);

    @GET("staticweather")
    Call<WeatherResponse>
    getStaticWeatherByLocation(@Query(NetworkUtils.QUERY_PARAM) String location,
                               @Query(NetworkUtils.FORMAT_PARAM) String format,
                               @Query(NetworkUtils.UNITS_PARAM) String units,
                               @Query(NetworkUtils.DAYS_PARAM) int numDays);
}
