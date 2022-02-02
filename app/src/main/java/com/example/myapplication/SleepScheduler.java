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
            handler.sendEmptyMessage(0);//쓰레드에 있는 핸들러에게 메세지를 보냄
            token = MainActivity.android_token;
            System.out.println(token);

            rfBtn1 = HomeFragment.refresh_button;
            rfBtn2 = ChargeFragment.refresh_button;
            rfBtn3 = StateFragment.refresh_button;
//            rfBtn1.callOnClick(); // 현재 fragment에서만 버튼을 클릭할 수 있음.

            String get_btry = "http://192.168.56.1:80/get_btry.php";
            URLConnector btry_thread = new URLConnector(get_btry);
            btry_thread.start();
            try{
                btry_thread.join();
            }
            catch(InterruptedException e){
                System.out.println(e);
            }
            JsonObject btry_resultObj = btry_thread.getResult();
            JsonArray btry_jsonArray = new JsonArray();
            btry_jsonArray = btry_resultObj.get("btry").getAsJsonArray();
            System.out.println("Sleep btry");

            String get_soc = "http://192.168.56.1:80/get_soc.php";
            URLConnector soc_thread = new URLConnector(get_soc);
            soc_thread.start();
            try{
                soc_thread.join();
            }
            catch(InterruptedException e){
                System.out.println(e);
            }
            JsonObject soc_resultObj = soc_thread.getResult();
            JsonArray soc_jsonArray = new JsonArray();
            soc_jsonArray = soc_resultObj.get("soc").getAsJsonArray();
            System.out.println("Sleep soc");

            String get_chrg = "http://192.168.56.1:80/get_chrg.php";
            URLConnector thread_chrg = new URLConnector(get_chrg);
            thread_chrg.start();
            try{
                thread_chrg.join();
            }
            catch(InterruptedException e){
                System.out.println(e);
            }
            JsonObject resultObj_chrg = thread_chrg.getResult();
            JsonArray jsonArray_chrg = new JsonArray();
            jsonArray_chrg = resultObj_chrg.get("chrg").getAsJsonArray();
            System.out.println("Sleep chrg");





            //가져온 데이터들을 확인해서 임계치가 넘어가면 푸시 보내기!
//            String push_url = "http://192.168.56.1:80/test_curl.php?token="+token+"&title="+push_title+"&message="+push_message;
//            URLConnector thread_post = new URLConnector(push_url);
//            thread_post.start();
//            try{
//                thread_post.join();
//                //System.out.println("waiting... for result");
//            }
//            catch(InterruptedException e){
//                System.out.println(e);
//            }
//            JsonObject resultObj_post = thread_post.getResult();
//            System.out.println(resultObj_post);

            try{
                Thread.sleep(10000); //10초씩 쉰다.
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
