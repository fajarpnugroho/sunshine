package com.example.android.sunshine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_WEATHER_DATA = "extra_weather_data";

    public static Intent createIntent(Context context, String weatherData) {
        Intent intent = new Intent(context, DetailActivity.class);

        // Intent dapat juga digunakan untuk mengirimkan data.
        intent.putExtra(EXTRA_WEATHER_DATA, weatherData);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // get data from share preference
        int fontSize = Integer.parseInt(sharedPreferences.getString(SettingActivity.PREF_FONT_SIZE,
                SettingActivity.DEFAULT_FONT_SIZE));

        // Cara menangkap data yang dikirimkan dari activity yang lain.
        // menggunakan getIntent().getExtras().getString(KEY) atau getIntent().getStringExtra(KEY)
        String extraData = getIntent().getExtras().getString(EXTRA_WEATHER_DATA);

        TextView tvWeatherData = (TextView) findViewById(R.id.tv_weather_data);
        tvWeatherData.setText(extraData);
        tvWeatherData.setTextSize(fontSize);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_share:
                openShareIntent();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openShareIntent() {

        String extraData = getIntent().getExtras().getString(EXTRA_WEATHER_DATA);

        /**
         * Implicit intent, jenis intent yang digunakan untuk menjalankan activity diluar aplikasi.
         * Biasa juga disebut sebagai Commont Intent. Ada bermacam commont intent, seperti camera, maps, email, dst.
         * Untuk lebih jelas bisa membuka link berikut ini:
         * @link https://developer.android.com/guide/components/intents-common.html
         */
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_TEXT, extraData);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
