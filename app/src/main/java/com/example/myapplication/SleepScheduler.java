package com.example.myapplication;

import android.os.Handler;
import android.widget.Button;

import com.github.mikephil.charting.data.Entry;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SleepScheduler extends Thread{
    Handler handler;
    boolean isRun = true;

    private String token, push_title, push_message;
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
            System.out.println(token);

            rfBtn1 = HomeFragment.refresh_button; //홈 화면 새로고침 버튼
            rfBtn2 = ChargeFragment.refresh_button; // 충전 화면 새로고침 버튼
            rfBtn3 = StateFragment.refresh_button; // 푸시 화면 새로고침 버튼
            selected_frag = MainActivity.selected;
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

            //가져온 데이터들을 확인해서 임계치가 넘어가면 푸시 보내기!
//            String push_url = "http://192.168.56.1:80/test_curl.php?token="+token+"&title="+push_title+"&message="+push_message;
//            URLConnector thread_push = new URLConnector(push_url);
//            thread_push.start();
//            try{
//                thread_push.join();
//                //System.out.println("waiting... for result");
//            }
//            catch(InterruptedException e){
//                System.out.println(e);
//            }
//            JsonObject resultObj_post = thread_push.getResult();
//            System.out.println(resultObj_post);

            try{
                Thread.sleep(15000); //15초씩 쉰다.
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
