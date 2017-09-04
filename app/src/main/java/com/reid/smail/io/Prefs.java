package com.reid.smail.io;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.reid.smail.SmailApp;

/**
 * Created by reid on 2017/9/3.
 */

public class Prefs {

    protected static final SharedPreferences mPreferences = SmailApp.getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
    protected static final SharedPreferences.Editor mEditor = mPreferences.edit();

    private static boolean checkKeyValid(String key){
        return !TextUtils.isEmpty(key);
    }

    public static boolean putString(String key, String value){
        if (!checkKeyValid(key)) return false;
        mEditor.putString(key, value);
        return mEditor.commit();
    }

    public static boolean putInt(String key, int value){
        if (!checkKeyValid(key)) return false;
        mEditor.putInt(key, value);
        return mEditor.commit();
    }

    public static boolean putLong(String key, long value){
        if (!checkKeyValid(key)) return false;
        mEditor.putLong(key, value);
        return mEditor.commit();
    }

    public static boolean putBoolean(String key, boolean value){
        if (!checkKeyValid(key)) return false;
        mEditor.putBoolean(key, value);
        return mEditor.commit();
    }

    public static boolean putFloat(String key, float value){
        if (!checkKeyValid(key)) return false;
        mEditor.putFloat(key, value);
        return mEditor.commit();
    }

    public static String getString(String key, String defVal){
        if (!checkKeyValid(key)) return defVal;
        return mPreferences.getString(key, defVal);
    }

    public static int getInt(String key, int defVal){
        if (!checkKeyValid(key)) return defVal;
        return mPreferences.getInt(key, defVal);
    }

    public static long getLong(String key, long defVal){
        if (!checkKeyValid(key)) return defVal;
        return mPreferences.getLong(key, defVal);
    }

    public static boolean getBoolean(String key, boolean defVal){
        if (!checkKeyValid(key)) return defVal;
        return mPreferences.getBoolean(key, defVal);
    }

    public static float getFloat(String key, float defVal){
        if (!checkKeyValid(key)) return defVal;
        return mPreferences.getFloat(key, defVal);
    }

    public static boolean remove(String key){
        if (!checkKeyValid(key)) return false;
        mEditor.remove(key);
        return mEditor.commit();
    }

    public static boolean clear(){
        mEditor.clear();
        return mEditor.commit();
    }
}
