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
import android.widget.Toast;

import com.reid.smail.R;
import com.reid.smail.adapter.RecyclerAdapter;
import com.reid.smail.content.Constant;
import com.reid.smail.model.Shot;
import com.reid.smail.model.span.TabSpan;
import com.reid.smail.net.NetService;
import com.reid.smail.net.api.ShotApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        mRefreshLayout.setColorSchemeResources(R.color.google_red, R.color.google_yellow, R.color.google_green, R.color.google_blue);
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

        curPage = loadMore?curPage+1:1;

        ShotApi shotApi = NetService.get().getShotApi();
        if (shotApi != null){
            String sort = mSpan != null?mSpan.title:"sort";
            Call<List<Shot>> call = shotApi.getShots(Constant.ACCESS_TOKEN, null, null, sort, curPage);
            if (call != null){
                isLoading = true;
                call.enqueue(new Callback<List<Shot>>() {
                    @Override
                    public void onResponse(Call<List<Shot>> call, Response<List<Shot>> response) {
                        isLoading = false;
                        mProgressBar.setVisibility(View.GONE);
                        mRefreshLayout.setRefreshing(false);
                        List<Shot> body = response.body();
                        if (body != null){
                            mAdapter.setData(body, curPage > 1);
                        }else {
                            Log.e(TAG, "body is null");
                            Toast.makeText(getContext(), "get data is null", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Shot>> call, Throwable t) {
                        isLoading = false;
                        mProgressBar.setVisibility(View.GONE);
                        mRefreshLayout.setRefreshing(false);
                        Log.e(TAG, "get body fail " + t.getMessage());
                        Toast.makeText(getContext(), "get data fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public void onRefresh() {
        loadData(false);
    }
}
