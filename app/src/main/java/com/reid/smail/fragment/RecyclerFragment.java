package com.reid.smail.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.reid.smail.R;
import com.reid.smail.adapter.RecyclerAdapter;
import com.reid.smail.content.AppGson;
import com.reid.smail.content.Tips;
import com.reid.smail.io.ACache;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.model.span.TabSpan;
import com.reid.smail.net.loader.ShotLoader;

import java.util.List;

import reid.list.PlasticAdapter;
import reid.list.PlasticView;
import reid.list.load.OnMoreListener;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by reid on 2017/8/30.
 */

public class RecyclerFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static RecyclerFragment newInstance(TabSpan span){
        RecyclerFragment fragment = new RecyclerFragment();
        Bundle args =  new Bundle();
        args.putSerializable("tab", span);
        fragment.setArguments(args);
        return fragment;
    }

    public RecyclerFragment(){

    }

    private TabSpan mSpan;
    private String mCacheKey = "Shot";
    private LinearLayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;
    private PlasticView mRecyclerView;

    private int curPage = 1;
    private boolean isLoading;
    private ShotLoader mLoader = ShotLoader.get();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mSpan = (TabSpan) getArguments().getSerializable("tab");
            mCacheKey = "Shot-" + mSpan.title;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setRefreshingColorResources(R.color.red, R.color.yellow, R.color.green, R.color.blue);
        mRecyclerView.setOnRefreshListener(this);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter();
        mRecyclerView.setAdapter(new PlasticAdapter(mAdapter));
        mRecyclerView.setOnMoreListener(new OnMoreListener() {
            @Override
            public void onMore(int totalItemCount, int itemCountToLoadMore, int lastVisibleItemPosition) {
                loadData(true);
            }
        });

        boolean useCache = false;
        String cache = ACache.get(getContext()).getString(mCacheKey);
        if (!TextUtils.isEmpty(cache)){
            List<Shot> shots = AppGson.get().fromJson(cache, new TypeToken<List<Shot>>(){}.getType());
            if (shots != null && !shots.isEmpty()){
                useCache = true;
                mAdapter.setData(shots);
                mRecyclerView.loadMoreComplete();
            }
        }

        if (!useCache){
            loadData(false);
        }
    }

    private void loadData(boolean loadMore) {
        if (isLoading){
            return;
        }

        isLoading = true;
        curPage = loadMore?curPage+1:1;
        String list = mSpan != null?mSpan.list:"";
        String sort = mSpan != null?mSpan.sort:"";
        Subscription subscribe = mLoader.getShots(list, sort, curPage)
                .subscribe(new Action1<List<Shot>>() {
                    @Override
                    public void call(List<Shot> shots) {
                        isLoading = false;
                        if (shots != null && shots.size() > 0){
                            mAdapter.setData(shots, curPage > 1);
                            if (curPage == 1){
                                ACache.get(getContext()).put(mCacheKey, AppGson.get().toJson(shots), 30*60);
                            }
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
    public void onRefresh() {
        loadData(false);
    }
}
