package com.sametal.za;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.util.ArrayList;

public class MyTaskAll_DetailAdapter extends ArrayAdapter<String> {

    private final Activity context;
    MainActivity activity = MainActivity.instance;

    private final ArrayList<String> itemtaskvalue;
    private final ArrayList<String> itemdescription;




    public MyTaskAll_DetailAdapter(Activity context, ArrayList<String> taskvalue, ArrayList<String> description) {
        super(context, R.layout.lsttemplatetaskall, description);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemtaskvalue = taskvalue;
        this.itemdescription = description;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.lsttemplatetaskall, null, true);


        TextView lbltaskvalue,lbldec;

        lbltaskvalue = (TextView) rootView.findViewById(R.id.lbltaskvalue);
        lbldec = (TextView) rootView.findViewById(R.id.lbldec);



        try {


            lbltaskvalue.setText(itemtaskvalue.get(position).toString());
            lbldec.setText(itemdescription.get(position).toString());




        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + " " + String.valueOf(position), Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }


        return rootView;

    }

    ;
}