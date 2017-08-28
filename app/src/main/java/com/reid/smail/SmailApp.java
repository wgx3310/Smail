package com.reid.smail;

import android.app.Application;
import android.content.Context;

/**
 * Created by reid on 2017/8/22.
 */

public class SmailApp extends Application {

    private static Context sContext;
    public static Context getContext(){
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }
}
