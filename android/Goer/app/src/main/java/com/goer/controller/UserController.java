package com.goer.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.goer.model.User;

import android.content.Context;

import com.goer.helper.*;
import com.goer.model.UserAction;

public class UserController {
	
	private static JSONHelper jsonParser = new JSONHelper();;
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_ID = "id";
	private static String KEY_NAME = "username";
	private static String KEY_AVATAR = "avatar";
	private static String KEY_EMAIL = "email";
	private static String KEY_PHONE = "phone";
	private static String KEY_DOB = "dob";
	private static String KEY_GENDER = "gender";

	private static String KEY_USERID = "user_id";
	private static String KEY_EVENTID = "event_id";
	private static String KEY_EVENTTITLE = "event_title";
	private static String KEY_ACTION = "action";
	private static String KEY_ACTIONDT = "action_dt";


	private static String login_api = "login.php";
	private static String user_api = "user.php";
	
	private static String login_tag = "login";

	private static String login_error_msg = "";
	
	// constructor
	public UserController(){
	}
	
	/**
	 * function make Login Request
	 * @param email
	 * @param password
	 * @throws Exception 
	 * @throws ExecutionException 
	 * */
	public static User loginUser(String email, String password, Context context) throws Exception{
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + login_api, params);
		if (json.getString(KEY_SUCCESS) != null) {
			String res = json.getString(KEY_SUCCESS);
			if(Integer.parseInt(res) == 1){
				JSONObject json_user = json.getJSONObject("user");
				login_error_msg = "";
				User u = new User(json_user.getString(KEY_ID), json_user.getString(KEY_NAME), json_user.getString(KEY_AVATAR), json_user.getString(KEY_EMAIL), json_user.getString(KEY_PHONE), json_user.getString(KEY_DOB), json_user.getString(KEY_GENDER));
				loginUser(u, context);
				return u;
			}
			else{
				login_error_msg = json.getString(KEY_ERROR_MSG);
			}
		}
		else{
			login_error_msg = json.getString(KEY_ERROR_MSG);
		}
		return null;
	}

	public static void loginUser(User u, Context context){
		DatabaseHelper db = new DatabaseHelper(context);
		logoutUser(context);
		db.addUser(u.getUserID(), u.getUsername(), u.getAvatar(), u.getEmail(), u.getPhone(), u.getDOB(), u.getGender());
	}
	
	/**
	 * Function get Login status
	 * */
	public static boolean isUserLoggedIn(Context context){
		DatabaseHelper db = new DatabaseHelper(context);
		int count = db.getRowCount();
		if(count > 0){
			// user logged in
			return true;
		}
		return false;
	}

	public static User getUserByUserID(String user_id) throws Exception{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "get_user_by_id"));
		params.add(new BasicNameValuePair("user_id", user_id));
		JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + user_api, params);
		if (json.getString(KEY_SUCCESS) != null) {
			String res = json.getString(KEY_SUCCESS);
			if (Integer.parseInt(res) == 1) {
				JSONObject json_user = json.getJSONObject("user");
				return new User(json_user.getString(KEY_ID), json_user.getString(KEY_NAME), json_user.getString(KEY_AVATAR), json_user.getString(KEY_EMAIL), json_user.getString(KEY_PHONE), json_user.getString(KEY_DOB), json_user.getString(KEY_GENDER));
			}
		}
		return null;
	}


	public static User getUserModel(Context context){
		DatabaseHelper db = new DatabaseHelper(context);
		if(isUserLoggedIn(context)){
			return db.getUserDetails();
		}
		return null;
	}

	public static ArrayList<UserAction> getFriendsNews(Context context) throws Exception{
		DatabaseHelper db = new DatabaseHelper(context);
		if(isUserLoggedIn(context)){
			String uid = db.getUserDetails().getUserID();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tag", "get_friend_news"));
			params.add(new BasicNameValuePair("user_id", uid));
			JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + user_api, params);
			if (json.getString(KEY_SUCCESS) != null) {
				String res = json.getString(KEY_SUCCESS);
				if(Integer.parseInt(res) == 1){
					ArrayList<UserAction> news = new ArrayList<>();
					JSONArray data = json.getJSONArray("friend_action_log");
					//for(int i = 0; i< data.names().length(); i++){
					for(int i = 0; i< data.length(); i++){
						//JSONObject tmp = data.getJSONObject(data.names().get(i).toString());
						JSONObject tmp = data.getJSONObject(i);
						news.add(new UserAction(tmp.getString(KEY_USERID), tmp.getString(KEY_NAME), tmp.getString(KEY_EVENTID), tmp.getString(KEY_EVENTTITLE), tmp.getString(KEY_AVATAR), tmp.getString(KEY_ACTION), tmp.getString(KEY_ACTIONDT)));
					}
					return news;
				}
			}
		}
		return null;
	}

	public static ArrayList<User> getFriendList(String user_id) throws Exception{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "get_friend_list"));
		params.add(new BasicNameValuePair("user_id", user_id));
		JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + user_api, params);
		if (json.getString(KEY_SUCCESS) != null) {
			String res = json.getString(KEY_SUCCESS);
			if (Integer.parseInt(res) == 1) {
				ArrayList<User> requests = new ArrayList<>();
				JSONArray data = json.getJSONArray("friends");
				//for(int i = 0; i< data.names().length(); i++){
				for (int i = 0; i < data.length(); i++) {
					//JSONObject tmp = data.getJSONObject(data.names().get(i).toString());
					JSONObject tmp = data.getJSONObject(i);
					requests.add(new User(tmp.getString(KEY_USERID), tmp.getString(KEY_NAME), tmp.getString(KEY_AVATAR), tmp.getString(KEY_EMAIL), tmp.getString(KEY_PHONE), tmp.getString(KEY_DOB), tmp.getString(KEY_GENDER)));
				}
				return requests;
			}
		}
		return null;
	}

	public static ArrayList<User> getFriendsRequest(Context context) throws Exception{
		DatabaseHelper db = new DatabaseHelper(context);
		if(isUserLoggedIn(context)){
			String uid = db.getUserDetails().getUserID();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tag", "get_friend_requests"));
			params.add(new BasicNameValuePair("user_id", uid));
			JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + user_api, params);
			if (json.getString(KEY_SUCCESS) != null) {
				String res = json.getString(KEY_SUCCESS);
				if(Integer.parseInt(res) == 1){
					ArrayList<User> requests = new ArrayList<>();
					JSONArray data = json.getJSONArray("friend_requests");
					//for(int i = 0; i< data.names().length(); i++){
					for(int i = 0; i< data.length(); i++){
						//JSONObject tmp = data.getJSONObject(data.names().get(i).toString());
						JSONObject tmp = data.getJSONObject(i);
						requests.add(new User(tmp.getString(KEY_USERID), tmp.getString(KEY_NAME), tmp.getString(KEY_AVATAR), tmp.getString(KEY_EMAIL), tmp.getString(KEY_PHONE), tmp.getString(KEY_DOB), tmp.getString(KEY_GENDER)));
					}
					return requests;
				}
			}
		}
		return null;
	}

	public static Boolean sendFriendRequest(String user_id, String friend_id) throws Exception{
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "send_friend_request"));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("friend_id", friend_id));
		JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + user_api, params);
		if (json.getString(KEY_SUCCESS) != null) {
			String res = json.getString(KEY_SUCCESS);
			if(Integer.parseInt(res) == 1){
				return true;
			}
		}
		//return json;
		return false;
	}

	public static Boolean isFriend(String user_id, String friend_id) throws Exception{
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "check_if_is_friend"));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("friend_id", friend_id));
		JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + user_api, params);
		if (json.getString(KEY_SUCCESS) != null) {
			String res = json.getString(KEY_SUCCESS);
			if(Integer.parseInt(res) == 1){
				return true;
			}
		}
		//return json;
		return false;
	}

	public static Boolean unfriend(String user_id, String friend_id) throws Exception{
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "un_friend"));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("friend_id", friend_id));
		JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + user_api, params);
		if (json.getString(KEY_SUCCESS) != null) {
			String res = json.getString(KEY_SUCCESS);
			if(Integer.parseInt(res) == 1){
				return true;
			}
		}
		//return json;
		return false;
	}

	public static Boolean hasRequestedFriend(String user_id, String friend_id) throws Exception{
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "has_requested_friend"));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("friend_id", friend_id));
		JSONObject json = jsonParser.getJSONFromUrlAsyncTask(GlobalHelper.APIUrl() + user_api, params);
		if (json.getString(KEY_SUCCESS) != null) {
			String res = json.getString(KEY_SUCCESS);
			if(Integer.parseInt(res) == 1){
				return true;
			}
		}
		//return json;
		return false;
	}


	public static String getLoginErrMsg(){
		return login_error_msg;
	}
	
	/**
	 * Function to logout user
	 * Reset Database
	 * */
	public static boolean logoutUser(Context context){
		DatabaseHelper db = new DatabaseHelper(context);
		db.resetTables();
		return true;
	}
	
}
