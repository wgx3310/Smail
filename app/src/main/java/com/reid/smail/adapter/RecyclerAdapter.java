package com.reid.smail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reid.smail.R;
import com.reid.smail.holder.RecyclerShotVH;
import com.reid.smail.holder.BaseVH;
import com.reid.smail.model.shot.Shot;


/**
 * Created by reid on 2017/8/30.
 */

public class RecyclerAdapter extends BaseAdapter<Shot> {

    @Override
    public BaseVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shot, parent, false);
        return new RecyclerShotVH(view);
    }

}
