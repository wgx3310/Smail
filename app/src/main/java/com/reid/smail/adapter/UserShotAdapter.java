package com.reid.smail.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reid.smail.R;
import com.reid.smail.holder.BaseVH;
import com.reid.smail.holder.UserContentVH;
import com.reid.smail.holder.UserShotVH;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.model.shot.User;

/**
 * Created by reid on 2017/9/1.
 */

public class UserShotAdapter extends BaseAdapter<Shot> {

    private static final int TYPE_CONTENT = 0;
    private static final int TYPE_SHOT = 1;

    private User mUser;

    public UserShotAdapter(User user){
        mUser = user;
    }

    @Override
    public BaseVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View container = null;
        BaseVH holder = null;
        switch (viewType){
            case TYPE_CONTENT:
                container = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_content, parent, false);
                holder = new UserContentVH(container, mUser !=null?mUser.bio:"");
                break;
            case TYPE_SHOT:
                container = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_shot, parent, false);
                holder = new UserShotVH(container, mUser);
                break;
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_CONTENT;
        }
        return TYPE_SHOT;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0){
                    return 3;
                }
                return 1;
            }
        });
    }
}
