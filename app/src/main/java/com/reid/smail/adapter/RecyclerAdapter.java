package com.reid.smail.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reid.smail.R;
import com.reid.smail.adapter.holder.ShotVH;
import com.reid.smail.adapter.holder.ShotViewHolder;
import com.reid.smail.model.Shot;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by reid on 2017/8/30.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<ShotViewHolder> {

    private List<Shot> shotList = new ArrayList<>();
    public RecyclerAdapter(){

    }

    public void setData(List<Shot> shots){
        if (shots != null && !shots.isEmpty()){
            shotList.clear();
            shotList.addAll(shots);
            notifyDataSetChanged();
        }
    }

    public void appendData(List<Shot> shots){
        if (shots != null && !shots.isEmpty()){
            shotList.addAll(shots);
            notifyDataSetChanged();
        }
    }

    @Override
    public ShotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shot, parent, false);
        return new ShotVH(view);
    }

    @Override
    public void onBindViewHolder(ShotViewHolder holder, int position) {
        holder.bindData(shotList.get(position));
    }

    @Override
    public int getItemCount() {
        return shotList.size();
    }

}
