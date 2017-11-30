package com.goer.model;

import com.goer.helper.GlobalHelper;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by kennicefung on 1/5/2017.
 */

public class UserAction implements Serializable {
    private String user_id;
    private String username;
    private String event_id;
    private String event_name;
    private String avatar = "default_avatar.jpg";
    private String action;
    private Date action_dt;

    public UserAction(){}

    public UserAction(String user_id, String username, String event_id, String event_name, String avatar, String action, String action_dt){
        this.user_id = user_id;
        this.username = username;
        this.event_id = event_id;
        this.event_name = event_name;
        this.avatar = (avatar.equals("") || avatar == null) ? this.avatar : avatar;
        this.action = action;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(action_dt);
            this.action_dt = date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getUserID(){return user_id;}
    public String getUsername(){return username;}
    public String getEventID(){return event_id;}
    public String getEventName(){return event_name;}
    public String getAvatar(){return avatar;}
    public String getAvatarURL(){return GlobalHelper.UserAvatarPath()+ avatar;}
    public String getNewsMsg(){
        String newsMsg = "";
        switch(Integer.parseInt(action)){
            case 1: newsMsg = "<b>" + username + "</b> published an event: <b>" + event_name + "</b>";
                break;
            case 2: newsMsg = "<b>" + username + "</b> is going to: <b>" + event_name + "</b>";
                break;
            case 3: newsMsg = "<b>" + username + "</b> liked <b>" + event_name + "</b>";
                break;
            case 4: newsMsg ="<b>" + username + "</b> leave a comment on event: <b>" + event_name + "</b>";
                break;
        }
        return newsMsg;}
    public String getActionDt(){return new SimpleDateFormat("MM-dd hh:mm:ss").format(action_dt);}

}
