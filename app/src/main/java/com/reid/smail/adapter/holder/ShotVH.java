package com.reid.smail.adapter.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.reid.smail.R;
import com.reid.smail.model.Shot;

/**
 * Created by reid on 2017/8/30.
 */

public class ShotVH extends ShotViewHolder implements View.OnClickListener {

    private ImageView mAvatar;
    private ImageView mPostImg;
    private TextView mTitle;
    private TextView mAuthor;

    public ShotVH(View itemView) {
        super(itemView);
        mAvatar = itemView.findViewById(R.id.avatar);
        mPostImg = itemView.findViewById(R.id.post_img);
        mTitle = itemView.findViewById(R.id.title);
        mAuthor = itemView.findViewById(R.id.author);
    }

    @Override
    public void bindData(Shot shot) {
        if (shot == null){
            return;
        }

        mTitle.setText(shot.title);
        if (shot.user != null){
            mAuthor.setText(shot.user.name);
        }

        if (shot.user!= null && !TextUtils.isEmpty(shot.user.avatar_url)){
            Glide.with(itemView.getContext()).load(shot.user.avatar_url).into(mAvatar);
        }

        mAuthor.setOnClickListener(this);
        mAvatar.setOnClickListener(this);

        String postUrl = null;
        if (shot.images != null && shot.images.normal != null){
            postUrl = shot.images.normal;
        }
        if (postUrl == null && shot.images != null){
            postUrl = shot.images.teaser;
        }
        if (postUrl != null){
            Glide.with(itemView.getContext()).load(postUrl).into(mPostImg);
        }

        mPostImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.avatar:
            case R.id.author:
                Toast.makeText(view.getContext(), "click user", Toast.LENGTH_SHORT).show();
                break;
            case R.id.post_img:
                Toast.makeText(view.getContext(), "click post", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
