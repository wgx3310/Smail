package com.reid.smail.holder;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.reid.smail.R;
import com.reid.smail.model.Shot;
import com.reid.smail.ui.UserActivity;

/**
 * Created by reid on 2017/8/30.
 */

public class RecyclerShotVH extends ShotViewHolder implements View.OnClickListener {

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
            Glide.with(context).load(shot.user.avatar_url).into(mAvatar);
        }

        String postUrl = null;
        if (shot.images != null && shot.images.normal != null){
            postUrl = shot.images.normal;
        }
        if (postUrl == null && shot.images != null){
            postUrl = shot.images.teaser;
        }
        if (postUrl != null){
            Glide.with(context).load(postUrl).placeholder(R.drawable.loading).into(mPostImg);
        }

        mTopLayout.setOnClickListener(this);
        mPostImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.top_layout:
                goUser();
                break;
            case R.id.post_img:
                Toast.makeText(view.getContext(), "click post", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void goUser() {
        Intent intent = new Intent(context, UserActivity.class);
        if (mShot != null && mShot.user != null){
            intent.putExtra("user", mShot.user);
        }
        context.startActivity(intent);
    }
}
