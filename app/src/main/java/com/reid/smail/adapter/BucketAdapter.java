package com.reid.smail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reid.smail.R;
import com.reid.smail.holder.BaseVH;
import com.reid.smail.holder.BucketVH;
import com.reid.smail.model.shot.Bucket;

/**
 * Created by reid on 2017/9/23.
 */

public class BucketAdapter extends BaseAdapter<Bucket> {

    @Override
    public BaseVH<Bucket> onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bucket, parent, false);
        return new BucketVH(view);
    }
}
