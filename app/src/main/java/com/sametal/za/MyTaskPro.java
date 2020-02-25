package com.sametal.za;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    ArrayList<Integer> item_serviceid = new ArrayList<Integer>();

    Connection con;
    String un, pass, db, ip,address1;
    TextView txttask;
    ListView lstgross;
    ImageView edtlogoImage;
    Bundle bundle;
    Bundle bundles = new Bundle();
    MainActivity activity = MainActivity.instance;

EditText edtsearch;
ImageButton search;
LinearLayout layoutsearch;
    int userid;

    public MyTaskPro() {

        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.mytaskpro, container, false);
        lstgross = (ListView) rootView.findViewById(R.id.lstgross);
        txttask = (TextView) rootView.findViewById(R.id.txttask);
        edtsearch = (EditText) rootView.findViewById(R.id.edtsearch);
        search = (ImageButton) rootView.findViewById(R.id.search);
        layoutsearch=(LinearLayout) rootView.findViewById(R.id.layoutsearch) ;
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
            String query = "select address from [UserProperty] where [userid]='" + activity.id + "'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                address1=rs.getString("address");

            }


if(bundle.getString("item_type")=="home"){
    layoutsearch.setVisibility(View.INVISIBLE);
    FillDataReleasePaymentAll();
}else if(bundle.getString("item_type")=="search"){
    String searchtx="";
    FillDataReleasePaymentAllSearch(searchtx);
    layoutsearch.setVisibility(View.VISIBLE);
}else{
    FillDataReleasePayment();
    layoutsearch.setVisibility(View.INVISIBLE);
}


        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());

        }
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchtx="\""+edtsearch.getText()+"\"";
               FillDataReleasePaymentAllSearch(searchtx);


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
            final String taskdec = bundle.getString("item_type");
            final String taskvalue = bundle.getString("selectedtaskvalue");
            txttask.setText(taskvalue.substring(14));

            item_proid.clear();
            item_dec.clear();
            item_profile.clear();

            String query = "SELECT *\n" +
                    "  FROM [Service] ";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            String desc = "";

            while (rs.next()) {

                if (taskdec.contains(rs.getString("service"))) {
                    String query11 = "SELECT *\n" +
                            "  FROM [Contractor] where service like '%"+rs.getString("service")+"%' ";
                    PreparedStatement ps11 = con.prepareStatement(query11);
                    ResultSet rs11 = ps11.executeQuery();
                    rs11.next();
                    if(rs11.getRow()>0){
                        Log.d("ReminderService In", rs.getString("id"));
                        item_serviceid.add(rs.getInt("id"));

                        int id = rs11.getInt("id");
                        if (!item_proid.contains(id)) {
                            String address = rs11.getString("address");
                            String query1 = "select count(controctorid) as request from UserPropertyCostTask\n" +
                                    "where controctorid=" + id;
                            PreparedStatement ps1 = con.prepareStatement(query1);
                            ResultSet rs1 = ps1.executeQuery();
                            rs1.next();

                            item_proid.add(String.valueOf(id));
                            desc = desc + rs11.getString("businessname").toString() + "\n" +
                                    rs1.getString("request").toString() + " Requests\n" +
                                    "Office " + distance(address,address1) + " Away";
                            item_dec.add(desc);

                            byte[] decodeString = Base64.decode(rs11.getString("image"), Base64.DEFAULT);
                            Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                            item_profile.add(decodebitmap);
                        }
                    }

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


                        final String selectedproid = item_proid.get(position);

                        String query = "SELECT *\n" +
                                "  FROM [Contractor] \n" +
                                " where  [id]='" + selectedproid + "'";
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {


                            if (currentSelectedView != null && currentSelectedView != view) {
                                unhighlightCurrentRow(currentSelectedView);
                            }
                            currentSelectedView = view;
                            highlightCurrentRow(currentSelectedView);

                            bundles.putString("selectedproid", selectedproid);
                            bundles.putString("item_type", taskdec);
                            bundles.putString("taskvalue", taskvalue);
                            bundles.putIntegerArrayList("serviceid", item_serviceid);
                            HomeContractor fragment = new HomeContractor();
                            fragment.setArguments(bundles);
                            fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                        }

                    } catch (Exception ex) {
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
    public void FillDataReleasePaymentAll() {
        //==============Initialize list=
        try {


           String query111 = "select upct.id as task_id,*  from [UserPropertyCostTask] upct\n" +
                    "                    inner join [Service] s on s.ID=upct.serviceid" +
                    " inner join [UserPropertyCostCategory] c on c.id=upct.propertycostcategoryid" +
                    " where  userid=" + activity.id;


        PreparedStatement ps111 = con.prepareStatement(query111);
        ResultSet rs111 = ps111.executeQuery();
        String item_taskall = "";
        ArrayList<String>proid=new ArrayList<>();
        while (rs111.next()) {
            if(!proid.contains(rs111.getString("controctorid"))&&rs111.getString("controctorid")!=null){
                proid.add(rs111.getString("controctorid"));
                item_taskall+=rs111.getString("service");
            }

        }
            final String taskdec = item_taskall;

            txttask.setText(proid.size()+" Pro Available");

            item_proid.clear();
            item_dec.clear();
            item_profile.clear();

            String query = "SELECT *\n" +
                    "  FROM [Service] ";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            String desc = "";

            while (rs.next()) {

                if (taskdec.contains(rs.getString("service"))) {
                    String query11 = "SELECT *\n" +
                            "  FROM [Contractor] where service like '%"+rs.getString("service")+"%' ";
                    PreparedStatement ps11 = con.prepareStatement(query11);
                    ResultSet rs11 = ps11.executeQuery();
                    rs11.next();
                    if(rs11.getRow()>0){
                        Log.d("ReminderService In", rs.getString("id"));
                        item_serviceid.add(rs.getInt("id"));

                        int id = rs11.getInt("id");
                        if (!item_proid.contains(id)) {
                            String address = rs11.getString("address");
                            String query1 = "select count(controctorid) as request from UserPropertyCostTask\n" +
                                    "where controctorid=" + id;
                            PreparedStatement ps1 = con.prepareStatement(query1);
                            ResultSet rs1 = ps1.executeQuery();
                            rs1.next();

                            item_proid.add(String.valueOf(id));
                            desc = desc + rs11.getString("businessname").toString() + "\n" +
                                    rs1.getString("request").toString() + " Requests\n" +
                                    "Office " + distance(address,address1) + " Away";
                            item_dec.add(desc);

                            byte[] decodeString = Base64.decode(rs11.getString("image"), Base64.DEFAULT);
                            Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                            item_profile.add(decodebitmap);
                        }
                    }

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


                        final String selectedproid = item_proid.get(position);

                        String query = "SELECT *\n" +
                                "  FROM [Contractor] \n" +
                                " where  [id]='" + selectedproid + "'";
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {


                            if (currentSelectedView != null && currentSelectedView != view) {
                                unhighlightCurrentRow(currentSelectedView);
                            }
                            currentSelectedView = view;
                            highlightCurrentRow(currentSelectedView);

                            bundles.putString("selectedproid", selectedproid);
                            bundles.putString("item_type", taskdec);
                            bundles.putString("taskvalue", "Pros");
                            bundles.putIntegerArrayList("serviceid", item_serviceid);
                            HomeContractor fragment = new HomeContractor();
                            fragment.setArguments(bundles);
                            fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                        }

                    } catch (Exception ex) {
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
    public void FillDataReleasePaymentAllSearch(String search) {
        //==============Initialize list=
        try {





            item_proid.clear();
            item_dec.clear();
            item_profile.clear();

            String query = "";
            if(!search.equals("")){
                query = "SELECT *\n" +
                        "  FROM [Contractor] \n" +
                        " where  contains([service],'" + search + "')";
                Log.d("ReminderService In", "SEARCH WITH VAlu"+query);
            }else{
                query = "SELECT *\n" +
                        "  FROM [Contractor] \n" ;
                Log.d("ReminderService In", "SEARCH NO VAlu"+query);
            }
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            String desc = "";

            while (rs.next()) {




                        item_serviceid.add(rs.getInt("id"));

                        int id = rs.getInt("id");
                       // if (!item_proid.contains(id)) {
                            String address = rs.getString("address");
                Log.d("ReminderService In", "ADRESSSS "+address+"  "+address1);
                            String query1 = "select count(controctorid) as request from UserPropertyCostTask\n" +
                                    "where controctorid=" + id;
                            PreparedStatement ps1 = con.prepareStatement(query1);
                            ResultSet rs1 = ps1.executeQuery();
                            rs1.next();

                            item_proid.add(String.valueOf(id));
                            desc = desc + rs.getString("businessname").toString() + "\n" +
                                    rs1.getString("request").toString() + " Requests\n" +
                                    "Office " + distance(address,address1) + " Away";
                            item_dec.add(desc);
desc="";
                            byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                            Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                            item_profile.add(decodebitmap);

                  //  }
            }
            txttask.setText(item_proid.size()+" Pro Available");

            MyTaskProAdapter adapter = new MyTaskProAdapter(this.getActivity(), item_profile, item_proid, item_dec);
            lstgross.setAdapter(adapter);
            lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {


                        final String selectedproid = item_proid.get(position);
                        final String taskdec = item_dec.get(position);

                        String query = "SELECT *\n" +
                                "  FROM [Contractor] \n" +
                                " where  [id]='" + selectedproid + "'";
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {


                            if (currentSelectedView != null && currentSelectedView != view) {
                                unhighlightCurrentRow(currentSelectedView);
                            }
                            currentSelectedView = view;
                            highlightCurrentRow(currentSelectedView);

                            bundles.putString("selectedproid", selectedproid);
                            bundles.putString("item_type", taskdec);
                            bundles.putString("taskvalue", "Pros");
                            bundles.putIntegerArrayList("serviceid", item_serviceid);
                            HomeContractor fragment = new HomeContractor();
                            fragment.setArguments(bundles);
                            fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                        }

                    } catch (Exception ex) {
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
    public  double distanceValue(double lat1,
                                  double lat2, double lon1,
                                  double lon2)
    {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r);
    }
    public String distance(String address,String address1) {
        String dist = "";
        try {


            GPSTracker mGPS = new GPSTracker(rootView.getContext());
            if (mGPS.canGetLocation) {

                Geocoder geocoder;
                geocoder = new Geocoder(rootView.getContext(), Locale.getDefault());
                double Lat = geocoder.getFromLocationName(address, 1).get(0).getLatitude();
                double Lon = geocoder.getFromLocationName(address, 1).get(0).getLongitude();

               Geocoder geocoder1;
                geocoder1 = new Geocoder(rootView.getContext(), Locale.getDefault());
                double Lat1= geocoder1.getFromLocationName(address1, 1).get(0).getLatitude();
                double Lon1 = geocoder1.getFromLocationName(address1, 1).get(0).getLongitude();


              /* mGPS.getLocation();
                double Lat1 = mGPS.getLatitude();
               double Lon1 = mGPS.getLongitude();*/

              /*  double distance;
                Location locationA = new Location("");
                locationA.setLatitude(Lat);
                locationA.setLongitude(Lon);

                Location locationB = new Location("");
                locationB.setLatitude(Lat1);
                locationB.setLongitude(Lon1);


                distance = locationA.distanceTo(locationB) / 1000; */  // in km
                dist = String.valueOf(Math.round(distanceValue(Lat,Lat1,Lon,Lon1) * 100) / 100) + "Km";
                // Get reults


            } else {
                Log.d("ReminderService In", "GPS OFF");
                mGPS.showSettingsAlert();
            }

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
        }
        return dist;
    }

    private void highlightCurrentRow(View rowView) {

        rowView.setBackgroundColor(getResources().getColor(R.color.focus_box_frame));

    }


    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);

    }
}








