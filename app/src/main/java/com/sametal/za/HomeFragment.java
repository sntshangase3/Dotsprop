package com.sametal.za;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.util.Log;
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
import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Owner on 2015-10-04.
 */
public class HomeFragment extends Fragment {
    View rootView;


    Button btn_premium;
    MainActivity activity = MainActivity.instance;
    Connection con;
    String un, pass, db, ip;
    Bundle bundles = new Bundle();
    TextView txtnotifications,txtaddress,txttotalannualcost,txtwelcomename,txtmaintance;
    ImageView b1, b2,b3,propertyprofileImage, edtprofileImage,home,setting;
    LinearLayout layouthomeownership,layoutmaintanance,layoutongoingproject;
    Bundle bundle;
Spinner spinnerprppertyname;
    int propertyexist=0;
    ArrayAdapter adapter;
    public String address;
    private SensorService mSensorService;
    public HomeFragment() {

        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_homee, container, false);

        b1 = (ImageView) rootView.findViewById(R.id.b1);
        b2 = (ImageView) rootView.findViewById(R.id.b2);
        b3 = (ImageView) rootView.findViewById(R.id.b3);
        home = (ImageView) rootView.findViewById(R.id.home);
        setting = (ImageView) rootView.findViewById(R.id.setting);
        spinnerprppertyname = (Spinner) rootView.findViewById(R.id.spinnerprppertyname);
        layouthomeownership =(LinearLayout) rootView.findViewById(R.id.layouthomeownership);
        layoutmaintanance =(LinearLayout) rootView.findViewById(R.id.layoutmaintanance);
        layoutongoingproject =(LinearLayout) rootView.findViewById(R.id.layoutongoingproject);

        txtnotifications = (TextView) rootView.findViewById(R.id.txtnotice1);
        txtmaintance = (TextView) rootView.findViewById(R.id.txtmaintance);
        txtaddress = (TextView) rootView.findViewById(R.id.txtaddress);
        txttotalannualcost = (TextView) rootView.findViewById(R.id.txttotalannualcost);
        txtwelcomename = (TextView) rootView.findViewById(R.id.txtwelcomename);
        propertyprofileImage = (ImageView) rootView.findViewById(R.id.propertyprofileImage);
        edtprofileImage = (ImageView) rootView.findViewById(R.id.profileImage);



        //fragment_mapy.edthidenuserid.getText().toString();

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
        FillPropertyData();
        txtwelcomename.setText("Welcome \n"+activity.firstname);



        try {
            String query2 = "select * from [UserPropertyCostTask] upct "+
                    " where status='Accepted' and upct.userid=" + Integer.parseInt(activity.id);
            PreparedStatement ps2 = con.prepareStatement(query2);
            ResultSet rs2 = ps2.executeQuery();
            int total=0;
            while (rs2.next()) {

                total=total+1;
                total=total+1;

            }
            txtnotifications.setText(String.valueOf(total));

        } catch (Exception ex) {

        }
        int total = 0;
        try {
            String query = "select * from [UserPropertyCostTask] where userid="+activity.id;
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            int tota2 = 0;
            while (rs.next()) {
                total = total + Integer.parseInt(rs.getString("cost"));
                Log.d("ReminderService In", "UserPropertyCostTask"+total);
                tota2=tota2+1;
            }
            txtmaintance.setText(String.valueOf(tota2));


        } catch (Exception ex) {
            Log.d("ReminderService In", "DDD"+ex.getMessage().toString());
        }
        //All HomeOwnership Sum

        try {
            String query = "select * from [UserOnGoingProjectTask] where userid="+activity.id;
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            Log.d("ReminderService In", query);
            while (rs.next()) {
                total = total + Integer.parseInt(rs.getString("cost"));
                Log.d("ReminderService In", "UserOnGoingProjectTask"+total);
            }


        } catch (Exception ex) {
            Log.d("ReminderService In", "DDD"+ex.getMessage().toString());
        }

        txttotalannualcost.setText("Total annual cost R" + String.valueOf(total));

        try {
            if(isGoogleMapsInstalled())
            {
                try {


                    try {
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=1+Poole+St,+Brooklyn,+Cape+Town,+7405,+South+Africa");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        // startActivity(mapIntent);
                    } catch (Exception ex) {
                        Toast.makeText(rootView.getContext(), "Google Maps Not Found...", Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception ex) {
                    Toast.makeText(rootView.getContext(), "Enable Device GPS...", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                builder.setMessage("Install Google Maps");
                builder.setCancelable(false);
                builder.setPositiveButton("Install", getGoogleMapsListener());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        } catch (Exception ex) {

        }

        spinnerprppertyname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                // item.toString()
                if(!spinnerprppertyname.getSelectedItem().toString().equals("Select Property")){
                    try{

                        FillData(spinnerprppertyname.getSelectedItem().toString());
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
        layouthomeownership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new HomeOwnershipFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

            }
        });
        layoutmaintanance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MyTaskAllFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

            }
        });
        layoutongoingproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new HomeOngoingProject();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new RegisterPropertyFrag();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, fragment).commit();
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new RegisterPropertyFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, fragment).commit();
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Fragment fragment = new NotificationCentre();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment frag = new MyTaskPro();
               bundles.putString("item_type","home");
                frag.setArguments(bundles);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();


            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment frag = new ReportsBarChartFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();


            }
        });


        LoadProfilePicture();
        AboutToExpiryNotification();
        ClearOldBooking();

       // PushNotify();
        return rootView;
    }
    public void FillPropertyData() {
        //==============Fill Data=
        try {

            String query = "select location from [UserProperty] where [userid]='" + activity.id + "'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();
            category.add("Select Property");
            while (rs.next()) {
                category.add(rs.getString("location"));
                propertyexist=propertyexist+1;
            }
            adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerprppertyname.setAdapter(adapter);
            if(propertyexist!=0){
                spinnerprppertyname.setVisibility(View.VISIBLE);
                spinnerprppertyname.setSelection(1);
                FillData(spinnerprppertyname.getSelectedItem().toString());
            }

        } catch (Exception ex) {
            // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here",Toast.LENGTH_LONG).show();
        }
//==========
    }
    public void FillData(String location) {
        //==============Fill Data=
        try {

            String  query = "select  * from [UserProperty] where [location]='" + location+"'";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            rs.next();

            if (rs.getRow() != 0) {

                txtaddress.setText(rs.getString("address").trim());
address=rs.getString("address").trim();
                ArrayAdapter adapter1 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.status_arrays, R.layout.spinner_item);
                adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);

                spinnerprppertyname.setSelection(adapter.getPosition(location));
                if (rs.getString("image") != null) {
                    byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    propertyprofileImage.setImageBitmap(decodebitmap);

                } else {

                    propertyprofileImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.profilephoto));

                }

            }

        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }
//==========
    }
    public boolean isGoogleMapsInstalled()
    {
        try
        {
            ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }
    public DialogInterface.OnClickListener getGoogleMapsListener()
    {
        return new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en"));
                startActivity(intent);

                //Finish the activity so they can't circumvent the check
                // finish();
            }
        };
    }
    public void LoadProfilePicture(){
        try {


            //====Update frofile pic from co-user
            String queryprofile = "select * from [AppUser] where [id]= '" + activity.id + "'";
            PreparedStatement psp = con.prepareStatement(queryprofile);
            ResultSet rsp = psp.executeQuery();
            rsp.next();
            if (rsp.getRow() != 0) {
                byte[] decodeString = Base64.decode(rsp.getString("image"), Base64.DEFAULT);
                Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                //Set rounded corner
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), decodebitmap);
                roundedBitmapDrawable.setCornerRadius(50.0f);
                roundedBitmapDrawable.setAntiAlias(true);
                edtprofileImage.setImageDrawable(roundedBitmapDrawable);
            }




        } catch (Exception ex) {
            //  Toast.makeText(rootView.getContext(), ex.getMessage()+"Here",Toast.LENGTH_LONG).show();
            Log.d("ReminderService In", ex.getMessage());
        }
    }
    public void ClearOldBooking() {
        try {

                StringBuilder sb = new StringBuilder();
                sb.append("select id,Datediff(D,convert(varchar,getDate(),112),Cast([enddate] as date)) as daysleft from [UserPropertyCostTask] where status='Declined'");
                ResultSet rsa = this.con.prepareStatement(sb.toString()).executeQuery();
                while (rsa.next()) {
                    Log.d("Remindersaervice In", "######"+rsa.getString("daysleft"));
                    if (Integer.parseInt(rsa.getString("daysleft").toString()) <= -2) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("delete from [UserPropertyCostTask]  where [id]='");
                        sb2.append(rsa.getString("id").toString());
                        sb2.append("'");
                       this.con.prepareStatement(sb2.toString()).executeUpdate();

                        sb2.append("delete from [UserPropertyCostTaskPhotos]  where [taskid]='");
                        sb2.append(rsa.getString("id").toString());
                        sb2.append("'");
                        this.con.prepareStatement(sb2.toString()).executeUpdate();
                    }
                }


        } catch (Exception e) {
        }
    }




    public void AboutToExpiryNotification() {
        HomeFragment homeFragment = this;
        try {
            if (!homeFragment.activity.edthidenuserid.getText().toString().equals("")) {
                Log.d("ReminderService In", "IN@@@@@@@@@@ CORE USER About to expire items");
                NotificationManager mgr2 = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                Intent notificationIntent2 = new Intent(homeFragment.rootView.getContext(), SplashFragment.class);
                Notification.Builder builder2 = new Notification.Builder(homeFragment.rootView.getContext());

               //  String to = "sibusison@sqaloitsolutions.co.za";

                String from = "info@sqaloitsolutions.co.za";

                StringBuilder sb2 = new StringBuilder();
                sb2.append("select id,taskdesc,Datediff(D,convert(varchar,getDate(),112),Cast([startdate] as date)) as daysleft from [UserPropertyCostTask] where [userid]='");
                sb2.append(activity.id);
                sb2.append("' and [isread]='No' and status='Accepted' and  Datediff(D,convert(varchar,getDate(),112),Cast([startdate] as date))>=0");
                ResultSet rs = homeFragment.con.prepareStatement(sb2.toString()).executeQuery();
                while (rs.next()) {
                    String notificationbody = "";
                    if (Integer.parseInt(rs.getString("daysleft").toString()) == 0) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(notificationbody);
                        sb3.append("Dotsprop task due...");

                        sb3.append("\n");
                        String notificationbody2 = sb3.toString();
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("update [UserPropertyCostTask] set [isread]='Yes' where [id]='");
                        sb4.append(rs.getString("id").toString());
                        sb4.append("'");
                        String commands = sb4.toString();
                        homeFragment.con.prepareStatement(commands).executeUpdate();

                        PendingIntent pendingIntent = PendingIntent.getActivity(homeFragment.rootView.getContext(), 0, notificationIntent2, PendingIntent.FLAG_ONE_SHOT);

                        builder2.setAutoCancel(false);
                        builder2.setTicker("Dotsprop task...");
                        builder2.setSmallIcon(R.drawable.logos);
                        builder2.setContentIntent(pendingIntent);
                        builder2.setContentTitle(rs.getString("taskdesc"));
                        builder2.setContentText(notificationbody2);
                        Notification myNotication = builder2.setStyle(new Notification.BigTextStyle().bigText(notificationbody2)).build();
                        myNotication.defaults |= 2;
                        mgr2.notify(0, myNotication);
                        String subject = "Dotsprop task: "+rs.getString("taskdesc").toString();

                        try {
                            StringBuilder sb5 = new StringBuilder();

                            try {
                                sb5.append("Dear ");
                                sb5.append(activity.firstname);
                                sb5.append("\n\nDotsprop task due today: "+rs.getString("taskdesc"));
                                sb5.append("\nLogin on your app and check.\n\n------\nRegards - Dotsprop App\n\nThis email was intended for & sent to you by Dotsprop App\nA platform of Digicom\n27 Limoniet Rd, Croydon,Gauteng - Kempton Park");
                                String message = sb5.toString();
                                String[] toArr = {activity.email};
                               // String[] toArr = {to};

                                try {

                                    try {
                                        Mail m = new Mail("Info@sqaloitsolutions.co.za", "Mgazi@251085");
                                        m.setTo(toArr);
                                        m.setFrom(from);
                                        m.setSubject(subject);
                                        m.setBody(message);
                                        m.send();
                                    } catch (Exception e) {

                                    }
                                } catch (Exception e2) {


                                }
                            } catch (Exception e3) {


                            }
                        } catch (Exception e4) {


                        }
                    }

                }

            }
        } catch (Exception ex) {
            StringBuilder sb7 = new StringBuilder();
            sb7.append("Timerrrrr&&&&&&&&&&&&&");
            sb7.append(ex.getMessage());
            Log.d("ReminderService In", sb7.toString());
        }
    }



    public void PushNotify() {
        try {
            NotificationManager mgr = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
            Intent notificationIntent = new Intent(this.rootView.getContext(), SplashFragment.class);
            StringBuilder sb = new StringBuilder();
            sb.append("select * from [Collection] where [carownerid]='");
            sb.append( activity.edthidenuserid.getText().toString());
            sb.append("' and [isread]='No'");
            ResultSet rs1 = this.con.prepareStatement(sb.toString()).executeQuery();
            String notificationbody1 = "";
            while (rs1.next()) {
                String status2 = rs1.getString("orderstatus").toString();
                if (status2.equals("Prep")) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Order is being prepared\n");
                    sb2.append("E.T.D "+rs1.getString("prepdate").toString());
                    notificationbody1 = sb2.toString();
                } else if (status2.equals("Transit")) {
                    notificationbody1 = "Order is on the way";
                } else if (status2.equals("Delivered")) {
                    notificationbody1 = "Order is right at your doorstep";
                } else if (status2.equals("Accepted")) {
                    notificationbody1 = "Thank you for ordering with us\nWe hope you enjoyed the delivery";
                } else if (status2.equals("Rejected")) {
                    notificationbody1 = "Order is Rejected";
                }
                if (!notificationbody1.equals("")) {
                    Log.d("ReminderService In", notificationbody1);
                    PendingIntent pendingIntent1 = PendingIntent.getActivity(this.rootView.getContext(), 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
                    Notification.Builder builder = new Notification.Builder(this.rootView.getContext());
                    builder.setAutoCancel(false);
                    builder.setTicker("Order Status");
                    builder.setSmallIcon(R.drawable.logos);
                    builder.setContentIntent(pendingIntent1);
                    builder.setContentTitle(getResources().getString(R.string.notify_new_task_title));
                    builder.setContentText(notificationbody1);
                    Notification myNotication1 = builder.setStyle(new Notification.BigTextStyle().bigText(notificationbody1)).build();
                    myNotication1.defaults |= 2;
                    mgr.notify(0, myNotication1);
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("update [Collection] set [isread]='Yes' where [id]='");
                    sb3.append(rs1.getString("id").toString());
                    sb3.append("'");
                    this.con.prepareStatement(sb3.toString()).executeUpdate();
                }
            }
        } catch (Exception ex) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(ex.getMessage());
            sb4.append("######");
            Log.d("ReminderService In", sb4.toString());
        }
    }



}
