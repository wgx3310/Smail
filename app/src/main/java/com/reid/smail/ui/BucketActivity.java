package com.reid.smail.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.reid.smail.R;
import com.reid.smail.adapter.RecyclerAdapter;
import com.reid.smail.content.AccountManager;
import com.reid.smail.content.Reminder;
import com.reid.smail.content.SettingKey;
import com.reid.smail.model.shot.Bucket;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.net.NetService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BucketActivity extends BaseActivity {

    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;

    private Bucket mBucket;
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        handleIntent();
        initView();
        bindData();
    }

    private void bindData() {
        if (mBucket != null){
            setTitle(mBucket.name);
        }

        loadData();
    }

    private void loadData() {
        if (isLoading || mBucket == null){
            return;
        }

        Call<List<Shot>> call = NetService.get().getShotApi().getBucketShots(mBucket.id, AccountManager.get().getAccessToken());
        if (call != null){
            isLoading = true;
            call.enqueue(new Callback<List<Shot>>() {
                @Override
                public void onResponse(Call<List<Shot>> call, Response<List<Shot>> response) {
                    isLoading = false;
                    mProgressBar.setVisibility(View.GONE);
                    if (response != null && response.body() != null){
                        List<Shot> shots = response.body();
                        mAdapter.setData(shots);
                    }else {
                        Log.e(TAG, "body is null");
                        Reminder.toast(R.string.empty_data);
                    }
                }

                @Override
                public void onFailure(Call<List<Shot>> call, Throwable t) {
                    isLoading = false;
                    mProgressBar.setVisibility(View.GONE);
                    Log.e(TAG, "get body fail " + t.getMessage());
                    Reminder.toast(R.string.load_data_failed);
                }
            });
        }
    }

    private void initView() {
        mProgressBar = findViewById(R.id.progress_bar);
        mRecyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent == null) return;
        mBucket = intent.getParcelableExtra(SettingKey.KEY_BUCKET);
    }
}
