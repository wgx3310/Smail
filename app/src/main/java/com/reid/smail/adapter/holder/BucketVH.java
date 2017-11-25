package com.reid.smail.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.reid.smail.R;
import com.reid.smail.model.shot.Bucket;
import com.reid.smail.util.IntentUtils;

/**
 * Created by reid on 2017/9/23.
 */

public class BucketVH extends BaseVH<Bucket> {

    private TextView mName;
    private TextView mDesc;
    private TextView mShotCount;
    private TextView mUpdateTime;

    public BucketVH(View itemView) {
        super(itemView);
        mName = itemView.findViewById(R.id.name);
        mDesc = itemView.findViewById(R.id.desc);
        mShotCount = itemView.findViewById(R.id.shot_count);
        mUpdateTime = itemView.findViewById(R.id.update_time);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.goBucket(context, mData);
            }
        });
    }

    @Override
    public void onBindData(Bucket data) {
        if (mData == null){
            return;
        }

        mName.setText(mData.name);
        mDesc.setText(mData.description);
        mShotCount.setText(mData.shots_count + " shots");
        mUpdateTime.setText(mData.updated_at);
    }
}
