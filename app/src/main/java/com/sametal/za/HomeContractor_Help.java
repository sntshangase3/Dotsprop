package com.sametal.za;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.sametal.za.BookingRequestFrag.DATE_TIME_FORMAT;

/**
 * Created by Sbusiso.
 */
public class HomeContractor_Help extends Fragment {
    private static final int REQUEST_CODE = 1;
    ImageView btnstart,btnend;
    Button btn_send;
    TextView txttasktype,txtdesc,txt_start, txt_end;
    ImageView  profilePic;
    View rootView;
    Bundle bundle;
    MainActivity activity =   MainActivity.instance;



    Calendar date;
    //---------con--------
    Connection con;
    String un,pass,db,ip, startdate="", enddate="",selectedproid="",tasktype,desc;

    public HomeContractor_Help(){

        super();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.homecontractor_help,
                container, false);


        btnstart = (ImageView) rootView.findViewById(R.id.btn_start);
        btnend = (ImageView) rootView.findViewById(R.id.btn_end);
        txttasktype = (TextView) rootView.findViewById(R.id.txttasktype);
        txtdesc = (TextView) rootView.findViewById(R.id.txtdesc);
        txt_start = (TextView) rootView.findViewById(R.id.txt_start);
        txt_end = (TextView) rootView.findViewById(R.id.txt_end);

        btn_send = (Button) rootView.findViewById(R.id.btn_send);

        profilePic = (ImageView) rootView.findViewById(R.id.profilePic);

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
                    tasktype=bundle.getString("taskvalue");
                    ArrayList<String> taskdec = bundle.getStringArrayList("item_dec");
                    String item="";
                    for (String tsk : taskdec) {
                      item=item+tsk+"\n";
                    }
                    desc=item;

                }
            }
        } catch (Exception ex) {

        }
        FillData();
        btnstart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                 showDateTimePicker();


            }
        });
        btnend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateTimePicker2();

            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    if(startdate.equals("")){
                        Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                        btnstart.setBackground(errorbg);
                    }else if(enddate.equals("")) {
                        Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                        btnend.setBackground(errorbg);
                    }else{
                    ArrayList<String> taskdec = bundle.getStringArrayList("item_dec");
                    for (String tsk : taskdec) {
                        String query1 = "select id from Service\n" +
                                "where service='" + tsk+"'";
                        Log.d("ReminderService In", query1);
                        PreparedStatement ps1 = con.prepareStatement(query1);
                        ResultSet rs1 = ps1.executeQuery();
                        rs1.next();
                        int serviceid=rs1.getInt("id");
                        String commands = "update [UserPropertyCostTask] set [status]='New', [startdate]='"+startdate+"',[enddate]='"+enddate+"',controctorid='"+Integer.parseInt(selectedproid)+"' where [serviceid]='" + serviceid + "'";
                        PreparedStatement preStmt = con.prepareStatement(commands);
                        preStmt.executeUpdate();
                        Toast ToastMessage = Toast.makeText(rootView.getContext(),"Task Sent!!!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_bground);
                        ToastMessage.show();
                        Fragment fragment = new HomeFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                    }
                    }
                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }

            }
        });



        return rootView;
    }
    public void showDateTimePicker() {

        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(rootView.getContext(),R.style.DialogDate1, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(rootView.getContext(),R.style.DialogDate1, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        SimpleDateFormat date_format = new SimpleDateFormat(DATE_TIME_FORMAT);

                        try {
                            startdate = date_format.format(date.getTime());
                            txt_start.setText(startdate);
                            Log.d("ReminderService In", "#######1" + startdate);

                        } catch (Exception e) {
                            Log.d("ReminderService In", e.getMessage().toString());
                        }
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
    public void showDateTimePicker2() {

        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(rootView.getContext(),R.style.DialogDate2, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(rootView.getContext(),R.style.DialogDate2, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        SimpleDateFormat date_format = new SimpleDateFormat(DATE_TIME_FORMAT);

                        try {
                            enddate = date_format.format(date.getTime());
                            txt_end.setText(enddate);
                            Log.d("ReminderService In", "#######2" + enddate);

                        } catch (Exception e) {
                            Log.d("ReminderService In", e.getMessage().toString());
                        }
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
    public void FillData() {
        //==============Fill Data=
        try {



                txttasktype.setText(tasktype);
                txtdesc.setText(desc);



        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage().toString());
        }
//==========
    }
}
