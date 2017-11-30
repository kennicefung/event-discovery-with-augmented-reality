<?php
    header('Access-Control-Allow-Origin: *');  
	require_once __DIR__.'/../config.php';
    require_once __DIR__.'/../helper/database.php';
    require_once __DIR__.'/../controller/controller_common.php';
    require_once __DIR__.'/../controller/controller_user.php';
/**
 * File to handle all API requests
 * Accepts POST and POST
 * 
 * Each request will be identified by TAG
 * Response will be JSON data

  /**
 * check for POST request 
 */


if (isset($_POST['tag']) && $_POST['tag'] != '') {
    // POST tag
    $tag = $_POST['tag'];

    // response Array
    $response = array("tag" => $tag, "success" => 0);

    // check for tag type
    if ($tag == 'get_user_by_id') {
        // Request type is check Login
        $id = $_POST['user_id'];

        // check for user
        $user = getUserByID($id);
        if ($user != false) {
            // user found
            // echo json with success = 1
            $response["success"] = 1;
            $response["id"] = $user["user_id"];
            $response["user"]["id"] = $user["user_id"];
            $response["user"]["username"] = $user["username"];
            $response["user"]["avatar"] = $user["avatar"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["phone"] = $user["phone"];
            $response["user"]["dob"] = $user["dob"];
            $response["user"]["gender"] = $user["gender"];
            echo json_encode(utf8ize($response));
        } else {
            // user not found
            // echo json with error = 1
            $response["success"] = 0;
            $response["error_msg"] = "Incorrect ID or password!";
            echo json_encode(utf8ize($response));
        }
        
    }

    if ($tag == 'get_friend_news') {

        $id = $_POST['user_id'];
        $result = getAllFriendsNews($id);
        if ($result != false) {
            $response["success"] = 1;
            $i = 0;
            while($row = $result->fetch_array()){
                $response["friend_action_log"][$i]["user_id"] = $row["user_id"];
                $response["friend_action_log"][$i]["username"] = $row["username"];
                $response["friend_action_log"][$i]["event_id"] = $row["event_id"];
                $response["friend_action_log"][$i]["event_id"] = $row["event_id"];
                $response["friend_action_log"][$i]["event_title"] = $row["event_title"];
                $response["friend_action_log"][$i]["avatar"] = $row["avatar"];
                $response["friend_action_log"][$i]["action"] = $row["action"];
                $response["friend_action_log"][$i]["action_dt"] = $row["action_dt"];
                $i = $i + 1;
            }
            echo json_encode(utf8ize($response));
        } else {
            $response["success"] = 0;
            $response["error_msg"] = "No Log.";
            echo json_encode(utf8ize($response));
        }
            
    }

    if ($tag == 'get_friend_list') {

        $id = $_POST['user_id'];
        $result = getFriendList($id);
        if ($result != false) {
            $response["success"] = 1;
            $i = 0;
            while($row = $result->fetch_array()){
                $response["friends"][$i]["user_id"] = $row["user_id"];
                $response["friends"][$i]["username"] = $row["username"];
                $response["friends"][$i]["avatar"] = $row["avatar"];
                $response["friends"][$i]["email"] = $row["email"];
                $response["friends"][$i]["phone"] = $row["phone"];
                $response["friends"][$i]["dob"] = $row["dob"];
                $response["friends"][$i]["gender"] = $row["gender"];
                $i = $i + 1;
            }
            echo json_encode(utf8ize($response));
        } else {
            $response["success"] = 0;
            $response["error_msg"] = "No Request.";
            echo json_encode(utf8ize($response));
        }
        
    }

    if ($tag == 'get_friend_requests') {

        $id = $_POST['user_id'];
        $result = getFriendRequests($id);
        if ($result != false) {
            $response["success"] = 1;
            $i = 0;
            while($row = $result->fetch_array()){
                $response["friend_requests"][$i]["user_id"] = $row["user_id"];
                $response["friend_requests"][$i]["username"] = $row["username"];
                $response["friend_requests"][$i]["avatar"] = $row["avatar"];
                $response["friend_requests"][$i]["email"] = $row["email"];
                $response["friend_requests"][$i]["phone"] = $row["phone"];
                $response["friend_requests"][$i]["dob"] = $row["dob"];
                $response["friend_requests"][$i]["gender"] = $row["gender"];
                $i = $i + 1;
            }
            echo json_encode(utf8ize($response));
        } else {
            $response["success"] = 0;
            $response["error_msg"] = "No Request.";
            echo json_encode(utf8ize($response));
        }
    }

    if ($tag == 'send_friend_request') {
        $user_id = $_POST['user_id'];
        $friend_id = $_POST['friend_id'];
        $result = sendFriendRequest($user_id, $friend_id);

        if($result === true){
            $response["success"] = 1;
        }
        echo json_encode(utf8ize($response));
    }

    if ($tag == 'check_if_is_friend') {
        $user_id = $_POST['user_id'];
        $friend_id = $_POST['friend_id'];
        $result = isFriend($user_id, $friend_id);

        if($result === true){
            $response["success"] = 1;
        }
        echo json_encode(utf8ize($response));
    }

    if ($tag == 'un_friend') {
        $user_id = $_POST['user_id'];
        $friend_id = $_POST['friend_id'];
        $result = unfriend($user_id, $friend_id);

        if($result === true){
            $response["success"] = 1;
        }
        echo json_encode(utf8ize($response));
    }

    if ($tag == 'has_requested_friend') {
        $user_id = $_POST['user_id'];
        $friend_id = $_POST['friend_id'];
        $result = hasRequestedFriend($user_id, $friend_id);

        if($result === true){
            $response["success"] = 1;
        }
        echo json_encode(utf8ize($response));
    }
} else {
    echo "Access Denied";
}
?>
