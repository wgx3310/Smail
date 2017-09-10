package com.reid.smail.io.offline;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

/**
 * Created by reid on 2017/9/10.
 */

public class Downloader {

    public static void download(final Context context, String url, final String path){
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
