package com.sametal.za;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.sametal.za.BookingRequestFrag.DATE_TIME_FORMAT;

/**
 * Created by Sbusiso.
 */
public class HomeContractor extends Fragment {
    private static final int REQUEST_CODE = 1;
    Button btnBook,btnCall;
    TextView txtbusinessname,txtcontactname,txttotalyears, txttotalrequest, txttotalemployees, txttprovince;
    ImageView  profilePic;
    View rootView;
    Bundle bundle;
    MainActivity activity =   MainActivity.instance;
    FragmentManager fragmentManager;

    String mobileno = "";

    Calendar date;
    //---------con--------
    Connection con;
    String un,pass,db,ip;
String selectedproid="";
    public HomeContractor(){

        super();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.homecontractor,
                container, false);


        btnBook = (Button) rootView.findViewById(R.id.btn_book);
        btnCall = (Button) rootView.findViewById(R.id.btn_call);
        txtbusinessname = (TextView) rootView.findViewById(R.id.txtbusinessname);
        txtcontactname = (TextView) rootView.findViewById(R.id.txtcontactname);
        txttotalyears = (TextView) rootView.findViewById(R.id.txttotalyears);
        txttotalrequest = (TextView) rootView.findViewById(R.id.txttotalrequest);
        txttotalemployees = (TextView) rootView.findViewById(R.id.txttotalemployees);
        txttprovince = (TextView) rootView.findViewById(R.id.txttprovince);

        profilePic = (ImageView) rootView.findViewById(R.id.profilePic);
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
            if (bundle != null) {

                if (!bundle.getString("selectedproid").toString().equals("")) {
                    selectedproid=bundle.getString("selectedproid");
                }
            }
        } catch (Exception ex) {

        }
        FillData();
        btnBook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                HomeContractor_Help fragment = new HomeContractor_Help();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent callnoIntent = new Intent(Intent.ACTION_CALL);
                    callnoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    callnoIntent.setData(Uri.parse("tel:" + mobileno));
                    getActivity().startActivity(callnoIntent);
                } catch (SecurityException ex) {
                    Toast.makeText(rootView.getContext(), "Tel/Cell No Invalid!!", Toast.LENGTH_LONG).show();
                }

            }
        });


        return rootView;
    }

    public void FillData() {
        //==============Fill Data=
        try {

            String query="";
            if(!selectedproid.equals("")){
                query = "select * from [Contractor] where [id]=" + Integer.parseInt(selectedproid);
            }else{
                query = "select * from [Contractor] where [id]=" + Integer.parseInt(activity.id);
                selectedproid=activity.id;
            }
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            rs.next();


            if (rs.getRow() != 0) {
mobileno=rs.getString("contact");
                txtcontactname.setText(rs.getString("contactname"));
                txtbusinessname.setText(rs.getString("businessname"));
                txttotalyears.setText(rs.getString("numberofserviceyears")+" Years");
                txttotalemployees.setText(rs.getString("numberofemployees")+" Employees");

                String query1 = "select count(controctorid) as request from UserPropertyCostTask\n" +
                        "where controctorid=" + selectedproid;
                PreparedStatement ps1 = con.prepareStatement(query1);
                ResultSet rs1 = ps1.executeQuery();
                rs1.next();
               String requestno= rs1.getString("request").toString();
               txttotalrequest.setText(requestno);

                txttprovince.setText(rs.getString("province"));
                 if (rs.getString("image") != null) {
                    byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                     //Set rounded corner
                     RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), decodebitmap);
                     roundedBitmapDrawable.setCornerRadius(50.0f);
                     roundedBitmapDrawable.setAntiAlias(true);
                     profilePic.setImageDrawable(roundedBitmapDrawable);

                } else {

                    profilePic.setImageDrawable(rootView.getResources().getDrawable(R.drawable.profilephoto));

                }

            }

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage().toString());
        }
//==========
    }
}
