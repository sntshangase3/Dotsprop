package com.sametal.za;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;

public class CarWashFragAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private final ArrayList<String> itempaymentno;
    private final ArrayList<String> itemcustomername;
    private final ArrayList<String> itemtype;
    private final ArrayList<String> itemchangedby;
    private final ArrayList<String> itemtotal;
    private final ArrayList<String> itembranch;


    public CarWashFragAdapter(Activity context, ArrayList<String> payno, ArrayList<String> name, ArrayList<String> type,
                              ArrayList<String> total, ArrayList<String> changedby, ArrayList<String> branch) {
        super(context, R.layout.lsttemplateadvancedreport, name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itempaymentno=payno;

        this.itemcustomername=name;
        this.itemtype=type;
        this.itemchangedby=changedby;
        this.itemtotal=total;
        this.itembranch=branch;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rootView=inflater.inflate(R.layout.lsttemplateadvancedreport, null,true);


        TextView payno,name,type,total,changedby,branch;

        payno =  (TextView) rootView.findViewById(R.id.lblpayno);;
          name = (TextView) rootView. findViewById(R.id.lblname);

        type = (TextView) rootView. findViewById(R.id.lbltype);
        total = (TextView) rootView. findViewById(R.id.lbltotal);
        changedby = (TextView) rootView. findViewById(R.id.lblchangedby);
        branch = (TextView) rootView. findViewById(R.id.lblbranch);


try{
    payno.setText(itempaymentno.get(position));
    name.setText(itemcustomername.get(position));
    type.setText(itemtype.get(position));
    total.setText(itemtotal.get(position));
    changedby.setText(itemchangedby.get(position));
    branch.setText(itembranch.get(position));

}catch (Exception ex){
   // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+" "+ String.valueOf(position), Toast.LENGTH_LONG).show();
    Log.d("ReminderService In", "#####"+ex.getMessage().toString());
}
     return rootView;

    };
}