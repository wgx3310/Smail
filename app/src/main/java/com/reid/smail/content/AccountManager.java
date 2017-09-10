package com.reid.smail.content;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.reid.smail.SmailApp;
import com.reid.smail.io.Prefs;
import com.reid.smail.model.shot.Token;
import com.reid.smail.model.shot.User;
import com.reid.smail.net.NetService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public void addCallback(AccountCallback callback){
        if (callback == null) return;
        callbacks.add(callback);
    }

    public String getAccessToken(){
        if (!isLogin()) return null;
        return mToken.access_token;
    }

    public void login() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Constant.OAUTH_URL));
        SmailApp.getContext().startActivity(intent);
    }

    public void checkAuthCallback(Intent intent) {
        if (intent == null) return;

        if (intent != null && intent.getData() != null
                && !TextUtils.isEmpty(intent.getScheme())
                && Constant.SCHEMA.equals(intent.getScheme())
                && intent.getData() != null) {
            String state = intent.getData().getQueryParameter("state");
            if (!Constant.OAUTH_STATE.equals(state)){
                return;
            }

            String code = intent.getData().getQueryParameter("code");
            if (TextUtils.isEmpty(code)){
                return;
            }

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
                            Toast.makeText(SmailApp.getContext(), "登录失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Toast.makeText(SmailApp.getContext(), "登录失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(SmailApp.getContext(), "登录失败，请重试", Toast.LENGTH_SHORT).show();
            }
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
            Call<User> call = NetService.get().getShotApi().getUserInfo(mToken.access_token);
            if (call != null){
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response != null && response.body() != null){
                            mUser = response.body();
                            Prefs.putString(SettingKey.SETTING_USER, AppGson.get().toJson(mUser));
                            Toast.makeText(SmailApp.getContext(), "登录成功", Toast.LENGTH_SHORT).show();
                            notifyLogin(mUser);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e(TAG, "getUserInfo fail: " + t.getMessage());
                        Toast.makeText(SmailApp.getContext(), "登录失败，请重试", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(SmailApp.getContext(), "登出成功", Toast.LENGTH_SHORT).show();
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
