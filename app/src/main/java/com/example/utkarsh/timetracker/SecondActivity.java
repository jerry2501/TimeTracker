package com.example.utkarsh.timetracker;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    CheckBox Draw, Usage;
    Button btn1;
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;
    public static int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 41;
    private int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        loadData();
        if (cnt <= 1) {
            Draw = (CheckBox) findViewById(R.id.draw_app);
            Draw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPermission();
                }
            });
            Usage = (CheckBox) findViewById(R.id.Usage_access);
            Usage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPermission1();
                }
            });



        }
        else {
            Intent Home_intent = new Intent(SecondActivity.this, Third_Activity.class);
            startActivity(Home_intent);
        }


    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // You don't have permission
                Draw.setChecked(false);
                Toast.makeText(getApplicationContext(), "We need this permission", Toast.LENGTH_LONG).show();
            } else {
                // Do as per your logic
                Draw.setChecked(true);
                Draw.setEnabled(false);
                cnt++;
                if (cnt == 2) {
                    Intent Home_intent = new Intent(SecondActivity.this, Third_Activity.class);
                    startActivity(Home_intent);
                }
            }

        }
        if (requestCode == MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS) {
            if (!hasUsageStatsPermission()) {
                // You don't have permission
                Usage.setChecked(false);
                Toast.makeText(getApplicationContext(), "We need this permission", Toast.LENGTH_LONG).show();
            }
            else {
                // Do as per your logic
                Usage.setChecked(true);
                Usage.setEnabled(false);
                cnt++;
                if (cnt == 2) {
                    Intent Home_intent = new Intent(SecondActivity.this, Third_Activity.class);
                    startActivity(Home_intent);


                }
            }

        }

    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }

    public void checkPermission1() {
        boolean g=hasUsageStatsPermission();
        if (!g) {

            startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
        }
    }

    public boolean hasUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,android.os.Process.myUid(), getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        return granted;
    }


    public  void saveData()
    {
        SharedPreferences Shared=getSharedPreferences("scounter",MODE_PRIVATE);
        SharedPreferences.Editor editor=Shared.edit();
        editor.putInt("countervalue",cnt);
        editor.apply();
    }
    public void loadData()
    {
        SharedPreferences Shared=getSharedPreferences("scounter",MODE_PRIVATE);
        cnt= Shared.getInt("countervalue",MODE_PRIVATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }
}
