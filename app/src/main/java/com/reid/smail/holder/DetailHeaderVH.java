package com.reid.smail.holder;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.reid.smail.R;
import com.reid.smail.model.Comment;
import com.reid.smail.model.Shot;
import com.reid.smail.util.IntentUtils;

/**
 * Created by reid on 2017/9/1.
 */

public class DetailHeaderVH extends BaseVH<Comment> implements View.OnClickListener {

    private Shot mShot;
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

    public DetailHeaderVH(View itemView, Shot shot) {
        super(itemView);
        mShot = shot;

        mAvatar = itemView.findViewById(R.id.avatar);
        mTitle = itemView.findViewById(R.id.title);
        mAuthor = itemView.findViewById(R.id.author);
        mDescription = itemView.findViewById(R.id.description);
        mTags = itemView.findViewById(R.id.tag);
        mTagLayout = itemView.findViewById(R.id.tag_layout);
        mAuthorLayout = itemView.findViewById(R.id.author_layout);

        mLikeCount = itemView.findViewById(R.id.like_count);
        mViewCount = itemView.findViewById(R.id.view_count);
        mBucketCount = itemView.findViewById(R.id.bucket_count);
        mCommentCount = itemView.findViewById(R.id.comment_count);
        mAttachCount = itemView.findViewById(R.id.attach_count);

        mAuthorLayout.setOnClickListener(this);
    }

    @Override
    public void bindData(Comment data) {
        super.bindData(data);

        if (mShot == null) return;

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
            Glide.with(context).load(mShot.user.avatar_url).into(mAvatar);
        }

        mLikeCount.setText(String.valueOf(mShot.likes_count));
        mViewCount.setText(String.valueOf(mShot.views_count));
        mBucketCount.setText(String.valueOf(mShot.buckets_count));
        mCommentCount.setText(String.valueOf(mShot.comments_count));
        mAttachCount.setText(String.valueOf(mShot.attachments_count));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.author_layout:
                if (mShot != null && mShot.user != null){
                    IntentUtils.goUser(context, mShot.user);
                }
                break;
        }
    }
}
