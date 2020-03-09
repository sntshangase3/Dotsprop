package com.sametal.za;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.sametal.za.BookingRequestFrag.DATE_TIME_FORMAT;


/**
 * Created by sibusiso
 */
public class OnGoingProjectTaskRegister extends Fragment {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip,startdate="";
    Calendar date;

    Button btncreate,btnsave;
    MainActivity activity = MainActivity.instance;
    ImageView back,prev,forward,delete,callno,email,map;

    FragmentManager fragmentManager;

    Spinner spinnerprojectname,spinnercategory,spinnerservice,spinnertaskby;
    EditText edttitle, edttask,edtplace, edtdate,  edtcost;
    TextView txttasklist;
    int propertyexist=0;
    int proid=0;
    int taskid=0;
    int id=0;
    CheckBox b1,b2,b3;
    String  selectedtaskvalue="";
    ArrayAdapter adapter;
    Bundle bundle;
    ArrayList<Integer> item_taskid = new ArrayList<Integer>();
    ArrayList<Integer> item_serviceid = new ArrayList<Integer>();
    String contact="",contacttname="",businessname="",address="",emailto="";
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.ongoingprojecttaskregister, container, false);
        back = (ImageView) rootView.findViewById(R.id.back);
        prev = (ImageView) rootView.findViewById(R.id.prev);
        forward = (ImageView) rootView.findViewById(R.id.forward);

        callno = (ImageView) rootView.findViewById(R.id.callno);
        email = (ImageView) rootView.findViewById(R.id.email);
        map = (ImageView) rootView.findViewById(R.id.map);

        delete = (ImageView) rootView.findViewById(R.id.delete);
        txttasklist = (TextView) rootView.findViewById(R.id.txttasklist);
        edttitle = (EditText) rootView.findViewById(R.id.edttitle);
        edttask = (EditText) rootView.findViewById(R.id.edttask);
        edtplace = (EditText) rootView.findViewById(R.id.edtplace);
        edtdate = (EditText) rootView.findViewById(R.id.edtdate);
        edtcost = (EditText) rootView.findViewById(R.id.edtcost);

        b1 = (CheckBox) rootView.findViewById(R.id.b1);
        b2 = (CheckBox) rootView.findViewById(R.id.b2);
        b3 = (CheckBox) rootView.findViewById(R.id.b3);

       btncreate = (Button) rootView.findViewById(R.id.btn_create);
        btnsave = (Button) rootView.findViewById(R.id.btn_save);
        spinnerprojectname = (Spinner) rootView.findViewById(R.id.spinnerprojectname);
        spinnercategory = (Spinner) rootView.findViewById(R.id.spinnercategory);
        spinnerservice = (Spinner) rootView.findViewById(R.id.spinnerservice);
        spinnertaskby = (Spinner) rootView.findViewById(R.id.spinnertaskby);

       // spinnerservice.setEnabled(false);
        //spinnerservice.setClickable(false);

        fragmentManager = getFragmentManager();
        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "Dotsprop";
        un = "sqaloits";
        pass = "422q5mfQzU";

        ConnectionClass cn = new ConnectionClass();
        con = cn.connectionclass(un, pass, db, ip);

        FillCategoryData();
        FillProData();
        FillProjectData();
      // FillServiceAll();
        bundle = this.getArguments();





        spinnerservice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);

                    try{

                   //FillData(spinnerservice.getSelectedItem().toString());
                    } catch (Exception ex) {
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });
        spinnertaskby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);

                try{
//proid=spinnertaskby.getSelectedItemPosition();
                    //FillData(spinnerservice.getSelectedItem().toString());
                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });
        spinnercategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                // item.toString()
               if(!spinnercategory.getSelectedItem().toString().equals("Select Category...")){
                    try{
                       FillServiceData(spinnercategory.getSelectedItemPosition());
                       spinnerservice.setSelection(item_serviceid.get(taskid)-1);

                        if (edttitle.getText().toString().trim().equals("")|| edttask.getText().toString().trim().equals("")||  edtplace.getText().toString().trim().equals("")|| edttitle.getText().toString().trim().equals("")){
                            Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground1);
                            spinnerservice.setBackground(errorbg);
                            spinnerservice.performClick();

                        }

                       // FillData(spinnercategory.getSelectedItem().toString());
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
        try {
            if (bundle != null) {

                selectedtaskvalue = bundle.getString("selectedtaskvalue");

                String  query = "SELECT t.id as t_id,s.id as s_id,*\n" +
                        "                        FROM UserOnGoingProjectTask t\n" +
                        "                         inner join UserOngoingProjectService s on s.id=t.projectserviceid\n" +
                        "                         inner join [UserOnGoingProject] uogp on uogp.id=t.projectid" +
                        "   where [service]='" + selectedtaskvalue+"'";

                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                item_serviceid.clear();
                String tasklist="Task ";
                int i=0;
                while(rs.next()){
                    i++;
                    tasklist=tasklist+String.valueOf(i)+" - ";
                    item_serviceid.add(rs.getInt("s_id"));
                    item_taskid.add(rs.getInt("t_id"));
                }
                txttasklist.setText(tasklist.substring(0,tasklist.length()-2));


                Log.d("ReminderService In", item_taskid.get(taskid)+" "+taskid);
                FillDataByService(item_taskid.get(taskid));
                // FillDataByService(4);

            }
        } catch (Exception ex) {

        }


        edtdate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("click", "onMtouch");
                edtdate.setFocusable(true);
                edtdate.requestFocus();
               showDateTimePicker();
                return false;
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    DeleteProfile updatePro = new DeleteProfile();
                    updatePro.execute("");
                    HomeOngoingProject fragment = new HomeOngoingProject();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    id-=1;
                    if(id==-1){
                        id=item_taskid.size()-1;
                    }
                    Log.d("ReminderService In", "Index n ID"+id+" "+item_taskid.get(id));
                    FillDataByService(item_taskid.get(id));

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    id+=1;
                    if(id==item_taskid.size()){
                        id=0;
                    }
                    Log.d("ReminderService In", "Index n ID"+id+" "+item_taskid.get(id));
                    FillDataByService(item_taskid.get(id));


                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                        CreateProfile addPro = new CreateProfile();
                        addPro.execute("");
                    HomeOngoingProject fragment = new HomeOngoingProject();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                   UpdateProfile updatePro = new UpdateProfile();
                    updatePro.execute("");
                    HomeOngoingProject fragment = new HomeOngoingProject();
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
                    callnoIntent.setData(Uri.parse("tel:" + contact));
                    getActivity().startActivity(callnoIntent);
                } catch (SecurityException ex) {
                    Toast.makeText(rootView.getContext(), "Tel/Cell No Invalid!!", Toast.LENGTH_LONG).show();
                }

            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Bundle bundle = new Bundle();
                    bundle.putString("email1", emailto);
                    bundle.putString("email", activity.email);

                    ContactUs fragment = new ContactUs();
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });

       map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                        if (isGoogleMapsInstalled()) {
                            try {

                                    try {
                                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + address);
                                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                        mapIntent.setPackage("com.google.android.apps.maps");
                                        startActivity(mapIntent);
                                    } catch (Exception ex) {
                                        Toast.makeText(rootView.getContext(), "Address/Google Maps Not Found...", Toast.LENGTH_LONG).show();
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



                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }

            }
        });


        return rootView;
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
    public void showDateTimePicker() {

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
                            startdate = date_format.format(date.getTime());
                            edtdate.setText(startdate);
                            Log.d("ReminderService In", "#######1" + startdate);

                        } catch (Exception e) {
                            Log.d("ReminderService In", e.getMessage().toString());
                        }
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
    public void FillProjectData() {
        //==============Fill Data=
        try {

            String query = "select projectname from [UserOnGoingProject] where [userid]='" + activity.id + "'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();
            category.add("Select Project");
            while (rs.next()) {
                category.add(rs.getString("projectname"));
                propertyexist=propertyexist+1;
            }
            adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerprojectname.setAdapter(adapter);
            if(propertyexist!=0){
                spinnerprojectname.setSelection(1);

              //  FillData(spinnerprojectname.getSelectedItem().toString());
            }

        } catch (Exception ex) {
            // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here",Toast.LENGTH_LONG).show();
        }
//==========
    }
    public void FillCategoryData() {
        //==============Fill Data=
        try {

            String query = "select [category] from [UserOngoingProjectCategory]";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();
            category.add("Select Category...");
            while (rs.next()) {
                category.add(rs.getString("category"));
            }
            adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnercategory.setAdapter(adapter);

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
        }
//==========
    }
    public void FillServiceData(int categoryid) {
        //==============Fill Data=
        try {

            String query = "select [service] from [UserOngoingProjectService] where UserOngoingProjectCategoryid="+categoryid;
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();

            while (rs.next()) {
                category.add(rs.getString("service"));
            }
            adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerservice.setAdapter(adapter);

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
        }}
        public void FillServiceAll(){
            //==============Fill Data=
            try {

                String query = "select [service] from [UserOngoingProjectService]";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                ArrayList<String> category = new ArrayList<String>();

                while (rs.next()) {
                    category.add(rs.getString("service"));
                }
                adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerservice.setAdapter(adapter);

            } catch (Exception ex) {
                Log.d("ReminderService In", ex.getMessage());
            }
//==========
    }
    public void FillProData() {
        //==============Fill Data=
        try {

            String query = "select [businessname] from Contractor";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();

            while (rs.next()) {
                category.add(rs.getString("businessname"));
            }
            adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnertaskby.setAdapter(adapter);

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
        }
//==========
    }

   /* public void FillData(String name) {
        //==============Fill Data=
        try {

                    String  query = "select uogp.id as uogp_id, * from [UserOnGoingProjectTask] uogpt" +
                            " inner join [UserOnGoingProject] uogp on uogp.id=uogpt.projectid where [projectname]='" + name+"'";

                   PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    rs.next();
            Log.d("ReminderService In", query);
                    if (rs.getRow() != 0) {
                        activity.projectid=rs.getInt("uogp_id");
                        edttitle.setText(rs.getString("title"));
                        edttask.setText(rs.getString("task").trim());
                           edtplace.setText(rs.getString("place"));
                        edtdate.setText(rs.getString("startdate"));

                        edtcost.setText(rs.getString("cost"));

                        spinnertaskby.setSelection(Integer.parseInt(rs.getString("taskbyid")));
                        spinnercategory.setSelection(Integer.parseInt(rs.getString("projectcategoryid")));
                        spinnerservice.setSelection(Integer.parseInt(rs.getString("projectserviceid"))+1);
                        if (rs.getString("taskstarted").equals("Yes")){
                            b1.setChecked(true);
                        }else{
                            b1.setChecked(false);
                        }
                        if (rs.getString("taskapproved").equals("Yes")){
                            b2.setChecked(true);
                        }else{
                            b2.setChecked(false);
                        }
                        if (rs.getString("taskonquery").equals("Yes")){
                            b3.setChecked(true);
                        }else{
                            b3.setChecked(false);
                        }


                    }

        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }
//==========
    }*/

    public void FillDataByService(int taskid) {
        //==============Fill Data=
        try {

            String  query = "SELECT uogp.id as uogp_id,t.id,*\n" +
                    "  FROM UserOnGoingProjectTask t\n" +
                    "  inner join UserOngoingProjectService s on s.id=t.projectserviceid\n" +
                    "   inner join [UserOnGoingProject] uogp on uogp.id=t.projectid\n" +
           "   where t.id='"+taskid+"' and service='"+selectedtaskvalue+"'";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            rs.next();
            Log.d("ReminderService In", query);
            if (rs.getRow() != 0) {
                activity.projectid=rs.getInt("uogp_id");
                edttitle.setText(rs.getString("title"));
                edttask.setText(rs.getString("task").trim());
                edtplace.setText(rs.getString("place"));
                edtdate.setText(rs.getString("startdate"));

                edtcost.setText(rs.getString("cost"));

              spinnertaskby.setSelection(Integer.parseInt(rs.getString("taskbyid"))-1);
                proid=Integer.parseInt(rs.getString("taskbyid"));
         spinnercategory.setSelection(Integer.parseInt(rs.getString("projectcategoryid")));

         spinnerservice.setSelection(Integer.parseInt(rs.getString("projectserviceid"))-1);
//spinnerservice.setSelection(2);
                if (rs.getString("taskstarted").equals("Yes")){
                    b1.setChecked(true);
                }else{
                    b1.setChecked(false);
                }
                if (rs.getString("taskapproved").equals("Yes")){
                    b2.setChecked(true);
                }else{
                    b2.setChecked(false);
                }
                if (rs.getString("taskonquery").equals("Yes")){
                    b3.setChecked(true);
                }else{
                    b3.setChecked(false);
                }
                String query2 = "select * from [Contractor] where [id]=" + proid;
                Log.d("ReminderService In", "##@@@@@ " + query2);
                PreparedStatement ps2 = con.prepareStatement(query2);
                ResultSet rs2 = ps2.executeQuery();


                while (rs2.next()) {

                    emailto=rs2.getString("email");
                    businessname=rs2.getString("businessname");
                    contacttname=rs2.getString("contactname");
                    contact=rs2.getString("contact");
                  address =rs2.getString("address");

                }
                Log.d("ReminderService In", contact+" "+contacttname+" "+businessname+" "+address+" "+emailto);
            }

        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }
//==========
    }

    public class CreateProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;


        String title = edttitle.getText().toString();
        String task = edttask.getText().toString();
        String projectname = spinnerprojectname.getSelectedItem().toString();
        int service = spinnerservice.getSelectedItemPosition()+1;
        int category = spinnercategory.getSelectedItemPosition() ;
        String place = edtplace.getText().toString();
        String date = edtdate.getText().toString();
        int taskby =  spinnertaskby.getSelectedItemPosition();
        String cost = edtcost.getText().toString();






        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {


            if (isSuccess == true) {


                Toast toast = Toast.makeText(rootView.getContext(), z, Toast.LENGTH_LONG);
                toast.show();


            }

        }



        @Override
        protected String doInBackground(String... params) {
            if (title.trim().equals("")|| task.trim().equals("")|| taskby==0  || projectname.trim().equals("") || place.trim().equals("")|| cost.trim().equals(""))
                z = "Please fill in all required details...";
            else {
                try {
                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    if (con == null) {
                        z = "Check your network connection!!";
                    } else {


                                String query = "INSERT INTO [UserOnGoingProjectTask]\n" +
                                        "           ([title]\n" +
                                        "           ,[task]\n" +
                                        "           ,[place]\n" +
                                        "           ,[startdate]\n" +
                                        "           ,[cost]\n" +
                                        "           ,[taskbyid]\n" +
                                        "           ,[userid]\n" +
                                        "           ,[projectid]\n" +
                                        "           ,[projectcategoryid]\n" +
                                        "           ,[projectserviceid]\n" +
                                        "           ,[taskstarted]\n" +
                                        "           ,[taskapproved]\n" +
                                        "           ,[taskonquery])" +
                                        "values ('" + title + "','" + task + "','" + place + "','" + date + "','" + cost + "','" + taskby + "','" + activity.id + "'," +
                                        "'" + activity.projectid + "','" +category + "','" + service + "','No','No','No')";
                                PreparedStatement preparedStatement = con.prepareStatement(query);
                                preparedStatement.executeUpdate();
                                z = "New Project Task Created!!!";
                                isSuccess = true;





                }
            } catch (Exception ex) {
                    isSuccess = false;
                    z = "Check your network connection!!";
                    // z=ex.getMessage();
                }
            }
            return z;
        }
    }

    //===================
   public class UpdateProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;


        String title = edttitle.getText().toString();
        String task = edttask.getText().toString();
        String projectname = spinnerprojectname.getSelectedItem().toString();
        int service = spinnerservice.getSelectedItemPosition()+1;
        int category = spinnercategory.getSelectedItemPosition() ;
        String place = edtplace.getText().toString();
        String date = edtdate.getText().toString();
        int taskby = spinnertaskby.getSelectedItemPosition();
        String cost = edtcost.getText().toString();


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {


            if (isSuccess == true) {


                Toast toast = Toast.makeText(rootView.getContext(), z, Toast.LENGTH_LONG);
                toast.show();


            }else{
                Toast toast = Toast.makeText(rootView.getContext(), z, Toast.LENGTH_LONG);
                toast.show();
            }

        }



        @Override
        protected String doInBackground(String... params) {
            if (title.trim().equals("")|| task.trim().equals("")|| taskby==0  || projectname.trim().equals("") || place.trim().equals("")|| cost.trim().equals(""))
                z = "Please fill in all required details...";
            else {
                try {
                    Log.d("ReminderService In", "IN UPDAT ##############");
                    String taskstarted,taskapproved,taskonquery;

if(b1.isChecked()){
    taskstarted="Yes";
}else{
    taskstarted="No";
}
                    if(b2.isChecked()){
                        taskapproved="Yes";
                    }else{
                        taskapproved="No";
                    }
                    if(b3.isChecked()){
                        taskonquery="Yes";
                    }else{
                        taskonquery="No";
                    }

                    String query = "update [UserOnGoingProjectTask] set  [title]='" + title + "',task='" + task + "',place='" + place + "',startdate='" + date + "',cost='" + cost + "',taskbyid='" + taskby + "',userid='" + activity.id + "'," +
                            " projectid='" + activity.projectid + "',projectcategoryid='" +category + "',projectserviceid='" + service + "',taskstarted='"+taskstarted+"',taskapproved='"+taskapproved+"',taskonquery='"+taskonquery+"'" +
                            " where id='"+(item_taskid.get(taskid))+"'";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Task Updated!!!";
                        isSuccess = true;

                    Log.d("ReminderService In", "OUT UPDAT ##############"+ item_taskid.get(taskid));




                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Check your network connection!!";
                    Log.d("ReminderService In", "An error GGGG: " + ex.getMessage());
                    // z=ex.getMessage();
                }
            }
            return z;
        }
    }
    public class DeleteProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {


            if (isSuccess == true) {


                Toast toast = Toast.makeText(rootView.getContext(), z, Toast.LENGTH_LONG);
                toast.show();


            }else{
                Toast toast = Toast.makeText(rootView.getContext(), z, Toast.LENGTH_LONG);
                toast.show();
            }

        }



        @Override
        protected String doInBackground(String... params) {
                         try {


                    String query = "Delete from [UserOnGoingProjectTask]  where id='"+(item_taskid.get(id))+"'";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();
                    z = "Task Deleted!!!";
                    isSuccess = true;


                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Check your network connection!!";
                    Log.d("ReminderService In", "An error GGGG: " + ex.getMessage());
                    // z=ex.getMessage();
                }

            return z;
        }
    }



}



