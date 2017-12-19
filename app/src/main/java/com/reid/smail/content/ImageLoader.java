package com.reid.smail.content;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import reid.utils.AppCompat;

/**
 * Created by reid on 2017/12/19.
 */

public class ImageLoader {

    public static void load(Context context, ImageView iv, String url){
        load(context, iv, url, null);
    }

    public static void load(Context context, ImageView iv, String url, Options options){
        RequestManager requestManager = Glide.with(context);
        RequestBuilder builder;
        if (options != null && options.asBitmap){
            builder = requestManager.asBitmap().load(url);
        }else {
            builder = requestManager.load(url);
        }
        if (options != null && options.thumbnail > 0f){
            builder.thumbnail(options.thumbnail);
        }
        builder.apply(buildRequestOptions(options)).into(iv);
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
}
