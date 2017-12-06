package com.reid.smail.view.glide;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by reid on 2017/12/6.
 */

public abstract class ProgressRequestListener implements RequestListener<Drawable> {

    private String mUrl;
    private GlideProgressListener mListener;

    public ProgressRequestListener(String url){
        mUrl = url;
        mListener = new GlideProgressListener() {
            @Override
            public void onProgress(String url, int progress) {
                if (progress < 100){
                    onProgressChanged(progress);
                }
            }
        };
        GlideProgress.addListener(url, mListener);
    }

    @Override
    public final boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        onProgressChanged(100);
        GlideProgress.removeListener(mUrl);
        return false;
    }

    @Override
    public final boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
        onProgressChanged(100);
        GlideProgress.removeListener(mUrl);
        return false;
    }

    public abstract void onProgressChanged(int progress);
}
