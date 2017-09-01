package com.reid.smail.holder;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.reid.smail.model.Shot;

/**
 * Created by reid on 2017/8/30.
 */

public class ShotViewHolder extends RecyclerView.ViewHolder {

    protected Context context;
    protected Shot mShot;

    public ShotViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
    }

    @CallSuper
    public void bindData(Shot shot){
        mShot = shot;
    }

    public void onViewAttachedToWindow(){

    }

    public void onViewDetachedFromWindow(){

    }
}
