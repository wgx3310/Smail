package com.reid.smail.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.reid.smail.R;
import com.reid.smail.adapter.DetailAdapter;
import com.reid.smail.content.Constant;
import com.reid.smail.content.FavoriteManager;
import com.reid.smail.content.Reminder;
import com.reid.smail.content.SettingKey;
import com.reid.smail.io.offline.Downloader;
import com.reid.smail.model.shot.Comment;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.net.NetService;
import com.reid.smail.net.api.ShotApi;
import com.reid.smail.util.IntentUtils;
import com.reid.smail.view.glide.GlideApp;

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
    private boolean mLiked;

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
            mShot = intent.getParcelableExtra(SettingKey.KEY_SHOT);
        }
    }

    private void initView() {
        mPoster = findViewById(R.id.post_img);
        mFavBtn = findViewById(R.id.fav_btn);
        mFavBtn.setOnClickListener(this);

        initRecyclerView();
    }

    private void checkShotLiked() {
        if (mShot != null){
            FavoriteManager.check(this, mShot.id, onFavListener);
        }
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
                GlideApp.with(this).load(postUrl).thumbnail(0.1f).placeholder(R.drawable.loading).into(mPoster);
            }
        }

        checkShotLiked();
        loadData(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkShotLiked();
    }

    private void loadData(boolean loadMore) {
        if (isLoading || mShot == null){
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
                            Reminder.toast(R.string.empty_data);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Comment>> call, Throwable t) {
                        isLoading = false;
                        Reminder.toast(R.string.load_data_failed);
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fav_btn:
                onFavClick();
                break;
        }
    }

    private void onFavClick() {
        if (mShot == null) return;

        if (mLiked){
            FavoriteManager.unlike(this, mShot.id, onFavListener);
        }else {
            FavoriteManager.like(this, mShot.id, onFavListener);
        }
        doFavAnimation();
    }

    private void doFavAnimation() {
        if (mFavBtn == null) return;
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFavBtn, "scaleY", 0.7f, 1.5f, 1f)
                .setDuration(1200);
        animator.setInterpolator(new OvershootInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                updateFavBtnState();
            }
        });
        animator.start();
    }

    private FavoriteManager.OnFavListener onFavListener = new FavoriteManager.OnFavListener() {
        @Override
        public void onSuccess(long id, boolean like) {
            mLiked = like;
            updateFavBtnState();
        }

        @Override
        public void onFail() {
            Reminder.toast(R.string.fav_failed);
        }
    };

    private void updateFavBtnState() {
        if (mFavBtn == null) return;
        mFavBtn.setImageResource(mLiked? R.drawable.ic_favorite_white_18dp:R.drawable.ic_favorite_border_light_24dp);
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
                if (mShot != null){
                    IntentUtils.goBrowser(this, mShot.html_url);
                }
                break;
            case R.id.add_bucket:
                break;
        }
        return true;
    }

    //TODO
    private void clickDownload() {
        if (mShot != null && mShot.images != null){
            String url = mShot.images.hidpi != null?mShot.images.hidpi:mShot.images.normal;
            File images = getExternalFilesDir("images");
            String path = new File(images, mShot.title +"." + url.substring(url.lastIndexOf(".")+1)).getAbsolutePath();
            Downloader.download(this, url, path);
        }
    }

    private void clickShare() {
        if (mShot != null && mShot.user != null){
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
}
