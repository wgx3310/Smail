package com.reid.smail.holder;

import android.support.v7.widget.ButtonBarLayout;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.reid.smail.R;
import com.reid.smail.model.Comment;
import com.reid.smail.util.DateUtils;

/**
 * Created by reid on 2017/9/1.
 */

public class DetailCommentVH extends BaseVH<Comment> {

    private ImageView mAvatar;
    private TextView mName;
    private TextView mComment;
    private TextView mTime;
    private ButtonBarLayout mLikeBtn;
    private TextView mLikeCount;

    public DetailCommentVH(View itemView) {
        super(itemView);
        mAvatar = itemView.findViewById(R.id.avatar);
        mName = itemView.findViewById(R.id.name);
        mComment = itemView.findViewById(R.id.comment);
        mTime = itemView.findViewById(R.id.time);
        mLikeBtn = itemView.findViewById(R.id.like_btn);
        mLikeCount = itemView.findViewById(R.id.like_count);
    }

    @Override
    public void bindData(Comment data) {
        super.bindData(data);

        if (mData == null) return;

        if (mData.user != null) {
            Glide.with(context).load(mData.user.avatar_url).into(mAvatar);
            mName.setText(mData.user.name);
        }

        if (!TextUtils.isEmpty(mData.body)){
            mComment.setText(Html.fromHtml(mData.body));
        }

        mLikeCount.setText(String.valueOf(mData.likes_count));
        if (!TextUtils.isEmpty(mData.created_at)){
            mTime.setText(DateUtils.formatDateUseCh(DateUtils.parseISO8601(mData.created_at)));
            mTime.setVisibility(View.VISIBLE);
        }else {
            mTime.setVisibility(View.GONE);
        }
    }
}
