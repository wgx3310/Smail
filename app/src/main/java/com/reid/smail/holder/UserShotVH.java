package com.reid.smail.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.reid.smail.R;
import com.reid.smail.model.Shot;

/**
 * Created by reid on 2017/9/1.
 */

public class UserShotVH extends ShotViewHolder {

    private ImageView mImg;
    public UserShotVH(View itemView) {
        super(itemView);
        mImg = itemView.findViewById(R.id.shot_img);
    }

    @Override
    public void bindData(Shot shot) {
        super.bindData(shot);

        if (mShot != null && mShot.images != null && !TextUtils.isEmpty(mShot.images.teaser)){
            Glide.with(context).load(mShot.images.teaser).into(mImg);
        }
    }

}
