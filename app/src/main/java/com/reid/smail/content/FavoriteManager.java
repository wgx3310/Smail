package com.reid.smail.content;

import android.content.Context;
import android.widget.Toast;

import com.reid.smail.model.Item;
import com.reid.smail.net.NetService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by reid on 2017/9/10.
 */

public class FavoriteManager {

    public static void unlike(Context context, final long id, final OnFavListener listener){
        if (context == null) return;

        if (AccountManager.get().isLogin()){
            Call<Item> call = NetService.get().getShotApi().unlikeShot(id, AccountManager.get().getAccessToken());
            if (call != null){
                call.enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(Call<Item> call, Response<Item> response) {
                        if (response.code() == 204){
                            if (listener != null){
                                listener.onSuccess(id, false);
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
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    public static void like(Context context, final long id, final OnFavListener listener){
        if (context == null) return;

        if (AccountManager.get().isLogin()){
            Call<Item> call = NetService.get().getShotApi().likeShot(id, AccountManager.get().getAccessToken());
            if (call != null){
                call.enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(Call<Item> call, Response<Item> response) {
                        if (response != null && response.body() != null){
                            if (listener != null){
                                listener.onSuccess(id, true);
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
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    public static void check(Context context, final long id, final OnFavListener listener){
        if (context == null) return;

        if (AccountManager.get().isLogin()){
            Call<Item> call = NetService.get().getShotApi().check(id, AccountManager.get().getAccessToken());
            if (call != null){
                call.enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(Call<Item> call, Response<Item> response) {
                        if (response != null && response.body() != null){
                            if (listener != null){
                                listener.onSuccess(id, true);
                            }
                        }else {
                            if (listener != null){
                                listener.onSuccess(id, false);
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
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnFavListener{
        void onSuccess(long id, boolean like);
        void onFail();
    }
}
