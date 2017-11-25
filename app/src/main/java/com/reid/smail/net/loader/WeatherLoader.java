package com.reid.smail.net.loader;

import com.reid.smail.content.Constant;
import com.reid.smail.model.weather.Weather;
import com.reid.smail.model.weather.WeatherInfo;
import com.reid.smail.net.api.WeatherApi;
import com.reid.smail.net.client.Loader;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by reid on 2017/11/25.
 */

public class WeatherLoader extends Loader<WeatherApi> {

    private WeatherLoader(){

    }

    private static class Internal{
        private static final WeatherLoader instance = new WeatherLoader();
    }

    public static WeatherLoader get(){
        return Internal.instance;
    }

    public Observable<Weather> getWeather(final String city){
        return Impl().getWeather(city, Constant.WEATHER_KEY)
                .flatMap(new Function<WeatherInfo, ObservableSource<Weather>>() {
                    @Override
                    public ObservableSource<Weather> apply(WeatherInfo weatherInfo) throws Exception {
                        if (weatherInfo == null || weatherInfo.HeWeather5 == null
                                || weatherInfo.HeWeather5.isEmpty()
                                || weatherInfo.HeWeather5.get(0) == null){
                            return Observable.error(new Throwable("data is empty"));
                        }

                        String status = weatherInfo.HeWeather5.get(0).status;
                        if ("no more requests".equals(status)) {
                            return Observable.error(new RuntimeException("/(ㄒoㄒ)/~~,API免费次数已用完"));
                        } else if ("unknown city".equals(status)) {
                            return Observable.error(new RuntimeException(String.format("API没有%s", city)));
                        }

                        return Observable.just(weatherInfo.HeWeather5.get(0));
                    }
                }).compose(this.<Weather>transformer());
    }


    @Override
    protected String getBaseUrl() {
        return Constant.BASE_URL_WEATHER;
    }
}
