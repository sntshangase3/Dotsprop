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

public class RolesAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private final ArrayList<String> itemname;
    private final ArrayList<Boolean> itemisselected;



    public RolesAdapter(Activity context, ArrayList<String> name, ArrayList<Boolean> isselected) {
        super(context, R.layout.lsttemplateroles, name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=name;
        this.itemisselected=isselected;



    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rootView=inflater.inflate(R.layout.lsttemplateroles, null,true);
        TextView name;
        CheckBox chkisselected;
        name =  (TextView) rootView.findViewById(R.id.lblname);
        chkisselected = (CheckBox) rootView. findViewById(R.id.chkisselected);




try{
    name.setText(itemname.get(position));
    chkisselected.setChecked(itemisselected.get(position));


}catch (Exception ex){
   // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+" "+ String.valueOf(position), Toast.LENGTH_LONG).show();
    Log.d("ReminderService In", "#####"+ex.getMessage().toString());
}
     return rootView;

    };
}