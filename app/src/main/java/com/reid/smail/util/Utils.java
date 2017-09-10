package com.reid.smail.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
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

    public static void downloadImage(final Context context, String url, final String path){
        if (context == null || TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) return;
        Toast.makeText(context, "下载开始",Toast.LENGTH_SHORT).show();

        FileDownloader.getImpl().create(url).setPath(path).setWifiRequired(true).setListener(new FileDownloadSampleListener() {

            @Override
            protected void completed(BaseDownloadTask task) {
                super.completed(task);
                Toast.makeText(context, "下载成功，文件保存至:" + path, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                super.error(task, e);
                Toast.makeText(context, "下载失败，请重试", Toast.LENGTH_SHORT).show();
            }
        }).start();
    }
}
