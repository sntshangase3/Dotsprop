package com.sametal.za;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;




import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Sbusiso.
 */
public class MyTaskPro extends Fragment implements AdapterView.OnItemSelectedListener {
    View rootView;
    FragmentManager fragmentManager;



    ArrayList<String> item_proid = new ArrayList<String>();
    ArrayList<Bitmap> item_profile = new ArrayList<Bitmap>();
    ArrayList<String> item_dec = new ArrayList<String>();

    Connection con;
    String un, pass, db, ip;
TextView txttask;
    ListView lstgross;
    ImageView edtlogoImage;
    Bundle bundle;

    MainActivity activity = MainActivity.instance;

    int userid;

    public MyTaskPro() {

        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.prolist, container, false);
        lstgross = (ListView) rootView.findViewById(R.id.lstgross);
        txttask = (TextView) rootView.findViewById(R.id.txttask);
        fragmentManager = getFragmentManager();


        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "Dotsprop";
        un = "sqaloits";
        pass = "422q5mfQzU";
        ConnectionClass cn=new ConnectionClass();
        con =cn.connectionclass(un, pass, db, ip);

        if (con == null)
        {
            Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
        }
        bundle = this.getArguments();


        try {

FillDataReleasePayment();

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
            Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
        }

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
ArrayList<String> task=bundle.getStringArrayList("item_taskall");
for(String tsk:task){
    String query = "SELECT *\n" +
            "  FROM [Contractor] c\n" +
            "where  contains([service] ,'" + tsk + "')";

    PreparedStatement ps = con.prepareStatement(query);
    ResultSet rs = ps.executeQuery();
    item_proid.clear();
    item_dec.clear();
    item_profile.clear();
    String desc="";
    while (rs.next()) {
int id=rs.getInt("id");
String address=rs.getString("businessname");
        String query1 = "select count(controctorid) as request from UserPropertyCostTask\n" +
                "where controctorid="+id;
        PreparedStatement ps1 = con.prepareStatement(query1);
        ResultSet rs1 = ps1.executeQuery();
        rs1.next();
        item_proid.add(String.valueOf(id));
        desc=desc+rs.getString("businessname").toString()+"\n"+
                rs1.getString("request").toString()+" Requests\n"+
                "Office "+distance(address)+" Away";
        item_dec.add(desc);
        byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        item_profile.add(decodebitmap);
    }
}





            MyTaskProAdapter adapter = new MyTaskProAdapter(this.getActivity(), item_profile, item_proid, item_dec);
            lstgross.setAdapter(adapter);
            lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {

/*
                        final String selecteddesc = item_dec.get(position);
                        String query = "SELECT * FROM [AdvanceAdjustments] p " +
                                " where [Status] ='A' and  AdvanceNumber='" + selectedpayno +"'";
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();
                        Log.d("ReminderService In",query);
                        while (rs.next()) {


                                if (currentSelectedView != null && currentSelectedView != view) {
                                    unhighlightCurrentRow(currentSelectedView);
                                }
                                currentSelectedView = view;
                                highlightCurrentRow(currentSelectedView);

                                bundles.putString("desc", selecteddesc);
                                HomeOwnershipFrag fragment = new HomeOwnershipFrag();
                                fragment.setArguments(bundles);
                                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();




                        }
                        */
                    } catch(Exception ex) {
                        //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }

                }


            });

        } catch (Exception ex) {
            //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", ex.getMessage().toString());
        }
//===========

    }
    public String distance(String address) {
        String dist="";
        try {


                GPSTracker mGPS = new GPSTracker(rootView.getContext());
                if (mGPS.canGetLocation) {

                    Geocoder geocoder;
                    geocoder = new Geocoder(rootView.getContext(), Locale.getDefault());
                    double Lat = geocoder.getFromLocationName(address, 1).get(0).getLatitude();
                    double Lon = geocoder.getFromLocationName(address, 1).get(0).getLongitude();
                    mGPS.getLocation();
                    double Lat1 = mGPS.getLatitude();
                    double Lon1 = mGPS.getLongitude();

                    double distance;
                    Location locationA = new Location("");
                    locationA.setLatitude(Lat);
                    locationA.setLongitude(Lon);

                    Location locationB = new Location("");
                    locationB.setLatitude(Lat1);
                    locationB.setLongitude(Lon1);


                    distance = locationA.distanceTo(locationB) / 1000;   // in km
                    dist=String.valueOf((double) Math.round(distance)) + "Km";
                    // Get reults


                } else {
                    Log.d("ReminderService In", "GPS OFF");
                    mGPS.showSettingsAlert();
                }

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
        }
        return  dist;
    }
    private void highlightCurrentRow(View rowView) {

        rowView.setBackgroundColor(getResources().getColor(R.color.focus_box_frame));

    }


    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);

    }
}








