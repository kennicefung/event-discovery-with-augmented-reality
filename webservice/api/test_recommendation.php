<?php
    header('Access-Control-Allow-Origin: *');  
	require_once __DIR__.'/../config.php';
    require_once __DIR__.'/../helper/database.php';
    //require_once __DIR__.'/../helper/similar.php';
    require_once __DIR__.'/../controller/controller_common.php';
    require_once __DIR__.'/../controller/controller_event.php';

	//$result = getRecommendation("4");
	$result = getRecommendation("8");
    //print_r($result);
    //echo '111';



?>