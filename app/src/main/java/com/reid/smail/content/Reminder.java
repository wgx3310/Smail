package com.reid.smail.content;

import android.widget.Toast;

import com.reid.smail.SmailApp;

/**
 * Created by reid on 2017/9/11.
 */

public class Reminder {

    public static void toast(String msg){
        Toast.makeText(SmailApp.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void toast(int resId){
        Toast.makeText(SmailApp.getContext(), resId, Toast.LENGTH_SHORT).show();
    }
}
