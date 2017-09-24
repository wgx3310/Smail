package com.reid.smail.content;

import android.content.Intent;
import android.text.TextUtils;

import com.reid.smail.R;
import com.reid.smail.io.Prefs;
import com.reid.smail.model.shot.Token;
import com.reid.smail.model.shot.User;
import com.reid.smail.net.NetService;
import com.reid.smail.ui.WebActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smail.util.AppCompat;

/**
 * Created by reid on 2017/9/2.
 */

public class AccountManager {

    private static final String TAG = "AccountManager";

    private AccountManager(){

    }

    public static AccountManager get(){
        return Internal.instance;
    }

    private Token mToken;
    private User mUser;
    private List<AccountCallback> callbacks = new ArrayList<>();

    public boolean isLogin(){
        return mToken != null && mUser != null;
    }

    public User getUser(){
        return mUser;
    }

    public void addCallback(AccountCallback callback){
        if (callback == null) return;
        callbacks.add(callback);
    }

    public void removeCallback(AccountCallback callback){
        if (callback == null) return;
        callbacks.remove(callback);
    }

    public String getAccessToken(){
        if (!isLogin()) return null;
        return mToken.access_token;
    }

    public void login() {
        Intent intent = new Intent(AppCompat.getContext(), WebActivity.class);
        intent.putExtra(SettingKey.KEY_TITLE, AppCompat.getContext().getString(R.string.login));
        intent.putExtra(SettingKey.KEY_URL, Constant.OAUTH_URL);
        AppCompat.getContext().startActivity(intent);
    }

    public void acquireAccessToken(String code){
        if (TextUtils.isEmpty(code)) return;

        Call<Token> call = NetService.get().getWebsiteApi().getToken(Constant.CLIENT_ID, Constant.CLIENT_SECRET, code);
        if (call != null){
            call.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    if (response != null && response.body() != null){
                        mToken = response.body();
                        Prefs.putString(SettingKey.SETTING_TOKEN, AppGson.get().toJson(mToken));
                        updateUserInfo();
                    }else {
                        Reminder.toast(R.string.login_failed);
                    }
                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                    Reminder.toast(R.string.login_failed);
                }
            });
        }else {
            Reminder.toast(R.string.login_failed);
        }
    }

    public void updateUserInfo() {
        if (mUser == null){
            initAccountFromPreference();
        }

        if (mToken == null){
            return;
        }

        if (mUser == null){
            Call<User> call = NetService.get().getShotApi().getMyInfo(mToken.access_token);
            if (call != null){
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response != null && response.body() != null){
                            mUser = response.body();
                            Prefs.putString(SettingKey.SETTING_USER, AppGson.get().toJson(mUser));
                            Reminder.toast(R.string.login_success);
                            notifyLogin(mUser);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Reminder.toast(R.string.login_failed);
                    }
                });
            }
        } else {
            notifyLogin(mUser);
        }
    }

    private void initAccountFromPreference() {
        if (mUser == null){
            String token = Prefs.getString(SettingKey.SETTING_TOKEN, null);
            if (!TextUtils.isEmpty(token)){
                mToken = AppGson.get().fromJson(token, Token.class);
            }

            String user = Prefs.getString(SettingKey.SETTING_USER, null);
            if (!TextUtils.isEmpty(user)){
                mUser = AppGson.get().fromJson(user, User.class);
            }
        }
    }

    public void logout() {
        mToken = null;
        mUser = null;
        Prefs.remove(SettingKey.SETTING_TOKEN);
        Prefs.remove(SettingKey.SETTING_USER);
        Reminder.toast(R.string.logout_success);
        notifyLogout();
    }

    private void notifyLogin(User user) {
        if (callbacks != null && callbacks.size() > 0){
            for (AccountCallback callback : callbacks){
                callback.onLogin(user);
            }
        }
    }

    private void notifyLogout() {
        if (callbacks != null && callbacks.size() > 0){
            for (AccountCallback callback : callbacks){
                callback.onLogout();
            }
        }
    }

    public static class Internal {
        private static final AccountManager instance = new AccountManager();
    }

    public interface AccountCallback {
        void onLogin(User user);
        void onLogout();
    }

}
