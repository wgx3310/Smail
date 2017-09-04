package com.reid.smail.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.reid.smail.SmailApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
        Toast.makeText(context, "开始下载", Toast.LENGTH_SHORT).show();
        Glide.with(context).load(url).asBitmap().toBytes().into(new SimpleTarget<byte[]>() {
            @Override
            public void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                save2File(context, resource, path);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                Toast.makeText(context, "下载失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void save2File(Context context, byte[] bytes, String path) {
        if (bytes == null || TextUtils.isEmpty(path)) return;
        FileOutputStream fs = null;
        try {
            File file = new File(path);
            File dir = file.getParentFile();
            if (!dir.exists()){
                dir.mkdirs();
            }
            fs = new FileOutputStream(path);
            fs.write(bytes);
            fs.close();
            fs = null;
            Toast.makeText(context, "下载成功，文件保存至:" + path, Toast.LENGTH_SHORT).show();
        } catch (Throwable e) {
            e.printStackTrace();
            Toast.makeText(context, "下载失败，请重试", Toast.LENGTH_SHORT).show();
        } finally {
            if (fs != null){
                try {
                    fs.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
