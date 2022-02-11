<?php
$servername = "localhost:3306";
$username = "root";
$password = "root";
$dbname = "battery_info";

$car_id = $_GET['car'];
$push_type = "\"".$_GET['type']."\"";
$send_time = "\"".date("Y-m-d H:i:s")."\"";
$send_msg = "\"".$_GET['msg']."\"";
$id = $_GET['id'];
// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}

$sql = "INSERT INTO push_history(id, car_id, push_type, send_time, send_msg) VALUES ($id, $car_id, $push_type, $send_time, $send_msg)";

if ($conn->query($sql) === TRUE) {
  echo "New record created successfully";
} else {
  echo "Error: " . $sql . "<br>" . $conn->error;
}

$conn->close();
?>