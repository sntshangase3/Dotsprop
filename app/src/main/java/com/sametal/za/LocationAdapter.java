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



import java.util.ArrayList;


public class LocationAdapter extends  ArrayAdapter<String> {

    private final Activity context;

    MainActivity activity = MainActivity.instance;
    private final ArrayList<Integer> item_id;
    private final ArrayList<Bitmap> item_image;
    private final ArrayList<String> item_address;
    private final ArrayList<String> item_coordinates;


    public LocationAdapter(Activity context, ArrayList<Integer> item_id, ArrayList<Bitmap> image, ArrayList<String> address, ArrayList<String> coordinate) {
        super(context, R.layout.location_row, address);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.item_id = item_id;
        this.item_image = image;
        this.item_address = address;
        this.item_coordinates = coordinate;


    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.location_row, null, true);

        ImageView image;
        TextView id,address,coordinates;
        id = (TextView) rootView.findViewById(R.id.tv_id);
        image = (ImageView) rootView.findViewById(R.id.iv_location);
        address = (TextView) rootView.findViewById(R.id.tv_address);
        coordinates = (TextView) rootView.findViewById(R.id.tv_coordinates);


        try {

            id.setText(item_id.get(position).toString());
            address.setText(item_address.get(position).toString());
            coordinates.setText(item_coordinates.get(position).toString());
            image.setImageBitmap(item_image.get(position));


        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + " " + String.valueOf(position), Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }


        return rootView;

    }
}
