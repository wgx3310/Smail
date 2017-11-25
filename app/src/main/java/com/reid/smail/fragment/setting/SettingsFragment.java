package com.reid.smail.fragment.setting;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.reid.smail.R;
import com.reid.smail.content.SettingKey;
import com.reid.smail.content.Tips;
import com.reid.smail.io.ACache;
import com.reid.smail.io.Prefs;
import com.reid.smail.util.WeatherUtils;
import com.reid.smail.view.glide.GlideApp;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import reid.utils.AppCompat;
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

    private int newType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);
        init();
    }

    private void init() {

        mChangeIcon = findPreference(KEY_CHANGE_ICON);
        int index = Prefs.getInt(SettingKey.TYPE_WEATHER_ICON, 0);
        String[] types = getResources().getStringArray(R.array.weather_icon);
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
                GlideApp.get(getActivity()).clearDiskCache();
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
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogLayout = inflater.inflate(R.layout.dialog_change_icon, (ViewGroup) getActivity().findViewById(R.id.dialog_root));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(dialogLayout);
        final AlertDialog alertDialog = builder.create();

        LinearLayout layoutTypeOne = dialogLayout.findViewById(R.id.layout_one);
        layoutTypeOne.setClickable(true);
        final RadioButton radioTypeOne = dialogLayout.findViewById(R.id.radio_one);
        LinearLayout layoutTypeTwo = dialogLayout.findViewById(R.id.layout_two);
        layoutTypeTwo.setClickable(true);
        final RadioButton radioTypeTwo = dialogLayout.findViewById(R.id.radio_two);
        TextView done = dialogLayout.findViewById(R.id.done);

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
        alertDialog.show();

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

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherUtils.updateStateMap(newType);
                String[] iconsText = getResources().getStringArray(R.array.weather_icon);
                mChangeIcon.setSummary(radioTypeOne.isChecked() ? iconsText[0] :
                        iconsText[1]);
                alertDialog.dismiss();
            }
        });

    }
}
