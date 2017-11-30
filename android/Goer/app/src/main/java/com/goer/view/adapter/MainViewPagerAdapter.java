package com.goer.view.adapter;

import com.goer.view.fragment.*;

import java.util.ArrayList;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import android.os.Bundle;

import com.goer.model.User;
/**
 * Created by kennicefung on 13/6/2016.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter  {

    private ArrayList<GoerFragment> fragments = new ArrayList<GoerFragment>();
    private GoerFragment currentFragment;
    public MainViewPagerAdapter(FragmentManager fm, User u) {
        super(fm);

        Bundle args = new Bundle();
        args.putSerializable("user_id", u.getUserID());
        ExploreFragment e = new ExploreFragment();
        e.setArguments(args);
        fragments.add(e);
        NearbyFragment n = new NearbyFragment();
        n.setArguments(args);
        fragments.add(n);
        fragments.add(new FriendsFragment());
        args = new Bundle();
        args.putSerializable("UserModel", u);
        ProfileFragment p = new ProfileFragment();
        p.setArguments(args);
        fragments.add(p);

        currentFragment = getItem(0);
    }

    @Override
    public GoerFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = ((GoerFragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    public GoerFragment getCurrentFragment() {
        return currentFragment;
    }
}
