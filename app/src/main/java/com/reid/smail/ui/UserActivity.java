package com.reid.smail.ui;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.reid.smail.R;
import com.reid.smail.adapter.UserShotAdapter;
import com.reid.smail.content.Reminder;
import com.reid.smail.content.SettingKey;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.model.shot.User;
import com.reid.smail.net.loader.ShotLoader;
import com.reid.smail.view.glide.GlideApp;

import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

public class UserActivity extends BaseActivity {

    private static final int EXPANDED = 0x02;
    private static final int COLLAPSED = 0x03;
    private static final int INTERNEDIATE = 0x04;

    private Toolbar mToolbar;
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

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private UserShotAdapter mAdapter;
    private ProgressBar mProgressBar;

    private User mUser;
    private int mAppBarState = EXPANDED;
    private int curPage = 1;
    private boolean isLoading;
    private ShotLoader mLoader = ShotLoader.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
        mLocationLayout = findViewById(R.id.location_layout);
        mShotsText = findViewById(R.id.shots_text);
        mBucketsText = findViewById(R.id.buckets_text);
        mFollowersText = findViewById(R.id.followers_text);
        mFollowingsText = findViewById(R.id.followings_text);
        mLikesText = findViewById(R.id.likes_text);
        mGetLikedText = findViewById(R.id.get_liked_text);
        mProjectsText = findViewById(R.id.projects_text);
        mTeamsText = findViewById(R.id.teams_text);
        mProgressBar = findViewById(R.id.progress_bar);

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
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(10, 10, 10, 10);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastCompletelyVisibleItemPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastCompletelyVisibleItemPosition + 3 >= recyclerView.getAdapter().getItemCount()){
//                    loadData(true);
                }
            }
        });
    }


    private void bindData() {
        if (mUser == null) return;

        GlideApp.with(this).load(mUser.avatar_url).circleCrop().into(mAvatar);
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

    private void loadData(boolean loadMore) {
        if (isLoading){
            return;
        }

        curPage = loadMore?curPage+1:1;
        isLoading = true;
        Subscription subscribe = mLoader.getUserShots(mUser.id, curPage).subscribe(new Action1<List<Shot>>() {
            @Override
            public void call(List<Shot> shots) {
                isLoading = false;
                mProgressBar.setVisibility(View.GONE);
                if (shots != null){
                    mAdapter.setData(shots, curPage > 1);
                }else {
                    Reminder.toast(R.string.empty_data);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                isLoading = false;
                mProgressBar.setVisibility(View.GONE);
                Reminder.toast(R.string.load_data_failed);
            }
        });
        addSubscription(subscribe);

    }
}
