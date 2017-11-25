package com.reid.smail.adapter.holder.weather;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.reid.smail.R;
import com.reid.smail.adapter.holder.BaseVH;
import com.reid.smail.model.weather.Weather;
import com.reid.smail.util.WeatherUtils;

import reid.utils.Logger;

/**
 * Created by reid on 2017/11/25.
 */

public class NowWeatherVH extends BaseVH<Weather> {

    private static final String TAG = "NowWeatherVH";

    private ImageView mWeatherIcon;
    TextView tempFlu;
    TextView tempMax;
    TextView tempMin;
    TextView tempPm;
    TextView tempQuality;

    public NowWeatherVH(View itemView) {
        super(itemView);

        mWeatherIcon = itemView.findViewById(R.id.weather_icon);
        tempFlu = itemView.findViewById(R.id.temp_flu);
        tempMax = itemView.findViewById(R.id.temp_max);
        tempMin = itemView.findViewById(R.id.temp_min);
        tempPm = itemView.findViewById(R.id.temp_pm);
        tempQuality = itemView.findViewById(R.id.temp_quality);
    }

    @Override
    public void onBindData(Weather data) {
        super.onBindData(data);
        try {
            tempFlu.setText(String.format("%s℃", data.now.tmp));
            tempMax.setText(
                    String.format("↑ %s ℃", data.daily_forecast.get(0).tmp.max));
            tempMin.setText(
                    String.format("↓ %s ℃", data.daily_forecast.get(0).tmp.min));

            tempPm.setText(String.format("PM2.5: %s μg/m³", data.aqi.city.pm25));
            tempQuality.setText(String.format("空气质量：%s", data.aqi.city.qlty));
            mWeatherIcon.setImageResource(WeatherUtils.getState(data.now.cond.txt));
        } catch (Exception e) {
            Logger.e(TAG, e.toString());
        }
    }
}
