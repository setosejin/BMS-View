package com.example.myapplication;

import android.os.Handler;
import android.widget.Button;

import com.github.mikephil.charting.data.Entry;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;

public class SleepScheduler extends Thread{
    Handler handler;
    boolean isRun = true;
    private int willpush = 0;

    private String car_id, user_id, time, token, push_title, push_message, max_cell_volt, min_cell_volt, max_temp, min_temp, soc, soh;
    String selected_frag, tomorrow;
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


            time = getTime(); // 날짜 & 현재 시간


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

            String now = getOnlyTime();
            String t = now.substring(0, 2);
            String m = now.substring(3, 5);
            String s = now.substring(6, 8);
            if(t == "17" && m == "30"){
                int sec = (int) Float.parseFloat(s);
                if (sec > 0 && sec <= 15){
                    //매일 저녁 날씨 확인
                    final WeatherAPI weatherAPI = new WeatherAPI();
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println("weather");
                                weatherAPI.func();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    //내일 온도에 따라 푸시
                    tomorrow = WeatherAPI.tomorrowTmp;
                    if(Float.parseFloat(tomorrow) < -10 || Float.parseFloat(tomorrow) > 30){
                        willpush = 7;
                        SendPush(willpush);
                    }
                }
            }

            Calendar cal = Calendar.getInstance();
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeek == 1){ // 일요일
                if(t == "20" && m == "30"){
                    int sec = (int) Float.parseFloat(s);
                    if (sec > 0 && sec <= 15){
                        // 주간 안전 리포트 푸시 알림

                        // 충전 습관 & 주행 습관

                    }
                }
            }

            if(willpush>0) {
//                System.out.println("will push");
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
            case 7:
                push_title = "내일을 위한 주차 팁";
                push_message = "내일은 배터리 수명을 위해 실내 주차를 권장합니다.";
                break;
        }

        // 푸시 알림 API
        String push_url = "http://192.168.56.1:80/test_curl.php?token="+token+"&title="+push_title+"&message="+push_message;
        URLConnector thread_push = new URLConnector(push_url);
        thread_push.start();
        try{
            thread_push.join();
        }
        catch(InterruptedException e){
            System.out.println(e);
        }
        JsonObject resultObj_push = thread_push.getResult();
//        System.out.println(resultObj_push);

        //마지막 푸시 id 갖고 옴
        String pushId_url = "http://192.168.56.1:80/get_last_push_id.php";
        URLConnector thread_pushId = new URLConnector(pushId_url);
        thread_pushId.start();
        try{
            thread_pushId.join();
        }
        catch(InterruptedException e){
            System.out.println(e);
        }
        JsonObject resultObj_pushId = thread_pushId.getResult();
        JsonArray jsonArray = new JsonArray();
        jsonArray = resultObj_pushId.get("push_id").getAsJsonArray();
        int last_id = (int) Float.parseFloat(jsonArray.get(0).getAsJsonObject().get("id").toString().replace("\"", ""));
        ++last_id;

        // push_history에 새로운 푸시 INSERT
        String post_url = "http://192.168.56.1:80/test_push.php?id="+last_id+"&car="+car_id+"&type="+push_title+"&msg="+push_message;

        URLConnectorPost thread_post = new URLConnectorPost(post_url);
        thread_post.start();
        try{
            thread_post.join();
        }
        catch(InterruptedException e){
            System.out.println(e);
        }
        String postResult = thread_post.getResult();
//        System.out.println(postResult);
    }

    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String gettime = dateFormat.format(date);
        return gettime;
    }

    private String getOnlyTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String getonlytime = dateFormat.format(date);
        return getonlytime;
    }

}
