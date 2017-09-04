package com.reid.smail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reid.smail.R;
import com.reid.smail.holder.BaseVH;
import com.reid.smail.holder.DetailCommentVH;
import com.reid.smail.holder.DetailHeaderVH;
import com.reid.smail.model.Comment;
import com.reid.smail.model.Shot;

/**
 * Created by reid on 2017/9/1.
 */

public class DetailAdapter extends BaseAdapter<Comment> {

    private static final int TYPE_DETAIL_HEADER = 1;
    private static final int TYPE_DETAIL_COMMENT = 2;

    private Shot mShot;

    public DetailAdapter(Shot shot){
        mShot = shot;
    }

    @Override
    public BaseVH<Comment> onCreateViewHolder(ViewGroup parent, int viewType) {
        View container = null;
        BaseVH holder = null;
        switch (viewType){
            case TYPE_DETAIL_HEADER:
                container = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_head, parent, false);
                holder = new DetailHeaderVH(container, mShot);
                break;
            case TYPE_DETAIL_COMMENT:
                container = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_comment, parent, false);
                holder = new DetailCommentVH(container);
                break;
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_DETAIL_HEADER;
        }
        return TYPE_DETAIL_COMMENT;
    }
}
