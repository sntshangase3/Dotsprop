package com.sametal.za;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class FunctionsAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private final ArrayList<String> itemname;
    private final ArrayList<String> itemisystem;



    public FunctionsAdapter(Activity context, ArrayList<String> name, ArrayList<String> system               ) {
        super(context, R.layout.lsttemplatefunctions, name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=name;
        this.itemisystem =system;



    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rootView=inflater.inflate(R.layout.lsttemplatefunctions, null,true);
        TextView name,system;

        name =  (TextView) rootView.findViewById(R.id.lblname);
        system = (TextView) rootView. findViewById(R.id.lblsystem);




try{
    name.setText(itemname.get(position));
    system.setText(itemisystem.get(position));


}catch (Exception ex){
   // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+" "+ String.valueOf(position), Toast.LENGTH_LONG).show();
    Log.d("ReminderService In", "#####"+ex.getMessage().toString());
}
     return rootView;

    };
}