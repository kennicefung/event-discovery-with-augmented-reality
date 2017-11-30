package com.goer.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GeneralViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> friendsFragmentList = new ArrayList<>();
    private final List<String> friendsFragmentTitleList = new ArrayList<>();

    public GeneralViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return friendsFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return friendsFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        friendsFragmentList.add(fragment);
        friendsFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return friendsFragmentTitleList.get(position);
    }
}