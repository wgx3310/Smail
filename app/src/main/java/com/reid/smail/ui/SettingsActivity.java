package com.reid.smail.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.reid.smail.R;
import com.reid.smail.fragment.setting.SettingsFragment;

public class SettingsActivity extends BaseActivity {

    public static void launch(Context context){
        if (context == null) return;

        context.startActivity(new Intent(context, SettingsActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initToolbar(R.string.label_settings);

        getFragmentManager().beginTransaction().replace(R.id.content_layout, new SettingsFragment(), "SettingsFragment").commit();
    }

}
