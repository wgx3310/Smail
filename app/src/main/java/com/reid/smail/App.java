package com.reid.smail;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams;
import com.reid.smail.io.offline.OkHttpConnection;

import smail.util.AppCompat;


/**
 * Created by reid on 2017/8/22.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompat.init(this);

        DownloadMgrInitialParams.InitCustomMaker initCustomMaker = FileDownloader.setupOnApplicationOnCreate(this);
        initCustomMaker.connectionCreator(new OkHttpConnection.Creator());
    }
}
