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
public class NotificationCentre extends Fragment implements AdapterView.OnItemSelectedListener {
    View rootView;
    FragmentManager fragmentManager;


    ArrayList<RoundedBitmapDrawable> item_profile = new ArrayList<RoundedBitmapDrawable>();
    ArrayList<String> item_type = new ArrayList<String>();
    ArrayList<String> item_taskid = new ArrayList<String>();
    ArrayList<String> item_proid = new ArrayList<String>();
    TextView txtnotifications;
    Connection con;
    String un, pass, db, ip;
    Button btnThank;
    ListView lstgross;
    ImageView edtlogoImage;
    Bundle bundle;
    Bundle bundles = new Bundle();
    MainActivity activity = MainActivity.instance;

    int userid;

    public NotificationCentre() {

        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.notification_centre, container, false);
        lstgross = (ListView) rootView.findViewById(R.id.lstgross);
        txtnotifications = (TextView) rootView.findViewById(R.id.txtnotice1);
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
            if (activity.userstate == "C") {
                btnThank.setVisibility(View.GONE);
            }

            FillDataReleasePayment();

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
            Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
        }
        btnThank.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                    HomeFragment fragment = new HomeFragment();
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
            String query = "";
            if (activity.userstate == "O") {
                query = "select c.contactname,c.id as pro_id,upct.id as task_id,a.firstname,businessname,upct.tasktype ,upct.taskdesc,status,startdate,a.image as user_image,c.image as pro_image from [UserPropertyCostTask] upct\n" +
                        "inner join [Contractor] c on c.id=upct.controctorid\n" +
                        "inner join [AppUser] a on a.id=upct.userid\n" +
                        " where status='Accepted' and a.id=" + Integer.parseInt(activity.id);
            } else {
                query = "select c.contactname,c.id as pro_id,upct.id as task_id,a.firstname,businessname,upct.tasktype ,upct.taskdesc,status,startdate,a.image as user_image,c.image as pro_image from [UserPropertyCostTask] upct\n" +
                        "inner join [Contractor] c on c.id=upct.controctorid\n" +
                        "inner join [AppUser] a on a.id=upct.userid\n" +
                        " where status='New' and c.id=" + Integer.parseInt(activity.id);
            }
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            int total = 0;
            item_taskid.clear();
            item_type.clear();
            item_profile.clear();
            item_proid.clear();
            while (rs.next()) {
                String status = rs.getString("status");

                if (activity.userstate == "O") {
                    item_type.add(rs.getString("tasktype").substring(14) + " Reminder");
                    byte[] decodeString = Base64.decode(rs.getString("user_image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), decodebitmap);
                    roundedBitmapDrawable.setCornerRadius(50.0f);
                    roundedBitmapDrawable.setAntiAlias(true);
                    item_profile.add(roundedBitmapDrawable);
                    item_proid.add(rs.getString("pro_id"));
                    item_taskid.add(rs.getString("task_id"));
                    total=total+1;

                   // if (status.equals("Accepted")) {
                        item_type.add(rs.getString("tasktype").substring(14) + " Acceptance");
                        byte[] decodeString1 = Base64.decode(rs.getString("pro_image"), Base64.DEFAULT);
                        Bitmap decodebitmap1 = BitmapFactory.decodeByteArray(decodeString1, 0, decodeString1.length);
                        RoundedBitmapDrawable roundedBitmapDrawable1 = RoundedBitmapDrawableFactory.create(getResources(), decodebitmap1);
                        roundedBitmapDrawable1.setCornerRadius(50.0f);
                        roundedBitmapDrawable1.setAntiAlias(true);
                        item_profile.add(roundedBitmapDrawable1);
                        item_proid.add(rs.getString("pro_id"));
                        item_taskid.add(rs.getString("task_id"));
                        total=total+1;
                   // }
                } else {
                    item_type.add(rs.getString("tasktype").substring(14) + " Request");
                    byte[] decodeString = Base64.decode(rs.getString("user_image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), decodebitmap);
                    roundedBitmapDrawable.setCornerRadius(50.0f);
                    roundedBitmapDrawable.setAntiAlias(true);
                    item_profile.add(roundedBitmapDrawable);
                    item_proid.add(rs.getString("pro_id"));
                    item_taskid.add(rs.getString("task_id"));
                    total=total+1;
                }


            }
            txtnotifications.setText("New "+String.valueOf(total));


            MyTaskNotificationAdapter adapter = new MyTaskNotificationAdapter(this.getActivity(), item_profile, item_taskid, item_proid, item_type);
            lstgross.setAdapter(adapter);
            lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {


                        final String selectedproid = item_proid.get(position);
                        final String selectedtype = item_type.get(position);
                        final String selectedtaskid = item_taskid.get(position);


                        if (currentSelectedView != null && currentSelectedView != view) {
                            unhighlightCurrentRow(currentSelectedView);
                        }
                        currentSelectedView = view;
                        highlightCurrentRow(currentSelectedView);

                        bundles.putString("selectedproid", selectedproid);
                        bundles.putString("selectedtype", selectedtype);
                        bundles.putString("selectedtaskid", selectedtaskid);
                        if (activity.userstate == "O") {
                            NotificationCentre_Details fragment = new NotificationCentre_Details();
                            fragment.setArguments(bundles);
                            fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                        }else{
                            Notification_Pro_Request fragment = new Notification_Pro_Request();
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


    private void highlightCurrentRow(View rowView) {

        rowView.setBackgroundColor(getResources().getColor(R.color.focus_box_frame));

    }


    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);

    }
}








