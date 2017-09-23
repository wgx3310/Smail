package com.reid.smail;

import android.app.Application;
import android.content.Context;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams;
import com.reid.smail.io.offline.OkHttpConnection;

import smail.util.Tools;


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
        Tools.setup(this);

        DownloadMgrInitialParams.InitCustomMaker initCustomMaker = FileDownloader.setupOnApplicationOnCreate(this);
        initCustomMaker.connectionCreator(new OkHttpConnection.Creator());
    }
}
