package com.goer.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import com.goer.view.adapter.GeneralViewPagerAdapter;

import com.goer.R;


public class FriendsFragment extends GoerFragment{
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.friends_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) v.findViewById(R.id.friends_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return v;
    }


    @Override
    public Boolean isAddEventButtonVisible() {
        return false;
    }

    private void setupViewPager(ViewPager viewPager) {
        GeneralViewPagerAdapter adapter = new GeneralViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new FriendNewsFragment(), "News Feed");
        adapter.addFragment(new FriendRequestFragment(), "Requests");
        adapter.addFragment(new InvitationFragment(), "Invitation");
        viewPager.setAdapter(adapter);
    }

}
