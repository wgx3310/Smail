package com.reid.smail.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.reid.smail.SmailApp;

/**
 * Created by reid on 2017/8/27.
 */

public class Utils {
    private static final String TAG = "Utils";

    public static boolean isNetConnected(){
        try{
            ConnectivityManager manager = (ConnectivityManager) SmailApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null && manager.getActiveNetworkInfo() != null) {
                NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
                return activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected();
            }
        }catch (Throwable t){
            Log.e(TAG, "isNetConnected err: " + t.getMessage());
        }

        return false;
    }

    public static String appVerName(){
        PackageManager packageManager = SmailApp.getContext().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(SmailApp.getContext().getPackageName(), 0);
            if (packageInfo != null){
                return packageInfo.versionName;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int appVerCode(){
        PackageManager packageManager = SmailApp.getContext().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(SmailApp.getContext().getPackageName(), 0);
            if (packageInfo != null){
                return packageInfo.versionCode;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return -1;
    }
}
