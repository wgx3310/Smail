package com.reid.smail.adapter.holder;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reid.smail.R;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.util.IntentUtils;
import com.reid.smail.view.glide.GlideApp;

import reid.list.DecorativeView;

/**
 * Created by reid on 2017/11/3.
 */

public class DetailHeaderView implements DecorativeView, View.OnClickListener {

    private Shot mShot;
    private Context mContext;

    private ImageView mAvatar;
    private TextView mTitle;
    private TextView mAuthor;
    private TextView mDescription;
    private TextView mTags;
    private RelativeLayout mTagLayout;
    private RelativeLayout mAuthorLayout;

    private TextView mLikeCount;
    private TextView mViewCount;
    private TextView mBucketCount;
    private TextView mCommentCount;
    private TextView mAttachCount;

    public DetailHeaderView(Shot shot){
        mShot = shot;
    }

    @Override
    public View getView(ViewGroup parent) {
        mContext = parent.getContext();
        View container = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_head, parent, false);
        mAvatar = container.findViewById(R.id.avatar);
        mTitle = container.findViewById(R.id.title);
        mAuthor = container.findViewById(R.id.author);
        mDescription = container.findViewById(R.id.description);
        mDescription.setMovementMethod(LinkMovementMethod.getInstance());
        mTags = container.findViewById(R.id.tag);
        mTagLayout = container.findViewById(R.id.tag_layout);
        mAuthorLayout = container.findViewById(R.id.author_layout);

        mLikeCount = container.findViewById(R.id.like_count);
        mViewCount = container.findViewById(R.id.view_count);
        mBucketCount = container.findViewById(R.id.bucket_count);
        mCommentCount = container.findViewById(R.id.comment_count);
        mAttachCount = container.findViewById(R.id.attach_count);

        mAuthorLayout.setOnClickListener(this);
        return container;
    }

    @Override
    public void bindView(int position) {
        mTitle.setText(mShot.title);
        if (mShot.user != null){
            mAuthor.setText(mShot.user.name);
        }

        if (!TextUtils.isEmpty(mShot.description)){
            mDescription.setText(Html.fromHtml(mShot.description));
        }
        StringBuilder tagInfo = new StringBuilder();
        for (int i = 0,count = mShot.tags.size(); i < count; i++){
            tagInfo.append(mShot.tags.get(i)).append(" | ");
        }
        if (tagInfo.length() > 0){
            String temp = tagInfo.toString();
            String tags = temp.substring(0, temp.length() - 3);
            mTags.setText(tags);
            mTagLayout.setVisibility(View.VISIBLE);
        }else {
            mTagLayout.setVisibility(View.GONE);
        }

        if (mShot.user != null && !TextUtils.isEmpty(mShot.user.avatar_url)){
            GlideApp.with(mContext).load(mShot.user.avatar_url).circleCrop().into(mAvatar);
        }

        mLikeCount.setText(String.valueOf(mShot.likes_count));
        mViewCount.setText(String.valueOf(mShot.views_count));
        mBucketCount.setText(String.valueOf(mShot.buckets_count));
        mCommentCount.setText(String.valueOf(mShot.comments_count));
        mAttachCount.setText(String.valueOf(mShot.attachments_count));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.author_layout:
                if (mShot != null && mShot.user != null){
                    IntentUtils.goUser(mContext, mShot.user);
                }
                break;
        }
    }
}
