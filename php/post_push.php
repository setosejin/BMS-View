<?php 
$con = mysqli_connect("localhost:3306", "root", "root", "battery_info");

// $car_id = $_GET['car'];
$car_id = 'KK';
// $push_type = $_GET['type'];
$push_type = '배터리 충전 알림';
$send_time = date("Y-m-d H:i:s")
// $send_msg = $_GET['msg'];
$send_msg = '배터리 잔량이 곧 20%가 돼요. 서둘러 충전소에서 충전을 해주세요.';
$id = 9;

$sql = "INSERT INTO push_history (id, car_id, push_type, send_time, send_msg) VALUES (9, 'MK', '배터리 충전 알림', '2022-02-08 21:45:00', '배터리 잔량이 곧 20%가 돼요. 서둘러 충전소에서 충전을 해주세요.')";
// $sql = "INSERT INTO battery_info.push_history(id, car_id, push_type, send_time, send_msg) VALUES ($id, $car_id, $push_type, $send_time, $send_msg);";
$result = mysqli_query($con, $sql);
// $result = $conn->query($sql);

if($result) { 
    echo $result;
else { echo "no results"; }

mysqli_close($con);
// $conn->close();
?>