package com.reid.smail;

import android.app.Application;
import android.content.Context;

import com.liulishuo.filedownloader.FileDownloader;

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

        FileDownloader.setupOnApplicationOnCreate(this);
    }
}
