package com.example.utkarsh.timetracker;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

import java.util.ArrayList;
import java.util.List;

public class graph extends AppCompatActivity {
    DatabaseHelper myDb;
    long whatsapp_t,facebook_t,snapchat_t,youtube_t,instagram_t,pubg_t,total;
    float percentage[]=new  float[6];
    private PieChart pieChart;
    private PieData pieData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_graph);
         myDb=new DatabaseHelper(this);
         whatsapp_t = myDb.getLimit("Whatsapp");
        facebook_t = myDb.getLimit("Facebook");
        snapchat_t = myDb.getLimit("Snapchat");
        youtube_t = myDb.getLimit("Youtube");
        instagram_t = myDb.getLimit("Instagram");
        Log.d("graph",""+myDb.getLimit("Instagram"));
         pubg_t = myDb.getLimit("PUBG");
         percentage();
         pieChart=(PieChart)findViewById(R.id.piechart);
         pieData= new PieData(getXValues(),getYValues());
         pieChart.setData(pieData);
         pieChart.setHoleRadius(45);
         pieChart.setCenterText("chart");
         pieChart.setCenterTextSize(25);
         pieChart.setContentDescription("Usage Summary");//setdescription
         pieChart.animateY(3000);
    }

    private void percentage() {
         total=(whatsapp_t+facebook_t+snapchat_t+youtube_t+instagram_t+pubg_t);
         percentage[0]=(whatsapp_t*100)/total;
         percentage[1]=(facebook_t*100)/total;
         percentage[2]=(snapchat_t*100)/total;
         percentage[3]=(youtube_t*100)/total;
         percentage[4]=(instagram_t*100)/total;
         percentage[5]=(pubg_t*100)/total;


    }
    private IPieDataSet getYValues()
    {
        PieDataSet pieDataSet;

        ArrayList<Entry> entries= new ArrayList<>();
        for (int i=0;i<percentage.length;i++)
        {
        entries.add(new Entry(percentage[i],i));
        }
        pieDataSet = new PieDataSet(entries,"Time Consumption");
        ArrayList<Integer> colors =new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);

        pieDataSet.setColors(colors);
        pieDataSet.setSliceSpace(8);
        return pieDataSet;
    }
    private List<String> getXValues()
    {
        ArrayList<String> xvalues= new ArrayList<>();
        xvalues.add("WhatsApp");
        xvalues.add("Facebook");
        xvalues.add("Snapchat");
        xvalues.add("Youtube");
        xvalues.add("Instagram");
        xvalues.add("PUBG");
        return xvalues;




    }

}
