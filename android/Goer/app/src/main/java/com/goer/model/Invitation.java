package com.goer.model;

import com.goer.helper.GlobalHelper;

import java.io.Serializable;

/**
 * Created by kennicefung on 1/5/2017.
 */

public class Invitation implements Serializable {
    private String inv_id;
    private String sender_id;
    private String sender;
    private String sender_avatar = "default_avatar.jpg";
    private String event_id;
    private String event_name;
    private String reciever_id;

    public Invitation(){}

    public Invitation(String inv_id, String sender_id, String sender, String sender_avatar, String event_id, String event_name, String reciever_id){
        this.inv_id = inv_id;
        this.sender_id = sender_id;
        this.sender = sender;
        this.sender_avatar = (sender_avatar.equals("") || sender_avatar == null) ? this.sender_avatar : sender_avatar;
        this.event_id = event_id;
        this.event_name = event_name;
        this.reciever_id = reciever_id;
    }

    public String getInvID(){return inv_id;}
    public String getSenderID(){return sender_id;}
    public String getSender(){return sender;}
    public String getSenderAvatar(){return sender_avatar;}
    public String getSenderAvatarURL(){return GlobalHelper.UserAvatarPath()+ sender_avatar;}
    public String getEventID(){return event_id;}
    public String getEventName(){return event_name;}
    public String getRecieverID(){return reciever_id;}

    public String getInvitationMsg(){
        return "<b>" + sender + "</b> invites you to attend: ";}

}
