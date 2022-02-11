<?php

$conn = mysqli_connect("localhost:3306", "root", "root", "battery_info");
// if($mysqli->connect_errno) {
// 	echo '[MySQL 연결 오류]';
// } else {
// 	echo '[MySQL 연결 성공]';
// }

//가장 최근 값 한 행만 불러오기
$sql = "SELECT * FROM battery_info.battery_record ORDER BY colec_time desc limit 1";
$res = $conn->query($sql);
$result = array();
if($res->num_rows > 0) {
    while($row = $res->fetch_assoc()){
        extract($row);

        array_push($result, array('max_cell_volt'=>$max_cell_volt,'min_cell_volt'=>$min_cell_volt,'btry_max_tempr'=>$btry_max_tempr,'btry_min_tempr'=>$btry_min_tempr, 'state_of_chrg_bms'=>$state_of_chrg_bms, 'state_of_health'=>$state_of_health, 'car_id'=>$car_id));
    }
    header('Content-Type: application/json; charset=utf8');
    $json = json_encode(array('data'=>$result), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
    echo $json;
}else{
    echo "no results";
}
if($res === false){
    echo mysqli_error($conn);
}

$conn->close();

?>