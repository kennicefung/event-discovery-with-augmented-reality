<?php
    header('Access-Control-Allow-Origin: *');  
	require_once __DIR__.'/../config.php';
    require_once __DIR__.'/../helper/database.php';
    require_once __DIR__.'/../controller/controller_common.php';
    require_once __DIR__.'/../controller/controller_event.php';
/**
 * File to handle all API requests
 * Accepts POST and POST
 * 
 * Each request will be identified by TAG
 * Response will be JSON data

  /**
 * check for POST request 
 */


if ((isset($_POST['tag']) && $_POST['tag'] != '')) {
    // POST tag
    if(isset($_POST['tag'])){
        $tag = $_POST['tag'];

        // response Array
        $response = array("tag" => $tag, "success" => 0);

        if ($tag == 'create_event') {
            $user_id = $_POST['user_id'];
            $title = $_POST['title'];
            $location = $_POST['location'];
            $locat_lat = $_POST['lat'];
            $locat_long = $_POST['long'];
            $des = $_POST['des'];
            $start_time = $_POST['start_time'];
            $end_time = $_POST['end_time'];
            $img = $_POST['img'];
            $is_public = $_POST['is_public'];
            $max_num_of_attendee = $_POST['max_num_of_attendee'];


            $result = createEvent($user_id, $title, $location, $locat_lat, $locat_long, $des, $start_time, $end_time, $is_public, $max_num_of_attendee);
            if($result != false){
                $response["event_id"] = $result;
                $img_path = "../upload/event_image/event-$result.png";
                $result = updateEventImage($result, "event-".$result.".png");
                if($result === true){
                	file_put_contents($img_path,base64_decode($img));
                    $response["success"] = 1;
                }
            }
            echo json_encode(utf8ize($response));
        }

        if ($tag == 'cancel_event') {
            $event_id = $_POST['event_id'];
            $result = cancelEvent($event_id);

            if($result === true){
                $response["success"] = 1;
            }
            echo json_encode(utf8ize($response));
        }

        if ($tag == 'get_all_events') {
            $user_id = $_POST['user_id'];
            $result = getAllEvents($user_id);
            if ($result != false) {
                $response["success"] = 1;
                while($row = $result->fetch_array()){
                    $response["event"][$row["event_id"]]["event_id"] = $row["event_id"];
                    $response["event"][$row["event_id"]]["user_id"] = $row["user_id"];
                    $response["event"][$row["event_id"]]["username"] = $row["username"];
                    $response["event"][$row["event_id"]]["title"] = $row["title"];
                    $response["event"][$row["event_id"]]["des"] = $row["des"];
                    $response["event"][$row["event_id"]]["img"] = $row["img"];
                    $response["event"][$row["event_id"]]["start_time"] = $row["start_time"];
                    $response["event"][$row["event_id"]]["end_time"] = $row["end_time"];
                    $response["event"][$row["event_id"]]["location"] = $row["location"];
                    $response["event"][$row["event_id"]]["lat"] = $row["locat_lat"];
                    $response["event"][$row["event_id"]]["long"] = $row["locat_long"];
                    $response["event"][$row["event_id"]]["max_num_of_attendee"] = $row["max_num_of_attendee"];
                }
                echo json_encode(utf8ize($response));
            } else {
                $response["success"] = 0;
                $response["error_msg"] = "No Event.";
                echo json_encode(utf8ize($response));
            }
            
        }

        if ($tag == 'get_event_by_id') {
            $id = $_POST['event_id'];

            $result = getEventByID($id);
            if ($result != false) {
                $response["success"] = 1;
                $response["event"]["event_id"] = $result["event_id"];
                $response["event"]["user_id"] = $result["user_id"];
                $response["event"]["username"] = $result["username"];
                $response["event"]["title"] = $result["title"];
                $response["event"]["des"] = $result["des"];
                $response["event"]["img"] = $result["img"];
                $response["event"]["start_time"] = $result["start_time"];
                $response["event"]["end_time"] = $result["end_time"];
                $response["event"]["location"] = $result["location"];
                $response["event"]["lat"] = $result["locat_lat"];
                $response["event"]["long"] = $result["locat_long"];
                $response["event"]["max_num_of_attendee"] = $result["max_num_of_attendee"];
                echo json_encode(utf8ize($response));
            } else {
                $response["success"] = 0;
                echo json_encode(utf8ize($response));
            }
            
        }

        if ($tag == 'like_event') {
        	$user_id = $_POST['user_id'];
        	$event_id = $_POST['event_id'];
        	$result = likeEvent($user_id, $event_id);

        	if($result === true){
        		$response["success"] = 1;
        	}
        	echo json_encode(utf8ize($response));
        }

        if ($tag == 'unlike_event') {
            $user_id = $_POST['user_id'];
            $event_id = $_POST['event_id'];
            $result = unlikeEvent($user_id, $event_id);

            if($result === true){
                $response["success"] = 1;
            }
            echo json_encode(utf8ize($response));
        }

        if ($tag == 'join_event') {
            $user_id = $_POST['user_id'];
            $event_id = $_POST['event_id'];
            $result = joinEvent($user_id, $event_id);

            if($result === true){
                $response["success"] = 1;
            }
            echo json_encode(utf8ize($response));
        }

        if ($tag == 'disjoin_event') {
            $user_id = $_POST['user_id'];
            $event_id = $_POST['event_id'];
            $result = disjoinEvent($user_id, $event_id);

            if($result === true){
                $response["success"] = 1;
            }
            echo json_encode(utf8ize($response));
        }

        if ($tag == 'has_liked_event') {
            $user_id = $_POST['user_id'];
            $event_id = $_POST['event_id'];
            $result = haslikedEvent($user_id, $event_id);

            if($result === true){
                $response["success"] = 1;
            }
            echo json_encode(utf8ize($response));
        }

        if ($tag == 'has_joined_event') {
            $user_id = $_POST['user_id'];
            $event_id = $_POST['event_id'];
            $result = hasJoinedEvent($user_id, $event_id);

            if($result === true){
                $response["success"] = 1;
            }
            echo json_encode(utf8ize($response));
        }

        if ($tag == 'recommended_events') {
            $user_id = $_POST['user_id'];
            $result = getRecommendation($user_id);

            if($result != false){
                $response["success"] = 1;
                $i=0;
                foreach ($result as $key => $value) {
                    if(i>=3) break;
                    if($value > 0){
                        $event_object = getEventByID($key);
                        if($event_object != false){
                            $response["event"][$event_object["event_id"]]["event_id"] = $event_object["event_id"];
                            $response["event"][$event_object["event_id"]]["user_id"] = $event_object["user_id"];
                            $response["event"][$event_object["event_id"]]["username"] = $event_object["username"];
                            $response["event"][$event_object["event_id"]]["title"] = $event_object["title"];
                            $response["event"][$event_object["event_id"]]["des"] = $event_object["des"];
                            $response["event"][$event_object["event_id"]]["img"] = $event_object["img"];
                            $response["event"][$event_object["event_id"]]["start_time"] = $event_object["start_time"];
                            $response["event"][$event_object["event_id"]]["end_time"] = $event_object["end_time"];
                            $response["event"][$event_object["event_id"]]["location"] = $event_object["location"];
                            $response["event"][$event_object["event_id"]]["lat"] = $event_object["locat_lat"];
                            $response["event"][$event_object["event_id"]]["long"] = $event_object["locat_long"];
                            $response["event"][$event_object["event_id"]]["max_num_of_attendee"] = $event_object["max_num_of_attendee"];
                        }
                    }
                    $i++;
                }
            }
            echo json_encode(utf8ize($response));
        }

        if ($tag == 'get_comments_by_event') {
            $event_id = $_POST['event_id'];
            $result = getCommentsByEvent($event_id);
            if ($result != false) {
                $response["success"] = 1;
                $i=0;
                while($row = $result->fetch_array()){
                    $response["comments"][$i]["event_id"] = $row["event_id"];
                    $response["comments"][$i]["user_id"] = $row["user_id"];
                    $response["comments"][$i]["username"] = $row["username"];
                    $response["comments"][$i]["avatar"] = $row["avatar"];
                    $response["comments"][$i]["event_id"] = $row["event_id"];
                    $response["comments"][$i]["comment"] = $row["comment"];
                    $response["comments"][$i]["cdate"] = $row["cdate"];
                    $i++;
                }
                echo json_encode(utf8ize($response));
            } else {
                $response["success"] = 0;
                $response["error_msg"] = "No Comments.";
                echo json_encode(utf8ize($response));
            }
            
        }

        if ($tag == 'write_comment') {
            $user_id = $_POST['user_id'];
            $event_id = $_POST['event_id'];
            $comment = $_POST['comment'];
            $result = writeComment($user_id, $event_id, $comment);

            if($result === true){
                $response["success"] = 1;
            }
            echo json_encode(utf8ize($response));
        }

        if ($tag == 'get_invitations') {
            $user_id = $_POST['user_id'];
            $result = getInvitationsByUser($user_id);
            if ($result != false) {
                $response["success"] = 1;
                $i=0;
                while($row = $result->fetch_array()){
                    $response["invitations"][$i]["inv_id"] = $row["inv_id"];
                    $response["invitations"][$i]["event_id"] = $row["event_id"];
                    $response["invitations"][$i]["title"] = $row["title"];
                    $response["invitations"][$i]["sender_id"] = $row["sender_id"];
                    $response["invitations"][$i]["sender"] = $row["sender"];
                    $response["invitations"][$i]["sender_avatar"] = $row["sender_avatar"];
                    $response["invitations"][$i]["receiver_id"] = $row["receiver_id"];
                    $i++;
                }
                echo json_encode(utf8ize($response));
            } else {
                $response["success"] = 0;
                $response["error_msg"] = "No invitation.";
                echo json_encode(utf8ize($response));
            }
        }
        if ($tag == 'send_invitation') {
            $sender_id = $_POST['sender_id'];
            $receiver_id = $_POST['receiver_id'];
            $event_id = $_POST['event_id'];
            $result = sendInvitation($sender_id, $receiver_id, $event_id);

            if($result === true){
                $response["success"] = 1;
            }
            echo json_encode(utf8ize($response));
        }
    }
} else if ((isset($_GET['tag']) && $_GET['tag'] == 'get_events_for_AR')){
    $user_id = $_GET['user_id'];
    $result = getAllEvents($user_id);
    if ($result != false) {
        $response = array();
        while($row = $result->fetch_array()){
            $new_arr = array("id" => $row["event_id"], "longitude" => $row["locat_long"], "latitude" => $row["locat_lat"], "description" => $row["des"], "name" => $row["title"], "host" => $row["username"], "start_time" => $row["start_time"], "end_time" => $row["end_time"], "location" => $row["location"], "img" => $row["img"]);
            array_push($response, $new_arr);
        }
        echo json_encode(utf8ize($response));
    } else {
        $response["success"] = 0;
        $response["error_msg"] = "No Event.";
        echo json_encode(utf8ize($response));
    }
} else {
    echo "Access Denied";
}
?>
