package com.reid.smail.adapter.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.reid.smail.R;
import com.reid.smail.content.ImageLoader;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.model.shot.User;
import com.reid.smail.util.IntentUtils;

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
            ImageLoader.load(context, mImg, mData.images.teaser, ImageLoader.Options.create().centerCrop());
        }

        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.goDetail(context, mData);
            }
        });
    }

}
