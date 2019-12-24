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

    TextView txtnotifications,txtaddress,txttotalannualcost,txtwelcomename;
    ImageView b1, b2,b3,propertyprofileImage, edtprofileImage,home,setting;
    Bundle bundle;
Spinner spinnerprppertyname;
    int propertyexist=0;
    ArrayAdapter adapter;
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
        spinnerprppertyname = (Spinner) rootView.findViewById(R.id.spinnerprppertyname);


        txtnotifications = (TextView) rootView.findViewById(R.id.txtnotice1);
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
            String query2 = "select * from [UserProperty]";
            PreparedStatement ps2 = con.prepareStatement(query2);
            ResultSet rs2 = ps2.executeQuery();
            int total=0;
            while (rs2.next()) {
                total=total+1;
            }
            txtnotifications.setText(String.valueOf(total));

        } catch (Exception ex) {

        }
        try {
            String query;
            if (activity.position.equals("carwash")) {
                query = "select  id from [Collection] where Datediff(D,convert(varchar,getDate(),112),Cast([collectionsdate] as date))>=0 and [collectionstatus]='New' and [carwashid]='" + activity.id+"'";
            } else {
                query = "select  id from [Collection] where Datediff(D,convert(varchar,getDate(),112),Cast([collectionsdate] as date))>=0 and [carownerid]=" + activity.id;
            }
            PreparedStatement ps1 = con.prepareStatement(query);
            ResultSet rs1 = ps1.executeQuery();
            int total = 0;
            while (rs1.next()) {
                total = total + 1;
            }
            txtnotifications.setText(String.valueOf(total));

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage() + "######");
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

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    Fragment fragment = new BookingRequestFrag();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();



            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment frag = new CarWashFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();


            }
        });
        if (this.activity.hometimes == 0) {
            new UpdateRemainDays().execute(new String[]{""});
            this.activity.hometimes++;
            Log.d("ReminderService In", "First time from Home");
        } else {
            this.activity.hometimes++;
            Log.d("ReminderService In", "From Some where");
        }

        LoadProfilePicture();
        AboutToExpiryNotification();
        ClearOldBooking();

       // PushNotify();
        return rootView;
    }
    public void FillPropertyData() {
        //==============Fill Data=
        try {

            String query = "select * from [UserProperty] where [userid]='" + activity.id + "'";
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
                txttotalannualcost.setText(rs.getString("paymentamount").trim());

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
                sb.append("select id,Datediff(D,convert(varchar,getDate(),112),Cast([collectionsdate] as date)) as daysleft from [Collection]");
                ResultSet rsa = this.con.prepareStatement(sb.toString()).executeQuery();
                while (rsa.next()) {
                    if (Integer.parseInt(rsa.getString("daysleft").toString()) <= -2) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("delete from [Collection]  where [id]='");
                        sb2.append(rsa.getString("id").toString());
                        sb2.append("'");
                        this.con.prepareStatement(sb2.toString()).executeUpdate();
                    }
                }
                Log.d("Remindersaervice In", "ClearOldBooking 2 old ");

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

                // String to = "sibusison@sqaloitsolutions.co.za";

                String from = "info@sqaloitsolutions.co.za";

                StringBuilder sb2 = new StringBuilder();
                sb2.append("select id,Datediff(D,convert(varchar,getDate(),112),Cast([collectionsdate] as date)) as daysleft from [Collection] where [carownerid]='");
                sb2.append(activity.id);
                sb2.append("' and [isread]='No' and  Datediff(D,convert(varchar,getDate(),112),Cast([collectionsdate] as date))>=0");
                ResultSet rs = homeFragment.con.prepareStatement(sb2.toString()).executeQuery();
                while (rs.next()) {
                    String notificationbody = "";
                    if (Integer.parseInt(rs.getString("daysleft").toString()) == 0) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(notificationbody);
                        sb3.append("Car wash due...");

                        sb3.append("\n");
                        String notificationbody2 = sb3.toString();
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("update [Collection] set [isread]='Yes' where [id]='");
                        sb4.append(rs.getString("id").toString());
                        sb4.append("'");
                        String commands = sb4.toString();
                        homeFragment.con.prepareStatement(commands).executeUpdate();

                        PendingIntent pendingIntent = PendingIntent.getActivity(homeFragment.rootView.getContext(), 0, notificationIntent2, PendingIntent.FLAG_ONE_SHOT);

                        builder2.setAutoCancel(false);
                        builder2.setTicker("Car wash due today");
                        builder2.setSmallIcon(R.drawable.logos);
                        builder2.setContentIntent(pendingIntent);
                        builder2.setContentTitle("Car wash due today");
                        builder2.setContentText(notificationbody2);
                        Notification myNotication = builder2.setStyle(new Notification.BigTextStyle().bigText(notificationbody2)).build();
                        myNotication.defaults |= 2;
                        mgr2.notify(0, myNotication);
                        String subject = "Car wash due today";

                        try {
                            StringBuilder sb5 = new StringBuilder();

                            try {
                                sb5.append("Dear ");
                                sb5.append(activity.firstname);
                                sb5.append("\nCar wash due today:\n");
                                sb5.append(notificationbody2);
                                sb5.append("\nLogin on your app and check.\n\n------\nRegards - MyCarWash App\n\nThis email was intended for & sent to you by MyCarWash App\nA platform of Sqalo IT Solutions\n1 Wemyss St,Brooklyn,7405,Cape Town");
                                String message = sb5.toString();
                                String[] toArr = {activity.email};

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

    private class UpdateRemainDays extends AsyncTask<String, Integer, Void> {
        Boolean isSuccess=false;
        private ProgressDialog progressDialog;
        private String z="";

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.progressDialog = ProgressDialog.show(HomeFragment.this.rootView.getContext(), "Please wait...", "Updating Booking Days Left", true, false);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void aVoid) {
            this.progressDialog.dismiss();
            if (!this.isSuccess.booleanValue()) {
                Toast.makeText(HomeFragment.this.rootView.getContext(), this.z, Toast.LENGTH_LONG).show();
            }
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(String... params) {

            String daysleftmessage2;
            try {

                    ConnectionClass cn=new ConnectionClass();
                    con =cn.connectionclass(un, pass, db, ip);
                    StringBuilder sb = new StringBuilder();
                    if (activity.position.equals("carwash")){
                        sb.append("select id ,Datediff(D,convert(varchar,getDate(),112),Cast([collectionsdate] as date)) as daysleft from [Collection] where [carwashid]='");
                    }else{
                        sb.append("select id ,Datediff(D,convert(varchar,getDate(),112),Cast([collectionsdate] as date)) as daysleft from [Collection] where [carownerid]='");
                    }
                    sb.append(activity.id);
                    sb.append("' and  Datediff(D,convert(varchar,getDate(),112),Cast([collectionsdate] as date))>=0 order by id asc");
                    ResultSet rs1 = HomeFragment.this.con.prepareStatement(sb.toString()).executeQuery();
                    while (rs1.next()) {

                        int daysleft = Integer.parseInt(rs1.getString("daysleft").toString());
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(rs1.getString("id").toString());
                        sb2.append("###Remain Days");
                        sb2.append(daysleft);
                        Log.d("ReminderService In", sb2.toString());
                        if (daysleft <= 0) {
                            daysleftmessage2 = "0 Days";
                        } else {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(daysleft);
                            sb3.append(" Days");
                            daysleftmessage2 = sb3.toString();
                        }
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("update [Collection] set [daysleft]='");
                        sb4.append(daysleftmessage2);
                        sb4.append("' where [id]='");
                        sb4.append(rs1.getString("id").toString());
                        sb4.append("'");
                        HomeFragment.this.con.prepareStatement(sb4.toString()).executeUpdate();
                    }

                this.isSuccess = Boolean.valueOf(true);
            } catch (Exception e) {
                this.isSuccess = Boolean.valueOf(false);
                Log.d("ReminderService In", e.getMessage());
                this.z = "Check your network connection!!!";
            }
            return null;
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
