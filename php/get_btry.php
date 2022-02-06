<?php
$conn = mysqli_connect("localhost:3306", "root", "root", "battery_info");
// if($mysqli->connect_errno) {
// 	echo '[MySQL 연결 오류]';
// } else {
// 	echo '[MySQL 연결 성공]';
// }

$sql = "SELECT * FROM battery_record ORDER BY colec_time desc limit 100";

$res = $conn->query($sql);

$result = array();

if($res->num_rows > 0) {
    while($row = $res->fetch_assoc()){
        extract($row);

        array_push($result, array('btry_mdul_tempr_arr'=>$btry_mdul_tempr_arr, 'btry_cells_arr'=>$btry_cells_arr));
    }
    header('Content-Type: application/json; charset=utf8');
    $json = json_encode(array('btry'=>$result), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
    echo $json;
}else{
    echo "no results";
}

if($res === false){
    echo mysqli_error($conn);
}

$conn->close();

?>