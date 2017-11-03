package com.reid.smail.holder;

import android.support.v7.widget.ButtonBarLayout;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.reid.smail.R;
import com.reid.smail.content.CommentManager;
import com.reid.smail.model.shot.Comment;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.util.DateUtils;
import com.reid.smail.view.glide.GlideApp;

/**
 * Created by reid on 2017/9/1.
 */

public class DetailCommentVH extends BaseVH<Comment> implements CommentManager.OnCommentListener {

    private Shot mShot;

    private ImageView mAvatar;
    private TextView mName;
    private TextView mComment;
    private TextView mTime;
    private ButtonBarLayout mLikeBtn;
    private ImageView mLikeImg;
    private TextView mLikeCount;

    public DetailCommentVH(View itemView, Shot shot) {
        super(itemView);
        mShot = shot;
        mAvatar = itemView.findViewById(R.id.avatar);
        mName = itemView.findViewById(R.id.name);
        mComment = itemView.findViewById(R.id.comment);
        mTime = itemView.findViewById(R.id.time);
        mLikeBtn = itemView.findViewById(R.id.like_btn);
        mLikeImg = itemView.findViewById(R.id.like_img);
        mLikeCount = itemView.findViewById(R.id.like_count);
    }

    @Override
    public void bindData(Comment data) {
        super.bindData(data);

        if (mData == null) return;

        if (mData.user != null) {
            GlideApp.with(context).load(mData.user.avatar_url).circleCrop().into(mAvatar);
            mName.setText(mData.user.name);
        }

        if (!TextUtils.isEmpty(mData.body)){
            mComment.setText(Html.fromHtml(mData.body));
        }

        mData.liked = CommentManager.checkLikedFromCache(mShot.id, mData.id);
        mLikeImg.setImageResource(mData.liked?R.drawable.ic_favorite_red_18dp:R.drawable.ic_favorite_black_18dp);
        if (mData.liked && mData.likes_count <= 0){
            mData.likes_count = 1;
        }
        mLikeCount.setText(String.valueOf(mData.likes_count));
        if (!TextUtils.isEmpty(mData.created_at)){
            mTime.setText(DateUtils.formatDateUseCh(DateUtils.parseISO8601(mData.created_at)));
            mTime.setVisibility(View.VISIBLE);
        }else {
            mTime.setVisibility(View.GONE);
        }

        mLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mData.liked){
                    CommentManager.unlike(context, mShot.id, mData.id, DetailCommentVH.this);
                }else {
                    CommentManager.like(context, mShot.id, mData.id, DetailCommentVH.this);
                }
            }
        });
    }

    @Override
    public void onSuccess(boolean like) {
        mData.liked = like;
        updateLikeState();
    }

    private void updateLikeState() {
        mLikeImg.setImageResource(mData.liked?R.drawable.ic_favorite_red_18dp:R.drawable.ic_favorite_black_18dp);
        mData.likes_count = mData.liked?mData.likes_count+1:Math.max(0, mData.likes_count - 1);
        mLikeCount.setText(String.valueOf(mData.likes_count));
    }

    @Override
    public void onFail() {

    }
}
