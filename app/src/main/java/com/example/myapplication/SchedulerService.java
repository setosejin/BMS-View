package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class SchedulerService extends Service {
    NotificationManager Notifi_M;
    SleepScheduler thread;
    Notification Notifi;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        thread = new SleepScheduler(handler);
        thread.start();
        return START_STICKY;
    }

    //서비스가 종료될 때 할 작업
    public void onDestroy() {
        thread.stopForever();
        thread = null; //쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
    }

    class myServiceHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent intent = new Intent(SchedulerService.this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(SchedulerService.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

            //handler가 호출될 때마다 출력
            System.out.println("service handler");

        }
    };
}