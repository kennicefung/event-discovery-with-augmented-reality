package com.goer.model;

import com.goer.helper.GlobalHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.Serializable;

/**
 * Created by kennicefung on 1/5/2017.
 */

public class Comment implements Serializable {
    private String user_id;
    private String username;
    private String avatar = "default_avatar.jpg";
    private String event_id;
    private String comment_content;
    private Date cdate;

    public Comment(){}

    public Comment(String user_id, String username, String avatar, String event_id, String comment_content, String cdate){
        this.user_id = user_id;
        this.username = username;
        this.avatar = (avatar.equals("") || avatar == null) ? this.avatar : avatar;
        this.event_id = event_id;
        this.comment_content = comment_content;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(cdate);
            this.cdate = date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getUserID(){return user_id;}
    public String getUsername(){return username;}
    public String getAvatar(){return avatar;}
    public String getAvatarURL(){return GlobalHelper.UserAvatarPath()+ avatar;}
    public String getEventID(){return event_id;}
    public String getContent(){return comment_content;}
    public String getCDate(){return new SimpleDateFormat("MM-dd hh:mm:ss").format(cdate);}

}
