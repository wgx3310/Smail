package com.reid.smail.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * Created by reid on 2017/8/22.
 */

public class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();

    protected Handler mHandler = new Handler(Looper.getMainLooper());

    private CompositeDisposable compositeSubscription = new CompositeDisposable();

    public Handler getMainHandler(){
        if (mHandler == null){
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    protected void addSubscription(Disposable subscription){
        if (subscription == null){
            return;
        }

        if (compositeSubscription == null){
            compositeSubscription = new CompositeDisposable();
        }
        compositeSubscription.add(subscription);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (compositeSubscription != null){
            compositeSubscription.dispose();
            compositeSubscription = null;
        }
        super.onDestroy();
    }
}
