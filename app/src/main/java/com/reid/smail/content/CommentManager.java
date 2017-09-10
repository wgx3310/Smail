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

public class CommentManager {

    public static void like(Context context, long shotId, long commentId, final OnCommentListener listener){
        if (context == null) return;

        if (AccountManager.get().isLogin()){
            Call<Item> call = NetService.get().getShotApi().likeComment(shotId, commentId, AccountManager.get().getAccessToken());
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

    public static void unlike(Context context, long shotId, long commentId, final OnCommentListener listener){
        if (context == null) return;

        if (AccountManager.get().isLogin()){
            Call<Item> call = NetService.get().getShotApi().unlikeComment(shotId, commentId, AccountManager.get().getAccessToken());
            if (call != null){
                call.enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(Call<Item> call, Response<Item> response) {
                        if (response.code() == 204){
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
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
        }
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
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnCommentListener{
        void onSuccess(boolean like);
        void onFail();
    }
}
