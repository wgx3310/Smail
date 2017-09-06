package com.reid.smail.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.reid.smail.R;
import com.reid.smail.model.Shot;
import com.reid.smail.util.IntentUtils;
import com.reid.smail.view.glide.GlideApp;

/**
 * Created by reid on 2017/9/1.
 */

public class UserShotVH extends BaseVH<Shot> {

    private ImageView mImg;
    public UserShotVH(View itemView) {
        super(itemView);
        mImg = itemView.findViewById(R.id.shot_img);
    }

    @Override
    public void bindData(Shot shot) {
        super.bindData(shot);

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
