package com.sametal.za;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserProfileAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private final ArrayList<String> itemname;
    private final ArrayList<String> itemusername;
    private final ArrayList<String> itemdepartment;


    public UserProfileAdapter(Activity context, ArrayList<String> name, ArrayList<String> username,
                              ArrayList<String> department) {
        super(context, R.layout.lsttemplateuserprofile, name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=name;
        this.itemusername=username;
        this.itemdepartment=department;


    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rootView=inflater.inflate(R.layout.lsttemplateuserprofile, null,true);
        TextView name,username,department;
        name =  (TextView) rootView.findViewById(R.id.lblname);
        username = (TextView) rootView. findViewById(R.id.lblusername);
        department = (TextView) rootView. findViewById(R.id.lbldepartment);



try{
    name.setText(itemname.get(position));
    username.setText(itemusername.get(position));
    department.setText(itemdepartment.get(position));

}catch (Exception ex){
   // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+" "+ String.valueOf(position), Toast.LENGTH_LONG).show();
    Log.d("ReminderService In", "#####"+ex.getMessage().toString());
}
     return rootView;

    };
}