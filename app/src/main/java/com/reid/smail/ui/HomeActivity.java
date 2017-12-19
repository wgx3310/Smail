package com.reid.smail.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.reid.smail.R;
import com.reid.smail.content.AccountManager;
import com.reid.smail.content.ImageLoader;
import com.reid.smail.fragment.BaseFragment;
import com.reid.smail.fragment.BucketsFragment;
import com.reid.smail.fragment.DesignFragment;
import com.reid.smail.fragment.HomeFragment;
import com.reid.smail.fragment.LikesFragment;
import com.reid.smail.model.shot.User;
import com.reid.smail.util.IntentUtils;

import java.util.List;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, AccountManager.AccountCallback {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;
    private ActionBarDrawerToggle mToggle;
    private ImageView mAvatar;
    private TextView mName;
    private ImageView mLogout;

    private AccountManager mAccountManager;

    private HomeFragment mHomeFragment;
    private BucketsFragment mBucketsFragment;
    private DesignFragment mDesignFragment;
    private LikesFragment mLikesFragment;

    private int mCurNavId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAccountManager = AccountManager.get();
        mAccountManager.addCallback(this);
        initView();
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mNavView = (NavigationView) findViewById(R.id.nav_view);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        if (mNavView != null) {
            View child = mNavView.getChildAt(0);
            if (child != null) {
                child.setVerticalScrollBarEnabled(false);
            }
        }
        mNavView.setCheckedItem(R.id.nav_home);
        mNavView.setNavigationItemSelectedListener(this);
        initNavHeaderView();

        showFragment();
    }

    private void showFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_enter,
                R.anim.fragment_exit, R.anim.fragment_enter, R.anim.fragment_exit);
        BaseFragment desFragment = getNavFragmentById();
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0){
            for (Fragment f : fragments){
                if (desFragment != f){
                    transaction.hide(f);
                }
            }
        }
        if (!desFragment.isAdded()){
            transaction.add(R.id.content_layout, desFragment);
        }
        transaction.show(desFragment).commitAllowingStateLoss();
    }

    private void initNavHeaderView() {
        View headerView = mNavView.inflateHeaderView(R.layout.activity_home_nav_header);
        mAvatar = headerView.findViewById(R.id.nav_header_avatar);
        mName = headerView.findViewById(R.id.nav_header_name);
        mLogout = headerView.findViewById(R.id.nav_header_logout);

        mAvatar.setOnClickListener(this);
        mName.setOnClickListener(this);
        mLogout.setOnClickListener(this);

        mAccountManager.updateUserInfo();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_settings:
                SettingsActivity.launch(this);
                break;
            case R.id.nav_home:
            case R.id.nav_design:
            case R.id.nav_buckets:
            case R.id.nav_likes:
                int oldNavId = mCurNavId;
                mCurNavId = item.getItemId();
                if (mCurNavId != oldNavId){
                    showFragment();
                    setTitle(item.getTitle());
                }
                break;
            case R.id.nav_weather:
                WeatherActivity.launch(this);
                break;
            default:
                showFragment();
                break;
        }

        if (mDrawerLayout != null){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private BaseFragment getNavFragmentById(){
        switch (mCurNavId){
            case R.id.nav_home:
                if (mHomeFragment == null){
                    mHomeFragment = new HomeFragment();
                }
                return mHomeFragment;
            case R.id.nav_design:
                if (mDesignFragment == null){
                    mDesignFragment = new DesignFragment();
                }
                return mDesignFragment;
            case R.id.nav_buckets:
                if (mBucketsFragment == null){
                    mBucketsFragment = new BucketsFragment();
                }
                return mBucketsFragment;
            case R.id.nav_likes:
                if (mLikesFragment == null){
                    mLikesFragment = new LikesFragment();
                }
                return mLikesFragment;
            default:
                if (mHomeFragment == null){
                    mHomeFragment = new HomeFragment();
                }
                mCurNavId = R.id.nav_home;
                return mHomeFragment;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nav_header_avatar:
            case R.id.nav_header_name:
                if (!mAccountManager .isLogin()){
                    login();
                } else {
                    IntentUtils.goUser(this, mAccountManager.getUser());
                }
                break;
            case R.id.nav_header_logout:
                mAccountManager.logout();
                break;
        }
    }

    private void login() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mAccountManager.login();
    }

    @Override
    public void onLogin(User user) {
        if (user == null || isDestroyed() || isFinishing()) return;

        if (!TextUtils.isEmpty(user.avatar_url)){
            ImageLoader.load(this, mAvatar, user.avatar_url, ImageLoader.Options.create().circleCrop());
        }

        mName.setText(user.name);
        mLogout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLogout() {
        if (isDestroyed() || isFinishing()) return;

        mAvatar.setImageResource(R.drawable.ic_avatar);
        mName.setText(R.string.click_to_login);
        mLogout.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        if (mAccountManager != null){
            mAccountManager.removeCallback(this);
            mAccountManager = null;
        }
        super.onDestroy();
    }
}
