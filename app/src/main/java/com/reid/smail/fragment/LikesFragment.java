package com.reid.smail.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.reid.smail.R;
import com.reid.smail.adapter.RecyclerAdapter;
import com.reid.smail.content.AccountManager;
import com.reid.smail.content.AppGson;
import com.reid.smail.content.Tips;
import com.reid.smail.io.ACache;
import com.reid.smail.model.shot.Like;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.net.loader.ShotLoader;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import reid.list.PlasticAdapter;
import reid.list.PlasticView;
import reid.utils.Logger;

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

    private Observable<List<Shot>> getCacheObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String cache = ACache.get(getContext()).getString("LikesFragment");
                if (cache == null) cache = "";
                emitter.onNext(cache);
                emitter.onComplete();
            }
        }).map(new Function<String, List<Shot>>() {
            @Override
            public List<Shot> apply(String s) throws Exception {
                if (TextUtils.isEmpty(s)) {
                    return new ArrayList<>();
                } else {
                    return AppGson.get().fromJson(s, new TypeToken<List<Shot>>() {
                    }.getType());
                }
            }
        });
    }

    private Observable<List<Shot>> getNetworkObservable(){
        return ShotLoader.get().getMyLikes()
                .map(new Function<List<Like>, List<Shot>>() {
                    @Override
                    public List<Shot> apply(List<Like> likes) throws Exception {
                        List<Shot> shots = new ArrayList<>();
                        for (Like like : likes){
                            shots.add(like.shot);
                        }
                        return shots;
                    }
                }).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends List<Shot>>>() {
                    @Override
                    public ObservableSource<? extends List<Shot>> apply(Throwable throwable) throws Exception {
                        return Observable.never();
                    }
                });
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

        Disposable subscribe = getNetworkObservable().subscribeOn(Schedulers.io()).publish(new Function<Observable<List<Shot>>, ObservableSource<List<Shot>>>() {
            @Override
            public ObservableSource<List<Shot>> apply(Observable<List<Shot>> listObservable) throws Exception {
                return Observable.merge(listObservable, getCacheObservable().subscribeOn(Schedulers.io()).takeUntil(listObservable));
            }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                isLoading = true;
            }
        }).doOnNext(new Consumer<List<Shot>>() {
            @Override
            public void accept(List<Shot> shots) throws Exception {
                ACache.get(getContext()).put("LikesFragment", AppGson.get().toJson(shots));
            }
        }).subscribe(new Consumer<List<Shot>>() {
            @Override
            public void accept(List<Shot> shots) throws Exception {
                isLoading = false;
                if (shots != null && shots.size() > 0) {
                    inited = true;
                    mAdapter.setData(shots);
                    mRecyclerView.loadMoreComplete();
                } else {
                    mRecyclerView.loadMoreEnd(true);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
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
