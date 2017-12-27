package com.reid.smail.ui;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reid.smail.R;
import com.reid.smail.adapter.UserShotAdapter;
import com.reid.smail.content.ImageLoader;
import com.reid.smail.content.Tips;
import com.reid.smail.content.SettingKey;
import com.reid.smail.adapter.holder.UserHeaderView;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.model.shot.User;
import com.reid.smail.net.loader.ShotLoader;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import reid.list.load.OnMoreListener;
import reid.list.PlasticAdapter;
import reid.list.PlasticView;

public class UserActivity extends BaseActivity implements View.OnClickListener {

    private static final int EXPANDED = 0x02;
    private static final int COLLAPSED = 0x03;
    private static final int INTERNEDIATE = 0x04;

    private ImageView mAvatar;
    private TextView mName;
    private TextView mLocation;
    private LinearLayout mShotsBtn;
    private LinearLayout mBucketsBtn;
    private LinearLayout mFollowersBtn;
    private LinearLayout mFollowingsBtn;
    private LinearLayout mLikesBtn;
    private LinearLayout mGetLikedBtn;
    private LinearLayout mProjectsBtn;
    private LinearLayout mTeamsBtn;
    private LinearLayout mLocationLayout;
    private TextView mShotsText;
    private TextView mBucketsText;
    private TextView mFollowersText;
    private TextView mFollowingsText;
    private TextView mLikesText;
    private TextView mGetLikedText;
    private TextView mProjectsText;
    private TextView mTeamsText;
    private AppBarLayout mAppBar;

    private PlasticView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private UserShotAdapter mAdapter;

    private User mUser;
    private int mAppBarState = EXPANDED;
    private int curPage = 1;
    private boolean isLoading;
    private ShotLoader mLoader = ShotLoader.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initToolbar();

        handleIntent();
        initView();
        bindData();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null){
            mUser = intent.getParcelableExtra(SettingKey.KEY_USER);
        }
    }

    private void initView() {
        mAvatar = findViewById(R.id.avatar);
        mName = findViewById(R.id.name);
        mLocation = findViewById(R.id.location);
        mShotsBtn = findViewById(R.id.shots_btn);
        mBucketsBtn = findViewById(R.id.buckets_btn);
        mFollowersBtn = findViewById(R.id.followers_btn);
        mFollowingsBtn = findViewById(R.id.followings_btn);
        mLikesBtn = findViewById(R.id.likes_btn);
        mGetLikedBtn = findViewById(R.id.get_liked_btn);
        mProjectsBtn = findViewById(R.id.projects_btn);
        mTeamsBtn = findViewById(R.id.teams_btn);
        mShotsBtn.setOnClickListener(this);
        mBucketsBtn.setOnClickListener(this);
        mFollowersBtn.setOnClickListener(this);
        mFollowingsBtn.setOnClickListener(this);
        mLikesBtn.setOnClickListener(this);
        mGetLikedBtn.setOnClickListener(this);
        mProjectsBtn.setOnClickListener(this);
        mTeamsBtn.setOnClickListener(this);
        mLocationLayout = findViewById(R.id.location_layout);
        mShotsText = findViewById(R.id.shots_text);
        mBucketsText = findViewById(R.id.buckets_text);
        mFollowersText = findViewById(R.id.followers_text);
        mFollowingsText = findViewById(R.id.followings_text);
        mLikesText = findViewById(R.id.likes_text);
        mGetLikedText = findViewById(R.id.get_liked_text);
        mProjectsText = findViewById(R.id.projects_text);
        mTeamsText = findViewById(R.id.teams_text);

        initAppBar();
        initRecyclerView();
    }

    private void initAppBar() {
        mAppBar = findViewById(R.id.app_bar);
        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0){
                    mAppBarState = EXPANDED;
                    setTitle("");
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()){
                    if (mAppBarState != COLLAPSED){
                        mAppBarState = COLLAPSED;
                        setTitle(mUser.name);
                    }
                } else {
                    if (mAppBarState != INTERNEDIATE){
                        mAppBarState = INTERNEDIATE;
                        setTitle("");
                    }
                }
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new UserShotAdapter(mUser);
        PlasticAdapter adapter = new PlasticAdapter(mAdapter);
        adapter.addHeaderView(new UserHeaderView(mUser));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(10, 10, 10, 10);
            }
        });
        mRecyclerView.setOnMoreListener(new OnMoreListener() {
            @Override
            public void onMore(int totalItemCount, int itemCountToLoadMore, int lastVisibleItemPosition) {
                loadData(true);
            }
        });
    }


    private void bindData() {
        if (mUser == null) return;

        ImageLoader.load(this, mAvatar, mUser.avatar_url,
                ImageLoader.Options.create().circleCrop());
        if (!TextUtils.isEmpty(mUser.location)){
            mLocation.setText(mUser.location);
        }
        mLocationLayout.setVisibility(!TextUtils.isEmpty(mUser.location)? View.VISIBLE:View.GONE);

        mName.setText(mUser.name);
        startCountAnimation();

        loadData(false);
    }

    private void startCountAnimation() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int fraction = (Integer) valueAnimator.getAnimatedValue();
                mShotsText.setText(String.valueOf((int)((float)fraction/100 * mUser.shots_count)));
                mBucketsText.setText(String.valueOf((int)((float)fraction/100*mUser.buckets_count)));
                mFollowersText.setText(String.valueOf((int)((float)fraction/100*mUser.followers_count)));
                mFollowingsText.setText(String.valueOf((int)((float)fraction/100*mUser.followings_count)));
                mLikesText.setText(String.valueOf((int)((float)fraction/100*mUser.likes_count)));
                mGetLikedText.setText(String.valueOf((int)((float)fraction/100*mUser.likes_received_count)));
                mProjectsText.setText(String.valueOf((int)((float)fraction/100*mUser.projects_count)));
                mTeamsText.setText(String.valueOf((int)((float)fraction/100*mUser.teams_count)));
            }
        });
        animator.setDuration(1000);
        animator.start();
    }

    private void loadData(final boolean loadMore) {
        if (isLoading){
            return;
        }

        curPage = loadMore?curPage+1:1;
        Disposable subscribe = mLoader.getUserShots(mUser.id, curPage)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        isLoading = true;
                    }
                })
                .subscribe(new Consumer<List<Shot>>() {
                    @Override
                    public void accept(List<Shot> shots) {
                        isLoading = false;

                        if (shots != null && shots.size() > 0){
                            mAdapter.setData(shots, curPage > 1);
                            mRecyclerView.loadMoreComplete();
                        }else {
                            mRecyclerView.loadMoreEnd();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        isLoading = false;
                        mRecyclerView.loadMoreComplete();
                        Tips.toast(R.string.load_data_failed);
                    }
                });
        addDisposable(subscribe);

    }

    @Override
    public void onClick(View v) {

    }
}
