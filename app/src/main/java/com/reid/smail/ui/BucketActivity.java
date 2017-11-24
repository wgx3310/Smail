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
import com.reid.smail.content.Tips;
import com.reid.smail.content.SettingKey;
import com.reid.smail.model.shot.Bucket;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.net.loader.ShotLoader;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


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
        Disposable subscribe = ShotLoader.get().getBucketShots(mBucket.id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        isLoading = true;
                    }
                })
                .subscribe(new Consumer<List<Shot>>() {
                    @Override
                    public void accept(List<Shot> shots) {
                        isLoading = false;
                        mProgressBar.setVisibility(View.GONE);
                        if (shots != null){
                            mAdapter.setData(shots);
                        }else {
                            Log.e(TAG, "body is null");
                            Tips.toast(R.string.empty_data);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        isLoading = false;
                        mProgressBar.setVisibility(View.GONE);
                        Log.e(TAG, "get body fail " + throwable.getMessage());
                        Tips.toast(R.string.load_data_failed);
                    }
                });
        addSubscription(subscribe);
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
