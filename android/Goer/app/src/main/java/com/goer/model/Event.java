package com.goer.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event implements Serializable {
    public String event_id;
    public String user_id;
    public String user_name;
    public String title;
    public String des;
    public String img;
    public Date start_time;
    public Date end_time;
    public String location;
    public double lat;
    public double lng;
    public int max_num_of_attendee;

    public Event(String event_id, String user_id, String user_name, String title, String des, String img
            , String start_time, String end_time, String location, double lat, double lng, String max_num_of_attendee) {
        this.event_id = event_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.title = title;
        this.des = des;
        this.img = img;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(start_time);
            this.start_time = date;
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(end_time);
            this.end_time = date;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.event_id = event_id;
        this.location = location;
        this.lat = lat;
        this.lng = lng;
        this.max_num_of_attendee = Integer.parseInt(max_num_of_attendee);
    }

    public String getFormattedStartTime(String format){
        SimpleDateFormat fm = new SimpleDateFormat(format);
        return fm.format(start_time);
    }
    public String getFormattedEndTime(String format){
        SimpleDateFormat fm = new SimpleDateFormat(format);
        return fm.format(end_time);
    }
}