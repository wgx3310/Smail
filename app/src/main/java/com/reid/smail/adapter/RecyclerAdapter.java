package com.reid.smail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reid.smail.R;
import com.reid.smail.holder.RecyclerShotVH;
import com.reid.smail.holder.ShotViewHolder;


/**
 * Created by reid on 2017/8/30.
 */

public class RecyclerAdapter extends ShotAdapter {

    @Override
    public ShotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shot, parent, false);
        return new RecyclerShotVH(view);
    }

}
