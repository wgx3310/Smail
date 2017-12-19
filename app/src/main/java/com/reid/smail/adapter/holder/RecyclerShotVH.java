package com.reid.smail.adapter.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reid.smail.R;
import com.reid.smail.content.ImageLoader;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.util.IntentUtils;

/**
 * Created by reid on 2017/8/30.
 */

public class RecyclerShotVH extends BaseVH<Shot> implements View.OnClickListener {

    private ImageView mAvatar;
    private ImageView mPostImg;
    private TextView mTitle;
    private TextView mAuthor;
    private RelativeLayout mTopLayout;

    public RecyclerShotVH(View itemView) {
        super(itemView);
        mAvatar = itemView.findViewById(R.id.avatar);
        mPostImg = itemView.findViewById(R.id.post_img);
        mTitle = itemView.findViewById(R.id.title);
        mAuthor = itemView.findViewById(R.id.author);
        mTopLayout = itemView.findViewById(R.id.top_layout);
    }

    @Override
    public void onBindData(Shot shot) {
        if (shot == null){
            return;
        }

        mTitle.setText(shot.title);
        if (shot.user != null){
            mAuthor.setText(shot.user.name);
        }

        if (shot.user!= null && !TextUtils.isEmpty(shot.user.avatar_url)){
            ImageLoader.load(context, mAvatar, shot.user.avatar_url, ImageLoader.Options.create().circleCrop());
        }

        String postUrl = null;
        if (shot.images != null && shot.images.normal != null){
            postUrl = shot.images.normal;
        }
        if (postUrl == null && shot.images != null){
            postUrl = shot.images.teaser;
        }
        if (postUrl != null){
            ImageLoader.load(context, mPostImg, postUrl,
                    ImageLoader.Options.create().placeholder(R.drawable.loading_icon).asBitmap());
        }

        mTopLayout.setOnClickListener(this);
        mPostImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.top_layout:
                if (mData != null && mData.user != null){
                    IntentUtils.goUser(context, mData.user);
                }
                break;
            case R.id.post_img:
                if (mData != null){
                    IntentUtils.goDetail(context, mData);
                }
                break;
            default:
                break;
        }
    }

}
