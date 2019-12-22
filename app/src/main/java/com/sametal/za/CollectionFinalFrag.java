package com.sametal.za;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by sibusison on 2017/07/30.
 */
public class CollectionFinalFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;




    //---------con--------
    Connection con;
    String un, pass, db, ip,deliverydaydate,selecteddeliverycost, day,hour,minute,fulladdress_search,price, selecteddriver,latlon;



    int qty, collectionid,selectedquantity;
    String userid;
    MainActivity activity =   MainActivity.instance;
    FragmentManager fragmentManager;

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";

    Calendar c;
    TextView txtcustomername,txtreference,txtlocation,txtdistance,txtdriverassigned, collectpreptxt, collecttransittxt, sametaldeliveredtxt,txttwarning,txtback;
    String mobileno = "",status="";
    Button accept,reject;
    ImageView callno, map, collectprep, collecttransit, sametaldelivered, collectionaccept, collectionreject, viewcollection,warning;
    Bundle bundle;
    Bundle bundles = new Bundle();
    LinearLayout layoutcollectionstatus;
    Spinner spinnerdriver;
    ArrayAdapter adapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.collectionfinal, container, false);


        txtcustomername = (TextView) rootView. findViewById(R.id.txtcustomername);
        txtreference = (TextView) rootView. findViewById(R.id.txtreference);
        txtlocation = (TextView) rootView.findViewById(R.id.txtlocation);
        txtdistance = (TextView) rootView. findViewById(R.id.txtdistance);
        txtdriverassigned = (TextView) rootView. findViewById(R.id.txtdriverassigned);
        collectpreptxt = (TextView) rootView.findViewById(R.id.collectpreptxt);
        collecttransittxt = (TextView) rootView. findViewById(R.id.collecttransittxt);
        sametaldeliveredtxt = (TextView) rootView. findViewById(R.id.sametaldeliveredtxt);
        txtback = (TextView) rootView. findViewById(R.id.txtback);
        txttwarning = (TextView) rootView.findViewById(R.id.txttwarning);


        callno = (ImageView) rootView.findViewById(R.id.callno);
        map = (ImageView) rootView.findViewById(R.id.map);

        collectprep = (ImageView) rootView.findViewById(R.id.collectprep);
        collecttransit = (ImageView) rootView.findViewById(R.id.ordertransit);
        sametaldelivered = (ImageView) rootView.findViewById(R.id.sametaldelivered);

        collectionaccept = (ImageView) rootView.findViewById(R.id.collectionaccept);
        collectionreject = (ImageView) rootView.findViewById(R.id.collectionreject);
        accept = (Button)rootView. findViewById(R.id.btn_accept);
        reject = (Button)rootView. findViewById(R.id.btn_decline);


        warning = (ImageView) rootView.findViewById(R.id.warning);
        viewcollection = (ImageView) rootView.findViewById(R.id.vieworder);
        layoutcollectionstatus = (LinearLayout) rootView.findViewById(R.id.layoutcollectionstatus);
        spinnerdriver = (Spinner) rootView. findViewById(R.id.spinnermember);

        fragmentManager = getFragmentManager();
        bundle = this.getArguments();
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



        collectionid = Integer.parseInt(bundle.getString("id"));
        FillData(collectionid);

        spinnerdriver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                // item.toString()
                if(!spinnerdriver.getSelectedItem().toString().equals("Assign Driver")){
                    try{
                        selecteddriver =spinnerdriver.getSelectedItem().toString();
                        ConnectionClass cn = new ConnectionClass();
                        con = cn.connectionclass(un, pass, db, ip);
                        String commands = "update [Collection] set [sadriverid]='"+ selecteddriver +"' where [id]='" + collectionid + "'";
                        PreparedStatement preStmt = con.prepareStatement(commands);
                        preStmt.executeUpdate();
                        Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                        txtdriverassigned.setBackground(errorbg);

                        bundles.putString("id", String.valueOf(collectionid));
                        bundles.putString("name", txtcustomername.getText().toString());
                        CollectionFinalFrag fragment = new CollectionFinalFrag();
                        fragment.setArguments(bundles);
                        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                    } catch (Exception ex) {
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });
        FillDriverData();

        collectionaccept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                try {
                    Date today = new Date();
                    SimpleDateFormat date_format = new SimpleDateFormat(DATE_TIME_FORMAT);
                    String todaydate = date_format.format(today);
                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    String commands = "update [Collection] set [collectionstatus]='Accepted',[collectdate]='"+todaydate+"' where [id]='" + collectionid + "'";
                    PreparedStatement preStmt = con.prepareStatement(commands);
                    preStmt.executeUpdate();



                    Toast ToastMessage = Toast.makeText(rootView.getContext(),"Collection Accepted!!!", Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();

                    BookingAllOrdersFrag fragment = new BookingAllOrdersFrag();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }


            }


        });
        collectionreject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Date today = new Date();
                    SimpleDateFormat date_format = new SimpleDateFormat(DATE_TIME_FORMAT);
                    String todaydate = date_format.format(today);
                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    String commands = "update [Collection] set [collectionstatus]='Rejected',[collectdate]='"+todaydate+"' where [id]='" + collectionid + "'";
                    PreparedStatement preStmt = con.prepareStatement(commands);
                    preStmt.executeUpdate();

                    Toast ToastMessage = Toast.makeText(rootView.getContext(),"Collection Rejected!!!", Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();

                    BookingAllOrdersFrag fragment = new BookingAllOrdersFrag();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }


            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                    try {

                        Date today = new Date();
                        SimpleDateFormat date_format = new SimpleDateFormat(DATE_TIME_FORMAT);
                        String todaydate = date_format.format(today);
                        ConnectionClass cn = new ConnectionClass();
                        con = cn.connectionclass(un, pass, db, ip);
                        String commands = "update [Collection] set [collectionstatus]='Accepted',[collectdate]='"+todaydate+"' where [id]='" + collectionid + "'";
                        PreparedStatement preStmt = con.prepareStatement(commands);
                        preStmt.executeUpdate();

                        Toast ToastMessage = Toast.makeText(rootView.getContext(),"Collection Accepted!!!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_bground);
                        ToastMessage.show();

                        BookingAllOrdersFrag fragment = new BookingAllOrdersFrag();
                        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                    } catch (Exception ex) {
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }


        }


    });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    Date today = new Date();
                    SimpleDateFormat date_format = new SimpleDateFormat(DATE_TIME_FORMAT);
                    String todaydate = date_format.format(today);
                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    String commands = "update [Collection] set [collectionstatus]='Rejected',[collectdate]='"+todaydate+"' where [id]='" + collectionid + "'";
                    PreparedStatement preStmt = con.prepareStatement(commands);
                    preStmt.executeUpdate();

                    Toast ToastMessage = Toast.makeText(rootView.getContext(),"Collection Rejected!!!", Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();

                    BookingAllOrdersFrag fragment = new BookingAllOrdersFrag();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }


            }
        });

        callno.setOnClickListener(new View.OnClickListener() {
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
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGoogleMapsInstalled()) {
                    try {
                       if (!fulladdress_search.equals("")) {
                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + fulladdress_search);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            getActivity().startActivity(mapIntent);
                        } else {

                            try {
                                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + fulladdress_search);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            } catch (Exception ex) {
                                Toast.makeText(rootView.getContext(), "Address/Google Maps Not Found...", Toast.LENGTH_LONG).show();
                            }
                        }

                    } catch (Exception ex) {
                        Toast.makeText(rootView.getContext(), "Enable Device GPS...", Toast.LENGTH_LONG).show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                    builder.setMessage("Install Google Maps");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Install", getGoogleMapsListener());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });
        collectprep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    Date today = new Date();
                    SimpleDateFormat date_format = new SimpleDateFormat(DATE_TIME_FORMAT);
                    String todaydate = date_format.format(today);

                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    String commands = "update [Collection] set [collectionstatus]='Prep',[prepdate]='"+todaydate+"',[transitdate]='#####',[deliverdate]='#####' where [id]='" + collectionid + "'";
                    PreparedStatement preStmt = con.prepareStatement(commands);
                    preStmt.executeUpdate();

                    Toast ToastMessage = Toast.makeText(rootView.getContext(),"Collection in Preparation!!!", Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();

                    bundles.putString("id", String.valueOf(collectionid));
                    bundles.putString("name", txtcustomername.getText().toString());
                    CollectionFinalFrag fragment = new CollectionFinalFrag();
                    fragment.setArguments(bundles);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }


            }
        });
        collecttransit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    Date today = new Date();
                    SimpleDateFormat date_format = new SimpleDateFormat(DATE_TIME_FORMAT);
                    String todaydate = date_format.format(today);

                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    String commands = "update [Collection] set [collectionstatus]='Transit',[transitdate]='"+todaydate+"',[deliverdate]='#####' where [id]='" + collectionid + "'";
                    PreparedStatement preStmt = con.prepareStatement(commands);
                    preStmt.executeUpdate();

                    Toast ToastMessage = Toast.makeText(rootView.getContext(),"Collection in Transit!!!", Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();

                    bundles.putString("id", String.valueOf(collectionid));
                    bundles.putString("name", txtcustomername.getText().toString());
                    CollectionFinalFrag fragment = new CollectionFinalFrag();
                    fragment.setArguments(bundles);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }


            }
        });
        sametaldelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    Date today = new Date();
                    SimpleDateFormat date_format = new SimpleDateFormat(DATE_TIME_FORMAT);
                    String todaydate = date_format.format(today);

                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    String commands = "update [Collection] set [collectionstatus]='Delivered',[deliverdate]='"+todaydate+"' where [id]='" + collectionid + "'";
                    PreparedStatement preStmt = con.prepareStatement(commands);
                    preStmt.executeUpdate();

                    Toast ToastMessage = Toast.makeText(rootView.getContext(),"Collection Delivered!!!", Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();

                    bundles.putString("id", String.valueOf(collectionid));
                    bundles.putString("name", txtcustomername.getText().toString());
                    CollectionFinalFrag fragment = new CollectionFinalFrag();
                    fragment.setArguments(bundles);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }


            }
        });
        warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                   final CharSequence[] items = {"No Reported Possible Delay",
                    "Possible Delay Due To Bad Weather",
                    "Possible Delay Due To Vehicle Breakdown",
                    "Possible Delay Due To Traffic Congestion",
                    "Possible Delay Due To Collection Address",
                    "No Response At Collection Address",
                    "Sorry, We Do not Collect In Your Area"};
                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                        TextView title = new TextView(rootView.getContext());
                        title.setPadding(10, 10, 10, 10);
                        title.setText("Select Warning");
                        title.setGravity(Gravity.CENTER);
                        title.setTextColor(getResources().getColor(R.color.colorPrimary));
                        builder.setCustomTitle(title);
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try{
                                    selecteddriver =items[which].toString();
                                    ConnectionClass cn = new ConnectionClass();
                                    con = cn.connectionclass(un, pass, db, ip);
                                    String commands = "update [Collection] set [warning]='"+ selecteddriver +"' where [id]='" + collectionid + "'";
                                    PreparedStatement preStmt = con.prepareStatement(commands);
                                    preStmt.executeUpdate();

                                    Toast ToastMessage = Toast.makeText(rootView.getContext(),"Collection Warning Updated!!!", Toast.LENGTH_LONG);
                                    View toastView = ToastMessage.getView();
                                    toastView.setBackgroundResource(R.drawable.toast_bground);
                                    ToastMessage.show();
                                    bundles.putString("id", String.valueOf(collectionid));
                                    bundles.putString("name", txtcustomername.getText().toString());
                                    CollectionFinalFrag fragment = new CollectionFinalFrag();
                                    fragment.setArguments(bundles);
                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                            } catch (Exception ex) {
                                Log.d("ReminderService In", ex.getMessage().toString());
                            }
                            }

                        });
                        builder.show();


                    } catch (Exception ex) {
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }


                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }


            }
        });
        viewcollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {

                    bundles.putString("id", String.valueOf(collectionid));
                    bundles.putString("name", txtcustomername.getText().toString());
                    BookingAllOrdersFrag fragment = new BookingAllOrdersFrag();
                    fragment.setArguments(bundles);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }


            }
        });

        txtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   try {

                       bundles.putString("id", String.valueOf(collectionid));
                       bundles.putString("name", txtcustomername.getText().toString());
                       BookingAllOrdersFrag fragment = new BookingAllOrdersFrag();
                    fragment.setArguments(bundles);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });
        return rootView;

}

    public void FillDriverData() {
        //==============Fill Data=
        try {
            ip = "winsqls01.cpt.wa.co.za";
            db = "SqaloITSolutionsTest";
            un = "sqaloits";
            pass = "422q5mfQzU";
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            String query = "select [Driver] from [Driver]";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();
            category.add("Assign Driver");
            while (rs.next()) {
                category.add(rs.getString("Driver"));
            }
            adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerdriver.setAdapter(adapter);

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
        }
//==========
    }
    public boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public DialogInterface.OnClickListener getGoogleMapsListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en"));
                startActivity(intent);

                //Finish the activity so they can't circumvent the check
                // finish();
            }
        };
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private View currentSelectedView;
    public void FillData( int id) {
        //==============Fill Data=FillScheduleData

        try {
            ip = "winsqls01.cpt.wa.co.za";
            db = "SqaloITSolutionsTest";
            un = "sqaloits";
            pass = "422q5mfQzU";
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {

                CharSequence text = "Check your network connection!!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(rootView.getContext(), text, duration);
                toast.show();
            } else {



                if (activity.position.equals("carwash")) {
                    layoutcollectionstatus.setVisibility(View.VISIBLE);
                } else {
                    layoutcollectionstatus.setVisibility(View.GONE);
                }

                String query = "select  * from [Collection] where id = '" + id + "'";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                 while (rs.next()) {

                     status=rs.getString("collectionstatus");

                     txtreference.setText("Your Collection #:"+rs.getString("ref"));
                     txtlocation.setText(rs.getString("location"));
                     txtdistance.setText("Distance:"+rs.getString("collectiondistance"));
                     txtdriverassigned.setText("Driver Assigned:"+rs.getString("sadriverid"));
                     txtcustomername.setText(rs.getString("customername"));

                     collectpreptxt.setText(rs.getString("prepdate"));
                     collecttransittxt.setText(rs.getString("transitdate"));
                     sametaldeliveredtxt.setText(rs.getString("deliverdate"));

                     txttwarning.setText(rs.getString("warning"));
                     fulladdress_search = rs.getString("location").toString().replace(" ", "+");


                }
            }

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage().toString());
        }

    }
}


