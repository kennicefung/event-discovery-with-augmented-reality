package com.goer.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.goer.R;
import com.goer.controller.UserController;
import com.goer.model.Event;
import com.goer.view.adapter.SendInvitationRVAdapter;


public class InviteFriendsActivity extends AppCompatActivity{

    private RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        Toolbar toolbar = (Toolbar) findViewById(R.id.invite_friends_toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Send Invitation");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent i = getIntent();
        Event e = (Event) i.getSerializableExtra("EventModel");
        String uid = UserController.getUserModel(this).getUserID();

        rv = (RecyclerView)findViewById(R.id.rv_invite_friend);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        try {
            SendInvitationRVAdapter adapter = new SendInvitationRVAdapter(e.event_id, uid, UserController.getFriendList(uid));
            rv.setAdapter(adapter);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
