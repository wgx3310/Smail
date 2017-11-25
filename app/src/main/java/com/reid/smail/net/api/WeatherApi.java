package com.reid.smail.net.api;

import com.reid.smail.model.weather.WeatherInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by reid on 2017/11/25.
 */

public interface WeatherApi {

    @GET("weather")
    Observable<WeatherInfo> getWeather(@Query("city") String city, @Query("key") String key);
}
