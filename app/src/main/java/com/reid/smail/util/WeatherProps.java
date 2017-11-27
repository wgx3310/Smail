package com.reid.smail.util;

import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.SparseArray;

import com.reid.smail.R;
import com.reid.smail.content.SettingKey;
import com.reid.smail.io.Prefs;

import java.util.HashMap;
import java.util.Map;

import reid.utils.AppCompat;
import reid.utils.Logger;

/**
 * Created by reid on 2017/11/25.
 */

public class WeatherProps {

    public static boolean isWeatherInitialized;
    private static int sIconType = 0;

    private static SparseArray<String> sWeatherNameMap;
    private static SparseArray<Integer> sWeatherStateMap;

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
                || sWeatherStateMap == null || sWeatherStateMap.size() == 0){
            isWeatherInitialized = true;

            if (sWeatherStateMap == null){
                sWeatherStateMap = new SparseArray<>();
            }
            sWeatherStateMap.clear();

            int[] codes = AppCompat.getContext().getResources().getIntArray(R.array.weather_code);

            if (sWeatherNameMap == null){
                sWeatherNameMap = new SparseArray<>();
            }
            if (sWeatherNameMap.size() == 0){
                String[] names = AppCompat.getContext().getResources().getStringArray(R.array.weather_name);
                for (int i = 0, size = codes.length; i < size; i++){
                    sWeatherNameMap.put(codes[i], names[i]);
                }
            }

            TypedArray typedArray;
            int defId = R.drawable.ic_unknown_1;
            switch (type){
                case 0:
                    typedArray = AppCompat.getContext().getResources().obtainTypedArray(R.array.weather_icon_1);
                    defId = R.drawable.ic_unknown_1;
                    break;
                case 1:
                    typedArray = AppCompat.getContext().getResources().obtainTypedArray(R.array.weather_icon_2);
                    defId = R.drawable.ic_unknown_2;
                    break;
                default:
                    typedArray = AppCompat.getContext().getResources().obtainTypedArray(R.array.weather_icon_1);
                    defId = R.drawable.ic_unknown_1;
                    break;
            }

            if (typedArray != null){
                for (int i = 0, size = codes.length; i < size; i++){
                    sWeatherStateMap.put(codes[i], typedArray.getResourceId(i, defId));
                }
                typedArray.recycle();
            }
        }
    }

    public static int getState(int code){
        if (!isWeatherInitialized){
            initWeatherState();
        }
        return sWeatherStateMap.get(code);
    }

    public static String getName(int code){
        if (!isWeatherInitialized){
            initWeatherState();
        }

        return sWeatherNameMap.get(code, "未知");
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
