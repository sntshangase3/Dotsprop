package com.sametal.za;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * Created by sibusiso
 */
public class OnGoingProjectTaskRegister extends Fragment {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip;
    Button btncreate,btnsave;
    MainActivity activity = MainActivity.instance;


    FragmentManager fragmentManager;
    Spinner spinnerprojectname,spinnercategory,spinnerservice;
    EditText edttitle, edttask,edtplace, edtdate, edttaskby, edtcost;
    int propertyexist=0;
    ArrayAdapter adapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.ongoingprojecttaskregister, container, false);


        edttitle = (EditText) rootView.findViewById(R.id.edttitle);
        edttask = (EditText) rootView.findViewById(R.id.edttask);
        edtplace = (EditText) rootView.findViewById(R.id.edtplace);
        edtdate = (EditText) rootView.findViewById(R.id.edtdate);
        edttaskby = (EditText) rootView.findViewById(R.id.edttaskby);
        edtcost = (EditText) rootView.findViewById(R.id.edtcost);


       btncreate = (Button) rootView.findViewById(R.id.btn_create);
        btnsave = (Button) rootView.findViewById(R.id.btn_save);
        spinnerprojectname = (Spinner) rootView.findViewById(R.id.spinnerprojectname);
        spinnercategory = (Spinner) rootView.findViewById(R.id.spinnercategory);
        spinnerservice = (Spinner) rootView.findViewById(R.id.spinnerservice);

        fragmentManager = getFragmentManager();
        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "Dotsprop";
        un = "sqaloits";
        pass = "422q5mfQzU";

        ConnectionClass cn = new ConnectionClass();
        con = cn.connectionclass(un, pass, db, ip);

        FillCategoryData();

        FillProjectData();
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
        spinnercategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                // item.toString()
               if(!spinnercategory.getSelectedItem().toString().equals("Select Category...")){
                    try{
                        FillServiceData(spinnercategory.getSelectedItemPosition());
                        spinnerservice.performClick();
                        Log.d("ReminderService In", spinnercategory.getSelectedItemPosition()+" HHH");
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




        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                        CreateProfile addPro = new CreateProfile();
                        addPro.execute("");
                    HomeFragment fragment = new HomeFragment();
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
                   // UpdateProfile updatePro = new UpdateProfile();
                   // updatePro.execute("");
                    HomeFragment fragment = new HomeFragment();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });




        return rootView;
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

                FillData(spinnerprojectname.getSelectedItem().toString());
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
        }
//==========
    }

    public void FillData(String name) {
        //==============Fill Data=
        try {

                    String  query = "select uogp.id as uogp_id, * from [UserOnGoingProjectTask] uogpt" +
                            " inner join [UserOnGoingProject] uogp on uogp.id=uogpt.projectid where [name]='" + name+"'";

                   PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    rs.next();

                    if (rs.getRow() != 0) {
                        activity.projectid=rs.getInt("uogp_id");
                        edttitle.setText(rs.getString("title"));
                        edttask.setText(rs.getString("task").trim());
                           edtplace.setText(rs.getString("place"));
                        edtdate.setText(rs.getString("startdate"));
                        edttaskby.setText(rs.getString("taskby"));
                        edtcost.setText(rs.getString("cost"));


                        spinnercategory.setSelection(Integer.parseInt(rs.getString("projectcategoryid"))-1);
                        spinnerservice.setSelection(Integer.parseInt(rs.getString("projectserviceid"))+1);

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
        String taskby = edttaskby.getText().toString();
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
            if (title.trim().equals("")|| task.trim().equals("")|| taskby.trim().equals("")  || projectname.trim().equals("") || place.trim().equals("")|| cost.trim().equals(""))
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
   /* public class UpdateProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;


        String location = edttitle.getText().toString();
        String address = edttask.getText().toString();
        String paystatus = spinnerpaystatus.getSelectedItem().toString();
        String amountpay = edtplace.getText().toString();
        String municipality = edtdate.getText().toString();
        String independent = edttaskby.getText().toString();
        String marketprice = edtcost.getText().toString();
        String bedroomno = edtnumberofbedroom.getText().toString();
        String bathroomno = edtnumberofbathroom.getText().toString();
        String otherroom = edtotherrooms.getText().toString();
        String lapa = edtlapaOrintertainment.getText().toString();
        String swimming = edthasswimmingpool.getText().toString();

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
            if (location.trim().equals("")|| paystatus.trim().equals("")|| amountpay.trim().equals("") || municipality.trim().equals("") || independent.trim().equals("") || marketprice.trim().equals("")|| swimming.trim().equals("")|| lapa.trim().equals(""))
                z = "Please fill in all required details...";
            else {
                try {



                        String query = "update [UserProperty] set [location]='" + location + "',[address]='" + address + "',[paymentstatus]='" + paystatus + "',[paymentamount]='" + amountpay + "',[image]='" + encodedImage + "',[municipalityvaluation]='" + municipality + "',[independentvaluation]='" + independent + "'," +
                                "[rangemarketprice]='" + marketprice + "',[numberofbedroom]='" + bedroomno + "',[numberofbathroom]='" + bathroomno + "',[otherrooms]='" + otherroom + "',[hasswimmingpool]='" + swimming + "',[lapaOrintertainment]='" + lapa + "' where [location]='"+spinnerprppertyname.getSelectedItem().toString()+"'";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Property Profile Updated!!!";
                        isSuccess = true;






                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Check your network connection!!";
                    Log.d("ReminderService In", "An error GGGG: " + ex.getMessage());
                    // z=ex.getMessage();
                }
            }
            return z;
        }
    }*/



}



