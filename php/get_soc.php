<?php

$conn = mysqli_connect("localhost:3306", "root", "root", "battery_info");
// if($mysqli->connect_errno) {
// 	echo '[MySQL 연결 오류]';
// } else {
// 	echo '[MySQL 연결 성공]';
// }

//가장 최근 값 한 행만 불러오기
$sql = "SELECT charge_status, key_status, soc_real, odometer FROM battery_info.driving_record ORDER BY updated_time desc limit 1" ;
$res = $conn->query($sql);
$result = array();
if($res->num_rows > 0) {
    while($row = $res->fetch_assoc()){
        extract($row);

    }
    
}else{
    echo "no results 1";
}
if($res === false){
    echo mysqli_error($conn);
}

//

$sql3 = "SELECT safety_grade FROM battery_info.analysis ORDER BY analysis_date desc limit 1";

$res3 = $conn->query($sql3);

$result = array();

if($res3->num_rows > 0) {
    while($row = $res3->fetch_assoc()){
        extract($row);

    }
}else{
    echo "no results 3";
}

if($res3 === false){
    echo mysqli_error($conn);
}

//

$sql2 = "SELECT mvmn_time FROM battery_record ORDER BY colec_time desc limit 1";
$res2 = $conn->query($sql2);
if($res2->num_rows > 0) {
    while($row = $res2->fetch_assoc()){
        extract($row);

        array_push($result, array('safety_grade'=>$safety_grade, 'charge_status'=>$charge_status, 'key_status'=>$key_status, 'soc_real'=> $soc_real, 'odometer'=>$odometer, 'mvmn_time'=>$mvmn_time));
    }
    header('Content-Type: application/json; charset=utf8');
    $json = json_encode(array('soc'=>$result), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
    echo $json;
}else{
    echo "no results 2";
}
if($res2 === false){
    echo mysqli_error($conn);
}

$conn->close();

?>