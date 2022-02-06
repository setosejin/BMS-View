<?php 

$con = mysqli_connect("localhost:3306", "root", "root", "battery_info");


$sql = "INSERT INTO push_history(car_id, user_id) VALUES('TEST1','TEST2')";
$result = mysqli_query($con, $sql);
 
if($result) { echo "insert success!"; }
else { echo "failure"; }

mysqli_close($con);
?>