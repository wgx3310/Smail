package com.reid.smail.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxSeekBar;
import com.reid.smail.R;
import com.reid.smail.content.SettingKey;
import com.reid.smail.io.Prefs;

import io.reactivex.functions.Consumer;

/**
 * Created by reid on 2017/12/20.
 */

public class AutoUpdateDialog extends AlertDialog {

    public AutoUpdateDialog(Context context, OnChangeListener listener) {
        super(context);
        mListener = listener;
    }

    private SeekBar mTimeSeekBar;
    private TextView mPeriod;
    private TextView mDone;

    private int mProgress;
    private OnChangeListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_auto_update);

        mTimeSeekBar = findViewById(R.id.seek_bar);
        mPeriod = findViewById(R.id.period);
        mDone = findViewById(R.id.done);

        mProgress = Prefs.getInt(SettingKey.PERIOD_AUTO_UPDATE, 3);
        mTimeSeekBar.setProgress(mProgress);
        mPeriod.setText(mProgress + "小时");
        RxSeekBar.changes(mTimeSeekBar).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer progress) throws Exception {
                mProgress = progress;
                mPeriod.setText(mProgress + "小时");
            }
        });

        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onChanged(mProgress);
                }
                dismiss();
            }
        });
    }

}
