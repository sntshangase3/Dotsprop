package com.sametal.za;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Owner on 2015-10-04.
 */
public class HomeOngoingProject extends Fragment {
    View rootView;


    Button btn_premium;
    MainActivity activity = MainActivity.instance;
    Connection con;
    String un, pass, db, ip;
    Bundle bundles = new Bundle();
    TextView txttasktotal,txttotalannualcost, txtpercent, txtprop;
    ImageView b1, b2,b3,b4, projectImages, home;
    LinearLayout layoutmyprojecttask;
    Bundle bundle;
int imageid=1;



    private SensorService mSensorService;
    public HomeOngoingProject() {

        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.homeongoingproject, container, false);

        b1 = (ImageView) rootView.findViewById(R.id.b1);
        b2 = (ImageView) rootView.findViewById(R.id.b2);
        b3 = (ImageView) rootView.findViewById(R.id.b3);
        b4 = (ImageView) rootView.findViewById(R.id.b4);
        home = (ImageView) rootView.findViewById(R.id.home);

        layoutmyprojecttask =(LinearLayout) rootView.findViewById(R.id.layoutmyprojecttask);


        txttasktotal = (TextView) rootView.findViewById(R.id.txttasktotal);
        txtprop = (TextView) rootView.findViewById(R.id.txtprop);
        txttotalannualcost = (TextView) rootView.findViewById(R.id.txttotalannualcost);
        txtpercent = (TextView) rootView.findViewById(R.id.txtpercent);

        projectImages = (ImageView) rootView.findViewById(R.id.projectImages);




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

        try {
            String query = "select id from [UserOnGoingProject] where [userid]='" + activity.id + "'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (rs.getRow() != 0) {
                activity.projectid=rs.getInt("id");
            }else{
                Toast.makeText(rootView.getContext(), "Add at least one Project!!!",Toast.LENGTH_LONG).show();
                Fragment fragment = new OnGoingProjectRegister();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
            }

        } catch (Exception ex) {

        }
        /*try {
            String query = "select projectid from [UserOnGoingProjectTask] where [userid]='" + activity.id + "'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (rs.getRow() != 0) {

                Fragment fragment = new OngoingProjectTaskListFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
            }else{
                Toast.makeText(rootView.getContext(), "Add at least one Project Task!!!",Toast.LENGTH_LONG).show();
                Fragment fragment = new OnGoingProjectTaskRegister();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
            }

        } catch (Exception ex) {

        }*/

        try {
            String query2 = "select * from [UserOnGoingProjectTask]  "+
                    " where userid=" + Integer.parseInt(activity.id);
            PreparedStatement ps2 = con.prepareStatement(query2);
            ResultSet rs2 = ps2.executeQuery();
            int total=0;
            while (rs2.next()) {

                total=total+1;


            }
            txttasktotal.setText(String.valueOf(total));

        } catch (Exception ex) {

        }
        try {
            String query = "select * from [UserOnGoingProjectTask] where taskapproved='Yes' and userid="+activity.id;
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            double TotalTask=Double.valueOf(txttasktotal.getText().toString());
            int total = 0;
            while (rs.next()) {

                total=total+1;
            }
            Log.d("ReminderService In", "DDD"+total+" "+TotalTask);
            double percent=Math.round((total/TotalTask)*100);

            txtpercent.setText(String.valueOf(percent)+"%");

        } catch (Exception ex) {
            Log.d("ReminderService In", "DDD"+ex.getMessage().toString());
        }

        try {
            String query = "select distinct taskbyid  from [UserOnGoingProjectTask] where userid="+activity.id;
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            int total = 0;
            while (rs.next()) {
                total=total+1;
            }
            txtprop.setText(String.valueOf(total));


        } catch (Exception ex) {
            Log.d("ReminderService In", "DDD"+ex.getMessage().toString());
        }

        try {
            String query = "select * from [UserOnGoingProjectTask] where userid="+activity.id;
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            int total = 0;
            while (rs.next()) {

                total = total + Integer.parseInt(rs.getString("cost"));
            }
            txttotalannualcost.setText("On-Going Project Cost R"+String.valueOf(total));

        } catch (Exception ex) {
            Log.d("ReminderService In", "DDD"+ex.getMessage().toString());
        }

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


        layoutmyprojecttask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String query = "select projectid from [UserOnGoingProjectTask] where [userid]='" + activity.id + "'";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    if (rs.getRow() != 0) {
                        activity.projectid=rs.getInt("projectid");
                        Fragment fragment = new OngoingProjectTaskListFrag();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                    }else{

                        Toast ToastMessage = Toast.makeText(rootView.getContext(), "Add at least one Project Task!!!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_bground);
                        ToastMessage.show();
                        Fragment fragment = new OnGoingProjectTaskRegister();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                    }

                } catch (Exception ex) {

                }

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new HomeFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, fragment).commit();
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String query = "select projectid from [UserOnGoingProjectTask] where [userid]='" + activity.id + "'";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    if (rs.getRow() != 0) {
                        activity.projectid=rs.getInt("projectid");
                        Fragment fragment = new OngoingProjectTaskListFrag();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                    }else{
                        Toast ToastMessage = Toast.makeText(rootView.getContext(), "Add at least one Project Task!!!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_bground);
                        ToastMessage.show();
                        Fragment fragment = new OnGoingProjectTaskRegister();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                    }

                } catch (Exception ex) {

                }



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

                Fragment frag = new ReportsBarChartOnGoingFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();


            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




            }
        });

        FillData(imageid);

       // AboutToExpiryNotification();
       // ClearOldBooking();

       // PushNotify();
        return rootView;
    }

    public void FillData(int imageid) {
        //==============Fill Data=
        try {

            String  query = "select image from [UserOnGoingProjectPhotos] where id='"+imageid+"' and [projectid]='" + activity.projectid+"'";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            rs.next();

            if (rs.getRow() != 0) {


                if (rs.getString("image") != null) {
                    byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    projectImages.setImageBitmap(decodebitmap);

                } else {

                    projectImages.setImageDrawable(rootView.getResources().getDrawable(R.drawable.profilephoto));

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
        HomeOngoingProject homeFragment = this;
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
