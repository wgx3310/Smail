package com.reid.smail.content;

import android.content.Context;
import android.text.TextUtils;

import com.reid.smail.R;
import com.reid.smail.io.Prefs;
import com.reid.smail.model.Item;
import com.reid.smail.net.loader.ShotLoader;

import java.util.HashSet;
import java.util.Set;

import rx.functions.Action1;

/**
 * Created by reid on 2017/9/10.
 */

public class CommentManager {

    public static void like(Context context, final long shotId, final long commentId, final OnCommentListener listener){
        if (context == null) return;

        if (AccountManager.get().isLogin()){
            ShotLoader.get().likeComment(shotId, commentId).subscribe(new Action1<Item>() {
                @Override
                public void call(Item item) {
                    if (item != null){
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
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    if (listener != null){
                        listener.onFail();
                    }
                }
            });
        }else {
            Tips.toast(R.string.not_login);
        }
    }

    public static void unlike(Context context, final long shotId, final long commentId, final OnCommentListener listener){
        if (context == null) return;

        if (AccountManager.get().isLogin()){
            ShotLoader.get().unlikeComment(shotId, commentId).subscribe(new Action1<Item>() {
                @Override
                public void call(Item item) {
                    cacheLiked(shotId, commentId, false);
                    if (listener != null){
                        listener.onSuccess(false);
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    if (listener != null){
                        listener.onFail();
                    }
                }
            });
        }else {
            Tips.toast(R.string.not_login);
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
            ShotLoader.get().checkCommentLiked(shotId, commentId).subscribe(new Action1<Item>() {
                @Override
                public void call(Item item) {
                    if (item != null){
                        if (listener != null){
                            listener.onSuccess(true);
                        }
                    }else {
                        if (listener != null){
                            listener.onSuccess(false);
                        }
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    if (listener != null){
                        listener.onFail();
                    }
                }
            });
        } else {
            Tips.toast(R.string.not_login);
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
