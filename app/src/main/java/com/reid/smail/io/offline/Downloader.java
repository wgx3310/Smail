package com.reid.smail.io.offline;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.reid.smail.R;
import com.reid.smail.content.Reminder;

/**
 * Created by reid on 2017/9/10.
 */

public class Downloader {

    public static void download(final Context context, String url, final String path){
        if (context == null || TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) return;
        Reminder.toast(R.string.download_start);

        FileDownloader.getImpl().create(url).setPath(path).setWifiRequired(true).setListener(new FileDownloadSampleListener() {

            @Override
            protected void completed(BaseDownloadTask task) {
                super.completed(task);
                Reminder.toast(context.getString(R.string.download_success, path));
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                super.error(task, e);
                Reminder.toast(R.string.download_failed);
            }
        }).start();
    }
}
