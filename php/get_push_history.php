<?php

$conn = mysqli_connect("localhost:3306", "root", "root", "battery_info");
// if($mysqli->connect_errno) {
// 	echo '[MySQL 연결 오류]';
// } else {
// 	echo '[MySQL 연결 성공]';
// }

$sql = "SELECT * FROM push_history";

$res = $conn->query($sql);

$result = array();

if($res->num_rows > 0) {
    while($row = $res->fetch_assoc()){
        //echo json_encode($row);
        extract($row);

        array_push($result, array('car_id'=>$car_id, 'user_id'=>$user_id, 'push_type'=>$push_type, 'send_time' =>$send_time, 'send_msg'=>$send_msg));
    }
    header('Content-Type: application/json; charset=utf8');
    $json = json_encode(array('push_history'=>$result), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
    echo $json;
}else{
    echo "no results";
}

if($res === false){
    echo mysqli_error($conn);
}

// $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
//     $conn->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);

//     if(function_exists('get_magic_quotes_gpc') && get_magic_quotes_gpc()) { 
//         function undo_magic_quotes_gpc(&$array) { 
//             foreach($array as &$value) { 
//                 if(is_array($value)) { 
//                     undo_magic_quotes_gpc($value); 
//                 } 
//                 else { 
//                     $value = stripslashes($value); 
//                 } 
//             } 
//         } 
//         undo_magic_quotes_gpc($_POST); 
//         undo_magic_quotes_gpc($_GET); 
//         undo_magic_quotes_gpc($_COOKIE); 
//     } 
//     header('Content-Type: text/html; charset=utf-8'); 

#mysqli_close($con);
$conn->close();

?>