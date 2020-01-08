package com.sametal.za;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AddTaskAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private final ArrayList<String> itemtaskname;
    private final ArrayList<String> itemprice;



    public AddTaskAdapter(Activity context, ArrayList<String> name, ArrayList<String> price) {
        super(context, R.layout.lsttemplatepropertytask, name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemtaskname =name;
        this.itemprice =price;



    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rootView=inflater.inflate(R.layout.lsttemplatepropertytask, null,true);
        TextView name,price;
        name =  (TextView) rootView.findViewById(R.id.txttaskname);
        price = (TextView) rootView. findViewById(R.id.edtcost);




try{
    name.setText(itemtaskname.get(position));
    price.setText(itemprice.get(position));


}catch (Exception ex){
   // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+" "+ String.valueOf(position), Toast.LENGTH_LONG).show();
    Log.d("ReminderService In", "#####"+ex.getMessage().toString());
}
     return rootView;

    };
}