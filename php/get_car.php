<?php

$conn = mysqli_connect("localhost:3306", "root", "root", "battery_info");
// if($mysqli->connect_errno) {
// 	echo '[MySQL 연결 오류]';
// } else {
// 	echo '[MySQL 연결 성공]';
// }

$sql = "SELECT * FROM car";

$res = $conn->query($sql);

$result = array();

if($res->num_rows > 0) {
    while($row = $res->fetch_assoc()){
        //echo json_encode($row);
        extract($row);

        array_push($result, array('car_id'=>$car_id, 'user_id'=>$user_id));
    }
    header('Content-Type: application/json; charset=utf8');
    $json = json_encode(array('car'=>$result), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
    echo $json;
}else{
    echo "no results";
}

if($res === false){
    echo mysqli_error($conn);
}

#mysqli_close($conn);
$conn->close();

?>