package com.sametal.za;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Sbusiso.
 */
public class NotificationCentre_Details extends Fragment implements AdapterView.OnItemSelectedListener {
    View rootView;
    FragmentManager fragmentManager;


    TextView txttasktype, txttaskdetail;
    Connection con;
    String un, pass, db, ip;
    Button btnThank;
    ListView lstgross;
    ImageView edtlogoImage;
    Bundle bundle;
    Bundle bundles = new Bundle();
    String selectedproid = "", selectedtaskid = "", selectedtype = "";
    MainActivity activity = MainActivity.instance;

    int userid;

    public NotificationCentre_Details() {

        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.notification_centre_detail, container, false);
        lstgross = (ListView) rootView.findViewById(R.id.lstgross);
        txttasktype = (TextView) rootView.findViewById(R.id.txttasktype);
        txttaskdetail = (TextView) rootView.findViewById(R.id.txttaskdetail);
        btnThank = (Button) rootView.findViewById(R.id.btn_thank);
        fragmentManager = getFragmentManager();


        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "Dotsprop";
        un = "sqaloits";
        pass = "422q5mfQzU";
        ConnectionClass cn = new ConnectionClass();
        con = cn.connectionclass(un, pass, db, ip);

        if (con == null) {
            Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
        }
        bundle = this.getArguments();
        try {
            if (bundle != null) {

                selectedtaskid = bundle.getString("selectedtaskid");
                selectedproid = bundle.getString("selectedproid");
                selectedtype = bundle.getString("selectedtype");

            }
        } catch (Exception ex) {

        }

        try {

            FillDataReleasePayment();

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
            Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
        }
        btnThank.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                    NotificationCentre fragment = new NotificationCentre();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }

            }
        });
        return rootView;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        //Toast.makeText(rootView.getContext(), "You've selected " + String.valueOf(position)+" "+pokeId+" "+l , Toast.LENGTH_LONG).show();
        // Log.d("POKEMON", "You've selected " + String.valueOf(position)+" "+pokeId+" "+l);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private View currentSelectedView;

    public void FillDataReleasePayment() {
        //==============Initialize list=
        try {
            String query = "select c.contactname,c.id as pro_id,upct.id as task_id,a.firstname,businessname,upct.tasktype ,upct.taskdesc,status,startdate,a.image as user_image,c.image as pro_image from [UserPropertyCostTask] upct\n" +
                    "inner join [Contractor] c on c.id=upct.controctorid\n" +
                    "inner join [AppUser] a on a.id=upct.userid\n" +
                    " where a.id=" + Integer.parseInt(activity.id) + " and c.id=" + selectedproid + " and upct.id=" + selectedtaskid;
            Log.d("ReminderService In", query);
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
                     while (rs.next()) {
                String status = rs.getString("status");

                if (selectedtype.contains("Reminder")) {
                    txttasktype.setText(selectedtype);
                    String dt="Hello "+rs.getString("firstname")+" your "+selectedtype.replace("Reminder","")+" schedule is due on:\nDate "+rs.getString("startdate")+"\n\n\n\nThank You";
txttaskdetail.setText(dt);
                } else {
                    txttasktype.setText("Service Request Acceptance");
                    String dt="Pro "+rs.getString("contactname")+" of "+rs.getString("businessname")+" has accepted your request for: "+selectedtype.replace("Acceptance","")+"\nDate "+rs.getString("startdate")+"\n\n\nThank You";
                    txttaskdetail.setText(dt);
                }


            }


        } catch (Exception ex) {
            //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", ex.getMessage().toString());
        }
//===========

    }


}








