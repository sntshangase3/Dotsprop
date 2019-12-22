package com.sametal.za;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;




import java.util.ArrayList;

public class BookingAllOrderAdapter extends ArrayAdapter<String> {

    private final Activity context;


    private final  ArrayList <String> itemid;
    private final  ArrayList <String> itemname;
    private final  ArrayList <String> itemdistance;
    private final  ArrayList <String> itemcollectiondate;
    private final  ArrayList <String> itemcollectionstatus;
    //private final  ArrayList <String> itemordercost;


    public BookingAllOrderAdapter(Activity context, ArrayList<String> id, ArrayList <String> name, ArrayList <String> itemdistance,
                                  ArrayList <String> collectdate, ArrayList <String> status) {
        super(context, R.layout.lsttemplatecollectionallrequest, name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemid=id;
        this.itemname=name;
        this.itemdistance=itemdistance;
        this.itemcollectiondate=collectdate;
        this.itemcollectionstatus=status;
       // this.itemordercost=cost;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rootView=inflater.inflate(R.layout.lsttemplatecollectionallrequest, null,true);


        TextView id,name,distance,collectdate,status;
        id = (TextView) rootView. findViewById(R.id.lblid);
          name = (TextView) rootView. findViewById(R.id.lblname);
        distance = (TextView) rootView. findViewById(R.id.lbldistance);
        collectdate = (TextView) rootView. findViewById(R.id.lblcollectiondate);
        status = (TextView) rootView. findViewById(R.id.lblstatus);



try{
    id.setText(itemid.get(position));
    name.setText(itemname.get(position));
    distance.setText(itemdistance.get(position));
    collectdate.setText(itemcollectiondate.get(position));
    status.setText(itemcollectionstatus.get(position));
   // cost.setText(itemordercost.get(position));

}catch (Exception ex){
    Toast.makeText(rootView.getContext(), ex.getMessage().toString()+" "+String.valueOf(position),Toast.LENGTH_LONG).show();
    Log.d("ReminderService In", ex.getMessage().toString());
}
     return rootView;

    };
}