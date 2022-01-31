<?php 
$con = mysqli_connect("localhost:3306", "root", "root", "battery_info");
// if($mysqli->connect_errno) {
// 	echo '[MySQL 연결 오류]';
// } else {
// 	echo '[MySQL 연결 성공]';
// }

echo $_GET['car_id'].$_GET['user_id'];

$_CAR_ID = $_GET['car_id'];
$_USER_ID = $_GET['user_id'];

$sql = "INSERT INTO car(car_id, user_id) VALUES('$_CAR_ID','$_USER_ID')";
$result = mysqli_query($con, $sql);
 
if($result) { echo "insert success!"; }
else { echo "failure"; }

mysqli_close($con);
?>