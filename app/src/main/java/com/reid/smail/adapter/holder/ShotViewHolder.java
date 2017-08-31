package com.reid.smail.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.reid.smail.model.Shot;

/**
 * Created by reid on 2017/8/30.
 */

public abstract class ShotViewHolder extends RecyclerView.ViewHolder {

    public ShotViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindData(Shot shot);
}
