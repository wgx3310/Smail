package com.reid.smail.fragment.setting;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;

import com.reid.smail.R;
import com.reid.smail.content.ImageLoader;
import com.reid.smail.content.SettingKey;
import com.reid.smail.content.Tips;
import com.reid.smail.io.ACache;
import com.reid.smail.io.Prefs;
import com.reid.smail.setting.AutoUpdateDialog;
import com.reid.smail.setting.ChangeIconDialog;
import com.reid.smail.setting.OnChangeListener;
import com.reid.smail.util.NotificationHelper;
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

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private static final String KEY_CHANGE_ICON = "change_icons";
    private static final String KEY_UPDATE_PERIOD = "update_period";
    private static final String KEY_NOTIFICATION_SHOW = "notification_show";
    private static final String KEY_NOTIFICATION_MODEL = "notification_model";
    private static final String KEY_CLEAR_IMAGE_CACHE = "clear_image_cache";
    private static final String KEY_CLEAR_DATA_CACHE = "clear_data_cache";
    private static final String KEY_APP_VERSION = "app_version";

    private Preference mChangeIcon;
    private Preference mUpdatePeriod;
    private CheckBoxPreference mNotificationShow;
    private CheckBoxPreference mNotificationModel;
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

        mUpdatePeriod = findPreference(KEY_UPDATE_PERIOD);
        int period = Prefs.getInt(SettingKey.PERIOD_AUTO_UPDATE, 3);
        mUpdatePeriod.setSummary("每"+period+"小时更新天气信息");
        mUpdatePeriod.setOnPreferenceClickListener(this);

        mNotificationShow = (CheckBoxPreference) findPreference(KEY_NOTIFICATION_SHOW);
        boolean show = Prefs.getBoolean(SettingKey.SHOW_WEATHER_NOTIFICATION, true);
        mNotificationShow.setChecked(show);
        mNotificationShow.setOnPreferenceChangeListener(this);

        mNotificationModel = (CheckBoxPreference) findPreference(KEY_NOTIFICATION_MODEL);
        boolean model = Prefs.getBoolean(SettingKey.MODEL_WEATHER_NOTIFICATION, true);
        mNotificationModel.setChecked(model);
        mNotificationModel.setOnPreferenceChangeListener(this);

        mClearImageCache = (PreferenceScreen) findPreference(KEY_CLEAR_IMAGE_CACHE);
        mClearImageCache.setOnPreferenceClickListener(this);

        mClearDataCache = (PreferenceScreen) findPreference(KEY_CLEAR_DATA_CACHE);
        mClearDataCache.setOnPreferenceClickListener(this);

        mVersionScreen = (PreferenceScreen) findPreference(KEY_APP_VERSION);
        mVersionScreen.setSummary(AppHelper.getAppVerName());
        mVersionScreen.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        switch (key){
            case KEY_NOTIFICATION_SHOW:
                boolean show = (Boolean) newValue;
                Prefs.putBoolean(SettingKey.SHOW_WEATHER_NOTIFICATION, show);
                mNotificationShow.setChecked(show);
                if (!show){
                    NotificationHelper.clearWeatherNotification(getActivity());
                }
                break;
            case KEY_NOTIFICATION_MODEL:
                boolean checked = (Boolean) newValue;
                Prefs.putBoolean(SettingKey.MODEL_WEATHER_NOTIFICATION, checked);
                mNotificationModel.setChecked(checked);
                break;
        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        switch (key){
            case KEY_CHANGE_ICON:
                showChangeIconDialog();
                break;
            case KEY_UPDATE_PERIOD:
                showUpdateDialog();
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

    private void showUpdateDialog() {
        final AutoUpdateDialog dialog = new AutoUpdateDialog(getActivity(), new OnChangeListener(){
            @Override
            public void onChanged(int value) {
                Prefs.putInt(SettingKey.PERIOD_AUTO_UPDATE, value);
                mUpdatePeriod.setSummary("每"+value+"小时更新天气信息");
            }
        });
        dialog.show();
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

        final ChangeIconDialog dialog = new ChangeIconDialog(getActivity(), new OnChangeListener() {
            @Override
            public void onChanged(int value) {
                WeatherProps.updateStateMap(value);
                String[] iconsText = getResources().getStringArray(R.array.weather_change_icon);
                mChangeIcon.setSummary(iconsText[value]);
            }
        });

        dialog.show();
    }
}
