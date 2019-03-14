package com.example.sibusiso_javapracticaltest;

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


public class PokemonAdapter extends  ArrayAdapter<String> {

    private final Activity context;

    MainActivity activity = MainActivity.instance;
    private final ArrayList<Integer> item_id;
    private final ArrayList<Bitmap> item_image;
    private final ArrayList<String> item_name;
    private final ArrayList<String> item_type;


    public PokemonAdapter(Activity context,ArrayList<Integer> item_id, ArrayList<Bitmap> image, ArrayList<String> name, ArrayList<String> type) {
        super(context, R.layout.pokemon_row, name);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.item_id = item_id;
        this.item_image = image;
        this.item_name = name;
        this.item_type = type;


    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.pokemon_row, null, true);

        ImageView image;
        TextView id,name,type;
        id = (TextView) rootView.findViewById(R.id.tv_id);
        image = (ImageView) rootView.findViewById(R.id.iv_pokemon);
        name = (TextView) rootView.findViewById(R.id.tv_name);
        type = (TextView) rootView.findViewById(R.id.tv_type);


        try {

            id.setText(item_id.get(position).toString());
            name.setText(item_name.get(position).toString());
            type.setText(item_type.get(position).toString());
            image.setImageBitmap(item_image.get(position));


        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage().toString() + " " + String.valueOf(position), Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }


        return rootView;

    }
}
