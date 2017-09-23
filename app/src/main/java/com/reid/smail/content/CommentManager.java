package com.reid.smail.content;

import android.content.Context;
import android.text.TextUtils;

import com.reid.smail.R;
import com.reid.smail.io.Prefs;
import com.reid.smail.model.Item;
import com.reid.smail.net.NetService;

import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by reid on 2017/9/10.
 */

public class CommentManager {

    public static void like(Context context, final long shotId, final long commentId, final OnCommentListener listener){
        if (context == null) return;

        if (AccountManager.get().isLogin()){
            Call<Item> call = NetService.get().getShotApi().likeComment(shotId, commentId, AccountManager.get().getAccessToken());
            if (call != null){
                call.enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(Call<Item> call, Response<Item> response) {
                        if (response != null && response.body() != null){
                            cacheLiked(shotId, commentId, true);
                            if (listener != null){
                                listener.onSuccess(true);
                            }
                        }else {
                            if (listener != null){
                                listener.onFail();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Item> call, Throwable t) {
                        if (listener != null){
                            listener.onFail();
                        }
                    }
                });
            }
        }else {
            Reminder.toast(R.string.not_login);
        }
    }

    public static void unlike(Context context, final long shotId, final long commentId, final OnCommentListener listener){
        if (context == null) return;

        if (AccountManager.get().isLogin()){
            Call<Item> call = NetService.get().getShotApi().unlikeComment(shotId, commentId, AccountManager.get().getAccessToken());
            if (call != null){
                call.enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(Call<Item> call, Response<Item> response) {
                        if (response.code() == 204){
                            cacheLiked(shotId, commentId, false);
                            if (listener != null){
                                listener.onSuccess(false);
                            }
                        }else {
                            if (listener != null){
                                listener.onFail();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Item> call, Throwable t) {
                        if (listener != null){
                            listener.onFail();
                        }
                    }
                });
            }
        }else {
            Reminder.toast(R.string.not_login);
        }
    }

    public static boolean checkLikedFromCache(long shotId, long commentId){
        String commentKey = getCommentKey(shotId, commentId);
        Set<String> likeSet = Prefs.getStringSet(SettingKey.KEY_COMMENT_LIKE);
        return likeSet != null && !TextUtils.isEmpty(commentKey) && likeSet.contains(commentKey);
    }

    //TODO 更新回调状态有问题
    public static void check(Context context, long shotId, long commentId, final OnCommentListener listener){
        if (context == null) return;

        if (AccountManager.get().isLogin()){
            Call<Item> call = NetService.get().getShotApi().checkCommentLiked(shotId, commentId, AccountManager.get().getAccessToken());
            if (call != null){
                call.enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(Call<Item> call, Response<Item> response) {
                        if (response != null && response.body() != null){
                            if (listener != null){
                                listener.onSuccess(true);
                            }
                        }else {
                            if (listener != null){
                                listener.onSuccess(false);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Item> call, Throwable t) {
                        if (listener != null){
                            listener.onFail();
                        }
                    }
                });
            }
        }else {
            Reminder.toast(R.string.not_login);
        }
    }

    private static void cacheLiked(long shotId, long commentId, boolean like) {
        String commentKey = getCommentKey(shotId, commentId);
        Set<String> likeSet = Prefs.getStringSet(SettingKey.KEY_COMMENT_LIKE);
        if (like){
            if (likeSet == null){
                likeSet = new HashSet<String>();
            }
            likeSet.add(commentKey);
        }else {
            if (likeSet != null){
                likeSet.remove(commentKey);
            }
        }
        Prefs.putStringSet(SettingKey.KEY_COMMENT_LIKE, likeSet);
    }

    public static String getCommentKey(long shotId, long commentId){
        return shotId + "-" + commentId;
    }

    public interface OnCommentListener{
        void onSuccess(boolean like);
        void onFail();
    }
}
