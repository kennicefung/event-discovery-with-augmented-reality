<?php
	class Database {
		var $_dbConn = 0;
		var $_queryResource = 0;
		
		function connect() {
			$success = false;
			global $_CONFIG;
			$dbConn = new mysqli($_CONFIG['db']['host'], $_CONFIG['db']['username'], $_CONFIG['db']['password'],$_CONFIG['db']['dbname']);
			if (mysqli_connect_error()) {
			   die('Connect Error ('.mysqli_connect_errno().') '.mysqli_connect_error());
			}
			else {
				$this->_dbConn = $dbConn;
				$success = true;
			}
			
			return $success;
		}
		
		function query($sql) {
			$result = $this->_dbConn->query($sql);
			$this->_queryResource = $result;
			return $result;     
		}
		
		function insert($table, $col, $val) {
			return $this->query("INSERT INTO " . $table . " ( " . $col ." ) VALUES (".$val.")");    
		}

		function update($table, $val, $where) {
			return $this->query("UPDATE " . $table . " SET " . $val ." WHERE ".$where);        
		}
		
		function delete($table, $where) {
			return $this->query("DELETE FROM" . $table . " WHERE ".$where);        
		}
		
		/*function batch($file){		
			$commands = file_get_contents($file);

			$commands = explode(";", $commands);

			$total = $success = 0;
			foreach($commands as $command){
				if(trim($command)){
					$success += (@mysql_query($command.";")==false ? 0 : 1);
					$total += 1;
				}
			}
			echo "Success : ".$success.'<br/>';
			echo "Total : ".$total;
		}*/
		
		function get_new_id(){
			return mysqli_insert_id($this->_dbConn);
		}
		function fetch_array() {
			return mysqli_fetch_array($this->_queryResource, MYSQLI_ASSOC);
		}
		
		function get_num_rows() {
			return mysqli_num_rows($this->_queryResource);
		}
		
		function get_error(){ return $this->_dbConn->error;}
	}
?>