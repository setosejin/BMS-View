<?php 

// error_reporting(E_ALL); 
// ini_set('display_errors',1); 

$con = mysqli_connect("localhost:3306", "root", "root", "battery_info");
// if($mysqli->connect_errno) {
// 	echo '[MySQL 연결 오류]';
// } else {
// 	echo '[MySQL 연결 성공]';
// }



$sql = "INSERT INTO push_history(car_id, user_id) VALUES('TEST1','TEST2')";
$result = mysqli_query($con, $sql);
 
if($result) { echo "insert success!"; }
else { echo "failure"; }

mysqli_close($con);
?>