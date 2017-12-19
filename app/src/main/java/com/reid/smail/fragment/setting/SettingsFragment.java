package com.reid.smail.fragment.setting;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.view.View;

import com.reid.smail.R;
import com.reid.smail.content.ImageLoader;
import com.reid.smail.content.SettingKey;
import com.reid.smail.content.Tips;
import com.reid.smail.io.ACache;
import com.reid.smail.io.Prefs;
import com.reid.smail.setting.ChangeIconDialog;
import com.reid.smail.util.WeatherProps;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import reid.utils.AppHelper;


/**
 * Created by reid on 2017/9/11.
 */

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private static final String KEY_CHANGE_ICON = "change_icons";
    private static final String KEY_CLEAR_IMAGE_CACHE = "clear_image_cache";
    private static final String KEY_CLEAR_DATA_CACHE = "clear_data_cache";
    private static final String KEY_APP_VERSION = "app_version";

    private Preference mChangeIcon;
    private PreferenceScreen mClearImageCache;
    private PreferenceScreen mClearDataCache;
    private PreferenceScreen mVersionScreen;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);
        init();
    }

    private void init() {

        mChangeIcon = findPreference(KEY_CHANGE_ICON);
        int index = Prefs.getInt(SettingKey.TYPE_WEATHER_ICON, 0);
        String[] types = getResources().getStringArray(R.array.weather_change_icon);
        mChangeIcon.setSummary(types[index]);
        mChangeIcon.setOnPreferenceClickListener(this);

        mClearImageCache = (PreferenceScreen) findPreference(KEY_CLEAR_IMAGE_CACHE);
        mClearImageCache.setOnPreferenceClickListener(this);

        mClearDataCache = (PreferenceScreen) findPreference(KEY_CLEAR_DATA_CACHE);
        mClearDataCache.setOnPreferenceClickListener(this);

        mVersionScreen = (PreferenceScreen) findPreference(KEY_APP_VERSION);
        mVersionScreen.setSummary(AppHelper.getAppVerName());
        mVersionScreen.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        switch (key){
            case KEY_CHANGE_ICON:
                showChangeIconDialog();
                break;
            case KEY_CLEAR_IMAGE_CACHE:
                clearImageCache();
                break;
            case KEY_CLEAR_DATA_CACHE:
                clearDataCache();
                break;
            case KEY_APP_VERSION:
                Tips.toast(R.string.setting_last_version);
                break;
        }
        return false;
    }

    private void clearDataCache() {
        Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(ObservableEmitter<Void> e) throws Exception {
                ACache.get(getActivity()).clear();
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        Tips.toast("清除完毕");
                    }
                }).subscribe();
    }

    private void clearImageCache() {
        Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(ObservableEmitter<Void> e) throws Exception {
                ImageLoader.clearDiskCache();
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                Tips.toast("清除完毕");
            }
        }).subscribe();
    }

    private void showChangeIconDialog() {

        final ChangeIconDialog dialog = new ChangeIconDialog(getActivity(), new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int type = (Integer) v.getTag();
                WeatherProps.updateStateMap(type);
                String[] iconsText = getResources().getStringArray(R.array.weather_change_icon);
                mChangeIcon.setSummary(iconsText[type]);
            }
        });

        dialog.show();
    }
}
