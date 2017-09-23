package com.reid.smail.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.reid.smail.R;
import com.reid.smail.adapter.BucketAdapter;
import com.reid.smail.adapter.RecyclerAdapter;
import com.reid.smail.content.AccountManager;
import com.reid.smail.content.Reminder;
import com.reid.smail.model.shot.Bucket;
import com.reid.smail.net.NetService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smail.util.AppHelper;

/**
 * Created by reid on 2017/9/11.
 */

public class BucketsFragment extends BaseFragment {

    private View mRecyclerLayout;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private BucketAdapter mAdapter;
    private ProgressBar mProgressBar;
    private TextView mEmptyView;

    private boolean inited;
    private boolean isLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buckets, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerLayout = view.findViewById(R.id.recycler_fragment_layout);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mEmptyView = view.findViewById(R.id.empty_text);

        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRefreshLayout.setEnabled(false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = (int) AppHelper.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8);
                outRect.right = outRect.left;
                outRect.bottom = (int) AppHelper.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10);
            }
        });
        mAdapter = new BucketAdapter();
        mRecyclerView.setAdapter(mAdapter);

        updateEmptyState();
        loadData();
    }

    private void updateEmptyState() {
        if (!AccountManager.get().isLogin()){
            mRecyclerLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }else {
            mRecyclerLayout.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    private void loadData() {
        if (inited ||isLoading|| !AccountManager.get().isLogin()){
            return;
        }

        Call<List<Bucket>> call = NetService.get().getShotApi().getUserBuckets(AccountManager.get().getAccessToken());
        if (call != null){
            call.enqueue(new Callback<List<Bucket>>() {
                @Override
                public void onResponse(Call<List<Bucket>> call, Response<List<Bucket>> response) {
                    isLoading = false;
                    mProgressBar.setVisibility(View.GONE);
                    mRefreshLayout.setRefreshing(false);
                    if (response != null && response.body() != null){
                        inited = true;
                        List<Bucket> buckets = response.body();
                        mAdapter.setData(buckets);
                    }else {
                        Log.e(TAG, "body is null");
                        Reminder.toast(R.string.empty_data);
                    }
                }

                @Override
                public void onFailure(Call<List<Bucket>> call, Throwable t) {
                    isLoading = false;
                    mProgressBar.setVisibility(View.GONE);
                    mRefreshLayout.setRefreshing(false);
                    Log.e(TAG, "get body fail " + t.getMessage());
                    Reminder.toast(R.string.load_data_failed);
                }
            });
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            updateEmptyState();
            if (!inited && !isLoading && AccountManager.get().isLogin()){
                loadData();
            }
        }
    }

}
