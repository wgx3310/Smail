package com.reid.smail.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.reid.smail.content.SettingKey;
import com.reid.smail.io.Prefs;
import com.reid.smail.model.weather.Weather;
import com.reid.smail.ui.WeatherActivity;

import reid.utils.AppCompat;

/**
 * Created by reid on 2017/12/21.
 */

public class NotificationHelper {

    private static final int NOTIFICATION_ID = 37;

    public static void showWeatherNotification(Context context, Weather weather){
        if (weather == null){
            return;
        }

        if (context == null) {
            context = AppCompat.getContext();
        }

        if (context == null){
            return;
        }

        Notification.Builder builder = new Notification.Builder(context);
        Intent intent = new Intent(context, WeatherActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = builder.setContentIntent(pendingIntent)
                .setContentTitle(weather.basic.city)
                .setContentText(String.format("%s 当前温度: %s℃ ", weather.now.cond.txt, weather.now.tmp))
                .setSmallIcon(WeatherProps.getState(Utils.parseInt(weather.now.cond.code)))
                .build();
        boolean model = Prefs.getBoolean(SettingKey.MODEL_WEATHER_NOTIFICATION, true);
        notification.flags = model ? Notification.FLAG_ONGOING_EVENT : Notification.FLAG_AUTO_CANCEL;

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);
    }
}
