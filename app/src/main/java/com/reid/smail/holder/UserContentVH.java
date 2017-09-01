package com.reid.smail.holder;

import android.view.View;
import android.widget.TextView;

import com.reid.smail.R;

/**
 * Created by reid on 2017/9/1.
 */

public class UserContentVH extends ShotViewHolder {

    private TextView mBioText;

    public UserContentVH(View itemView, String bio) {
        super(itemView);
        mBioText = itemView.findViewById(R.id.bio_text);
        setBioText(bio);
    }

    public void setBioText(String bioText){
        mBioText.setText(bioText);
    }
}
