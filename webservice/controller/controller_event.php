<?php

	function createEvent($user_id, $title, $location, $locat_lat, $locat_long, $des, $start_time, $end_time, $is_public, $max_num_of_attendee){
    	$db = new Database();
        $db -> connect();
        if($db->insert("`event`", "`user_id`, `title`, `location`, `locat_lat`, `locat_long`, `des`, `start_time`, `end_time`, `is_public`, `max_num_of_attendee`,`active`", "'$user_id', '$title', '$location', '$locat_lat', '$locat_long', '$des', '$start_time', '$end_time', '$is_public', '$max_num_of_attendee', '1'")){
            return $db->get_new_id();
        }
        return false;
    }

    function updateEventImage($event_id, $img_path){
    	$db = new Database();
        $db -> connect();
        if($db->update("`event`", "`img` = '$img_path'", "`event_id`='".$event_id."'")){
            return true;
        }
        return false;
    }

	function cancelEvent($event_id){
    	$db = new Database();
        $db -> connect();
        if($db->update("`event`", "`active` = '0'", "`event_id`='".$event_id."'")){
            delAllActionLog($event_id);
            return true;
        }
        return false;
    }

	function getAllEvents($user_id) {
        $db = new Database();
		$db -> connect();
		
       $result = $db->query("SELECT e.*, u.username FROM event e, user u WHERE e.user_id = u.user_id and active='1' and start_time > NOW() and ((is_public = '1') or (is_public = '0' and ('$user_id' in (SELECT receiver_id from event_invitation i where i.event_id = e.event_id) or '$user_id' = e.user_id))) ORDER BY start_time");
       
        // check for result 
        if ($db->get_num_rows() > 0) {
        	return $db; 
        } else {
            return false;
        }
	}

    function getEventByID($event_id) {
        $db = new Database();
        $db -> connect();
        
       $result = $db->query("SELECT e.*, u.username FROM event e, user u WHERE e.user_id = u.user_id and event_id='$event_id' and active='1'");
       
        if ($db->get_num_rows() > 0) {
            if ($result = $db->fetch_array()){
                return $result;
            }
        } else {
            return false;
        }
    }

    function likeEvent($user_id, $event_id) {
        $db = new Database();
        $db -> connect();
        if($db->insert("`event_like_log`", "`event_id`,`user_id`", "'".$event_id."','".$user_id."'")){
            actionLogging($user_id, $event_id, 3);
            return true;
        }
        return false;
    }

    function unlikeEvent($user_id, $event_id) {
        $db = new Database();
        $db -> connect();
        if($db->delete("`event_like_log`", "`event_id` = '$event_id' AND `user_id` = '$user_id'")){
            delActionLog($user_id, $event_id, 3);
            return true;
        }
        return false;
    }

    function joinEvent($user_id, $event_id) {
        $db = new Database();
        $db -> connect();
        if($db->insert("`event_attendee`", "`event_id`,`user_id`", "'".$event_id."','".$user_id."'")){
            actionLogging($user_id, $event_id, 2);
            return true;
        }
        return false;
    }

    function disjoinEvent($user_id, $event_id) {
        $db = new Database();
        $db -> connect();
        if($db->delete("`event_attendee`", "`event_id` = '$event_id' AND `user_id` = '$user_id'")){
            delActionLog($user_id, $event_id, 2);
            return true;
        }
        return false;
    }

    function actionLogging($user_id, $event_id, $action_id) {
        $db = new Database();
        $db -> connect();
        return $db->insert("`user_action_log`", "`user_id`,`action_id`,`event_id`", "'".$user_id."','".$action_id."','".$event_id."'");
    }

	function delAllActionLog($event_id) {
        $db = new Database();
        $db -> connect();
        return $db->delete("`user_action_log`", "`event_id` = '$event_id'");
    }

    function delActionLog($user_id, $event_id, $action_id) {
        $db = new Database();
        $db -> connect();
        return $db->delete("`user_action_log`", "`user_id` = '$user_id' AND `action_id` = '$action_id' AND `event_id` = '$event_id'");
    }

    function hasLikedEvent($user_id, $event_id) {
        $db = new Database();
        $db -> connect();
        $result = $db->query("SELECT * FROM event_like_log WHERE user_id = '$user_id' and event_id='$event_id'");

        if ($db->get_num_rows() > 0) {
            return true; 
        } else {
            return false;
        }
    }

    function hasJoinedEvent($user_id, $event_id) {
        $db = new Database();
        $db -> connect();
        $result = $db->query("SELECT * FROM event_attendee WHERE user_id = '$user_id' and event_id='$event_id'");

        if ($db->get_num_rows() > 0) {
            return true; 
        } else {
            return false;
        }
    }

    function getUsersWhoLikedByEvents($event_id){
        $db = new Database();
        $db -> connect();
        
       $all_users_who_liked = $db->query("SELECT user_id FROM event_like_log WHERE event_id = '$event_id'");

        // check for result 
        if ($db->get_num_rows() > 0) {
            return $db; 
        } else {
            return false;
        }
    }

    function getLikedEventsByUser($user_id){
        $db = new Database();
        $db -> connect();
        
       $liked_events = $db->query("SELECT event_id FROM event_like_log WHERE user_id = '$user_id'");
       
        // check for result 
        if ($db->get_num_rows() > 0) {
            return $db; 
        } else {
            return false;
        }
    }

    function getRecommendation($user_id){
        $user_liked_events = array();
        $result = getLikedEventsByUser($user_id);
        if($result != false ){
            while ($row = $result->fetch_array()){
                $user_liked_events[] = $row["event_id"];
            }

            $all_events = array();
            $result = getAllEvents($user_id);
            if($result->get_num_rows() > 0){
	            while($row = $result->fetch_array()){
                    if(!in_array($row["event_id"], $user_liked_events)){
	                   $all_events[$row["event_id"]] = calculatePreferenceIndex($user_id, $row["event_id"],$user_liked_events);
                   }
	            }
        	}
            //print_r($result->get_num_rows());
            arsort($all_events);
            return $all_events;
        }
        return false;
    }

    function calculateSimilarityIndex($u1_likes_array, $u2_likes_array){
        $index = sizeof(array_intersect($u1_likes_array, $u2_likes_array)) / sizeof(array_unique(array_merge($u1_likes_array, $u2_likes_array)));
        //echo "Similarity Index:". $index."<br/>";
        return $index; 
    }

    function calculatePreferenceIndex($uid, $eid, $user_liked_events){
        $others = getUsersWhoLikedByEvents($eid);
        
        //echo "Event ID:". $eid."<br/>";
        $sum_s = 0;
        if($others != false){
            while($o = $others->fetch_array()){
                if($uid != $o["user_id"]){
                    //echo "User ID:". $o["user_id"]."<br/>";
                    $o_liked_events = array();
                    $result = getLikedEventsByUser($o["user_id"]);
                    if($result != false ){
                        while ($row = $result->fetch_array()){
                            $o_liked_events[] = $row["event_id"];
                        }
                    }
                    $s = calculateSimilarityIndex($user_liked_events, $o_liked_events);
                    $sum_s += $s;
                }
            }
            $index = $sum_s / $others->get_num_rows();
        }
        //echo "Sum Similarity:". $sum_s."<br/>";
        //echo "Total Likers:". $others->get_num_rows()."<br/>";
        //echo "Preference Index:". $index."<br/><br/>";
        return $index;
    }
    function writeComment($user_id,$event_id,$comment){
    	$db = new Database();
        $db -> connect();
        if($db->insert("`event_comment`", "`event_id`,`user_id`,`comment`", "'".$event_id."','".$user_id."','".$comment."'")){
            actionLogging($user_id, $event_id, 4);
            return true;
        }
        return false;
    }

    function getCommentsByEvent($event_id) {
        $db = new Database();
		$db -> connect();
		
       $result = $db->query("SELECT c.*, u.username, u.avatar FROM event_comment c, user u WHERE c.user_id = u.user_id and c.event_id='$event_id' ORDER BY cdate desc");
       
        // check for result 
        if ($db->get_num_rows() > 0) {
        	return $db; 
        } else {
            return false;
        }
	}

	function getInvitationsByUser($user_id) {
        $db = new Database();
		$db -> connect();
		
       $result = $db->query("SELECT i.*, u.username as sender, u.avatar as sender_avatar, e.title FROM event_invitation i, event e, user u WHERE i.sender_id = u.user_id and i.event_id = e.event_id and i.receiver_id='$user_id'");
       
        // check for result 
        if ($db->get_num_rows() > 0) {
        	return $db; 
        } else {
            return false;
        }
	}

	function sendInvitation($sender_id, $receiver_id, $event_id) {
        $db = new Database();
        $db -> connect();
        if($db->insert("`event_invitation`", "`event_id`,`sender_id`, `receiver_id`, `active`", "'$event_id','$sender_id','$receiver_id', '1'")){
            return true;
        }
        return false;
    }

?>