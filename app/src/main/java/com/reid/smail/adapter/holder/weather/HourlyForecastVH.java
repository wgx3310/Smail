package com.reid.smail.adapter.holder.weather;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reid.smail.R;
import com.reid.smail.adapter.holder.BaseVH;
import com.reid.smail.model.weather.Weather;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reid on 2017/11/25.
 */

public class HourlyForecastVH extends BaseVH<Weather> {

    private TextView mTitle;
    private RecyclerView mRecycler;
    private Adapter mAdapter;

    public HourlyForecastVH(View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.list_title);
        mRecycler = itemView.findViewById(R.id.suggestion_recycler_view);
        mRecycler.setFocusable(false);
        mRecycler.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
    }

    @Override
    public void onBindData(Weather data) {
        if (data == null || data.hourly_forecast == null || data.hourly_forecast.isEmpty()){
            itemView.setVisibility(View.GONE);
            return;
        }

        mTitle.setText("24小时内天气");
        mAdapter = new Adapter(data.hourly_forecast);
        mRecycler.setAdapter(mAdapter);
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.VH>{

        private List<Weather.HourlyForecast> forecasts = new ArrayList<>();

        public Adapter(List<Weather.HourlyForecast> list){
            forecasts.clear();
            if (list != null && !list.isEmpty()){
                forecasts.addAll(list);
            }
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_weather_hourly_item_view, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.onBindData(forecasts.get(position));
        }

        @Override
        public int getItemCount() {
            return forecasts.size();
        }

        public class VH extends RecyclerView.ViewHolder{

            public TextView mClock;
            public TextView mTemp;
            public TextView mHumidity;
            public TextView mWind;

            public VH(View itemView) {
                super(itemView);

                mClock = itemView.findViewById(R.id.clock);
                mTemp = itemView.findViewById(R.id.temp);
                mHumidity = itemView.findViewById(R.id.humidity);
                mWind = itemView.findViewById(R.id.wind);
            }

            public void onBindData(Weather.HourlyForecast forecast){
                String mDate = forecast.date;
                mClock.setText(
                        mDate.substring(mDate.length() - 5, mDate.length()));
                mTemp.setText(
                        String.format("%s℃", forecast.tmp));
                mHumidity.setText(
                        String.format("%s%%", forecast.hum)
                );
                mWind.setText(
                        String.format("%sKm/h", forecast.wind.spd)
                );
            }
        }
    }

}
