package com.example.utkarsh.timetracker;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static com.example.utkarsh.timetracker.service.NOTIFIED;

public class usageStates extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private TextView whatsapp_time, facebook_time, instagram_time, snaphat_time, youtube_time, pubg_time;
     long whatsapp_t,facebook_t,snapchat_t,youtube_t,instagram_t,pubg_t,total;
     public int req_code=1;
    private long backPressedTime;
    private Toast backToast;
    DatabaseHelper myDb;
    String manufacturer="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_states);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        myDb = new DatabaseHelper(this);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        manufacturer = android.os.Build.MANUFACTURER;

        try {
            Intent intent = new Intent();
            // manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            }else if ("huawei".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }
            else if("samsung".equalsIgnoreCase(manufacturer))
            {
                intent.setComponent(new ComponentName("com.samsung.android.lool","com.samsung.android.sm.ui.battery.BatteryActivity"));
            }

            List<ResolveInfo> list = this.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if  (list.size() > 0) {
                this.startActivity(intent);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }





        if (!checkUsagestats()) {
            Intent usageAccess = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            usageAccess.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(usageAccess);
            if (checkUsagestats()) {
                Log.d("usageStates", "In else");
                startService(new Intent(usageStates.this, service.class));
            } else {
                Toast.makeText(getApplicationContext(), "Please give Access", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d("MainActivity", "In else");
            startService(new Intent(usageStates.this, service.class));
        }
        whatsapp_time = (TextView)findViewById(R.id.whatsapp_t);
        facebook_time = findViewById(R.id.facebook_time);
        instagram_time = findViewById(R.id.instagram_time);
        snaphat_time = findViewById(R.id.snapchat_time);
        youtube_time = findViewById(R.id.youtube_time);
        pubg_time = findViewById(R.id.pubg_time);

        TimerTask updateView = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("MainActivity", "Whatsapp:" + myDb.getLimit("Whatsapp"));
                         whatsapp_t = myDb.getLimit("Whatsapp");

                        long second = (whatsapp_t / 1000) % 60;
                        long minute = (whatsapp_t / (1000 * 60)) % 60;
                        long hour = (whatsapp_t / (1000 * 60 * 60));
                        String whatsapp_val = hour + "hr" + minute + "min" + second + "sec";
                        Log.d("MainActivity", "" + whatsapp_val);
                        whatsapp_time.setText(whatsapp_val);


                        Log.d("MainActivity", "Facebook:" + myDb.getLimit("Facebook"));
                         facebook_t = myDb.getLimit("Facebook");
                        second = (facebook_t / 1000) % 60;
                        minute = (facebook_t / (1000 * 60)) % 60;
                        hour = (facebook_t / (1000 * 60 * 60));
                        String facebook_val = hour + "hr" + minute + "min" + second + "sec";
                        facebook_time.setText(facebook_val);

                        Log.d("MainActivity", "" + myDb.getLimit("Instagram"));
                         instagram_t = myDb.getLimit("Instagram");
                        second = (instagram_t / 1000) % 60;
                        minute = (instagram_t / (1000 * 60)) % 60;
                        hour = (instagram_t / (1000 * 60 * 60));
                        String instagram_val = hour + "hr" + minute + "min" + second + "sec";
                        instagram_time.setText(instagram_val);

                        Log.d("MainActivity", "" + myDb.getLimit("Snapchat"));
                         snapchat_t = myDb.getLimit("Snapchat");
                        second = (snapchat_t / 1000) % 60;
                        minute = (snapchat_t / (1000 * 60)) % 60;
                        hour = (snapchat_t / (1000 * 60 * 60));
                        String snapchat_val = hour + "hr" + minute + "min" + second + "sec";
                        snaphat_time.setText(snapchat_val);

                        Log.d("MainActivity", "" + myDb.getLimit("Youtube"));
                         youtube_t = myDb.getLimit("Youtube");
                        second = (youtube_t / 1000) % 60;
                        minute = (youtube_t / (1000 * 60)) % 60;
                        hour = (youtube_t / (1000 * 60 * 60));
                        String youtube_val = hour + "hr" + minute + "min" + second + "sec";
                        youtube_time.setText(youtube_val);

                        Log.d("MainActivity", "" + myDb.getLimit("PUBG"));
                         pubg_t = myDb.getLimit("PUBG");
                        second = (pubg_t / 1000) % 60;
                        minute = (pubg_t / (1000 * 60)) % 60;
                        hour = (pubg_t / (1000 * 60 * 60));
                        String pubg_val = hour + "hr" + minute + "min" + second + "sec";
                        pubg_time.setText(pubg_val);
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(updateView, 0, 1000);
        total=whatsapp_t+facebook_t+instagram_t+snapchat_t+youtube_t+pubg_t;



    }

    @Override
    public void onBackPressed() {
      finishAffinity();
    }

    public boolean checkUsagestats() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == MODE_ALLOWED);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "cannot found any uasge stats manager", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();


        if (id == R.id.home) {

            Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.graph) {
            Toast.makeText(this, "Graph selected", Toast.LENGTH_SHORT).show();
            Intent Third_intent = new Intent(usageStates.this,graph.class);
            startActivity(Third_intent);
        }
        if (id == R.id.settings) {
            Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
            Intent Third_intent = new Intent(usageStates.this,Setting.class);
            startActivity(Third_intent);
        }
        if (id == R.id.about) {

            Toast.makeText(this, "About Us selected", Toast.LENGTH_SHORT).show();
            Intent intent3 = new Intent(usageStates.this,about.class);
            startActivity(intent3);
        }
        if (id == R.id.help) {

            Toast.makeText(this, "Help selected", Toast.LENGTH_SHORT).show();
            Intent Third_intent = new Intent(usageStates.this,help.class);
            startActivity(Third_intent);
        }

        return false;
    }


}
