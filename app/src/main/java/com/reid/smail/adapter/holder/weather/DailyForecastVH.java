package com.reid.smail.adapter.holder.weather;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reid.smail.R;
import com.reid.smail.adapter.holder.BaseVH;
import com.reid.smail.model.weather.Weather;
import com.reid.smail.util.Utils;
import com.reid.smail.util.WeatherProps;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reid on 2017/11/25.
 */

public class DailyForecastVH extends BaseVH<Weather> {

    private TextView mTitle;
    private RecyclerView mRecycler;
    private Adapter mAdapter;

    public DailyForecastVH(View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.list_title);
        mRecycler = itemView.findViewById(R.id.suggestion_recycler_view);
        mRecycler.setFocusable(false);
        mRecycler.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
    }

    @Override
    public void onBindData(Weather data) {
        if (data == null || data.daily_forecast == null || data.daily_forecast.isEmpty()){
            itemView.setVisibility(View.GONE);
            return;
        }

        mTitle.setText("一周内天气");
        mAdapter = new Adapter(data.daily_forecast);
        mRecycler.setAdapter(mAdapter);
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.VH>{

        private List<Weather.DailyForecast> forecasts = new ArrayList<>();

        public Adapter(List<Weather.DailyForecast> list){
            forecasts.clear();
            if (list != null && !list.isEmpty()){
                forecasts.addAll(list);
            }
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_weather_daily_item_view, parent, false);
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

            public ImageView mIcon;
            public TextView mDate;
            public TextView mTemp;
            public TextView mText;

            public VH(View itemView) {
                super(itemView);

                mIcon = itemView.findViewById(R.id.forecast_icon);
                mDate = itemView.findViewById(R.id.forecast_date);
                mTemp = itemView.findViewById(R.id.forecast_temp);
                mText = itemView.findViewById(R.id.forecast_txt);
            }

            public void onBindData(Weather.DailyForecast forecast){
                mDate.setText(forecast.date);
                mIcon.setImageResource(WeatherProps.getState(Utils.parseInt(forecast.cond.code_d)));
                mTemp.setText(String.format("%s℃ - %s℃", forecast.tmp.min, forecast.tmp.max));
                mText.setText(
                        String.format("%s。 %s %s %s km/h。 降水几率 %s%%。",
                                forecast.cond.txt_d,
                                forecast.wind.sc,
                                forecast.wind.dir,
                                forecast.wind.spd,
                                forecast.pop));
            }
        }
    }
}
