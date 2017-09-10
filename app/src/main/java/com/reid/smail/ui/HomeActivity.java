package com.reid.smail.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
import com.reid.smail.fragment.HomeFragment;
import com.reid.smail.model.shot.User;
import com.reid.smail.view.glide.GlideApp;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, AccountManager.AccountCallback {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavView;
    private ActionBarDrawerToggle mToggle;
    private ImageView mAvatar;
    private TextView mName;
    private ImageView mLogout;

    private AccountManager mAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAccountManager = AccountManager.get();
        mAccountManager.addCallback(this);
        mAccountManager.checkAuthCallback(getIntent());
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

        mNavView.setCheckedItem(R.id.nav_home);
        mNavView.setNavigationItemSelectedListener(this);
        initNavHeaderView();

        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, new HomeFragment()).commitAllowingStateLoss();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mAccountManager.checkAuthCallback(intent);
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
                Intent settingIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingIntent);
                break;
        }

        if (mDrawerLayout != null){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
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
                    //TODO 去往自己主页
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
            GlideApp.with(this).load(user.avatar_url).circleCrop().into(mAvatar);
        }

        mName.setText(user.name);
        mLogout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLogout() {
        GlideApp.with(this).load(R.mipmap.ic_launcher_round).circleCrop().into(mAvatar);
        mName.setText(R.string.click_to_login);
        mLogout.setVisibility(View.GONE);
    }
}
