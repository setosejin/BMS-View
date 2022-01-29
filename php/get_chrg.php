<?php

$conn = mysqli_connect("localhost:3306", "root", "root", "battery_info");

$sql = "SELECT vehicle_speed, odometer FROM battery_info.driving_record";
        // SELECT odometer, mvmn_time FROM battery_record";

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

$sql3 = "SELECT quick_recharge_cnt, slow_recharge_cnt FROM battery_info.analysis";

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

$sql2 = "SELECT state_of_chrg_bms, state_of_health FROM battery_info.battery_record";

$res2 = $conn->query($sql2);
if($res2->num_rows > 0) {
    while($row = $res2->fetch_assoc()){
        //echo json_encode($row);
        extract($row);

        array_push($result, array('vehicle_speed'=>$vehicle_speed, 'odometer'=>$odometer, 'state_of_chrg_bms'=>$state_of_chrg_bms, 'state_of_health'=>$state_of_health, 'quick_recharge_cnt'=>$quick_recharge_cnt, 'slow_recharge_cnt'=>$slow_recharge_cnt));
    }
    header('Content-Type: application/json; charset=utf8');
    $json = json_encode(array('chrg'=>$result), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);


    // echo sizeof($result);

    echo $json;
}else{
    echo "no results 2";
}

if($res2 === false){
    echo mysqli_error($conn);
}

#mysqli_close($conn);
$conn->close();

?>