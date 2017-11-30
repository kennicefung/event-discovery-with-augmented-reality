<?php
	function loginUser($email, $password) {
		$db = new Database();
		$db -> connect();
		
       $result = $db->query("SELECT * FROM user WHERE email = '$email' or user_id = '$email'");
       
        // check for result 
        if ($db->get_num_rows() > 0) {
            if ($result = $db->fetch_array()){
            // check for password equality
				if ($result['password'] == $password) {

					// user authentication details are correct
					return $result;
				}
			}
        } else {
            // user not found
            return false;
        }
	}

	function getUserByID($id) {
		$db = new Database();
		$db -> connect();
		
       $result = $db->query("SELECT user_id, username, avatar, email, phone, dob, gender FROM user WHERE user_id = '$id'");
       
        // check for result 
        if ($db->get_num_rows() > 0) {
            if ($result = $db->fetch_array()){
				return $result;
			}
        } else {
            // user not found
            return false;
        }
	}

	function getFriendList($id){
		$db = new Database();
		$db -> connect();

		$result = $db->query("SELECT u.* FROM user u, user_friends f
				 WHERE u.user_id = f.user_id AND f.friend_id = '$id' 
				 AND f.user_id IN (SELECT friend_id FROM user_friends f2 WHERE f2.user_id = '$id')");

		if ($db->get_num_rows() > 0) {
        	return $db; 
        } else {
            return false;
        }
	}

	function getFriendRequests($id){
		$db = new Database();
		$db -> connect();

		$result = $db->query("SELECT u.* FROM user u, user_friends f
				 WHERE u.user_id = f.user_id AND f.friend_id = '$id' 
				 AND f.user_id NOT IN (SELECT friend_id FROM user_friends f2 WHERE f2.user_id = '$id')");

		if ($db->get_num_rows() > 0) {
        	return $db; 
        } else {
            return false;
        }
	}

	function sendFriendRequest($user_id, $friend_id){
		$db = new Database();
		$db -> connect();

		return $db->insert("`user_friends`", "`user_id`,`friend_id`", "'".$user_id."','".$friend_id."'");
	}

	function isFriend($user_id, $friend_id){
		$db = new Database();
		$db -> connect();

		$result = $db->query("SELECT *  FROM user_friends WHERE (user_id = '$user_id' AND friend_id = '$friend_id') OR (friend_id = '$user_id' AND user_id = '$friend_id') ");

		if ($db->get_num_rows() >= 2) {
        	return true; 
        } else {
            return false;
        }
	}

	function unfriend($user_id, $friend_id){
        $db = new Database();
        $db -> connect();
        if($db->delete("`user_friends`", "`user_id` = '$user_id' AND `friend_id` = '$friend_id'")){
        	return $db->delete("`user_friends`", "`friend_id` = '$user_id' AND `user_id` = '$friend_id'");
        }
        return false;
	}

	function hasRequestedFriend($user_id, $friend_id){
		$db = new Database();
		$db -> connect();

		$result = $db->query("SELECT *  FROM user_friends WHERE user_id = '$user_id' AND friend_id = '$friend_id' AND user_id NOT IN (SELECT friend_id  FROM user_friends WHERE user_id = '$friend_id' AND friend_id = '$user_id')");

		if ($db->get_num_rows() > 0) {
        	return true; 
        } else {
            return false;
        }
	}

	function getAllFriendsNews($id){
		$db = new Database();
		$db -> connect();

		$result = $db->query("SELECT u.user_id, u.username, e.event_id, e.title as event_title, u.avatar, ual.action_id as action, ual.action_dt  FROM user u, event e, user_action_log ual WHERE u.user_id = ual.user_id AND e.event_id = ual.event_id AND u.user_id IN (SELECT f2.friend_id FROM user_friends f2 WHERE f2.user_id = '$id' AND f2.friend_id = u.user_id) AND u.user_id IN (SELECT f3.user_id FROM user_friends f3 WHERE f3.user_id = u.user_id AND f3.friend_id = '$id') ORDER BY action_dt desc");

		/*$result = $db->query("SELECT u.user_id, u.username, e.event_id, e.title as event_title, u.avatar, ual.action_id as action, ual.action_dt  FROM user u, event e, user_action_log ual WHERE u.user_id = ual.user_id AND e.event_id = ual.event_id  ORDER BY action_dt desc");*/

		if ($db->get_num_rows() > 0) {
        	return $db; 
        } else {
            return false;
        }
	}

?>