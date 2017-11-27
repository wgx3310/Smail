package com.reid.smail.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.reid.smail.R;
import com.reid.smail.adapter.WeatherAdapter;
import com.reid.smail.model.weather.Weather;
import com.reid.smail.net.loader.WeatherLoader;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import reid.list.PlasticAdapter;
import reid.list.PlasticView;

public class WeatherActivity extends BaseActivity {

    public static void launch(Context context){
        if (context == null) return;

        context.startActivity(new Intent(context, WeatherActivity.class));
    }

    private PlasticView plasticView;
    private WeatherAdapter mAdapter;

    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        initToolbar(R.string.navigation_weather);
        initView();
        loadData();
    }

    private void initView() {
        plasticView = findViewById(R.id.recycler_view);
        plasticView.setLayoutManager(new LinearLayoutManager(this));
        plasticView.setLoadMoreEnable(false);
        mAdapter = new WeatherAdapter();
        PlasticAdapter adapter = new PlasticAdapter(mAdapter);
        plasticView.setAdapter(adapter);
        plasticView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    private void loadData() {
        if (isLoading) return;

        Disposable disposable = WeatherLoader.get().getWeather("北京")
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        isLoading = true;
                    }
                }).subscribe(new Consumer<Weather>() {
                    @Override
                    public void accept(Weather weather) throws Exception {
                        isLoading = false;
                        mAdapter.setData(weather);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        isLoading = false;
                        mAdapter.setData(null);
                    }
                });
        addDisposable(disposable);
    }
}
