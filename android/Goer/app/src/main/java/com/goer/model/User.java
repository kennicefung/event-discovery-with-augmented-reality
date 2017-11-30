package com.goer.model;

import com.goer.helper.GlobalHelper;

import java.io.Serializable;

/**
 * Created by kennicefung on 1/5/2017.
 */

public class User implements Serializable {
    private String user_id;
    private String username;
    private String avatar = "default_avatar.jpg";
    private String email;
    private String phone;
    private String dob;
    private String gender;

    public User(){}

    public User(String user_id, String username, String avatar, String email, String phone, String dob, String gender){
        this.user_id = user_id;
        this.username = username;
        this.avatar = (avatar.equals("") || avatar == null) ? this.avatar : avatar;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.gender = gender;
    }

    public String getUserID(){return user_id;}
    public String getUsername(){return username;}
    public String getAvatar(){return avatar;}
    public String getAvatarURL(){return GlobalHelper.UserAvatarPath()+ avatar;}
    public String getEmail(){return email;}
    public String getPhone(){return phone;}
    public String getDOB(){return dob;}
    public String getGender(){return gender;}

    public void setEmail(String email){this.email = email;}
    public void setPhone(String phone){this.phone = phone;}
    public void setDOB(String dob){this.dob = dob;}
    public void setGender(String gender){this.gender = gender;}
}
