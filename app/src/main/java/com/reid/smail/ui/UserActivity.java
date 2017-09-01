package com.reid.smail.ui;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.reid.smail.R;
import com.reid.smail.adapter.UserShotAdapter;
import com.reid.smail.content.Constant;
import com.reid.smail.model.Shot;
import com.reid.smail.model.User;
import com.reid.smail.net.NetService;
import com.reid.smail.net.api.ShotApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private User mUser;
    private int mAppBarState = EXPANDED;
    private int curPage = 1;
    private boolean isLoading;

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
            mUser = intent.getParcelableExtra("user");
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
        mAdapter = new UserShotAdapter(mUser.bio);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastCompletelyVisibleItemPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastCompletelyVisibleItemPosition + 3 >= recyclerView.getAdapter().getItemCount()){
                    loadData(true);
                }
            }
        });
    }


    private void bindData() {
        if (mUser == null) return;

        Glide.with(this).load(mUser.avatar_url).into(mAvatar);
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

        ShotApi shotApi = NetService.get().getShotApi();
        if (shotApi != null){
            Call<List<Shot>> call = shotApi.getUserShots("users", mUser.id, Constant.ACCESS_TOKEN, curPage);
            if (call != null){
                isLoading = true;
                call.enqueue(new Callback<List<Shot>>() {
                    @Override
                    public void onResponse(Call<List<Shot>> call, Response<List<Shot>> response) {
                        isLoading = false;
                        List<Shot> body = response.body();
                        if (body != null){
                            mAdapter.setData(body, curPage > 1);
                        }else {
                            Log.e(TAG, "body is null");
                            Toast.makeText(UserActivity.this, "get data is null", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Shot>> call, Throwable t) {
                        isLoading = false;
                        Log.e(TAG, "get body fail " + t.getMessage());
                        Toast.makeText(UserActivity.this, "get data fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
