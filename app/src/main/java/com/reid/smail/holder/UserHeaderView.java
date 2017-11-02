package com.reid.smail.holder;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reid.smail.R;
import com.reid.smail.model.shot.User;

import reid.list.DecorativeView;

/**
 * Created by reid on 2017/11/3.
 */

public class UserHeaderView implements DecorativeView {

    private User mUser;
    private TextView text;
    public UserHeaderView(User user){
        mUser = user;
    }

    @Override
    public View getView(ViewGroup parent) {
        View container = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_content, parent, false);
        text = container.findViewById(R.id.bio_text);
        return container;
    }

    @Override
    public void bindView(int position) {
        if (mUser == null || TextUtils.isEmpty(mUser.bio)) return;
        text.setText(Html.fromHtml(mUser.bio));
    }
}
