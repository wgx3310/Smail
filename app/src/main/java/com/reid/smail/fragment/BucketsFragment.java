package com.reid.smail.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reid.smail.R;
import com.reid.smail.adapter.BucketAdapter;
import com.reid.smail.content.AccountManager;
import com.reid.smail.content.Tips;
import com.reid.smail.model.shot.Bucket;
import com.reid.smail.net.loader.ShotLoader;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import reid.list.PlasticAdapter;
import reid.list.PlasticView;
import reid.utils.AppHelper;

/**
 * Created by reid on 2017/9/11.
 */

public class BucketsFragment extends BaseFragment {

    private View mRecyclerLayout;
    private PlasticView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private BucketAdapter mAdapter;
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
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = (int) AppHelper.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8);
                outRect.right = outRect.left;
                outRect.bottom = (int) AppHelper.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10);
            }
        });
        mAdapter = new BucketAdapter();
        mRecyclerView.setAdapter(new PlasticAdapter(mAdapter));

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
        Disposable subscribe = ShotLoader.get().getMyBuckets()
                .subscribe(new Consumer<List<Bucket>>() {
                    @Override
                    public void accept(List<Bucket> buckets) {
                        isLoading = false;
                        if (buckets != null && buckets.size() > 0){
                            inited = true;
                            mAdapter.setData(buckets);
                            mRecyclerView.loadMoreComplete();
                        }else {
                            mRecyclerView.loadMoreEnd(true);
                            Log.e(TAG, "body is null");
                            Tips.toast(R.string.empty_data);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
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
