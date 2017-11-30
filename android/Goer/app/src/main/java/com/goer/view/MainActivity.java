package com.goer.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.animation.Animator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.animation.OvershootInterpolator;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;

import com.goer.view.adapter.MainViewPagerAdapter;
import com.goer.R;
import com.goer.view.fragment.*;
import com.goer.controller.UserController;

public class MainActivity extends AppCompatActivity implements AHBottomNavigation.OnTabSelectedListener {

    public AHBottomNavigation nav;
    private FloatingActionButton fab;
    private AHBottomNavigationViewPager viewPager;

    private MainViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(UserController.isUserLoggedIn(this)) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
            setSupportActionBar(toolbar);
            initUI();

            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addEvent = new Intent(getApplicationContext(), AddEventActivity.class);
                    startActivity(addEvent);
                }
            });
        }
        else
        {
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(login);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_logout :
                UserController.logoutUser(this);
                finish();
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initUI(){
        nav = (AHBottomNavigation) findViewById(R.id.bottom_nav);
        nav.setOnTabSelectedListener(this);

        AHBottomNavigationItem itm_explore = new AHBottomNavigationItem("Explore", R.drawable.whatshot);
        //AHBottomNavigationItem itm_search = new AHBottomNavigationItem("Search", R.drawable.search);
        AHBottomNavigationItem itm_scan = new AHBottomNavigationItem("Nearby",R.drawable.scan);
        //AHBottomNavigationItem itm_inbox = new AHBottomNavigationItem("Inbox",R.drawable.inbox);
        AHBottomNavigationItem itm_friend = new AHBottomNavigationItem("Friends",R.drawable.friend);
        AHBottomNavigationItem itm_user = new AHBottomNavigationItem("Profile",R.drawable.user);

        nav.addItem(itm_explore);
        //nav.addItem(itm_search);
        nav.addItem(itm_scan);
        //nav.addItem(itm_inbox);
        nav.addItem(itm_friend);
        nav.addItem(itm_user);

        nav.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));
        nav.setAccentColor(Color.parseColor("#EE7674"));

        viewPager = (AHBottomNavigationViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(4);
        adapter = new MainViewPagerAdapter(getSupportFragmentManager(), UserController.getUserModel(getApplicationContext()));
        viewPager.setAdapter(adapter);

        nav.setCurrentItem(0);

        setFloatingAddEventButton(adapter.getCurrentFragment().isAddEventButtonVisible());
    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        GoerFragment current = adapter.getCurrentFragment();
        if (wasSelected) {
            current.refresh();
            return true;
        }

        if (current != null) {
            current.hide();
        }

        viewPager.setCurrentItem(position, false);
        current = adapter.getCurrentFragment();
        current.display();

        setFloatingAddEventButton(current.isAddEventButtonVisible());
        return true;
    }

    private void setFloatingAddEventButton(boolean visible) {
        fab = (FloatingActionButton) findViewById(R.id.fab);

        if (visible) {
            fab.setVisibility(View.VISIBLE);
            fab.setAlpha(0f);
            fab.setScaleX(0f);
            fab.setScaleY(0f);
            fab.animate()
                    .alpha(1)
                    .scaleX(1)
                    .scaleY(1)
                    .setDuration(300)
                    .setInterpolator(new OvershootInterpolator())
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            fab.animate()
                                    .setInterpolator(new LinearOutSlowInInterpolator())
                                    .start();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .start();
        }
        else {
            if (fab.getVisibility() == View.VISIBLE) {
                fab.animate()
                        .alpha(0)
                        .scaleX(0)
                        .scaleY(0)
                        .setDuration(300)
                        .setInterpolator(new LinearOutSlowInInterpolator())
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                fab.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                                fab.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        })
                        .start();
            }
        }
    }
}
