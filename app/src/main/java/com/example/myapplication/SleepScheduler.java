package com.example.myapplication;

import android.os.Handler;

import com.github.mikephil.charting.data.Entry;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SleepScheduler extends Thread{
    Handler handler;
    boolean isRun = true;

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
//            for(int i = 0; i < btry_jsonArray.size(); i++){
                System.out.println("Sleep btry");
//                System.out.println((btry_jsonArray.get(i).getAsJsonObject().get("btry_mdul_tempr_arr").toString()));
//            }

            String get_soc = "http://192.168.56.1:80/get_soc.php";
            URLConnector soc_thread = new URLConnector(get_soc);
            soc_thread.start();
            try{
                soc_thread.join();
                //System.out.println("waiting... for result");
            }
            catch(InterruptedException e){
                System.out.println(e);
            }
            JsonObject soc_resultObj = soc_thread.getResult();
            JsonArray soc_jsonArray = new JsonArray();
            soc_jsonArray = soc_resultObj.get("soc").getAsJsonArray();
//            for(int i = 0; i < soc_jsonArray.size(); i++){
                System.out.println("Sleep soc");
//                System.out.println((soc_jsonArray.get(i).getAsJsonObject().get("soc_real").toString()));
//            }

            String get_chrg = "http://192.168.56.1:80/get_chrg.php";
            URLConnector thread_chrg = new URLConnector(get_chrg);

            thread_chrg.start();
            try{
                thread_chrg.join();
                //System.out.println("waiting... for result");
            }
            catch(InterruptedException e){
                System.out.println(e);
            }
            JsonObject resultObj_chrg = thread_chrg.getResult();
            JsonArray jsonArray_chrg = new JsonArray();
            jsonArray_chrg = resultObj_chrg.get("chrg").getAsJsonArray();
//            for(int i = 0; i < jsonArray_chrg.size(); i++){
                System.out.println("Sleep chrg");
//                System.out.println("["+i+"] "+(jsonArray_chrg.get(i).getAsJsonObject().get("state_of_chrg_bms").toString()));
//            }

            try{
                Thread.sleep(10000); //10초씩 쉰다.
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
