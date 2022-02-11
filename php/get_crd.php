<?php
$conn = mysqli_connect("localhost:3306", "root", "root", "battery_info");
// if($mysqli->connect_errno) {
// 	echo '[MySQL 연결 오류]';
// } else {
// 	echo '[MySQL 연결 성공]';
// }

$sql = "SELECT * FROM driving_record order by updated_time limit 1";

$res = $conn->query($sql);

$result = array();

if($res->num_rows > 0) {
    while($row = $res->fetch_assoc()){
        extract($row);

        array_push($result, array('updated_time'=>$updated_time, 'xcord'=>$xcord, 'ycord'=>$ycord));
    }
    header('Content-Type: application/json; charset=utf8');
    $json = json_encode(array('crd'=>$result), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
    echo $json;
}else{
    echo "no results";
}

if($res === false){
    echo mysqli_error($conn);
}

$conn->close();

?>