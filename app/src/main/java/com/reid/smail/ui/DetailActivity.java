package com.reid.smail.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.reid.smail.R;
import com.reid.smail.adapter.DetailAdapter;
import com.reid.smail.content.AccountManager;
import com.reid.smail.content.FavoriteManager;
import com.reid.smail.content.Reminder;
import com.reid.smail.content.SettingKey;
import com.reid.smail.holder.DetailHeaderView;
import com.reid.smail.io.offline.Downloader;
import com.reid.smail.model.shot.Comment;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.net.loader.ShotLoader;
import com.reid.smail.util.IntentUtils;
import com.reid.smail.view.glide.GlideApp;

import java.io.File;
import java.util.List;

import reid.list.load.OnMoreListener;
import reid.list.PlasticAdapter;
import reid.list.PlasticView;
import reid.utils.AppHelper;
import rx.Subscription;
import rx.functions.Action1;

public class DetailActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private Shot mShot;

    private ImageView mPoster;
    private FloatingActionButton mFavBtn;
    private PlasticView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private DetailAdapter mAdapter;

    private View mAddCommentLayout;
    private ImageView mAvatar;
    private ImageView mSendBtn;
    private ProgressBar mSendProgress;
    private EditText mCommentEdit;

    private int curPage = 1;
    private boolean isLoading;
    private boolean mLiked;
    private ShotLoader mLoader = ShotLoader.get();

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
        initCommentView();
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
        PlasticAdapter adapter = new PlasticAdapter(mAdapter);
        adapter.addHeaderView(new DetailHeaderView(mShot));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setOnMoreListener(new OnMoreListener() {
            @Override
            public void onMore(int totalItemCount, int itemCountToLoadMore, int lastVisibleItemPosition) {
                loadData(true);
            }
        });
    }

    private void initCommentView() {
        mAddCommentLayout = findViewById(R.id.add_comment_layout);
        mAddCommentLayout.setVisibility(View.GONE);
        mAvatar = findViewById(R.id.comment_avatar);
        mSendBtn = findViewById(R.id.send_btn);
        mSendProgress = findViewById(R.id.send_progress);
        mCommentEdit = findViewById(R.id.comment_edit);
        if (mShot == null){
            mAddCommentLayout.setVisibility(View.GONE);
        }

        if (AccountManager.get().isLogin()){
            mCommentEdit.setFocusable(true);
            mCommentEdit.setFocusableInTouchMode(true);
        }

        mCommentEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!AccountManager.get().isLogin()){
                    Reminder.toast(R.string.not_login);
                }
            }
        });

        mCommentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence)){
                    mSendBtn.setImageResource(R.drawable.ic_send_disable_24dp);
                    mSendBtn.setEnabled(false);
                }else {
                    mSendBtn.setImageResource(R.drawable.ic_send_enable_24dp);
                    mSendBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!AccountManager.get().isLogin()){
                    Reminder.toast(R.string.not_login);
                    return;
                }

                if (mCommentEdit != null && !TextUtils.isEmpty(mCommentEdit.getText())
                        && !TextUtils.isEmpty(mCommentEdit.getText().toString()) && !TextUtils.isEmpty(mCommentEdit.getText().toString().trim())){
                    if (mShot == null) return;
                    String text = mCommentEdit.getText().toString().trim();
                    Subscription subscribe = mLoader.createComment(mShot.id, text)
                            .subscribe(new Action1<Comment>() {
                                @Override
                                public void call(Comment comment) {
                                    if (comment != null){
                                        mAdapter.appendData(comment, true);
                                        mCommentEdit.setText("");
                                    }else {
                                        Reminder.toast(R.string.error_no_player);
                                    }
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    Reminder.toast(R.string.error_add_comment);
                                }
                            });
                    addSubscription(subscribe);
                }else {
                    Reminder.toast(R.string.empty_comment_hint);
                }
                AppHelper.hideSoftInput(DetailActivity.this);
            }
        });
    }

    private void bindData() {
        if (mShot == null) return;

        if (mShot.images != null){
            String postUrl = mShot.images.hidpi != null? mShot.images.hidpi:mShot.images.normal;
            if (!TextUtils.isEmpty(postUrl)){
                GlideApp.with(this).load(postUrl).thumbnail(0.1f).placeholder(R.drawable.loading_icon).into(mPoster);
            }
        }

        if (AccountManager.get().isLogin()){
            GlideApp.with(this).load(AccountManager.get().getUser().avatar_url).circleCrop().into(mAvatar);
            checkShotLiked();
        }

        loadData(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AccountManager.get().isLogin()){
            checkShotLiked();
        }
    }

    private void loadData(boolean loadMore) {
        if (isLoading || mShot == null){
            return;
        }

        curPage = loadMore?curPage+1:1;
        isLoading = true;

        Subscription subscribe = mLoader.getShotComments(mShot.id, curPage)
                .subscribe(new Action1<List<Comment>>() {
                    @Override
                    public void call(List<Comment> comments) {
                        isLoading = false;
                        if (comments != null && comments.size() > 0){
                            mAdapter.setData(comments, curPage > 1);
                            mRecyclerView.loadMoreComplete();
                        }else {
                            mRecyclerView.loadMoreEnd();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        isLoading = false;
                        mRecyclerView.loadMoreComplete();
                        Reminder.toast(R.string.load_data_failed);
                    }
                });
        addSubscription(subscribe);
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
