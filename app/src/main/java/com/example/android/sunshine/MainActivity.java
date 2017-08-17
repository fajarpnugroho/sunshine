/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.data.WeatherResponse;
import com.example.android.sunshine.services.ServiceManager;
import com.example.android.sunshine.services.WeatherServices;
import com.example.android.sunshine.utilities.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements ForecastAdapter.ItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {


    private ForecastAdapter adapter;
    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;
    private RecyclerView recyclerView;
    private WeatherServices udacityServices;

    // one of storage option on android, using key value pair to store data
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        // use default share preference
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);
        recyclerView.setAdapter(adapter);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ForecastAdapter(this);

        // get data from share preference
        adapter.changeFontSize(Integer
                .parseInt(sharedPreferences.getString(SettingActivity.PREF_FONT_SIZE,
                        SettingActivity.DEFAULT_FONT_SIZE)));


        recyclerView.setAdapter(adapter);
        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle. We didn't make the rules (or the names of Views), we just follow them.
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        // Create service
        udacityServices = ServiceManager.getRetrofitInstance().create(WeatherServices.class);

        /* Once all of our views are setup, we can load the weather data. */
        loadWeatherData();
    }

    public void showLoading() {
        mLoadingIndicator.setVisibility(VISIBLE);
    }

    public void hideLoading() {
        mLoadingIndicator.setVisibility(GONE);
    }

    /**
     * This method will get the user's preferred location for weather, and then tell some
     * background method to get the weather data in the background.
     */
    private void loadWeatherData() {
        showWeatherDataView();

        String location = SunshinePreferences.getPreferredWeatherLocation(this);
        // Doing network call using retrofit 2
        Call<WeatherResponse> call = udacityServices.getStaticWeatherByLocation(location,
                NetworkUtils.format, NetworkUtils.units, NetworkUtils.numDays);

        // Use enqueue for async call
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call,
                                   Response<WeatherResponse> response) {
                // all success response will be handled here

                if (response.isSuccessful()) {
                    // Success mean HTTP Status code 200
                    WeatherResponse weatherResponse = response.body();

                    if (weatherResponse == null) {
                        showErrorMessage();
                        return;
                    }

                    adapter.setWeatherDatas(weatherResponse.list);
                    hideLoading();

                } else {
                    // All HTTP status code 400 will be handled here
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // all error will be handled here
                showErrorMessage();
            }
        });

    }

    /**
     * This method will make the View for the weather data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showWeatherDataView() {
        /* First, make sure the error is invisible */
        showLoading();
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the weather
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        hideLoading();

        /* First, hide the currently visible data */
        recyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.forecast, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            adapter.clearData();
            loadWeatherData();
            return true;
        }

        if (id == R.id.action_setting) {
            openSetting();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openSetting() {
        Intent intent = SettingActivity.createIntent(this);
        startActivity(intent);
    }

    @Override
    public void onItemClick(String extraData) {
        /*
        Explicit Intent: explicit intent adalah jenis intent yang jelas.
        Biasanya digunakan untuk berpindah activity, atau menjalankan service
        pada satu aplikasi yang sama.
         */
        Intent intent = DetailActivity.createIntent(this, extraData);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equalsIgnoreCase(SettingActivity.PREF_FONT_SIZE)) {
            int fontSize = Integer.parseInt(sharedPreferences.getString(key,
                    SettingActivity.DEFAULT_FONT_SIZE));
            adapter.changeFontSize(fontSize);
        }
    }
}