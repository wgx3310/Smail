package com.reid.smail.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reid.smail.R;
import com.reid.smail.adapter.holder.BaseVH;
import com.reid.smail.adapter.holder.weather.DailyForecastVH;
import com.reid.smail.adapter.holder.weather.HourlyForecastVH;
import com.reid.smail.adapter.holder.weather.NowWeatherVH;
import com.reid.smail.adapter.holder.weather.SuggestionVH;
import com.reid.smail.model.weather.Weather;

/**
 * Created by reid on 2017/11/25.
 */

public class WeatherAdapter extends RecyclerView.Adapter<BaseVH<Weather>> {

    private Weather mWeather = new Weather();

    public void setData(Weather weather){
        mWeather = weather;
        notifyDataSetChanged();
    }

    @Override
    public BaseVH<Weather> onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseVH<Weather> vh = null;
        View view;
        switch (viewType){
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_now_weather, parent, false);
                vh = new NowWeatherVH(view);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_list, parent, false);
                vh = new HourlyForecastVH(view);
                break;
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_list, parent, false);
                vh = new SuggestionVH(view);
                break;
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_list, parent, false);
                vh = new DailyForecastVH(view);
                break;
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(BaseVH<Weather> holder, int position) {
        holder.bindData(mWeather);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mWeather != null && mWeather.isValid()? 4:0;
    }
}
