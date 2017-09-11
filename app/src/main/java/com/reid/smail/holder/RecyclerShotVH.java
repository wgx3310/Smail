package com.reid.smail.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reid.smail.R;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.util.IntentUtils;
import com.reid.smail.view.glide.GlideApp;

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
    public void bindData(Shot shot) {
        super.bindData(shot);
        if (shot == null){
            return;
        }

        mTitle.setText(shot.title);
        if (shot.user != null){
            mAuthor.setText(shot.user.name);
        }

        if (shot.user!= null && !TextUtils.isEmpty(shot.user.avatar_url)){
            GlideApp.with(context).load(shot.user.avatar_url).circleCrop().into(mAvatar);
        }

        String postUrl = null;
        if (shot.images != null && shot.images.normal != null){
            postUrl = shot.images.normal;
        }
        if (postUrl == null && shot.images != null){
            postUrl = shot.images.teaser;
        }
        if (postUrl != null){
            GlideApp.with(context).asBitmap().load(postUrl).placeholder(R.drawable.loading_icon).into(mPostImg);
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
