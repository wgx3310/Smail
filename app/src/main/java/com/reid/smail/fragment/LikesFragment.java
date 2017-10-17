package com.reid.smail.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.reid.smail.R;
import com.reid.smail.adapter.RecyclerAdapter;
import com.reid.smail.content.AccountManager;
import com.reid.smail.content.Reminder;
import com.reid.smail.model.shot.Like;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.net.NetService;
import com.reid.smail.net.loader.ShotLoader;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by reid on 2017/9/11.
 */

public class LikesFragment extends BaseFragment {

    private View mRecyclerLayout;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;
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
        mAdapter = new RecyclerAdapter();
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
            if (!inited){
                mProgressBar.setVisibility(View.VISIBLE);
            }
            mEmptyView.setVisibility(View.GONE);
        }
    }

    private void loadData() {
        if (inited ||isLoading|| !AccountManager.get().isLogin()){
            return;
        }

        isLoading = true;
        Subscription subscribe = ShotLoader.get().getMyLikes()
                .subscribe(new Action1<List<Like>>() {
                    @Override
                    public void call(List<Like> likes) {
                        isLoading = false;
                        mProgressBar.setVisibility(View.GONE);
                        mRefreshLayout.setRefreshing(false);
                        if (likes != null){
                            inited = true;
                            List<Shot> shots = new ArrayList<>();
                            for (Like like:likes){
                                shots.add(like.shot);
                            }
                            mAdapter.setData(shots);
                        }else {
                            Log.e(TAG, "body is null");
                            Reminder.toast(R.string.empty_data);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        isLoading = false;
                        mProgressBar.setVisibility(View.GONE);
                        mRefreshLayout.setRefreshing(false);
                        Log.e(TAG, "get body fail " + throwable.getMessage());
                        Reminder.toast(R.string.load_data_failed);
                    }
                });
        addSubscription(subscribe);
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
