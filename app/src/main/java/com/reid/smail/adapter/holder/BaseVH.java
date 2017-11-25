package com.reid.smail.adapter.holder;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by reid on 2017/8/30.
 */

public class BaseVH<T> extends RecyclerView.ViewHolder {

    protected Context context;
    protected T mData;

    public BaseVH(View itemView) {
        super(itemView);
        context = itemView.getContext();
    }

    public final void bindData(T data){
        mData = data;
        onBindData(data);
    }

    public void onBindData(T data){

    }

    public void onViewAttachedToWindow(){

    }

    public void onViewDetachedFromWindow(){

    }
}
