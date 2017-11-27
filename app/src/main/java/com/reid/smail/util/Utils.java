package com.reid.smail.util;

import android.text.TextUtils;

import reid.utils.Logger;

/**
 * Created by reid on 2017/11/26.
 */

public class Utils {

    private static final String TAG = "Utils";

    public static int parseInt(String s){
        if (TextUtils.isEmpty(s)) return 0;

        try{
            return Integer.parseInt(s);
        }catch (Throwable t){
            Logger.e(TAG, "parseInt err: " + t.getMessage());
        }
        return 0;
    }
}
