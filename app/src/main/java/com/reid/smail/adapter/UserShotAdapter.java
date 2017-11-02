package com.reid.smail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reid.smail.R;
import com.reid.smail.holder.BaseVH;
import com.reid.smail.holder.UserShotVH;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.model.shot.User;

/**
 * Created by reid on 2017/9/1.
 */

public class UserShotAdapter extends BaseAdapter<Shot> {

    private User mUser;

    public UserShotAdapter(User user){
        mUser = user;
    }

    @Override
    public BaseVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View container = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_shot, parent, false);
        return new UserShotVH(container, mUser);
    }

}
