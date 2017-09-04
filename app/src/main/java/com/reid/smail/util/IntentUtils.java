package com.reid.smail.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.reid.smail.model.Shot;
import com.reid.smail.model.User;
import com.reid.smail.ui.DetailActivity;
import com.reid.smail.ui.UserActivity;

/**
 * Created by reid on 2017/9/2.
 */

public class IntentUtils {
    private static final String TAG = "IntentUtils";

    public static void goUser(Context context, User user){
        if (context == null || user == null){
            return;
        }

        try {
            Intent intent = new Intent(context, UserActivity.class);
            intent.putExtra("user", user);
            context.startActivity(intent);
        }catch (Throwable t){
            Log.e(TAG, "goUser err: " + t.getMessage());
        }
    }

    public static void goDetail(Context context, Shot shot){
        if (context == null || shot == null){
            return;
        }

        try{
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("shot", shot);
            context.startActivity(intent);
        }catch (Throwable t){
            Log.e(TAG, "goDetail err: " + t.getMessage());
        }
    }

    public static void shareTo(Context context, String title, String msg){
        if (context == null || TextUtils.isEmpty(msg)) return;
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String t = TextUtils.isEmpty(title)?"分享":title;
            context.startActivity(Intent.createChooser(shareIntent, t));
        }catch (Throwable t){
            Log.e(TAG, "shareTo err: " + t.getMessage());
        }
    }

    public static void goBrowser(Context context, String url){
        if (context == null || TextUtils.isEmpty(url)) return;

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        }catch (Throwable t){
            Log.e(TAG, "goBrowser err: " + t.getMessage());
        }
    }

}
