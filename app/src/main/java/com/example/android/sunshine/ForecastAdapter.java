package com.example.android.sunshine;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;


public class ForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String[] weatherDatas;

    public void setWeatherDatas(String[] weatherDatas) {
        this.weatherDatas = weatherDatas;
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

        String weatherItem = weatherDatas[position];
        ForecaseViewHolder viewHolder = (ForecaseViewHolder) holder;
        viewHolder.bindData(weatherItem);
    }

    @Override
    public int getItemCount() {
        if (weatherDatas== null) return 0;
        return weatherDatas.length;
    }

    public void clearData() {
        weatherDatas = null;
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

        public void bindData(String weatherData) {
            textView.setText(weatherData);
        }
    }
}
