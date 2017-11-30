package com.goer.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;

import com.goer.R;
import com.goer.controller.EventController;
import com.goer.controller.UserController;
import com.goer.model.Event;
import com.goer.model.User;
import com.goer.view.adapter.CommentRVAdapter;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import android.widget.LinearLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.Menu;
import android.animation.ObjectAnimator;
import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

public class EventDetailActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener  {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean isTheTitleVisible          = false;
    private boolean isTheTitleContainerVisible = true;

    private Event e;
    private User u, h;
    private boolean liked = false, joined = false;

    private Toolbar toolbar;
    private TextView toolbar_title;
    private LinearLayout title_container;
    private AppBarLayout appbar;
    private RecyclerView rv;
    private TextView comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        try {
            Intent i = getIntent();
            this.e = (Event) i.getSerializableExtra("EventModel");
            this.u = UserController.getUserModel(getApplicationContext());
            this.h = UserController.getUserByUserID(e.user_id);

            LinearLayout host_container = (LinearLayout) findViewById(R.id.event_detail_host_layout);
            TextView title = (TextView) findViewById(R.id.event_detail_name);
            TextView host = (TextView) findViewById(R.id.event_detail_host);
            ImageView img = (ImageView) findViewById(R.id.event_detail_image);
            TextView date = (TextView) findViewById(R.id.event_detail_date);
            TextView loc = (TextView) findViewById(R.id.event_detail_location);
            TextView des = (TextView) findViewById(R.id.event_detail_des);
            ImageView avatar = (ImageView) findViewById(R.id.event_detail_avatar);
            comment = (TextView) findViewById(R.id.event_detail_inputComment) ;
            Button btnComment = (Button) findViewById(R.id.event_detail_btnComment);

            toolbar = (Toolbar) findViewById(R.id.event_detail_toolbar);
            toolbar_title = (TextView) findViewById(R.id.main_textview_title);
            title_container = (LinearLayout) findViewById(R.id.main_linearlayout_title);
            appbar = (AppBarLayout) findViewById(R.id.main_appbar);
            rv = (RecyclerView)findViewById(R.id.rv_detail_comment);
            rv.setFocusable(false);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            rv.setLayoutManager(llm);

            CommentRVAdapter adapter = new CommentRVAdapter(EventController.getCommentsByEvent(e.event_id));
            rv.setAdapter(adapter);

            title.setText(e.title);
            toolbar_title.setText(e.title);
            host.setText(e.user_name);
            date.setText(e.getFormattedStartTime("E, MMM dd") + System.getProperty("line.separator") + e.getFormattedStartTime("h:mm a") + " - " + e.getFormattedEndTime("h:mm a"));
            des.setText(e.des);
            loc.setText(e.location);

            UrlImageViewHelper.setUrlDrawable(img, e.img);
            UrlImageViewHelper.setUrlDrawable(avatar, h.getAvatarURL());

            host_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(EventDetailActivity.this, ProfileActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("SelfUserModel", u);
                    b.putSerializable("TargetUserModel", h);
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                }
            });

            btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if(!comment.getText().toString().equals("")) {

                            if(EventController.writeComment(u.getUserID(), e.event_id, comment.getText().toString())){
                                comment.setText("");
                                CommentRVAdapter adapter = (CommentRVAdapter)rv.getAdapter();
                                adapter.updateDataSet(EventController.getCommentsByEvent(e.event_id));
                                adapter.notifyDataSetChanged();
                            }
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }



        appbar.addOnOffsetChangedListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        startAlphaAnimation(toolbar_title, 0, View.INVISIBLE);
        startBackgroundAnimation(toolbar, 0, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_detail, menu);
        try {
            if (EventController.hasLikedEvent(u.getUserID(), e.event_id)) {
                liked = true;
                menu.findItem(R.id.action_like).setIcon(ContextCompat.getDrawable(this,R.drawable.ic_heart_active));
            }

            if(u.getUserID().equals(e.user_id)){
                menu.findItem(R.id.action_cancel_event).setVisible(true);
                menu.findItem(R.id.action_join).setVisible(false);
            }else{
                menu.findItem(R.id.action_cancel_event).setVisible(false);
                menu.findItem(R.id.action_join).setVisible(true);
                if (EventController.hasJoinedEvent(u.getUserID(), e.event_id)) {
                    joined = true;
                    menu.findItem(R.id.action_join).setIcon(ContextCompat.getDrawable(this,R.drawable.ic_attend_active));
                }
            }
        }
        catch (Exception e){
                e.printStackTrace();
            }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_like :
                try {
                    if(!liked) {
                        EventController.likeEvent(u.getUserID(), e.event_id);
                        item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_heart_active));
                        liked = true;
                    }
                    else{
                        EventController.unlikeEvent(u.getUserID(), e.event_id);
                        item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_heart));
                        liked = false;
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            case R.id.action_join :
                try {
                    if(!joined) {
                        EventController.joinEvent(u.getUserID(), e.event_id);
                        item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_attend_active));
                        joined = true;
                    }
                    else
                    {
                        EventController.disjoinEvent(u.getUserID(), e.event_id);
                        item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_attend));
                        joined =false;
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            case R.id.action_invite:
                Intent intent = new Intent(this, InviteFriendsActivity.class);
                intent.putExtra("EventModel", e);
                startActivity(intent);
                return true;
            case R.id.action_cancel_event :
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                try {
                                    if(EventController.cancelEvent(e.event_id)) {
                                        finish();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder dlgBuilder  = new AlertDialog.Builder(this);
                dlgBuilder.setMessage("Are you sure to cancel the event?");
                dlgBuilder.setTitle("Warning");
                dlgBuilder.setPositiveButton("Yes", dialogClickListener);
                dlgBuilder.setNegativeButton("No", dialogClickListener);
                dlgBuilder.setCancelable(true);
                final AlertDialog dlg = dlgBuilder.create();
                dlg.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        dlg.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDarkGreen));
                        dlg.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDarkGreen));
                    }
                });

                dlg.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!isTheTitleVisible) {
                startAlphaAnimation(toolbar_title, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startBackgroundAnimation(toolbar, ALPHA_ANIMATIONS_DURATION, true);
                isTheTitleVisible = true;
            }

        } else {

            if (isTheTitleVisible) {
                startAlphaAnimation(toolbar_title, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startBackgroundAnimation(toolbar, ALPHA_ANIMATIONS_DURATION, false);
                isTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(isTheTitleContainerVisible) {
                startAlphaAnimation(title_container, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                isTheTitleContainerVisible = false;
            }

        } else {

            if (!isTheTitleContainerVisible) {
                startAlphaAnimation(title_container, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                isTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    public static void startBackgroundAnimation(View v,long duration, boolean visible){
        ObjectAnimator backgroundColorAnimator = null;
        if(visible) {
            backgroundColorAnimator = ObjectAnimator.ofObject(v,
                    "backgroundColor",
                    new ArgbEvaluator(),
                    Color.parseColor("#00EE7674"),
                    Color.parseColor("#EE7674"));
        }
        else
        {
            backgroundColorAnimator = ObjectAnimator.ofObject(v,
                    "backgroundColor",
                    new ArgbEvaluator(),
                    Color.parseColor("#EE7674"),
                    Color.parseColor("#00EE7674"));
        }
        backgroundColorAnimator.setDuration(duration);
        backgroundColorAnimator.start();
    }
}
