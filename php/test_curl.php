<?php
//이 코드가 잘 실행되면 post_push와 합쳐서 실행할 예정

// 특정 사용자 토큰, 보낼 메시지 제목 및 내용을 전달 받습니다.
$token = $_GET['token'];
$title = $_GET['title'];
$message = $_GET['message'];
// echo $token.$title.$message;

if($token == "" || $title == "" || $message == "") {
        $this->output->set_status_header(400); // 입력 값 형식이 올바르지 않습니다.
        exit;
}
// 보내고자 하는 데이터를 배열에 담습니다.
$notification = array();
$notification['title'] = $title;
$notification['body'] = $message;
$tokens = array();
$tokens[0] = $token;
$data = array();
$data['title'] = 'notify test';
// 전송을 진행합니다.
$url = 'https://fcm.googleapis.com/fcm/send';
$apiKey = "AAAAvm6_DU4:APA91bFnfCkTp9mA2dbODtF928ak4fSmFYxVKzTuSzDoerYNXz15A6K5tmAQpABLog7YunCKqs0rq0bFpqf5bumhaYpu7tkuh1ZnD2vFqmBERTIMb5IrZIJldqfD2pTspUmC01hlGARU";
$fields = array('registration_ids' => $tokens,'notification' => $notification, 'data' => $data, 'priority' => "high");
$headers = array('Authorization:key='.$apiKey,'Content-Type: application/json');
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0); 
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
$result = curl_exec($ch);
if ($result === FALSE) {
        $this->output->set_status_header(500); // FCM 푸시 메시지 전송에 실패했습니다.
}
curl_close($ch);
echo $result;
?>
