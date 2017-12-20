package com.reid.smail.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.reid.smail.R;
import com.reid.smail.content.SettingKey;
import com.reid.smail.io.Prefs;

/**
 * Created by reid on 2017/11/27.
 */

public class ChangeIconDialog extends AlertDialog {

    public ChangeIconDialog(Context context, OnChangeListener listener) {
        super(context);
        mListener = listener;
    }

    private LinearLayout layoutTypeOne;
    private LinearLayout layoutTypeTwo;
    private RadioButton radioTypeOne;
    private RadioButton radioTypeTwo;
    private TextView doneBtn;

    private int newType = 0;
    private OnChangeListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_change_icon);

        layoutTypeOne = findViewById(R.id.layout_one);
        radioTypeOne = findViewById(R.id.radio_one);
        layoutTypeTwo = findViewById(R.id.layout_two);
        radioTypeTwo = findViewById(R.id.radio_two);
        doneBtn = findViewById(R.id.done);

        layoutTypeOne.setClickable(true);
        layoutTypeTwo.setClickable(true);
        radioTypeOne.setClickable(false);
        radioTypeTwo.setClickable(false);

        int type = Prefs.getInt(SettingKey.TYPE_WEATHER_ICON, 0);
        switch (type) {
            case 0:
                radioTypeOne.setChecked(true);
                radioTypeTwo.setChecked(false);
                break;
            case 1:
                radioTypeOne.setChecked(false);
                radioTypeTwo.setChecked(true);
                break;
        }
        layoutTypeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newType = 0;
                radioTypeOne.setChecked(true);
                radioTypeTwo.setChecked(false);
            }
        });

        layoutTypeTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newType = 1;
                radioTypeOne.setChecked(false);
                radioTypeTwo.setChecked(true);
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onChanged(newType);
                }
                dismiss();
            }
        });
    }
}
