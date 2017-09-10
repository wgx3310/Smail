package com.reid.smail.holder;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.reid.smail.R;
import com.reid.smail.model.shot.Shot;

/**
 * Created by reid on 2017/9/1.
 */

public class UserContentVH extends BaseVH<Shot> {

    private TextView mBioText;

    public UserContentVH(View itemView, String bio) {
        super(itemView);
        mBioText = itemView.findViewById(R.id.bio_text);
        setBioText(bio);
    }

    public void setBioText(String bioText){
        if (TextUtils.isEmpty(bioText)) return;
        mBioText.setText(Html.fromHtml(bioText));
    }
}
