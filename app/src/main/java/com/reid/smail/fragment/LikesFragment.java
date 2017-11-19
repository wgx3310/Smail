package com.reid.smail.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reid.smail.R;
import com.reid.smail.adapter.RecyclerAdapter;
import com.reid.smail.content.AccountManager;
import com.reid.smail.content.Tips;
import com.reid.smail.model.shot.Like;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.net.loader.ShotLoader;

import java.util.ArrayList;
import java.util.List;

import reid.list.PlasticAdapter;
import reid.list.PlasticView;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by reid on 2017/9/11.
 */

public class LikesFragment extends BaseFragment {

    private View mRecyclerLayout;
    private PlasticView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;
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
        mEmptyView = view.findViewById(R.id.empty_text);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter();
        PlasticAdapter adapter = new PlasticAdapter(mAdapter);
        mRecyclerView.setAdapter(adapter);

        updateEmptyState();
        loadData();
    }

    private void updateEmptyState() {
        if (!AccountManager.get().isLogin()){
            mRecyclerLayout.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }else {
            mRecyclerLayout.setVisibility(View.VISIBLE);
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
                        if (likes != null && likes.size() > 0){
                            inited = true;
                            List<Shot> shots = new ArrayList<>();
                            for (Like like:likes){
                                shots.add(like.shot);
                            }
                            mAdapter.setData(shots);
                            mRecyclerView.loadMoreComplete();
                        }else {
                            mRecyclerView.loadMoreEnd(true);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        isLoading = false;
                        mRecyclerView.loadMoreComplete();
                        Log.e(TAG, "get body fail " + throwable.getMessage());
                        Tips.toast(R.string.load_data_failed);
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
