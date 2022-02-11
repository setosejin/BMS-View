<?php

$conn = mysqli_connect("localhost:3306", "root", "root", "battery_info");

$sql = "SELECT id FROM push_history order by id desc limit 1";
$res = $conn->query($sql);
$result = array();
if($res->num_rows > 0) {
    while($row = $res->fetch_assoc()){
        //echo json_encode($row);
        extract($row);

        array_push($result, array('id'=>$id));
    }
    header('Content-Type: application/json; charset=utf8');
    $json = json_encode(array('push_id'=>$result), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
    echo $json;
}else{
    echo "no results";
}

if($res === false){
    echo mysqli_error($conn);
}

$conn->close();

?>