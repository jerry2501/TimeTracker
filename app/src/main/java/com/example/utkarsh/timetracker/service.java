package com.example.utkarsh.timetracker;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class service extends Service {
    DatabaseHelper myDb= new DatabaseHelper(this);
    long whatsapp_t1,facebook_t1,snapchat_t1,youtube_t1,instagram_t1,pubg_t1,total1;
    public static int NOTIFIED=1;
    public static int cnt=0;
    public static String[] str={"WhatsApp","Instagram","Snapchat","Facebook","Youtube","PUBG"};
    public static long[] time = new long[6];
    public  service()
        {}
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service", "onStartCommand");

        TimerTask detectapp =new TimerTask() {
            @Override
            synchronized public void run() {
                Log.d("service", "In void run");
                UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);

                if(myDb.getLimit("limit")==1 && Calendar.HOUR_OF_DAY==0)
                {
                    myDb.updateData("limit",0);
                }
                long limit=myDb.getLimit("Seekbar");
                Log.d("Background","in notification"+myDb.getLimit("limit"));
                Log.d("Background","in notification"+myDb.getLimit("Seekbar"));

                if(myDb.getLimit("limit")==0 && (total1/(1000*3600))>=limit)
                {
                    cnt++;
                    myDb.updateData("limit",cnt);
                    long number=myDb.getLimit("number");
                    //  int index=sort();
                    String num=Long.toString(number);
                    NotificationChannel channel=new NotificationChannel("noti","channel",NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription("Discription");
                    Intent intent1=new Intent();
                    PendingIntent pendingIntent=PendingIntent.getActivity(service.this,0,intent1,0);
                    NotificationCompat.Builder builder=new NotificationCompat.Builder(service.this,"noti")
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("Time tracker")
                            .setContentText("You're addicted to your phone.Please get a life!")
                            .setAutoCancel(true);
                    NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.createNotificationChannel(channel);
                    notificationManager.notify(1,builder.build());

                    if(checkPermission(Manifest.permission.SEND_SMS))
                    {
                        SmsManager smsManager=SmsManager.getDefault();
                        smsManager.sendTextMessage(num,null,"Your child/companion is overusing smart phone. It's done for today!!.",null,null);

                    }


                }
                long endTime = System.currentTimeMillis();
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                Log.d("service",""+calendar);
                long beginTime = calendar.getTimeInMillis();

                List<UsageStats> usageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginTime, endTime);
                if (usageStats != null) {
                    for (UsageStats usageStat : usageStats) {
                        if (usageStat.getPackageName().toLowerCase().contains("com.whatsapp")) {
                            boolean isUpdate = myDb.updateData("Whatsapp", usageStat.getTotalTimeInForeground());
                            whatsapp_t1=usageStat.getTotalTimeInForeground();
                            Log.d("Background", "whatsapp: " + usageStat.getTotalTimeInForeground());

                        }
                        if (usageStat.getPackageName().toLowerCase().contains("com.facebook.katana")) {
                            boolean isUpdate = myDb.updateData("Facebook", usageStat.getTotalTimeInForeground());
                            facebook_t1=usageStat.getTotalTimeInForeground();
                            Log.d("Background", "Facebook: " + usageStat.getTotalTimeInForeground());


                        }
                        if (usageStat.getPackageName().toLowerCase().contains("com.instagram.android")) {
                            boolean isUpdate = myDb.updateData("Instagram", usageStat.getTotalTimeInForeground());
                            instagram_t1=usageStat.getTotalTimeInForeground();
                            Log.d("Background", "Instagram " + usageStat.getTotalTimeInForeground());


                        }
                        if (usageStat.getPackageName().toLowerCase().contains("com.snapchat.android")) {
                            boolean isUpdate = myDb.updateData("Snapchat", usageStat.getTotalTimeInForeground());
                            snapchat_t1=usageStat.getTotalTimeInForeground();
                            Log.d("Background", "Snapchat: " + usageStat.getTotalTimeInForeground());


                        }
                        if (usageStat.getPackageName().toLowerCase().contains("com.google.android.youtube")) {
                            boolean isUpdate = myDb.updateData("Youtube", usageStat.getTotalTimeInForeground());
                            youtube_t1=usageStat.getTotalTimeInForeground();
                            Log.d("Background", "Youtube: " + usageStat.getTotalTimeInForeground());


                        }
                        if (usageStat.getPackageName().toLowerCase().contains("com.tencent.ig")) {
                            boolean isUpdate = myDb.updateData("PUBG", usageStat.getTotalTimeInForeground());
                            pubg_t1=usageStat.getTotalTimeInForeground();
                            Log.d("Background", "PUBG: " + usageStat.getTotalTimeInForeground());


                        }
                        total1=whatsapp_t1+facebook_t1+snapchat_t1+instagram_t1+youtube_t1+pubg_t1;


                    }
                }
            }


        };
        long  seek=myDb.getLimit("Seekbar");
        Timer detectAppTimer=new Timer();
        detectAppTimer.scheduleAtFixedRate(detectapp,0,10*1000);


        return START_NOT_STICKY;
//        return super.onStartCommand(intent, flags, startId);


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public boolean checkPermission(String permission)
    {

        int check= ContextCompat.checkSelfPermission(this,permission);
        return (check== PackageManager.PERMISSION_GRANTED );
    }
  /*  public int sort()
    {
        time[0]=whatsapp_t1;
        time[1]=facebook_t1;
        time[2]=snapchat_t1;
        time[3]=youtube_t1;
        time[4]=instagram_t1;
        time[5]=pubg_t1;
        long max=time[0];
        int index=0;
        for (int i=0;i<6;i++)
        {
            if(time[i]<time[i+1])
            {
                 max=time[i+1];
            }
        }
        for(int j=0;j<6;j++)
        {
            if(max==time[j])
            {
                 index=j;
                break;
            }
        }
        return index;
    }*/
}
