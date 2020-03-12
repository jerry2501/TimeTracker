package com.example.utkarsh.timetracker;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.IBinder;
//import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.USAGE_STATS_SERVICE;

public class Background extends Service {
    DatabaseHelper myDb= new DatabaseHelper(this);
   // public Background() {
    //}
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Background", "onStartCommand");

        TimerTask detectapp =new TimerTask() {
            @Override
            synchronized public void run() {
                Log.d("Background", "In void run");
                UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);


                long endTime = System.currentTimeMillis();
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                long beginTime = calendar.getTimeInMillis();

                List<UsageStats> usageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginTime, endTime);
                if (usageStats != null) {
                    for (UsageStats usageStat : usageStats) {
                        if (usageStat.getPackageName().toLowerCase().contains("com.whatsapp")) {
                            boolean isUpdate = myDb.updateData("Whatsapp", usageStat.getTotalTimeInForeground());

                            Log.d("Background", "whatsapp: " + usageStat.getTotalTimeInForeground());

                        }
                        if (usageStat.getPackageName().toLowerCase().contains("com.facebook.katana")) {
                            boolean isUpdate = myDb.updateData("Facebook", usageStat.getTotalTimeInForeground());

                            Log.d("Background", "Facebook: " + usageStat.getTotalTimeInForeground());


                        }
                        if (usageStat.getPackageName().toLowerCase().contains("com.instagram.android")) {
                            boolean isUpdate = myDb.updateData("Instagram", usageStat.getTotalTimeInForeground());

                            Log.d("Background", "Instagram " + usageStat.getTotalTimeInForeground());


                        }
                        if (usageStat.getPackageName().toLowerCase().contains("com.snapchat.android")) {
                            boolean isUpdate = myDb.updateData("Snapchat", usageStat.getTotalTimeInForeground());

                            Log.d("Background", "Snapchat: " + usageStat.getTotalTimeInForeground());


                        }
                        if (usageStat.getPackageName().toLowerCase().contains("com.google.android.youtube")) {
                            boolean isUpdate = myDb.updateData("Youtube", usageStat.getTotalTimeInForeground());

                            Log.d("Background", "Youtube: " + usageStat.getTotalTimeInForeground());


                        }
                        if (usageStat.getPackageName().toLowerCase().contains("com.tencent.ig")) {
                            boolean isUpdate = myDb.updateData("PUBG", usageStat.getTotalTimeInForeground());

                            Log.d("Background", "PUBG: " + usageStat.getTotalTimeInForeground());


                        }
                    }
                }
            }


        };
        Timer detectAppTimer=new Timer();
        detectAppTimer.scheduleAtFixedRate(detectapp,0,5*60*1000);
        return START_NOT_STICKY;
//        return super.onStartCommand(intent, flags, startId);



    }


    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");

    }
}
