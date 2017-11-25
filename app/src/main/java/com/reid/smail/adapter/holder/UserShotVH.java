package com.reid.smail.adapter.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.reid.smail.R;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.model.shot.User;
import com.reid.smail.util.IntentUtils;
import com.reid.smail.view.glide.GlideApp;

/**
 * Created by reid on 2017/9/1.
 */

public class UserShotVH extends BaseVH<Shot> {

    private ImageView mImg;
    private User mUser;
    public UserShotVH(View itemView, User user) {
        super(itemView);
        mImg = itemView.findViewById(R.id.shot_img);
        mUser = user;
    }

    @Override
    public void onBindData(Shot shot) {

        if (mData != null){
            mData.user = mUser;
        }

        if (mData != null && mData.images != null && !TextUtils.isEmpty(mData.images.teaser)){
            GlideApp.with(context).load(mData.images.teaser).centerCrop().into(mImg);
        }

        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.goDetail(context, mData);
            }
        });
    }

}
