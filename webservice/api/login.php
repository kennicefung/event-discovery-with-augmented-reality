<?php
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
    if ($tag == 'login') {
        // Request type is check Login
        $email = $_POST['email'];
        $password = $_POST['password'];

        // check for user
        $user = loginUser($email, $password);
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
} else {
    echo "Access Denied";
}
?>
