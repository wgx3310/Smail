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

import com.reid.smail.R;
import com.reid.smail.adapter.RecyclerAdapter;
import com.reid.smail.content.Reminder;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.model.span.TabSpan;
import com.reid.smail.net.loader.ShotLoader;

import java.util.List;

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
    private SwipeRefreshLayout mRefreshLayout;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;

    private int curPage = 1;
    private boolean isLoading;
    private ShotLoader mLoader = ShotLoader.get();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mSpan = (TabSpan) getArguments().getSerializable("tab");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.red, R.color.yellow, R.color.green, R.color.blue);
        mRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastCompletelyVisibleItemPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastCompletelyVisibleItemPosition + 5 >= recyclerView.getAdapter().getItemCount()){
                    loadData(true);
                }
            }
        });

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        loadData(false);
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
                        mProgressBar.setVisibility(View.GONE);
                        mRefreshLayout.setRefreshing(false);
                        if (shots != null){
                            mAdapter.setData(shots, curPage > 1);
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
    public void onRefresh() {
        loadData(false);
    }
}
