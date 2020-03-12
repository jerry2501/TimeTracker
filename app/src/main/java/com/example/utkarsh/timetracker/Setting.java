package com.example.utkarsh.timetracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Setting extends AppCompatActivity {
    private TextView textView;
    private SeekBar seekbar;
    private Button btn_nxt,btn_seek;
    DatabaseHelper myDb;
    private EditText et1;
    String number=new String();
    long sb,num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        myDb=new DatabaseHelper(this);
        seekbar =(SeekBar) findViewById(R.id.seekBar);
        textView=(TextView)findViewById(R.id.textView);
        btn_nxt=(Button)findViewById(R.id.btn_nxt);
        btn_seek=(Button)findViewById(R.id.btn_seek);
        textView.setText("Time limit:"+ seekbar.getProgress());
        et1=(EditText)findViewById(R.id.et1);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            long progress_value,hour;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_value=progress;
                hour=(progress_value/10) + 1;
                if (progress_value==100)
                {
                    hour=10;
                    sb=hour;
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
        btn_seek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.updateData("Seekbar",sb);
               Toast.makeText(Setting.this,"TIME UPDATED",Toast.LENGTH_LONG).show();
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

                myDb.updateData("number",num);
                Toast.makeText(Setting.this,"NUMBER UPDATED",Toast.LENGTH_LONG).show();
            }
        });
    }
}
