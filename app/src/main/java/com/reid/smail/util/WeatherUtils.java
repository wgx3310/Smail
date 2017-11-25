package com.reid.smail.util;

import android.text.TextUtils;
import android.util.ArrayMap;

import com.reid.smail.R;
import com.reid.smail.content.SettingKey;
import com.reid.smail.io.Prefs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by reid on 2017/11/25.
 */

public class WeatherUtils {

    public static boolean isWeatherInitialized;
    private static int sIconType = 0;

    private static Map<String, Integer> sWeatherStateMap;

    private static Map<String, Integer> sWeatherSuggestionIconMap;
    private static Map<String, String> sWeatherSuggestionBriefMap;

    public static void initWeatherState(){
        int originType = Prefs.getInt(SettingKey.TYPE_WEATHER_ICON, 0);
        updateStateMap(originType);
    }

    public static void updateStateMap(int type){

        boolean typeChanged = type != sIconType;
        if (typeChanged){
            sIconType = type;
            Prefs.putInt(SettingKey.TYPE_WEATHER_ICON, sIconType);
        }

        if (!isWeatherInitialized || typeChanged
                || sWeatherStateMap == null || sWeatherStateMap.isEmpty()){
            isWeatherInitialized = true;

            if (sWeatherStateMap == null){
                sWeatherStateMap = new HashMap<>();
            }
            sWeatherStateMap.clear();

            switch (type){
                case 0:
                    sWeatherStateMap.put("未知", R.mipmap.none);
                    sWeatherStateMap.put("晴", R.mipmap.type_one_sunny);
                    sWeatherStateMap.put("阴", R.mipmap.type_one_cloudy);
                    sWeatherStateMap.put("多云", R.mipmap.type_one_cloudy);
                    sWeatherStateMap.put("少云", R.mipmap.type_one_cloudy);
                    sWeatherStateMap.put("晴间多云", R.mipmap.type_one_cloudytosunny);
                    sWeatherStateMap.put("小雨", R.mipmap.type_one_light_rain);
                    sWeatherStateMap.put("中雨", R.mipmap.type_one_light_rain);
                    sWeatherStateMap.put("大雨", R.mipmap.type_one_heavy_rain);
                    sWeatherStateMap.put("阵雨", R.mipmap.type_one_thunderstorm);
                    sWeatherStateMap.put("雷阵雨", R.mipmap.type_one_thunder_rain);
                    sWeatherStateMap.put("霾", R.mipmap.type_one_fog);
                    sWeatherStateMap.put("雾", R.mipmap.type_one_fog);
                    break;
                case 1:
                    sWeatherStateMap.put("未知", R.mipmap.none);
                    sWeatherStateMap.put("晴", R.mipmap.type_two_sunny);
                    sWeatherStateMap.put("阴", R.mipmap.type_two_cloudy);
                    sWeatherStateMap.put("多云", R.mipmap.type_two_cloudy);
                    sWeatherStateMap.put("少云", R.mipmap.type_two_cloudy);
                    sWeatherStateMap.put("晴间多云", R.mipmap.type_two_cloudytosunny);
                    sWeatherStateMap.put("小雨", R.mipmap.type_two_light_rain);
                    sWeatherStateMap.put("中雨", R.mipmap.type_two_light_rain);
                    sWeatherStateMap.put("大雨", R.mipmap.type_two_rain);
                    sWeatherStateMap.put("阵雨", R.mipmap.type_two_rain);
                    sWeatherStateMap.put("雷阵雨", R.mipmap.type_two_thunderstorm);
                    sWeatherStateMap.put("霾", R.mipmap.type_two_haze);
                    sWeatherStateMap.put("雾", R.mipmap.type_two_fog);
                    sWeatherStateMap.put("雨夹雪", R.mipmap.type_two_snowrain);
                    break;
            }
        }
    }

    public static int getState(String state){
        if (TextUtils.isEmpty(state)) return R.mipmap.none;

        if (!isWeatherInitialized){
            initWeatherState();
        }

        return sWeatherStateMap.get(state);
    }

    public static void initSuggestion(){
        if (sWeatherSuggestionIconMap == null || sWeatherSuggestionIconMap.isEmpty()
                || sWeatherSuggestionBriefMap == null || sWeatherSuggestionBriefMap.isEmpty()){
            if (sWeatherSuggestionIconMap == null){
                sWeatherSuggestionIconMap = new ArrayMap<>();
            }
            sWeatherSuggestionIconMap.clear();

            if (sWeatherSuggestionBriefMap == null){
                sWeatherSuggestionBriefMap = new ArrayMap<>();
            }
            sWeatherSuggestionBriefMap.clear();

            sWeatherSuggestionIconMap.put("air", R.drawable.icon_air);
            sWeatherSuggestionBriefMap.put("air", "空气指数---");
            sWeatherSuggestionIconMap.put("comf", R.drawable.icon_comf);
            sWeatherSuggestionBriefMap.put("comf", "舒适度---");
            sWeatherSuggestionIconMap.put("cw", R.drawable.icon_cw);
            sWeatherSuggestionBriefMap.put("cw", "洗车指数---");
            sWeatherSuggestionIconMap.put("drsg", R.drawable.icon_cloth);
            sWeatherSuggestionBriefMap.put("drsg", "穿衣指数---");
            sWeatherSuggestionIconMap.put("flu", R.drawable.icon_flu);
            sWeatherSuggestionBriefMap.put("flu", "感冒指数---");
            sWeatherSuggestionIconMap.put("sport", R.drawable.icon_sport);
            sWeatherSuggestionBriefMap.put("sport", "运动指数---");
            sWeatherSuggestionIconMap.put("trav", R.drawable.icon_travel);
            sWeatherSuggestionBriefMap.put("trav", "旅游指数---");
            sWeatherSuggestionIconMap.put("uv", R.drawable.icon_uv);
            sWeatherSuggestionBriefMap.put("uv", "紫外线指数---");
        }
    }

    public static int getSuggestionIcon(String key){
        if (TextUtils.isEmpty(key)){
            return 0;
        }

        if (sWeatherSuggestionIconMap == null
                || sWeatherSuggestionIconMap.isEmpty()){
            initSuggestion();
        }

        return sWeatherSuggestionIconMap.get(key);
    }

    public static String getSuggestionBrief(String key){
        if (TextUtils.isEmpty(key)){
            return null;
        }

        if (sWeatherSuggestionBriefMap == null
                || sWeatherSuggestionBriefMap.isEmpty()){
            initSuggestion();
        }

        return sWeatherSuggestionBriefMap.get(key);
    }
}
