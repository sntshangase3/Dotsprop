package com.sametal.za;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyTaskNotificationAdapter extends ArrayAdapter<String> {

    private final Activity context;
    MainActivity activity = MainActivity.instance;
    private final ArrayList<RoundedBitmapDrawable> itemimage;
    private final ArrayList<String> itemid;
    private final ArrayList<String> itemdescription;
    private final ArrayList<String> itemproid;




    public MyTaskNotificationAdapter(Activity context, ArrayList<RoundedBitmapDrawable> image, ArrayList<String> id, ArrayList<String> proid, ArrayList<String> description) {
        super(context, R.layout.lsttemplatetasknotification, description);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemimage = image;
        this.itemid = id;
        this.itemproid=proid;
        this.itemdescription = description;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.lsttemplatetasknotification, null, true);

        ImageView logoimage;
        TextView lblid,lblproid,lbldec;
        logoimage = (ImageView) rootView.findViewById(R.id.lbllogoimage);
        lblid = (TextView) rootView.findViewById(R.id.lblid);
        lblproid = (TextView) rootView.findViewById(R.id.lblproid);
        lbldec = (TextView) rootView.findViewById(R.id.lbldescribtion);



        try {

            logoimage.setImageDrawable(itemimage.get(position));
            lblid.setText(itemid.get(position).toString());
            lblproid.setText(itemproid.get(position).toString());
            lbldec.setText(itemdescription.get(position).toString());




        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + " " + String.valueOf(position), Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }


        return rootView;

    }

    ;
}