package com.example.android.sunshine;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sunshine.data.WeatherItem;
import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

import java.util.ArrayList;
import java.util.List;


public class ForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WeatherItem> weatherDatas = new ArrayList<>();

    public void setWeatherDatas(List<WeatherItem>  weatherDatas) {
        this.weatherDatas.clear();
        this.weatherDatas.addAll(weatherDatas);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ForecaseViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (weatherDatas == null) return;

        WeatherItem weatherItem = weatherDatas.get(position);
        ForecaseViewHolder viewHolder = (ForecaseViewHolder) holder;
        viewHolder.bindData(weatherItem, position);
    }

    @Override
    public int getItemCount() {
        return weatherDatas.size();
    }

    public void clearData() {
        weatherDatas.clear();
        notifyDataSetChanged();
    }

    public class BaseHolder extends RecyclerView.ViewHolder {

        public BaseHolder(@LayoutRes int resid, ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(resid, parent, false));
        }
    }

    public class ForecaseViewHolder extends BaseHolder {

        private TextView textView;

        public ForecaseViewHolder(ViewGroup parent) {
            super(R.layout.forecase_item_list, parent);
            textView = (TextView)
                    itemView.findViewById(R.id.tv_weather_data);
        }

        public void bindData(WeatherItem weatherData, int position) {

            long localDate = System.currentTimeMillis();
            long utcDate = SunshineDateUtils.getUTCDateFromLocal(localDate);
            long startDay = SunshineDateUtils.normalizeDate(utcDate);

            long dateTimeMillis = startDay + SunshineDateUtils.DAY_IN_MILLIS * position;
            String date = SunshineDateUtils.getFriendlyDateString(itemView.getContext(),
                    dateTimeMillis, false);

            String description = weatherData.weather.get(0).description;

            String highAndLow = SunshineWeatherUtils.formatHighLows(itemView.getContext(),
                    weatherData.temp.max, weatherData.temp.min);

            textView.setText(String.format("%s - %s - %s", date, description, highAndLow));
        }
    }
}
