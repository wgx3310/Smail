package com.reid.smail.fragment.setting;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;

import com.reid.smail.R;
import com.reid.smail.content.Tips;

import reid.utils.AppHelper;


/**
 * Created by reid on 2017/9/11.
 */

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private static final String KEY_APP_VERSION = "app_version";

    private PreferenceScreen mVersionScreen;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);
        init();
    }

    private void init() {
        mVersionScreen = (PreferenceScreen) findPreference(KEY_APP_VERSION);
        mVersionScreen.setSummary(AppHelper.getAppVerName());
        mVersionScreen.setOnPreferenceClickListener(this);
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        switch (key){
            case KEY_APP_VERSION:
                Tips.toast(R.string.setting_last_version);
                break;
        }
        return false;
    }
}
