package com.example.myapplication;

import android.os.Handler;
import android.widget.Button;

import com.github.mikephil.charting.data.Entry;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SleepScheduler extends Thread{
    Handler handler;
    boolean isRun = true;
    private int willpush = 0;

    private String car_id, user_id, time, token, push_title, push_message, max_cell_volt, min_cell_volt, max_temp, min_temp, soc, soh;
    String selected_frag;
    Button rfBtn1, rfBtn2, rfBtn3;


    public SleepScheduler(Handler handler){
        this.handler = handler;
    }

    public void stopForever(){
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void run(){
        //반복적으로 수행할 작업을 한다.
        while(isRun){
            handler.sendEmptyMessage(0); //쓰레드에 있는 핸들러에게 메세지를 보냄
            token = MainActivity.android_token; //발급 받은 FCM 기기 토큰을 받아옴
//            System.out.println(token);

            rfBtn1 = HomeFragment.refresh_button; // 홈 화면 새로고침 버튼
            rfBtn2 = ChargeFragment.refresh_button; // 충전 화면 새로고침 버튼
            rfBtn3 = StateFragment.refresh_button; // 푸시 화면 새로고침 버튼
            selected_frag = MainActivity.selected; // 선택된 Fragment
            System.out.println(selected_frag);

            if(selected_frag == "home") {
                rfBtn1.callOnClick(); // 현재 fragment에서만 버튼을 클릭할 수 있음
            }
            else if(selected_frag == "charge") {
                rfBtn2.callOnClick();
            }
            else if(selected_frag == "state"){
                rfBtn3.callOnClick();
            }
            //이 외의 상태는 null


            String get_data = "http://192.168.56.1:80/realtime_data.php";
            URLConnector data_thread = new URLConnector(get_data);
            data_thread.start();
            try{
                data_thread.join();
            }
            catch(InterruptedException e){
                System.out.println(e);
            }
            JsonObject resultObj = data_thread.getResult();
            JsonArray jsonArray = new JsonArray();
            jsonArray = resultObj.get("data").getAsJsonArray();
            car_id = jsonArray.get(0).getAsJsonObject().get("car_id").toString();
            max_cell_volt = jsonArray.get(0).getAsJsonObject().get("max_cell_volt").toString().replace("\"", "");
            min_cell_volt = jsonArray.get(0).getAsJsonObject().get("min_cell_volt").toString().replace("\"", "");

            max_temp = jsonArray.get(0).getAsJsonObject().get("btry_max_tempr").toString().replace("\"", "");
            min_temp = jsonArray.get(0).getAsJsonObject().get("btry_min_tempr").toString().replace("\"", "");

            soc = jsonArray.get(0).getAsJsonObject().get("state_of_chrg_bms").toString().replace("\"", "");
            soh = jsonArray.get(0).getAsJsonObject().get("state_of_health").toString().replace("\"", "");

            System.out.println(min_cell_volt);
            //가져온 데이터들을 확인해서 임계치가 넘어가면 푸시 보내기!
            if(Float.parseFloat(max_temp)>60){
                willpush = 1;
                SendPush(willpush);
            }
            if(Float.parseFloat(min_temp)<-30){
                willpush = 2;
                SendPush(willpush);
            }
            if(Float.parseFloat(max_cell_volt)>4.2){
                willpush = 3;
                SendPush(willpush);
            }
            if(Float.parseFloat(min_cell_volt)<2.5){
                willpush = 4;
                SendPush(willpush);
            }
            if(Float.parseFloat(soc)<30){
                willpush = 5;
                SendPush(willpush);
            }
            if(Float.parseFloat(soh)<80){
                willpush = 6;
                SendPush(willpush);
            }

            if(willpush>0) {
                System.out.println("willpush");
                willpush = 0;
            }

            try{
                Thread.sleep(15000); //15초씩 쉰다.
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void SendPush(int willpush) {
        switch (willpush){
            case 1:
                push_title = "배터리 온도 이상 감지";
                push_message = "배터리 모듈의 온도가 높습니다. 화재 위험이 있으니 점검을 받아보세요.";
                break;
            case 2:
                push_title = "배터리 온도 이상 감지";
                push_message = "배터리 모듈의 온도가 낮습니다. 차량 상태가 위험하니 점검을 받아보세요.";
                break;
            case 3:
                push_title = "배터리 셀 전압 이상 감지";
                push_message = "배터리 셀의 전압이 일정하지 않습니다. 정비소를 들려 확인하세요.";
                break;
            case 4:
                push_title = "배터리 셀 전압 이상 감지";
                push_message = "배터리 셀의 전압이 일정하지 않습니다. 배터리 용량 손실이 발생할 가능성이 있습니다. 정비소를 들려 확인하세요.";
                break;
            case 5:
                push_title = "배터리 충전 알림";
                push_message = "배터리 잔량이 곧 20%가 돼요. 서둘러 충전소에서 충전을 해주세요.";
                break;
            case 6:
                push_title = "배터리 교체 알림";
                push_message = "배터리 수명이 80% 미만입니다. 새로운 배터리로 교체를 권장합니다.";
                break;
        }

        String push_url = "http://192.168.56.1:80/test_curl.php?token="+token+"&title="+push_title+"&message="+push_message;
        URLConnector thread_push = new URLConnector(push_url);
        thread_push.start();
        try{
            thread_push.join();
            //System.out.println("waiting... for result");
        }
        catch(InterruptedException e){
            System.out.println(e);
        }
        JsonObject resultObj_push = thread_push.getResult();
        System.out.println(resultObj_push);

        time = getTime();
        System.out.println(car_id);
        System.out.println(time);

//        String post_url = "http://192.168.56.1:80/test_push.php?car="+car_id+"&user="+user_id+"&type="+push_title+"&time="+time+"&msg="+push_message;
        String post_url = "http://192.168.56.1:80/test_push.php?car="+car_id+"&type="+push_title+"&msg="+push_message;

        //id(PK)를 겹치지 않게 잘 설정해야함!
        //->마지막 id를 갖고 와서 +1해주는 코드 필요
        URLConnectorPost thread_post = new URLConnectorPost(post_url);
        thread_post.start();
        try{
            thread_post.join();
        }
        catch(InterruptedException e){
            System.out.println(e);
        }
        String postResult = thread_post.getResult();
        System.out.println(postResult);
    }

    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String gettime = dateFormat.format(date);
        return gettime;
    }
}
