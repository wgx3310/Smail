package com.reid.smail.model.weather;

import java.io.Serializable;
import java.util.List;

/**
 * Created by reid on 2017/11/25.
 */

public class WeatherInfo implements Serializable {

    public List<Weather> HeWeather5;

    @Override
    public String toString() {
        return "WeatherProps{" +
                "HeWeather5=" + HeWeather5 +
                '}';
    }
}
