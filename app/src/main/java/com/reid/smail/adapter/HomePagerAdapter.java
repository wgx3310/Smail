package com.reid.smail.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;

import com.reid.smail.fragment.BaseFragment;
import com.reid.smail.fragment.RecyclerFragment;
import com.reid.smail.model.span.TabSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reid on 2017/8/30.
 */

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    private List<RecyclerFragment> fragmentList = new ArrayList<>();
    private List<TabSpan> tabSpanList = new ArrayList<>();
    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(List<TabSpan> list){
        if (list != null && !list.isEmpty()){
            tabSpanList.clear();
            fragmentList.clear();
            tabSpanList.addAll(list);
            for (TabSpan span : list){
                RecyclerFragment fragment = RecyclerFragment.newInstance(span);
                fragmentList.add(fragment);
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return tabSpanList != null ? tabSpanList.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (tabSpanList != null){
            TabSpan tabSpan = tabSpanList.get(position);
            if (tabSpan != null && !TextUtils.isEmpty(tabSpan.title)){
                return tabSpan.title;
            }
        }
        return super.getPageTitle(position);
    }
}
