package com.reid.smail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reid.smail.R;
import com.reid.smail.adapter.holder.BaseVH;
import com.reid.smail.adapter.holder.DetailCommentVH;
import com.reid.smail.model.shot.Comment;
import com.reid.smail.model.shot.Shot;

/**
 * Created by reid on 2017/9/1.
 */

public class DetailAdapter extends BaseAdapter<Comment> {

    private Shot mShot;

    public DetailAdapter(Shot shot){
        mShot = shot;
    }

    @Override
    public BaseVH<Comment> onCreateViewHolder(ViewGroup parent, int viewType) {
        View container = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_comment, parent, false);
        return new DetailCommentVH(container, mShot);
    }
}
