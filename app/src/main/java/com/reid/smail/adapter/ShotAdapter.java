package com.reid.smail.adapter;

import android.support.v7.widget.RecyclerView;

import com.reid.smail.holder.ShotViewHolder;
import com.reid.smail.model.Shot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reid on 2017/9/1.
 */

public abstract class ShotAdapter extends RecyclerView.Adapter<ShotViewHolder> {

    protected List<Shot> shotList = new ArrayList<>();

    public void setData(List<Shot> shots){
        setData(shots, false);
    }

    public void setData(List<Shot> shots, boolean append){
        if (shots != null && !shots.isEmpty()){
            if (!append) shotList.clear();
            shotList.addAll(shots);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(ShotViewHolder holder, int position) {
        holder.bindData(shotList.get(position));
    }

    @Override
    public int getItemCount() {
        return shotList.size();
    }

    @Override
    public void onViewAttachedToWindow(ShotViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(ShotViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onViewDetachedFromWindow();
    }
}
