package com.sametal.za;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


/**
 * Created by sibusison on 2017/07/30.
 */
public class ReportsBarChartFrag extends Fragment {

    View rootView;

    //---------con--------
    Connection con;
    String un,pass,db,ip;
    MainActivity activity =   MainActivity.instance;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.reportbarchart, container, false);

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "Dotsprop";
        un = "sqaloits";
        pass = "422q5mfQzU";
        double total1=0;double total2=0;double total3=0;

        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {

                CharSequence text = "Error in internet connection!!";

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(rootView.getContext(), text, duration);
                toast.show();
            } else {



                String query11 = "select * from [UserPropertyCostTask] where [userid]=" + activity.edthidenuserid.getText().toString() ;
                PreparedStatement ps11 = con.prepareStatement(query11);
                ResultSet rs11 = ps11.executeQuery();
                while (rs11.next()) {


                    if(rs11.getString("status").equals("New")){
                        total1=total1+1;
                    }else if(rs11.getString("status").equals("Incomplete")){
                        total2=total2+1;
                    }else if(rs11.getString("status").equals("Complete")){
                        total3=total3+1;
                    }

                }



            }
        } catch (Exception ex) {
            // Toast.makeText(rootView.getContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
//===========



        BarChart chart = (BarChart) rootView.findViewById(R.id.barchart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        int index=0;
        entries.add(new BarEntry((float)total1, index));
        index++;
        entries.add(new BarEntry((float)total2, index));
        index++;
        entries.add(new BarEntry((float)total3, index));

       /* if((float)total1>0.0f){
            entries.add(new BarEntry((float)total1, index));
            index++;
        }
        if((float)total2>0.0f){
            entries.add(new BarEntry((float)total2, index));
            index++;
        }
        if((float)total3>0.0f){
            entries.add(new BarEntry((float)total3, index));
            index++;
        }*/



        BarDataSet dataset = new BarDataSet(entries, "Task Progress");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Not Started");
        labels.add("Incomplete");
        labels.add("Completed");
       /* if((float)total1>0.0f){
            labels.add("New");
        }
        if((float)total2>0.0f){
            labels.add("Accepted");
        }
        if((float)total3>0.0f){
            labels.add("Declined");
        }*/




        BarData data = new BarData(labels, dataset);

        chart.getXAxis().setLabelsToSkip(0);
        chart.setData(data);
        chart.setDrawGridBackground(false);
        chart.setDescription("Progress Report");
        chart.animateY(3000);



       /* 1
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));        // gap of 2f
        entries.add(new BarEntry(4f, 70f));
        entries.add(new BarEntry(5f, 60f));

        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        set.setDrawIcons(true);


        BarData data = new BarData(set,set);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh
        chart.setDrawGridBackground(false);*/

        //=========

       /* 2
        float barWidth = 0.9f; // x4 dataset
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();

            yVals1.add(new BarEntry(0f, 30f));
            yVals2.add(new BarEntry(1f, 80f));
            yVals3.add(new BarEntry(2f, 60f));
            yVals4.add(new BarEntry(3f, 50f));


        BarDataSet  set1 = new BarDataSet(yVals1, "Company A");
        set1.setColor(Color.rgb(104, 241, 175));
        set1.setDrawIcons(true);
        BarDataSet  set2 = new BarDataSet(yVals2, "Company B");
        set2.setColor(Color.rgb(164, 228, 251));
        BarDataSet  set3 = new BarDataSet(yVals3, "Company C");
        set3.setColor(Color.rgb(242, 247, 158));
        BarDataSet set4 = new BarDataSet(yVals4, "Company D");
        set4.setColor(Color.rgb(255, 102, 0));

        BarData data = new BarData(set1, set2, set3, set4);
        data.setValueFormatter(new LargeValueFormatter());
        data.setBarWidth(barWidth);
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh
        chart.setDrawGridBackground(false);


        chart.getXAxis().setDrawGridLines(false);

        //==========

        chart.animateXY(2000, 2000);
        chart.invalidate();*/

        return rootView;
    }






}


