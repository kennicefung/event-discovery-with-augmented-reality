<?php
	global $_CONFIG;	

	// Database Setting
	$_CONFIG['db']['host'] = "localhost";
	$_CONFIG['db']['username'] = "root";
	$_CONFIG['db']['password'] = "root";
	$_CONFIG['db']['dbname'] = "goer";

	// System Global Setting
	define('APP_PATH', str_replace('\\', '/', dirname(__FILE__)));
	define('APP_NAME', 'Goer - AR Event Explorer');

	// Set Default Lang
	$_CONFIG['lang'] = "en";
?>