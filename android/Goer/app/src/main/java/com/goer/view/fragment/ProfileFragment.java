package com.goer.view.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.goer.R;
import com.goer.model.User;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;



public class ProfileFragment extends GoerFragment {

    private User user;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("UserModel");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        ImageView profile_pic = (ImageView) v.findViewById(R.id.profile_avatar);
        TextView username = (TextView) v.findViewById(R.id.profile_username);

        UrlImageViewHelper.setUrlDrawable(profile_pic, user.getAvatarURL());
        username.setText(user.getUsername());

        /*viewPager = (ViewPager) v.findViewById(R.id.profile_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) v.findViewById(R.id.profile_tabs);
        tabLayout.setupWithViewPager(viewPager);*/
        return v;
    }

    @Override
    public Boolean isAddEventButtonVisible() {
        return false;
    }

    /*private void setupViewPager(ViewPager viewPager) {
        GeneralViewPagerAdapter adapter = new GeneralViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new FriendNewsFragment(), "News Feed");
        adapter.addFragment(new FriendRequestFragment(), "Requests");
        viewPager.setAdapter(adapter);
    }*/

}