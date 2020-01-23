package com.sametal.za;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Connection;
import java.util.ArrayList;

public class MyTaskAll_PhotoListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    MainActivity activity = MainActivity.instance;
    private final ArrayList<Bitmap> itemimage;
    private final ArrayList<String> itemname;


    Connection con;
    String un, pass, db, ip;

    public MyTaskAll_PhotoListAdapter(Activity context, ArrayList<String> name, ArrayList<Bitmap> image ) {
        super(context, R.layout.lsttemplateorderpointbundle, name);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemimage = image;

        this.itemname = name;



    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.lsttemplateorderpointbundle, null, true);


        TextView name = (TextView) rootView.findViewById(R.id.lblname);


        try {


            name.setText(itemname.get(position).toString());


            Bitmap back=itemimage.get(position);

            Drawable profile =new BitmapDrawable(rootView.getContext().getResources(),back);

          rootView.setBackground(profile);

        } catch (Exception ex) {
//            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + " " + String.valueOf(position), Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }


        return rootView;

    }

    ;
}