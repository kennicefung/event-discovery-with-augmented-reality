package com.goer.controller;

import com.goer.helper.GlobalHelper;
import com.goer.helper.JSONHelper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;

import com.goer.model.Comment;
import com.goer.model.Event;
import com.goer.model.Invitation;

/**
 * Created by kennicefung on 1/5/2017.
 */

public class EventController {
    private static JSONHelper jsonParser = new JSONHelper();
    private static String event_api = "event.php";

    private static String KEY_SUCCESS = "success";
    private static String KEY_ID = "event_id";
    private static String KEY_USERID = "user_id";
    private static String KEY_USERNAME = "username";
    private static String KEY_AVATAR = "avatar";
    private static String KEY_TITLE = "title";
    private static String KEY_DES = "des";
    private static String KEY_IMG = "img";
    private static String KEY_START = "start_time";
    private static String KEY_END = "end_time";
    private static String KEY_LOCATION = "location";
    private static String KEY_LAT = "lat";
    private static String KEY_LNG = "long";
    private static String KEY_PUPBIC = "is_public";
    private static String KEY_MAXNUM = "max_num_of_attendee";

    private static String KEY_COMMENT = "comment";
    private static String KEY_CDATE = "cdate";

    public EventController(){}

    public static ArrayList<Event> getRecommendation(String uid) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "recommended_events"));
        params.add(new BasicNameValuePair(KEY_USERID, uid));
        try {
            JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + event_api, params);
            if (json.getString(KEY_SUCCESS) != null) {
                String res = json.getString(KEY_SUCCESS);
                if(Integer.parseInt(res) == 1){
                    ArrayList<Event> events = new ArrayList<>();
                    JSONObject data = json.getJSONObject("event");
                    for(int i = 0; i< data.names().length(); i++){
                        JSONObject tmp = data.getJSONObject(data.names().get(i).toString());
                        events.add(new Event(tmp.getString(KEY_ID), tmp.getString(KEY_USERID), tmp.getString(KEY_USERNAME), tmp.getString(KEY_TITLE),tmp.getString(KEY_DES), GlobalHelper.EventImgPath()+tmp.getString(KEY_IMG)
                                ,tmp.getString(KEY_START), tmp.getString(KEY_END), tmp.getString(KEY_LOCATION), tmp.getDouble(KEY_LAT), tmp.getDouble(KEY_LNG), tmp.getString(KEY_MAXNUM)));
                    }
                    return events;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<Event> getAllEvents(String uid) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "get_all_events"));
        params.add(new BasicNameValuePair(KEY_USERID, uid));
        try {
            JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + event_api, params);
            if (json.getString(KEY_SUCCESS) != null) {
                String res = json.getString(KEY_SUCCESS);
                if(Integer.parseInt(res) == 1){
                    ArrayList<Event> events = new ArrayList<>();
                    JSONObject data = json.getJSONObject("event");
                    for(int i = 0; i< data.names().length(); i++){
                        JSONObject tmp = data.getJSONObject(data.names().get(i).toString());
                        events.add(new Event(tmp.getString(KEY_ID), tmp.getString(KEY_USERID), tmp.getString(KEY_USERNAME), tmp.getString(KEY_TITLE),tmp.getString(KEY_DES), GlobalHelper.EventImgPath()+tmp.getString(KEY_IMG)
                            ,tmp.getString(KEY_START), tmp.getString(KEY_END), tmp.getString(KEY_LOCATION), tmp.getDouble(KEY_LAT), tmp.getDouble(KEY_LNG), tmp.getString(KEY_MAXNUM)));
                    }
                    return events;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Event getEventByEventID(String event_id) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "get_event_by_id"));
        params.add(new BasicNameValuePair(KEY_ID, event_id));
        JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + event_api, params);
        if (json.getString(KEY_SUCCESS) != null) {
            String res = json.getString(KEY_SUCCESS);
            if (Integer.parseInt(res) == 1) {
                JSONObject tmp = json.getJSONObject("event");
                return new Event(tmp.getString(KEY_ID), tmp.getString(KEY_USERID), tmp.getString(KEY_USERNAME), tmp.getString(KEY_TITLE),tmp.getString(KEY_DES), GlobalHelper.EventImgPath()+tmp.getString(KEY_IMG)
                        ,tmp.getString(KEY_START), tmp.getString(KEY_END), tmp.getString(KEY_LOCATION), tmp.getDouble(KEY_LAT), tmp.getDouble(KEY_LNG), tmp.getString(KEY_MAXNUM));
            }
        }
        return null;
    }

    public static Boolean likeEvent(String user_id, String event_id) throws Exception{
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "like_event"));
        params.add(new BasicNameValuePair(KEY_USERID, user_id));
        params.add(new BasicNameValuePair(KEY_ID, event_id));
        JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + event_api, params);
        if (json.getString(KEY_SUCCESS) != null) {
            String res = json.getString(KEY_SUCCESS);
            if(Integer.parseInt(res) == 1){
                return true;
            }
        }
        return false;
    }

    public static Boolean unlikeEvent(String user_id, String event_id) throws Exception{
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "unlike_event"));
        params.add(new BasicNameValuePair(KEY_USERID, user_id));
        params.add(new BasicNameValuePair(KEY_ID, event_id));
        JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + event_api, params);
        if (json.getString(KEY_SUCCESS) != null) {
            String res = json.getString(KEY_SUCCESS);
            if(Integer.parseInt(res) == 1){
                return true;
            }
        }
        return false;
    }

    public static Boolean hasLikedEvent(String user_id, String event_id) throws Exception{
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "has_liked_event"));
        params.add(new BasicNameValuePair(KEY_USERID, user_id));
        params.add(new BasicNameValuePair(KEY_ID, event_id));
        JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + event_api, params);
        if (json.getString(KEY_SUCCESS) != null) {
            String res = json.getString(KEY_SUCCESS);
            if(Integer.parseInt(res) == 1){
                return true;
            }
        }
        //return json;
        return false;
    }

    public static Boolean joinEvent(String user_id, String event_id) throws Exception{
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "join_event"));
        params.add(new BasicNameValuePair(KEY_USERID, user_id));
        params.add(new BasicNameValuePair(KEY_ID, event_id));
        JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + event_api, params);
        if (json.getString(KEY_SUCCESS) != null) {
            String res = json.getString(KEY_SUCCESS);
            if(Integer.parseInt(res) == 1){
                return true;
            }
        }
        return false;
    }

    public static Boolean disjoinEvent(String user_id, String event_id) throws Exception{
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "disjoin_event"));
        params.add(new BasicNameValuePair(KEY_USERID, user_id));
        params.add(new BasicNameValuePair(KEY_ID, event_id));
        JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + event_api, params);
        if (json.getString(KEY_SUCCESS) != null) {
            String res = json.getString(KEY_SUCCESS);
            if(Integer.parseInt(res) == 1){
                return true;
            }
        }
        return false;
    }

    public static Boolean hasJoinedEvent(String user_id, String event_id) throws Exception{
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "has_joined_event"));
        params.add(new BasicNameValuePair(KEY_USERID, user_id));
        params.add(new BasicNameValuePair(KEY_ID, event_id));
        JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + event_api, params);
        if (json.getString(KEY_SUCCESS) != null) {
            String res = json.getString(KEY_SUCCESS);
            if(Integer.parseInt(res) == 1){
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Comment> getCommentsByEvent(String event_id) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "get_comments_by_event"));
        params.add(new BasicNameValuePair(KEY_ID, event_id));
        try {
            JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + event_api, params);
            if (json.getString(KEY_SUCCESS) != null) {
                String res = json.getString(KEY_SUCCESS);
                if(Integer.parseInt(res) == 1){
                    ArrayList<Comment> comments = new ArrayList<>();
                    JSONArray data = json.getJSONArray("comments");
                    //for(int i = 0; i< data.names().length(); i++){
                    for(int i = 0; i< data.length(); i++){
                        //JSONObject tmp = data.getJSONObject(data.names().get(i).toString());
                        JSONObject tmp = data.getJSONObject(i);
                        comments.add(new Comment(tmp.getString(KEY_USERID), tmp.getString(KEY_USERNAME), tmp.getString(KEY_AVATAR), tmp.getString(KEY_ID),tmp.getString(KEY_COMMENT)
                                ,tmp.getString(KEY_CDATE)));
                    }
                    return comments;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean writeComment(String user_id, String event_id, String comment) throws Exception{
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "write_comment"));
        params.add(new BasicNameValuePair(KEY_USERID, user_id));
        params.add(new BasicNameValuePair(KEY_ID, event_id));
        params.add(new BasicNameValuePair(KEY_COMMENT, comment));
        JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + event_api, params);
        if (json.getString(KEY_SUCCESS) != null) {
            String res = json.getString(KEY_SUCCESS);
            if(Integer.parseInt(res) == 1){
                return true;
            }
        }
        return false;
    }

    public static Boolean cancelEvent(String event_id) throws Exception{
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "cancel_event"));
        params.add(new BasicNameValuePair(KEY_ID, event_id));
        JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + event_api, params);
        if (json.getString(KEY_SUCCESS) != null) {
            String res = json.getString(KEY_SUCCESS);
            if(Integer.parseInt(res) == 1){
                return true;
            }
        }
        return false;
    }

    public static String createEvent(String user_id, String event_title, String location, String locat_lat, String locat_long, String des, String start_time, String end_time, String img, String isPublic, String maxNum) throws Exception{
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "create_event"));
        params.add(new BasicNameValuePair(KEY_USERID, user_id));
        params.add(new BasicNameValuePair(KEY_TITLE, event_title));
        params.add(new BasicNameValuePair(KEY_LOCATION, location));
        params.add(new BasicNameValuePair(KEY_LAT, locat_lat));
        params.add(new BasicNameValuePair(KEY_LNG, locat_long));
        params.add(new BasicNameValuePair(KEY_DES, des));
        params.add(new BasicNameValuePair(KEY_START, start_time));
        params.add(new BasicNameValuePair(KEY_END, end_time));
        params.add(new BasicNameValuePair(KEY_IMG, img));
        params.add(new BasicNameValuePair(KEY_PUPBIC, isPublic));
        params.add(new BasicNameValuePair(KEY_MAXNUM, maxNum));
        JSONObject json = jsonParser.getJSONFromUrl(GlobalHelper.APIUrl() + event_api, params);
        if (json.getString(KEY_SUCCESS) != null) {
            String res = json.getString(KEY_SUCCESS);
            if(Integer.parseInt(res) == 1){
                return json.getString(KEY_ID);
            }
        }
        return null;
    }

    public static ArrayList<Invitation> getInvitations(Context context) throws Exception{
        String uid = (UserController.getUserModel(context)).getUserID();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "get_invitations"));
        params.add(new BasicNameValuePair("user_id", uid));
        JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + event_api, params);
        if (json.getString(KEY_SUCCESS) != null) {
            String res = json.getString(KEY_SUCCESS);
            if(Integer.parseInt(res) == 1){
                ArrayList<Invitation> invitations = new ArrayList<>();
                JSONArray data = json.getJSONArray("invitations");
                //for(int i = 0; i< data.names().length(); i++){
                for(int i = 0; i< data.length(); i++){
                    //JSONObject tmp = data.getJSONObject(data.names().get(i).toString());
                    JSONObject tmp = data.getJSONObject(i);
                    invitations.add(new Invitation(tmp.getString("inv_id"), tmp.getString("sender_id"), tmp.getString("sender"), tmp.getString("sender_avatar"), tmp.getString(KEY_ID), tmp.getString(KEY_TITLE), tmp.getString("receiver_id")));
                }
                return invitations;
            }
        }
        return null;
    }

    public static Boolean sendInvitation(String sender_id, String event_id, String receiver_id) throws Exception{
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "send_invitation"));
        params.add(new BasicNameValuePair("sender_id", sender_id));
        params.add(new BasicNameValuePair("receiver_id", receiver_id));
        params.add(new BasicNameValuePair(KEY_ID, event_id));
        JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + event_api, params);
        if (json.getString(KEY_SUCCESS) != null) {
            String res = json.getString(KEY_SUCCESS);
            if(Integer.parseInt(res) == 1){
                return true;
            }
        }
        return false;
    }
}
