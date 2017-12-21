package com.reid.smail.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.reid.smail.content.SettingKey;
import com.reid.smail.io.Prefs;
import com.reid.smail.model.weather.Weather;
import com.reid.smail.net.loader.WeatherLoader;
import com.reid.smail.util.NotificationHelper;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import reid.utils.Logger;

/**
 * Created by reid on 2017/12/19.
 */

public class AutoUpdateService extends Service {

    public static void startAutoUpdateService(Context context){
        Intent intent = new Intent(context, AutoUpdateService.class);
        context.startService(intent);
    }

    private Disposable disposable;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        synchronized (this){
            if (disposable != null && !disposable.isDisposed()){
                disposable.dispose();
            }

            int period = Prefs.getInt(SettingKey.PERIOD_AUTO_UPDATE, 3);
            disposable = Observable.interval(0, period, TimeUnit.HOURS)
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            fetchCityWeather();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });

        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }

    private void fetchCityWeather() {
        WeatherLoader.get().getWeather("beijing").subscribe(new Consumer<Weather>() {
            @Override
            public void accept(Weather weather) throws Exception {
                NotificationHelper.showWeatherNotification(AutoUpdateService.this, weather);
            }
        });
    }
}
