package com.reid.smail.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by reid on 2017/8/22.
 */

public class BaseActivity extends AppCompatActivity {

    protected Handler mHandler = new Handler(Looper.getMainLooper());

    public Handler getMainHandler(){
        if (mHandler == null){
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
