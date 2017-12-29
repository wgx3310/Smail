package com.reid.smail.content;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import reid.utils.AppCompat;

/**
 * Created by reid on 2017/12/19.
 */

public class ImageLoader {

    public static void load(Context context, ImageView iv, String url){
        load(context, iv, url, null);
    }

    public static void load(Context context, ImageView iv, String url, Options options){
        load(context, iv, url, options, null);
    }

    public static void load(Context context, final ImageView iv, String url, Options options, final OnRequestListener listener){
        final RequestManager requestManager = Glide.with(context);
        RequestBuilder builder;
        if (options != null && options.asBitmap){
            builder = requestManager.asBitmap().load(url);
        }else {
            builder = requestManager.load(url);
        }
        if (options != null && options.thumbnail > 0f){
            builder.thumbnail(options.thumbnail);
        }
        builder.apply(buildRequestOptions(options));
        if (listener != null){
            SimpleTarget target = new SimpleTarget() {

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    if (listener != null){
                        listener.onLoadFailed();
                    }
                }

                @Override
                public void onResourceReady(Object resource, Transition transition) {
                    if (resource instanceof Drawable){
                        iv.setImageDrawable((Drawable) resource);
                    } else if (resource instanceof Bitmap){
                        iv.setImageBitmap((Bitmap) resource);
                    }
                    if (listener != null){
                        listener.onLoadSuccess();
                    }
                }
            };
            builder.into(target);
        }else {
            builder.into(iv);
        }
    }

    private static RequestOptions buildRequestOptions(Options opt){
        RequestOptions options = new RequestOptions();
        if (opt == null) return options;

        if (opt.isCircleCrop){
            options = options.circleCrop();
        }
        if (opt.isCenterCrop){
            options = options.centerCrop();
        }
        if (opt.placeholder > 0){
            options = options.placeholder(opt.placeholder);
        }

        return options;
    }

    public static void clearDiskCache(){
        Glide.get(AppCompat.getContext()).clearDiskCache();;
    }

    public static class Options{

        public static Options create(){
            return new Options();
        }

        private boolean isCircleCrop;
        private boolean isCenterCrop;
        private boolean asBitmap;
        private int placeholder;
        private float thumbnail;

        public Options circleCrop(){
            isCircleCrop = true;
            return this;
        }

        public Options centerCrop(){
            isCenterCrop = true;
            return this;
        }

        public Options asBitmap(){
            asBitmap = true;
            return this;
        }

        public Options placeholder(int resId){
            placeholder = resId;
            return this;
        }

        public Options thumbnail(float thumbnail){
            this.thumbnail = thumbnail;
            return this;
        }
    }

    public interface OnRequestListener{
        void onLoadSuccess();
        void onLoadFailed();
    }
}
