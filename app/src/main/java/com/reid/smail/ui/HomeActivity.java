package com.reid.smail.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.reid.smail.R;
import com.reid.smail.content.Constant;
import com.reid.smail.fragment.HomeFragment;
import com.reid.smail.fragment.RecyclerFragment;
import com.reid.smail.model.Shot;
import com.reid.smail.model.span.TabSpan;
import com.reid.smail.net.NetService;
import com.reid.smail.net.api.ShotApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavView;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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

        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, new HomeFragment()).commitAllowingStateLoss();
//        doTestAction();
    }

    private void doTestAction() {
        ShotApi shotApi = NetService.get().getShotApi();
        if (shotApi != null){
            Call<List<Shot>> sort = shotApi.getShots(Constant.ACCESS_TOKEN, null, null, "sort", 1);
            if (sort != null){
                sort.enqueue(new Callback<List<Shot>>() {
                    @Override
                    public void onResponse(Call<List<Shot>> call, Response<List<Shot>> response) {
                        List<Shot> body = response.body();
                        if (body != null){
                            Log.e("TAG" ,"body : " + body);

                        }else {
                            Log.e(TAG, "body is null");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Shot>> call, Throwable t) {
                        Log.e(TAG, "get body fail " + t.getMessage());
                    }
                });
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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
}
