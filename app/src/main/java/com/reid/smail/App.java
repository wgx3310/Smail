package com.reid.smail;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams;
import com.reid.smail.content.Constant;
import com.reid.smail.io.offline.OkHttpConnection;
import com.reid.smail.net.client.ApiClient;
import com.reid.smail.util.WeatherUtils;
import com.squareup.leakcanary.LeakCanary;

import io.reactivex.schedulers.Schedulers;
import reid.utils.AppCompat;
import reid.utils.ProcessHelper;


/**
 * Created by reid on 2017/8/22.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompat.init(this);

        if (ProcessHelper.isMainProcess()){
            DownloadMgrInitialParams.InitCustomMaker initCustomMaker = FileDownloader.setupOnApplicationOnCreate(this);
            initCustomMaker.connectionCreator(new OkHttpConnection.Creator());

            ApiClient.getInstance().register(Constant.BASE_URL_DESIGN);
            ApiClient.getInstance().register(Constant.BASE_URL_WEBSITE);

            if (LeakCanary.isInAnalyzerProcess(this)){
                return;
            }
            LeakCanary.install(this);

            Schedulers.io().createWorker().schedule(new Runnable() {
                @Override
                public void run() {
                    WeatherUtils.initWeatherState();
                    WeatherUtils.initSuggestion();
                }
            });
        }
    }
}
