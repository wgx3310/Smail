package com.reid.smail.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.reid.smail.R;
import com.reid.smail.adapter.DetailAdapter;
import com.reid.smail.content.Constant;
import com.reid.smail.model.Comment;
import com.reid.smail.model.Shot;
import com.reid.smail.net.NetService;
import com.reid.smail.net.api.ShotApi;
import com.reid.smail.util.IntentUtils;
import com.reid.smail.util.Utils;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private Shot mShot;

    private ImageView mPoster;
    private FloatingActionButton mFavBtn;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private DetailAdapter mAdapter;

    private int curPage = 1;
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initToolbar();

        handleIntent();
        initView();
        bindData();
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null){
            mShot = intent.getParcelableExtra("shot");
        }
    }

    private void initView() {
        mPoster = findViewById(R.id.post_img);
        mFavBtn = findViewById(R.id.fav_btn);
        mFavBtn.setOnClickListener(this);

        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DetailAdapter(mShot);
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
    }

    private void bindData() {
        if (mShot == null) return;

        if (mShot.images != null){
            String postUrl = mShot.images.hidpi != null? mShot.images.hidpi:mShot.images.normal;
            if (!TextUtils.isEmpty(postUrl)){
                Glide.with(this).load(postUrl).thumbnail(0.1f).placeholder(R.drawable.loading).into(mPoster);
            }
        }

        loadData(false);
    }

    private void loadData(boolean loadMore) {
        if (isLoading){
            return;
        }

        curPage = loadMore?curPage+1:1;

        ShotApi shotApi = NetService.get().getShotApi();
        if (shotApi != null){
            Call<List<Comment>> call = shotApi.getShotComments(mShot.id, Constant.ACCESS_TOKEN, curPage, 100);
            if (call != null){
                isLoading = true;
                call.enqueue(new Callback<List<Comment>>() {
                    @Override
                    public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                        isLoading = false;
                        List<Comment> body = response.body();
                        if (body != null){
                            mAdapter.setData(body, curPage > 1);
                        }else {
                            Log.e(TAG, "body is null");
                            Toast.makeText(DetailActivity.this, "get data is null", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Comment>> call, Throwable t) {
                        isLoading = false;
                        Log.e(TAG, "get body fail " + t.getMessage());
                        Toast.makeText(DetailActivity.this, "get data fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fav_btn:
                Toast.makeText(this, "click fav button", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share:
                clickShare();
                break;
            case R.id.download:
                clickDownload();
                break;
            case R.id.open_in_browser:
                IntentUtils.goBrowser(this, mShot.html_url);
                break;
            case R.id.add_bucket:
                break;
        }
        return true;
    }

    //TODO
    private void clickDownload() {
        String url = mShot.images.hidpi != null?mShot.images.hidpi:mShot.images.normal;
        String path = Environment.getExternalStorageDirectory() + File.separator + mShot.title +"." + url.substring(url.lastIndexOf(".")+1);
        Utils.downloadImage(this, url, path);
    }

    private void clickShare() {
        StringBuilder msg = new StringBuilder();
        msg.append("我分享了")
            .append(mShot.user.name)
            .append("的作品《")
            .append(mShot.title)
            .append("》\n")
            .append(mShot.html_url)
            .append("\n来自\"")
            .append(getString(R.string.app_name))
            .append("\"");
        IntentUtils.shareTo(this, null, msg.toString());
    }
}
