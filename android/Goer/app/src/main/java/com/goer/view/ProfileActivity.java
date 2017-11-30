package com.goer.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.goer.R;
import com.goer.view.fragment.ProfileFragment;
import com.goer.controller.UserController;
import com.goer.model.User;

public class ProfileActivity  extends AppCompatActivity {

    private User self;
    private User target;
    private boolean hasRequested = false;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_profile );

        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle args = getIntent().getExtras();
        if(args != null) {
            self = (User) args.getSerializable("SelfUserModel");
            target = (User) args.getSerializable("TargetUserModel");
        }

        args = new Bundle();
        args.putSerializable("UserModel", target);
        ProfileFragment p = new ProfileFragment();
        p.setArguments(args);

        final FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace( R.id.profile_content, p );
        fragmentTransaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem add_friend = menu.findItem(R.id.action_add_friend);
        MenuItem remove_friend = menu.findItem(R.id.action_remove_friend);
        MenuItem accept_friend = menu.findItem(R.id.action_accept_friend);
        MenuItem reject_friend = menu.findItem(R.id.action_reject_friend);
        if(!self.getUserID().equals(target.getUserID())) {
            try {
                if (UserController.isFriend(self.getUserID(), target.getUserID())) {
                    add_friend.setVisible(false);
                    remove_friend.setVisible(true);
                    accept_friend.setVisible(false);
                    reject_friend.setVisible(false);
                } else {
                    if (!UserController.hasRequestedFriend(target.getUserID(), self.getUserID())) {
                        add_friend.setVisible(true);
                        remove_friend.setVisible(false);
                        accept_friend.setVisible(false);
                        reject_friend.setVisible(false);
                        if (UserController.hasRequestedFriend(self.getUserID(), target.getUserID())) {
                            hasRequested = true;
                            add_friend.setTitle("Requested");
                        } else {
                            hasRequested = false;
                            add_friend.setTitle("Add Friend");
                        }
                    } else {
                        add_friend.setVisible(false);
                        remove_friend.setVisible(false);
                        accept_friend.setVisible(true);
                        reject_friend.setVisible(true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            add_friend.setVisible(false);
            remove_friend.setVisible(false);
            accept_friend.setVisible(false);
            reject_friend.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                    return true;
                case R.id.action_add_friend:
                    if (!hasRequested) {
                        UserController.sendFriendRequest(self.getUserID(), target.getUserID());
                    } else {
                        UserController.unfriend(self.getUserID(), target.getUserID());
                    }
                    invalidateOptionsMenu();
                    return true;
                case R.id.action_remove_friend:
                    UserController.unfriend(self.getUserID(), target.getUserID());
                    invalidateOptionsMenu();
                    return true;
                case R.id.action_accept_friend:
                    UserController.sendFriendRequest(self.getUserID(), target.getUserID());
                    invalidateOptionsMenu();
                    return true;
                case R.id.action_reject_friend:
                    UserController.unfriend(self.getUserID(), target.getUserID());
                    invalidateOptionsMenu();
                    return true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}