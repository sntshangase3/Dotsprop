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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

/**
 * Created by Sbusiso.
 */
public class Notification_Pro_Request extends Fragment {
    private static final int REQUEST_CODE = 1;
    Button btnAccept,btnDecline;
    TextView txtcontactname, txtcustomername, txtlocation, txtcontact, txtaddress, txttasktype,txtdesc,txtready;
    ImageView  profilePic;
    View rootView;
    Bundle bundle;
    MainActivity activity =   MainActivity.instance;
    FragmentManager fragmentManager;



    Calendar date;
    //---------con--------
    Connection con;
    String un,pass,db,ip;
String selectedproid="",selectedtaskid="",fulladdress_search="", status="";
    public Notification_Pro_Request(){

        super();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.notification_pro_request,
                container, false);


        btnAccept = (Button) rootView.findViewById(R.id.btn_accept);
        btnDecline = (Button) rootView.findViewById(R.id.btn_decline);
        txtready = (TextView) rootView.findViewById(R.id.txtready);
        txtcustomername = (TextView) rootView.findViewById(R.id.txtcustomername);
        txtcontactname = (TextView) rootView.findViewById(R.id.txtcontactname);
        txtlocation = (TextView) rootView.findViewById(R.id.txtlocation);
        txtcontact = (TextView) rootView.findViewById(R.id.txtcontact);
        txtaddress = (TextView) rootView.findViewById(R.id.txtaddress);
        txttasktype = (TextView) rootView.findViewById(R.id.txttasktype);
        txtdesc = (TextView) rootView.findViewById(R.id.txtdesc);

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

                    selectedtaskid=bundle.getString("selectedtaskid");
                    selectedproid=bundle.getString("selectedproid");

            }
        } catch (Exception ex) {

        }

        FillData();
        btnAccept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    try {
                            String commands = "update [UserPropertyCostTask] set [status]='Accepted' where [id]='" + selectedtaskid + "'";
                            PreparedStatement preStmt = con.prepareStatement(commands);
                            preStmt.executeUpdate();

                        Toast ToastMessage = Toast.makeText(rootView.getContext(),"Task Accepted!!!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_bground);
                        ToastMessage.show();
                        Notification_Pro_Request fragment = new Notification_Pro_Request();
                        fragment.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                    } catch (Exception ex) {
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }

            }
        });
        btnDecline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    String commands = "update [UserPropertyCostTask] set [status]='Declined' where [id]='" + selectedtaskid + "'";
                    PreparedStatement preStmt = con.prepareStatement(commands);
                    preStmt.executeUpdate();

                    Toast ToastMessage = Toast.makeText(rootView.getContext(),"Task Declined!!!", Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();
                    Notification_Pro_Request fragment = new Notification_Pro_Request();
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }

            }
        });
        profilePic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if(status.equals("Accepted")){
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
                    }else{
                        Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                        btnAccept.setBackground(errorbg);
                    }


                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }

            }
        });


        return rootView;
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
    public void FillData() {
        //==============Fill Data=
        try {

            String query = "select c.contactname,a.firstname,p.location,p.address,a.contact,p.address,upct.tasktype ,upct.taskdesc,status from [UserPropertyCostTask] upct\n" +
                    "inner join [Contractor] c on c.id=upct.controctorid\n" +
                    "inner join [AppUser] a on a.id=upct.userid\n" +
                    "inner join [UserProperty] p on p.userid=a.id where c.id=" + Integer.parseInt(selectedproid)+" and upct.id="+Integer.parseInt(selectedtaskid);
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            rs.next();

            if (rs.getRow() != 0) {
                 status=rs.getString("status");
                fulladdress_search = rs.getString("address").toString().replace(" ", "+");
                if(status.equals("Accepted")){
                    txtcontactname.setText("Hello Pro - "+rs.getString("contactname"));
                    txtcustomername.setText(rs.getString("firstname"));
                    txtlocation.setText(rs.getString("location"));
                    txtaddress.setText(rs.getString("address"));
                    txtdesc.setText(rs.getString("taskdesc"));
                    txtcontact.setText(rs.getString("contact"));
                    txttasktype.setText(rs.getString("tasktype"));
                    txtready.setVisibility(View.VISIBLE);

                }else{
                    String num=rs.getString("contact");
                    String numhide="";
                    for(int i=0 ;i<num.length();i++){
                        if((i>=0 && i<=3)||i==num.length()-1){
                            numhide=numhide+num.charAt(i);
                        }else{
                            numhide=numhide+'#';
                        }

                    }
                    txtcontactname.setText("Hello Pro - "+rs.getString("contactname"));
                    txtcustomername.setText(rs.getString("firstname"));
                    txtlocation.setText(rs.getString("location"));
                    // txtaddress.setText(rs.getString("address"));
                    txtdesc.setText(rs.getString("taskdesc"));
                    txtcontact.setText(numhide);
                    txttasktype.setText(rs.getString("tasktype"));
                    txtready.setVisibility(View.INVISIBLE);

                }


            }

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage().toString());
        }
//==========
    }

}
