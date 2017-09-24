package com.reid.smail.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
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
 * Created by reid on 2017/9/23.
 */

public class DesignFragment extends BaseFragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private HomePagerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_design, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewPager(view);
        setupTabLayout(view);
    }

    private void setupTabLayout(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.home_tab);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        if (mViewPager == null) {
            setupViewPager(view);
        }
        mTabLayout.setupWithViewPager(mViewPager, false);
    }

    private void setupViewPager(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.home_view_pager);
        mAdapter = new HomePagerAdapter(getChildFragmentManager());
        List<TabSpan> spans = new ArrayList<>();
        {
            TabSpan span = new TabSpan();
            span.title = "animated";
            span.list = "animated";
            span.id = String.valueOf(1);
            spans.add(span);
        }
        {
            TabSpan span = new TabSpan();
            span.title = "attachments";
            span.list = "attachments";
            span.id = String.valueOf(1);
            spans.add(span);
        }
        {
            TabSpan span = new TabSpan();
            span.title = "debuts";
            span.list = "debuts";
            span.id = String.valueOf(1);
            spans.add(span);
        }
        {
            TabSpan span = new TabSpan();
            span.title = "playoffs";
            span.list = "playoffs";
            span.id = String.valueOf(1);
            spans.add(span);
        }
        {
            TabSpan span = new TabSpan();
            span.title = "rebounds";
            span.list = "rebounds";
            span.id = String.valueOf(1);
            spans.add(span);
        }
        {
            TabSpan span = new TabSpan();
            span.title = "teams";
            span.list = "teams";
            span.id = String.valueOf(1);
            spans.add(span);
        }

        mAdapter.setData(spans);
        mViewPager.setAdapter(mAdapter);
    }
}
