package com.example.utkarsh.timetracker;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Third_Activity extends AppCompatActivity {
    DatabaseHelper myDb;
    private TextView textView;
    private SeekBar seekbar;
    private Button btn_nxt;
    private static int cnt1=0;
    private long backPressedTime;
    private Toast backToast;
    long sb,num;
    private EditText et1;
    public int req_code=1;
    String number=new String();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_);
        myDb=new DatabaseHelper(this);
        myDb.insertData("Seekbar",0);
         sb = myDb.getLimit("Seekbar");
         myDb.insertData("number",0);



        if(sb!=0)
        {
            Intent Third_intent = new Intent(Third_Activity.this,usageStates.class);
            startActivity(Third_intent);
        }
else{
            textView = (TextView) findViewById(R.id.textView);
            btn_nxt = (Button) findViewById(R.id.btn_nxt);
            seekbar =(SeekBar) findViewById(R.id.seekBar);
            textView.setText("Time limit:"+ seekbar.getProgress());
            et1=(EditText)findViewById(R.id.et1);
            if(!checkPermission(Manifest.permission.SEND_SMS))
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},req_code);
            }

            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                long progress_value,hour;
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progress_value=progress;
                    hour=(progress_value/10) + 1;
                    if (progress_value==100)
                    {
                        hour=10;
                    }
                    textView.setText("Time limit:"+ hour +" Hours");
                    myDb.updateData("Seekbar",hour);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    //textView.setText("Time limit:" + "1 hours");
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    textView.setText("Time limit:" + hour + " Hours");
                    myDb.updateData("Seekbar",hour);

                }
            });







            btn_nxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(et1.getText().toString().length()!=10)
                    {
                        et1.setError("Enter the valid number");
                        return;

                    }
                    else{
                    try{num= Long.valueOf(et1.getText().toString());
                        Log.d("Third",""+num);}

                    catch(Exception e){} }
                    myDb.insertData("Whatsapp",0);
                    myDb.insertData("Facebook",0);
                    myDb.insertData("Instagram",0);
                    myDb.insertData("Snapchat",0);
                    myDb.insertData("Youtube",0);
                    myDb.insertData("PUBG",0);
                    myDb.insertData("limit",0);
                    myDb.updateData("number",num);

                    Intent Third_intent = new Intent(Third_Activity.this, usageStates.class);
                    startActivity(Third_intent);
                }
            });
}
        }

    @Override
    public void onBackPressed() {
       finishAffinity();
    }
    public boolean checkPermission(String permission)
    {

        int check= ContextCompat.checkSelfPermission(this,permission);
        return (check== PackageManager.PERMISSION_GRANTED );
    }
        /* public  void saveData3()
    {
        SharedPreferences Shared3=getSharedPreferences("scounter",MODE_PRIVATE);
        SharedPreferences.Editor editor=Shared3.edit();
        editor.putInt("countervalue",cnt1);
        editor.apply();
    }
    public void loadData3()
    {
        SharedPreferences Shared3=getSharedPreferences("scounter",MODE_PRIVATE);
        cnt1= Shared3.getInt("countervalue",MODE_PRIVATE);
    }



    @Override
    protected void onPause() {
        super.onPause();
        saveData3();
    }*/

}
