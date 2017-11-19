package com.reid.smail.content;

import android.content.Context;

import com.reid.smail.R;
import com.reid.smail.model.Item;
import com.reid.smail.net.loader.ShotLoader;

import rx.functions.Action1;

/**
 * Created by reid on 2017/9/10.
 */

public class FavoriteManager {

    public static void unlike(Context context, final long id, final OnFavListener listener){
        if (context == null) return;

        if (AccountManager.get().isLogin()){
            ShotLoader.get().unlikeShot(id).subscribe(new Action1<Item>() {
                @Override
                public void call(Item item) {
                    if (listener != null){
                        listener.onSuccess(id, false);
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

    public static void like(Context context, final long id, final OnFavListener listener){
        if (context == null) return;

        if (AccountManager.get().isLogin()){
            ShotLoader.get().likeShot(id).subscribe(new Action1<Item>() {
                @Override
                public void call(Item item) {
                    if (item != null){
                        if (listener != null){
                            listener.onSuccess(id, true);
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

    private static boolean isChecking = false;
    public static void check(Context context, final long id, final OnFavListener listener){
        if (context == null || isChecking) return;

        if (AccountManager.get().isLogin()){
            isChecking = true;
            ShotLoader.get().checkShotLiked(id).subscribe(new Action1<Item>() {
                @Override
                public void call(Item item) {
                    isChecking = false;
                    if (item != null){
                        if (listener != null){
                            listener.onSuccess(id, true);
                        }
                    } else {
                        if (listener != null){
                            listener.onSuccess(id, false);
                        }
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    isChecking = false;
                    if (listener != null){
                        listener.onSuccess(id, false);
                    }
                }
            });
        }else {
            Tips.toast(R.string.not_login);
        }
    }

    public interface OnFavListener{
        void onSuccess(long id, boolean like);
        void onFail();
    }
}
