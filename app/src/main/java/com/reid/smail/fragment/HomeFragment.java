package com.reid.smail.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reid.smail.R;
import com.reid.smail.adapter.HomePagerAdapter;
import com.reid.smail.model.span.TabSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private HomePagerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        setupViewPager(view);
        setupTabLayout(view);
    }

    private void setupTabLayout(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.home_tab);
        if (mViewPager == null) {
            setupViewPager(view);
        }
        mTabLayout.setupWithViewPager(mViewPager, false);
    }

    private void setupViewPager(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.home_view_pager);
        mViewPager.setOffscreenPageLimit(2);
        mAdapter = new HomePagerAdapter(getChildFragmentManager());
        List<TabSpan> spans = new ArrayList<>();
        TabSpan sortSpan = new TabSpan();
        sortSpan.title = "popular";
        sortSpan.sort = "comments";
        sortSpan.id = String.valueOf(1);
        spans.add(sortSpan);
        TabSpan recentSpan = new TabSpan();
        recentSpan.title = "recent";
        recentSpan.sort = "recent";
        recentSpan.id = String.valueOf(2);
        spans.add(recentSpan);
        TabSpan viewSpan = new TabSpan();
        viewSpan.title = "views";
        viewSpan.sort = "views";
        viewSpan.id = String.valueOf(3);
        spans.add(viewSpan);
        mAdapter.setData(spans);
        mViewPager.setAdapter(mAdapter);
    }
}
