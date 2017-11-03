package com.reid.smail.adapter;

import android.support.v7.widget.RecyclerView;

import com.reid.smail.holder.BaseVH;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reid on 2017/9/1.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseVH<T>> {

    protected List<T> mDataList = new ArrayList<>();

    public void setData(List<T> list){
        setData(list, false);
    }

    public void setData(List<T> list, boolean append){
        if (!append) mDataList.clear();
        if (list != null && !list.isEmpty()){
            mDataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void appendData(T data, boolean front){
        if (data == null) return;
        if (front) {
            mDataList.add(0, data);
        }else {
            mDataList.add(data);
        }
        notifyDataSetChanged();
    }

    public void clearData(){
        mDataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(BaseVH holder, int position) {
        holder.bindData(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public T getItem(int position){
        return mDataList.get(position);
    }

    @Override
    public void onViewAttachedToWindow(BaseVH holder) {
        super.onViewAttachedToWindow(holder);
        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(BaseVH holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onViewDetachedFromWindow();
    }
}
