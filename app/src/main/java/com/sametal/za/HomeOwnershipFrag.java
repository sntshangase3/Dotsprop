package com.sametal.za;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by sibusiso
 */
public class HomeOwnershipFrag extends Fragment {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip;
    int taskid;
    EditText edttaskprice;
    TextView txthomeownership,txtpropertycost, txtmunicipalitycost, txtmaintancecost;
    TextView txttotalinsurance, txttotalbond, txttotalhomeassociate, txttotaltaxrates, txttotalelectricity, txttotalwater, txttotalcleaning, txttotalsecurity, txttotalgardening, txttotalhomerepair;

    ImageView setting11, setting12, setting13, setting21, setting22, setting23, setting31, setting32, setting33, setting34;
    ArrayList<String> taskname = new ArrayList<String>();
    ArrayList<String> price = new ArrayList<String>();


    Button btncreate, btnsave;
    MainActivity activity = MainActivity.instance;
    ListView lstgross11, lstgross12, lstgross13, lstgross21, lstgross22, lstgross23, lstgross31, lstgross32, lstgross33, lstgross34;
    Spinner spinnerservice, spinnercategory;
    ArrayAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.homeownership, container, false);

        lstgross11 = (ListView) rootView.findViewById(R.id.lstgross11);
        lstgross12 = (ListView) rootView.findViewById(R.id.lstgross12);
        lstgross13 = (ListView) rootView.findViewById(R.id.lstgross13);
        lstgross21 = (ListView) rootView.findViewById(R.id.lstgross21);
        lstgross22 = (ListView) rootView.findViewById(R.id.lstgross22);
        lstgross23 = (ListView) rootView.findViewById(R.id.lstgross23);
        lstgross31 = (ListView) rootView.findViewById(R.id.lstgross31);
        lstgross32 = (ListView) rootView.findViewById(R.id.lstgross32);
        lstgross33 = (ListView) rootView.findViewById(R.id.lstgross33);
        lstgross34 = (ListView) rootView.findViewById(R.id.lstgross34);

        edttaskprice = (EditText) rootView.findViewById(R.id.edttaskprice);
        txthomeownership = (TextView) rootView.findViewById(R.id.txthomeownership);
        txtpropertycost = (TextView) rootView.findViewById(R.id.txtpropertycost);
        txtmunicipalitycost = (TextView) rootView.findViewById(R.id.txtmunicipalitycost);
        txtmaintancecost = (TextView) rootView.findViewById(R.id.txtmaintancecost);

        txttotalinsurance = (TextView) rootView.findViewById(R.id.txttotalinsurance);
        txttotalbond = (TextView) rootView.findViewById(R.id.txttotalbond);
        txttotalhomeassociate = (TextView) rootView.findViewById(R.id.txttotalhomeassociate);
        txttotaltaxrates = (TextView) rootView.findViewById(R.id.txttotaltaxrates);
        txttotalelectricity = (TextView) rootView.findViewById(R.id.txttotalelectricity);
        txttotalwater = (TextView) rootView.findViewById(R.id.txttotalwater);
        txttotalcleaning = (TextView) rootView.findViewById(R.id.txttotalcleaning);
        txttotalsecurity = (TextView) rootView.findViewById(R.id.txttotalsecurity);
        txttotalgardening = (TextView) rootView.findViewById(R.id.txttotalgardening);
        txttotalhomerepair = (TextView) rootView.findViewById(R.id.txttotalhomerepair);

        setting11 = (ImageView) rootView.findViewById(R.id.setting11);
        setting12 = (ImageView) rootView.findViewById(R.id.setting12);
        setting13 = (ImageView) rootView.findViewById(R.id.setting13);
        setting21 = (ImageView) rootView.findViewById(R.id.setting21);
        setting22 = (ImageView) rootView.findViewById(R.id.setting22);
        setting23 = (ImageView) rootView.findViewById(R.id.setting23);
        setting31 = (ImageView) rootView.findViewById(R.id.setting31);
        setting32 = (ImageView) rootView.findViewById(R.id.setting32);
        setting33 = (ImageView) rootView.findViewById(R.id.setting33);
        setting34 = (ImageView) rootView.findViewById(R.id.setting34);

        spinnerservice = (Spinner) rootView.findViewById(R.id.spinnerservice);
        spinnercategory = (Spinner) rootView.findViewById(R.id.spinnercategory);


        btncreate = (Button) rootView.findViewById(R.id.btn_create);
        btnsave = (Button) rootView.findViewById(R.id.btn_addtask);


        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "Dotsprop";
        un = "sqaloits";
        pass = "422q5mfQzU";
        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage() + "DSDSD");
        }



        FillServiceData();
        FillCategoryData();
        try {
            String query = "select * from [UserPropertyCostTask]";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            int total = 0;
            while (rs.next()) {
                total = total + Integer.parseInt(rs.getString("cost"));
            }
            txthomeownership.setText("Home Ownership Cost R" + String.valueOf(total));

        } catch (Exception ex) {
            Log.d("ReminderService In", "DDD"+ex.getMessage().toString());
        }
        //Property Cost Total
        try {
            String query = "select * from [UserPropertyCostTask] where propertycostcategoryid in(1,2,3)";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            int total = 0;
            while (rs.next()) {
                total = total + Integer.parseInt(rs.getString("cost"));
            }
            txtpropertycost.setText("Property Cost R" + String.valueOf(total));

        } catch (Exception ex) {
            Log.d("ReminderService In", "DDD"+ex.getMessage().toString());
        }
        //Municipality cost total
        try {
            String query = "select * from [UserPropertyCostTask] where propertycostcategoryid in(4,5,6)";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            int total = 0;
            while (rs.next()) {
                total = total + Integer.parseInt(rs.getString("cost"));
            }
            txtmunicipalitycost.setText("Municipality Cost R" + String.valueOf(total));

        } catch (Exception ex) {
            Log.d("ReminderService In", "DDD"+ex.getMessage().toString());
        }
        //Maintanance cost Total
        try {
            String query = "select * from [UserPropertyCostTask] where propertycostcategoryid in(7,8,9,10)";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            int total = 0;
            while (rs.next()) {
                total = total + Integer.parseInt(rs.getString("cost"));
            }
            txtmaintancecost.setText("Maintanance Cost R" + String.valueOf(total));

        } catch (Exception ex) {
            Log.d("ReminderService In", "DDD"+ex.getMessage().toString());
        }
        try {
            String query = "select * from [UserPropertyCostTask] where propertycostcategoryid in(7,8,9,10)";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            int total = 0;
            while (rs.next()) {
                total = total + Integer.parseInt(rs.getString("cost"));
            }
            txtmaintancecost.setText("Maintanance Cost R" + String.valueOf(total));

        } catch (Exception ex) {
            Log.d("ReminderService In", "DDD"+ex.getMessage().toString());
        }
        FillData1();
        FillData2();
        FillData3();
        FillData4();
        FillData5();
        FillData6();
        FillData7();
        FillData8();
        FillData9();
        FillData10();
        spinnercategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });
        spinnerservice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                if (!spinnerservice.getSelectedItem().toString().equals("Add Task...")) {
                    spinnercategory.performClick();
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


                Fragment fragment = new HomeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, fragment).commit();


            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (btnsave.getText().toString().equals("UPDATE")) {
                    UpdateProfile updatePro = new UpdateProfile();
                    updatePro.execute("");
                } else {
                    CreateProfile addPro = new CreateProfile();
                    addPro.execute("");
                }


            }
        });
        setting11.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lstgross11.setVisibility(View.VISIBLE);
                lstgross12.setVisibility(View.GONE);
                lstgross13.setVisibility(View.GONE);
                lstgross21.setVisibility(View.GONE);
                lstgross22.setVisibility(View.GONE);
                lstgross23.setVisibility(View.GONE);
                lstgross31.setVisibility(View.GONE);
                lstgross32.setVisibility(View.GONE);
                lstgross33.setVisibility(View.GONE);
                lstgross34.setVisibility(View.GONE);
                FillData1();
            }
        });
        setting12.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lstgross11.setVisibility(View.GONE);
                lstgross12.setVisibility(View.VISIBLE);
                lstgross13.setVisibility(View.GONE);
                lstgross21.setVisibility(View.GONE);
                lstgross22.setVisibility(View.GONE);
                lstgross23.setVisibility(View.GONE);
                lstgross31.setVisibility(View.GONE);
                lstgross32.setVisibility(View.GONE);
                lstgross33.setVisibility(View.GONE);
                lstgross34.setVisibility(View.GONE);
                FillData2();
            }
        });
        setting13.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lstgross11.setVisibility(View.GONE);
                lstgross12.setVisibility(View.GONE);
                lstgross13.setVisibility(View.VISIBLE);
                lstgross21.setVisibility(View.GONE);
                lstgross22.setVisibility(View.GONE);
                lstgross23.setVisibility(View.GONE);
                lstgross31.setVisibility(View.GONE);
                lstgross32.setVisibility(View.GONE);
                lstgross33.setVisibility(View.GONE);
                lstgross34.setVisibility(View.GONE);
                FillData3();
            }
        });
        setting21.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lstgross11.setVisibility(View.GONE);
                lstgross12.setVisibility(View.GONE);
                lstgross13.setVisibility(View.GONE);
                lstgross21.setVisibility(View.VISIBLE);
                lstgross22.setVisibility(View.GONE);
                lstgross23.setVisibility(View.GONE);
                lstgross31.setVisibility(View.GONE);
                lstgross32.setVisibility(View.GONE);
                lstgross33.setVisibility(View.GONE);
                lstgross34.setVisibility(View.GONE);
                FillData4();
            }
        });
        setting22.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lstgross11.setVisibility(View.GONE);
                lstgross12.setVisibility(View.GONE);
                lstgross13.setVisibility(View.GONE);
                lstgross21.setVisibility(View.GONE);
                lstgross22.setVisibility(View.VISIBLE);
                lstgross23.setVisibility(View.GONE);
                lstgross31.setVisibility(View.GONE);
                lstgross32.setVisibility(View.GONE);
                lstgross33.setVisibility(View.GONE);
                lstgross34.setVisibility(View.GONE);
                FillData5();
            }
        });
        setting23.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lstgross11.setVisibility(View.GONE);
                lstgross12.setVisibility(View.GONE);
                lstgross13.setVisibility(View.GONE);
                lstgross21.setVisibility(View.GONE);
                lstgross22.setVisibility(View.GONE);
                lstgross23.setVisibility(View.VISIBLE);
                lstgross31.setVisibility(View.GONE);
                lstgross32.setVisibility(View.GONE);
                lstgross33.setVisibility(View.GONE);
                lstgross34.setVisibility(View.GONE);
                FillData6();
            }
        });
        setting31.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lstgross11.setVisibility(View.GONE);
                lstgross12.setVisibility(View.GONE);
                lstgross13.setVisibility(View.GONE);
                lstgross21.setVisibility(View.GONE);
                lstgross22.setVisibility(View.GONE);
                lstgross23.setVisibility(View.GONE);
                lstgross31.setVisibility(View.VISIBLE);
                lstgross32.setVisibility(View.GONE);
                lstgross33.setVisibility(View.GONE);
                lstgross34.setVisibility(View.GONE);
                FillData7();
            }
        });
        setting32.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lstgross11.setVisibility(View.GONE);
                lstgross12.setVisibility(View.GONE);
                lstgross13.setVisibility(View.GONE);
                lstgross21.setVisibility(View.GONE);
                lstgross22.setVisibility(View.GONE);
                lstgross23.setVisibility(View.GONE);
                lstgross31.setVisibility(View.GONE);
                lstgross32.setVisibility(View.VISIBLE);
                lstgross33.setVisibility(View.GONE);
                lstgross34.setVisibility(View.GONE);
                FillData8();
            }
        });
        setting33.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lstgross11.setVisibility(View.GONE);
                lstgross12.setVisibility(View.GONE);
                lstgross13.setVisibility(View.GONE);
                lstgross21.setVisibility(View.GONE);
                lstgross22.setVisibility(View.GONE);
                lstgross23.setVisibility(View.GONE);
                lstgross31.setVisibility(View.GONE);
                lstgross32.setVisibility(View.GONE);
                lstgross33.setVisibility(View.VISIBLE);
                lstgross34.setVisibility(View.GONE);
                FillData9();

            }
        });
        setting34.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lstgross11.setVisibility(View.GONE);
                lstgross12.setVisibility(View.GONE);
                lstgross13.setVisibility(View.GONE);
                lstgross21.setVisibility(View.GONE);
                lstgross22.setVisibility(View.GONE);
                lstgross23.setVisibility(View.GONE);
                lstgross31.setVisibility(View.GONE);
                lstgross32.setVisibility(View.GONE);
                lstgross33.setVisibility(View.GONE);
                lstgross34.setVisibility(View.VISIBLE);

                FillData10();
            }
        });
        return rootView;
    }


    private View currentSelectedView;

    public void FillServiceData() {
        //==============Fill Data=
        try {

            String query = "select [service] from [Service]";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();
            category.add("Add Task...");
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

    public void FillCategoryData() {
        //==============Fill Data=
        try {

            String query = "select [category] from [UserPropertyCostCategory]";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();

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


    public void FillData1() {
        //==============Fill Data=
        try {

            String query = "select cost,upct.id as upct_id,service  from [UserPropertyCostTask] upct\n" +
                    "inner join [Service] s on s.ID=upct.serviceid \n" +
                    " where propertycostcategoryid=1";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            taskname.clear();
            price.clear();

            int total=0;
            while (rs.next()) {

                taskname.add(rs.getString("service").toString().trim());
                price.add("R" + rs.getString("cost").toString().trim());
                total =total+Integer.parseInt(rs.getString("cost").toString().trim());
            }
            txttotalinsurance.setText("R"+String.valueOf(total));

            AddTaskAdapter adapter = new AddTaskAdapter(this.getActivity(), taskname, price);
            lstgross11.setAdapter(adapter);
            lstgross11.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {

                        final String serviceid = taskname.get(position);
                        final String cost = price.get(position).replace("R", "");
                        String query = "select cost,upct.id as upct_id,s.id as s_id from [UserPropertyCostTask] upct\n" +
                                "  inner join [Service] s on s.ID=upct.serviceid\n" +
                                " where serviceid=" + serviceid + " and cost=" + cost;
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();
                        Log.d("ReminderService In", query);
                        while (rs.next()) {
                            if (currentSelectedView != null && currentSelectedView != view) {
                                unhighlightCurrentRow(currentSelectedView);
                            }
                            currentSelectedView = view;
                            highlightCurrentRow(currentSelectedView);
                            btnsave.setText("UPDATE");

                            try {
                                taskid = rs.getInt("upct_id");
                                edttaskprice.setText(rs.getString("cost").trim());
                                spinnerservice.setSelection(rs.getInt("s_id"));

                            } catch (Exception ex) {
                                // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                Log.d("ReminderService In", "#####" + ex.getMessage().toString());
                            }
                        }


                    } catch (Exception ex) {
                        //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }

                }
            });

                  /*  if (rs.getRow() != 0) {
                        edtstaff.setText(rs.getString("staff"));
                        edtposition.setText(rs.getString("position").trim());
                           edtemail.setText(rs.getString("email"));
                        edtpassword.setText(rs.getString("password"));
                        edtsms.setText(rs.getString("sms"));

                    }*/


        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }
//==========
    }

    public void FillData2() {
        //==============Fill Data=
        try {

            String query = "select cost,upct.id as upct_id,service  from [UserPropertyCostTask] upct\n" +
                    "inner join [Service] s on s.ID=upct.serviceid \n" +
                    " where propertycostcategoryid=2";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            taskname.clear();
            price.clear();

            int total=0;
            while (rs.next()) {

                taskname.add(rs.getString("service").toString().trim());
                price.add("R" + rs.getString("cost").toString().trim());
                total =total+Integer.parseInt(rs.getString("cost").toString().trim());
            }
            txttotalbond.setText("R"+String.valueOf(total));

            AddTaskAdapter adapter = new AddTaskAdapter(this.getActivity(), taskname, price);
            lstgross12.setAdapter(adapter);
            lstgross12.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {

                        final String serviceid = taskname.get(position);
                        final String cost = price.get(position).replace("R", "");
                        String query = "select cost,upct.id as upct_id,s.id as s_id from [UserPropertyCostTask] upct\n" +
                                "  inner join [Service] s on s.ID=upct.serviceid\n" +
                                " where serviceid=" + serviceid + " and cost=" + cost;
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();
                        Log.d("ReminderService In", query);
                        while (rs.next()) {
                            if (currentSelectedView != null && currentSelectedView != view) {
                                unhighlightCurrentRow(currentSelectedView);
                            }
                            currentSelectedView = view;
                            highlightCurrentRow(currentSelectedView);
                            btnsave.setText("UPDATE");

                            try {
                                taskid = rs.getInt("upct_id");
                                edttaskprice.setText(rs.getString("cost").trim());
                                spinnerservice.setSelection(rs.getInt("s_id"));

                            } catch (Exception ex) {
                                // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                Log.d("ReminderService In", "#####" + ex.getMessage().toString());
                            }
                        }


                    } catch (Exception ex) {
                        //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }

                }
            });

                  /*  if (rs.getRow() != 0) {
                        edtstaff.setText(rs.getString("staff"));
                        edtposition.setText(rs.getString("position").trim());
                           edtemail.setText(rs.getString("email"));
                        edtpassword.setText(rs.getString("password"));
                        edtsms.setText(rs.getString("sms"));

                    }*/


        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }
//==========
    }

    public void FillData3() {
        //==============Fill Data=
        try {

            String query = "select cost,upct.id as upct_id,service  from [UserPropertyCostTask] upct\n" +
                    "inner join [Service] s on s.ID=upct.serviceid \n" +
                    " where propertycostcategoryid=3";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            taskname.clear();
            price.clear();

            int total=0;
            while (rs.next()) {

                taskname.add(rs.getString("service").toString().trim());
                price.add("R" + rs.getString("cost").toString().trim());
                total =total+Integer.parseInt(rs.getString("cost").toString().trim());
            }
            txttotalhomeassociate.setText("R"+String.valueOf(total));

            AddTaskAdapter adapter = new AddTaskAdapter(this.getActivity(), taskname, price);
            lstgross13.setAdapter(adapter);
            lstgross13.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {

                        final String serviceid = taskname.get(position);
                        final String cost = price.get(position).replace("R", "");
                        String query = "select cost,upct.id as upct_id,s.id as s_id from [UserPropertyCostTask] upct\n" +
                                "  inner join [Service] s on s.ID=upct.serviceid\n" +
                                " where serviceid=" + serviceid + " and cost=" + cost;
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();
                        Log.d("ReminderService In", query);
                        while (rs.next()) {
                            if (currentSelectedView != null && currentSelectedView != view) {
                                unhighlightCurrentRow(currentSelectedView);
                            }
                            currentSelectedView = view;
                            highlightCurrentRow(currentSelectedView);
                            btnsave.setText("UPDATE");

                            try {
                                taskid = rs.getInt("upct_id");
                                edttaskprice.setText(rs.getString("cost").trim());
                                spinnerservice.setSelection(rs.getInt("s_id"));

                            } catch (Exception ex) {
                                // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                Log.d("ReminderService In", "#####" + ex.getMessage().toString());
                            }
                        }


                    } catch (Exception ex) {
                        //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }

                }
            });

                  /*  if (rs.getRow() != 0) {
                        edtstaff.setText(rs.getString("staff"));
                        edtposition.setText(rs.getString("position").trim());
                           edtemail.setText(rs.getString("email"));
                        edtpassword.setText(rs.getString("password"));
                        edtsms.setText(rs.getString("sms"));

                    }*/


        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }
//==========
    }

    public void FillData4() {
        //==============Fill Data=
        try {

            String query = "select cost,upct.id as upct_id,service  from [UserPropertyCostTask] upct\n" +
                    "inner join [Service] s on s.ID=upct.serviceid \n" +
                    " where propertycostcategoryid=4";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            taskname.clear();
            price.clear();

            int total=0;
            while (rs.next()) {

                taskname.add(rs.getString("service").toString().trim());
                price.add("R" + rs.getString("cost").toString().trim());
                total =total+Integer.parseInt(rs.getString("cost").toString().trim());
            }
            txttotaltaxrates.setText("R"+String.valueOf(total));

            AddTaskAdapter adapter = new AddTaskAdapter(this.getActivity(), taskname, price);
            lstgross21.setAdapter(adapter);
            lstgross21.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {

                        final String serviceid = taskname.get(position);
                        final String cost = price.get(position).replace("R", "");
                        String query = "select cost,upct.id as upct_id,s.id as s_id from [UserPropertyCostTask] upct\n" +
                                "  inner join [Service] s on s.ID=upct.serviceid\n" +
                                " where serviceid=" + serviceid + " and cost=" + cost;
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();
                        Log.d("ReminderService In", query);
                        while (rs.next()) {
                            if (currentSelectedView != null && currentSelectedView != view) {
                                unhighlightCurrentRow(currentSelectedView);
                            }
                            currentSelectedView = view;
                            highlightCurrentRow(currentSelectedView);
                            btnsave.setText("UPDATE");

                            try {
                                taskid = rs.getInt("upct_id");
                                edttaskprice.setText(rs.getString("cost").trim());
                                spinnerservice.setSelection(rs.getInt("s_id"));

                            } catch (Exception ex) {
                                // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                Log.d("ReminderService In", "#####" + ex.getMessage().toString());
                            }
                        }


                    } catch (Exception ex) {
                        //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }

                }
            });

                  /*  if (rs.getRow() != 0) {
                        edtstaff.setText(rs.getString("staff"));
                        edtposition.setText(rs.getString("position").trim());
                           edtemail.setText(rs.getString("email"));
                        edtpassword.setText(rs.getString("password"));
                        edtsms.setText(rs.getString("sms"));

                    }*/


        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }
//==========
    }

    public void FillData5() {
        //==============Fill Data=
        try {

            String query = "select cost,upct.id as upct_id,service  from [UserPropertyCostTask] upct\n" +
                    "inner join [Service] s on s.ID=upct.serviceid \n" +
                    " where propertycostcategoryid=5";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            taskname.clear();
            price.clear();

            int total=0;
            while (rs.next()) {

                taskname.add(rs.getString("service").toString().trim());
                price.add("R" + rs.getString("cost").toString().trim());
                total =total+Integer.parseInt(rs.getString("cost").toString().trim());
            }
            txttotalelectricity.setText("R"+String.valueOf(total));

            AddTaskAdapter adapter = new AddTaskAdapter(this.getActivity(), taskname, price);
            lstgross22.setAdapter(adapter);
            lstgross22.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {

                        final String serviceid = taskname.get(position);
                        final String cost = price.get(position).replace("R", "");
                        String query = "select cost,upct.id as upct_id,s.id as s_id from [UserPropertyCostTask] upct\n" +
                                "  inner join [Service] s on s.ID=upct.serviceid\n" +
                                " where serviceid=" + serviceid + " and cost=" + cost;
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();
                        Log.d("ReminderService In", query);
                        while (rs.next()) {
                            if (currentSelectedView != null && currentSelectedView != view) {
                                unhighlightCurrentRow(currentSelectedView);
                            }
                            currentSelectedView = view;
                            highlightCurrentRow(currentSelectedView);
                            btnsave.setText("UPDATE");

                            try {
                                taskid = rs.getInt("upct_id");
                                edttaskprice.setText(rs.getString("cost").trim());
                                spinnerservice.setSelection(rs.getInt("s_id"));

                            } catch (Exception ex) {
                                // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                Log.d("ReminderService In", "#####" + ex.getMessage().toString());
                            }
                        }


                    } catch (Exception ex) {
                        //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }

                }
            });

                  /*  if (rs.getRow() != 0) {
                        edtstaff.setText(rs.getString("staff"));
                        edtposition.setText(rs.getString("position").trim());
                           edtemail.setText(rs.getString("email"));
                        edtpassword.setText(rs.getString("password"));
                        edtsms.setText(rs.getString("sms"));

                    }*/


        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }
//==========
    }

    public void FillData6() {
        //==============Fill Data=
        try {

            String query = "select cost,upct.id as upct_id,service  from [UserPropertyCostTask] upct\n" +
                    "inner join [Service] s on s.ID=upct.serviceid \n" +
                    " where propertycostcategoryid=6";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            taskname.clear();
            price.clear();

            int total=0;
            while (rs.next()) {

                taskname.add(rs.getString("service").toString().trim());
                price.add("R" + rs.getString("cost").toString().trim());
                total =total+Integer.parseInt(rs.getString("cost").toString().trim());
            }
            txttotalwater.setText("R"+String.valueOf(total));

            AddTaskAdapter adapter = new AddTaskAdapter(this.getActivity(), taskname, price);
            lstgross23.setAdapter(adapter);
            lstgross23.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {

                        final String serviceid = taskname.get(position);
                        final String cost = price.get(position).replace("R", "");
                        String query = "select cost,upct.id as upct_id,s.id as s_id from [UserPropertyCostTask] upct\n" +
                                "  inner join [Service] s on s.ID=upct.serviceid\n" +
                                " where serviceid=" + serviceid + " and cost=" + cost;
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();
                        Log.d("ReminderService In", query);
                        while (rs.next()) {
                            if (currentSelectedView != null && currentSelectedView != view) {
                                unhighlightCurrentRow(currentSelectedView);
                            }
                            currentSelectedView = view;
                            highlightCurrentRow(currentSelectedView);
                            btnsave.setText("UPDATE");

                            try {
                                taskid = rs.getInt("upct_id");
                                edttaskprice.setText(rs.getString("cost").trim());
                                spinnerservice.setSelection(rs.getInt("s_id"));

                            } catch (Exception ex) {
                                // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                Log.d("ReminderService In", "#####" + ex.getMessage().toString());
                            }
                        }


                    } catch (Exception ex) {
                        //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }

                }
            });

                  /*  if (rs.getRow() != 0) {
                        edtstaff.setText(rs.getString("staff"));
                        edtposition.setText(rs.getString("position").trim());
                           edtemail.setText(rs.getString("email"));
                        edtpassword.setText(rs.getString("password"));
                        edtsms.setText(rs.getString("sms"));

                    }*/


        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }
//==========
    }

    public void FillData7() {
        //==============Fill Data=
        try {

            String query = "select cost,upct.id as upct_id,service  from [UserPropertyCostTask] upct\n" +
                    "inner join [Service] s on s.ID=upct.serviceid \n" +
                    " where propertycostcategoryid=7";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            taskname.clear();
            price.clear();

            int total=0;
            while (rs.next()) {

                taskname.add(rs.getString("service").toString().trim());
                price.add("R" + rs.getString("cost").toString().trim());
                total =total+Integer.parseInt(rs.getString("cost").toString().trim());
            }
            txttotalcleaning.setText("R"+String.valueOf(total));

            AddTaskAdapter adapter = new AddTaskAdapter(this.getActivity(), taskname, price);
            lstgross31.setAdapter(adapter);
            lstgross31.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {

                        final String task = taskname.get(position);
                        String query = "select * from [UserPropertyCostTask] upct\n" +
                                "  inner join [Service] s on s.ID=upct.serviceid\n" +
                                " where task=" + task;
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();
                        Log.d("ReminderService In", query);
                        while (rs.next()) {
                            if (currentSelectedView != null && currentSelectedView != view) {
                                unhighlightCurrentRow(currentSelectedView);
                            }
                            currentSelectedView = view;
                            highlightCurrentRow(currentSelectedView);
                            btnsave.setText("UPDATE");

                            try {
                                taskid = rs.getInt("upct_id");
                                edttaskprice.setText(rs.getString("cost").trim());
                                spinnerservice.setSelection(rs.getInt("s_id"));

                            } catch (Exception ex) {
                                // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                Log.d("ReminderService In", "#####" + ex.getMessage().toString());
                            }
                        }


                    } catch (Exception ex) {
                        //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }

                }
            });

                  /*  if (rs.getRow() != 0) {
                        edtstaff.setText(rs.getString("staff"));
                        edtposition.setText(rs.getString("position").trim());
                           edtemail.setText(rs.getString("email"));
                        edtpassword.setText(rs.getString("password"));
                        edtsms.setText(rs.getString("sms"));

                    }*/


        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }
//==========
    }

    public void FillData8() {
        //==============Fill Data=
        try {

            String query = "select cost,upct.id as upct_id,service  from [UserPropertyCostTask] upct\n" +
                    "inner join [Service] s on s.ID=upct.serviceid \n" +
                    " where propertycostcategoryid=8";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            taskname.clear();
            price.clear();

            int total=0;
            while (rs.next()) {

                taskname.add(rs.getString("service").toString().trim());
                price.add("R" + rs.getString("cost").toString().trim());
                total =total+Integer.parseInt(rs.getString("cost").toString().trim());
            }
            txttotalsecurity.setText("R"+String.valueOf(total));

            AddTaskAdapter adapter = new AddTaskAdapter(this.getActivity(), taskname, price);
            lstgross32.setAdapter(adapter);
            lstgross32.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {

                        final String serviceid = taskname.get(position);
                        final String cost = price.get(position).replace("R", "");
                        String query = "select cost,upct.id as upct_id,s.id as s_id from [UserPropertyCostTask] upct\n" +
                                "  inner join [Service] s on s.ID=upct.serviceid\n" +
                                " where serviceid=" + serviceid + " and cost=" + cost;
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();
                        Log.d("ReminderService In", query);
                        while (rs.next()) {
                            if (currentSelectedView != null && currentSelectedView != view) {
                                unhighlightCurrentRow(currentSelectedView);
                            }
                            currentSelectedView = view;
                            highlightCurrentRow(currentSelectedView);
                            btnsave.setText("UPDATE");

                            try {
                                taskid = rs.getInt("upct_id");
                                edttaskprice.setText(rs.getString("cost").trim());
                                spinnerservice.setSelection(rs.getInt("s_id"));

                            } catch (Exception ex) {
                                // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                Log.d("ReminderService In", "#####" + ex.getMessage().toString());
                            }
                        }


                    } catch (Exception ex) {
                        //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }

                }
            });

                  /*  if (rs.getRow() != 0) {
                        edtstaff.setText(rs.getString("staff"));
                        edtposition.setText(rs.getString("position").trim());
                           edtemail.setText(rs.getString("email"));
                        edtpassword.setText(rs.getString("password"));
                        edtsms.setText(rs.getString("sms"));

                    }*/


        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }
//==========
    }

    public void FillData9() {
        //==============Fill Data=
        try {

            String query = "select cost,upct.id as upct_id,service  from [UserPropertyCostTask] upct\n" +
                    "inner join [Service] s on s.ID=upct.serviceid \n" +
                    " where propertycostcategoryid=9";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            taskname.clear();
            price.clear();
            Log.d("ReminderService In", "@@@@ " + rs.getRow());

            int total=0;
            while (rs.next()) {

                taskname.add(rs.getString("service").toString().trim());
                price.add("R" + rs.getString("cost").toString().trim());
                total =total+Integer.parseInt(rs.getString("cost").toString().trim());
            }
            txttotalgardening.setText("R"+String.valueOf(total));



            AddTaskAdapter adapter = new AddTaskAdapter(this.getActivity(), taskname, price);
            lstgross33.setAdapter(adapter);
            lstgross33.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {

                        final String serviceid = taskname.get(position);
                        final String cost = price.get(position).replace("R", "");
                        String query = "select cost,upct.id as upct_id,s.id as s_id from [UserPropertyCostTask] upct\n" +
                                "  inner join [Service] s on s.ID=upct.serviceid\n" +
                                " where serviceid=" + serviceid + " and cost=" + cost;
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();
                        Log.d("ReminderService In", query);
                        while (rs.next()) {
                            if (currentSelectedView != null && currentSelectedView != view) {
                                unhighlightCurrentRow(currentSelectedView);
                            }
                            currentSelectedView = view;
                            highlightCurrentRow(currentSelectedView);
                            btnsave.setText("UPDATE");

                            try {
                                taskid = rs.getInt("upct_id");
                                edttaskprice.setText(rs.getString("cost").trim());
                                spinnerservice.setSelection(rs.getInt("s_id"));

                            } catch (Exception ex) {
                                // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                Log.d("ReminderService In", "#####" + ex.getMessage().toString());
                            }
                        }


                    } catch (Exception ex) {
                        //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }

                }
            });

                  /*  if (rs.getRow() != 0) {
                        edtstaff.setText(rs.getString("staff"));
                        edtposition.setText(rs.getString("position").trim());
                           edtemail.setText(rs.getString("email"));
                        edtpassword.setText(rs.getString("password"));
                        edtsms.setText(rs.getString("sms"));

                    }*/


        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }
//==========
    }

    public void FillData10() {
        //==============Fill Data=
        try {

            String query = "select cost,upct.id as upct_id,service  from [UserPropertyCostTask] upct\n" +
                    " inner join [Service] s on s.ID=upct.serviceid \n" +
                    " where propertycostcategoryid=10";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            taskname.clear();
            price.clear();
int total=0;
            while (rs.next()) {

                taskname.add(rs.getString("service").toString().trim());
                price.add("R" + rs.getString("cost").toString().trim());
total =total+Integer.parseInt(rs.getString("cost").toString().trim());
            }
txttotalhomerepair.setText("R"+String.valueOf(total));
            AddTaskAdapter adapter = new AddTaskAdapter(this.getActivity(), taskname, price);
            lstgross34.setAdapter(adapter);
            lstgross34.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {

                        final String serviceid = taskname.get(position);
                        final String cost = price.get(position).replace("R", "");
                        String query = "select cost,upct.id as upct_id,s.id as s_id from [UserPropertyCostTask] upct\n" +
                                "  inner join [Service] s on s.ID=upct.serviceid\n" +
                                " where service='" + serviceid + "' and cost='" + cost + "'";
                        Log.d("ReminderService In", query);
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {
                            if (currentSelectedView != null && currentSelectedView != view) {
                                unhighlightCurrentRow(currentSelectedView);
                            }
                            currentSelectedView = view;
                            highlightCurrentRow(currentSelectedView);
                            btnsave.setText("UPDATE");

                            try {
                                taskid = rs.getInt("upct_id");
                                edttaskprice.setText(rs.getString("cost").trim());
                                spinnerservice.setSelection(rs.getInt("s_id"));

                            } catch (Exception ex) {
                                // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                Log.d("ReminderService In", "#####" + ex.getMessage().toString());
                            }
                        }


                    } catch (Exception ex) {
                        //  Toast.makeText(rootView.getContext(), ex.getMessage().toString(),Toast.LENGTH_LONG).show();
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }

                }
            });

                  /*  if (rs.getRow() != 0) {
                        edtstaff.setText(rs.getString("staff"));
                        edtposition.setText(rs.getString("position").trim());
                           edtemail.setText(rs.getString("email"));
                        edtpassword.setText(rs.getString("password"));
                        edtsms.setText(rs.getString("sms"));

                    }*/


        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }
//==========
    }

    private void highlightCurrentRow(View rowView) {

        rowView.setBackgroundColor(getResources().getColor(R.color.focus_box_frame));

    }


    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);

    }


    public class CreateProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;


        String price = edttaskprice.getText().toString();
        int service = spinnerservice.getSelectedItemPosition() + 1;
        int category = spinnercategory.getSelectedItemPosition() + 1;


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {


            if (isSuccess == true) {

                CharSequence text = z;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(rootView.getContext(), text, duration);
                toast.show();


            } else {
                Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                Drawable bg = getResources().getDrawable(R.drawable.edittext_bground);
                if ((edttaskprice.getText().toString().trim().equals(""))) {
                    edttaskprice.setBackground(errorbg);
                } else {
                    edttaskprice.setBackground(bg);
                }

                Toast.makeText(rootView.getContext(), r, Toast.LENGTH_SHORT).show();
            }

        }


        @Override
        protected String doInBackground(String... params) {
            if (price.trim().equals(""))
                z = "Please fill in all required details...";
            else {
                try {


                    String query = "insert into [UserPropertyCostTask]([cost],[serviceid],[propertycostcategoryid]) " +
                            "values ('" + price + "','" + service + "','" + category + "')";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();
                    z = "Task Created!!!";
                    isSuccess = true;
                    Fragment fragment = new HomeOwnershipFrag();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Check your network connection!!";
                    Log.d("ReminderService In", ex.getMessage());
                    // z=ex.getMessage();
                }
            }
            return z;
        }
    }

    //===================

    class UpdateProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;


        String price = edttaskprice.getText().toString();
        int service = spinnerservice.getSelectedItemPosition() + 1;
        int category = spinnercategory.getSelectedItemPosition() + 1;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {


            if (isSuccess == true) {
                CharSequence text = z;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(rootView.getContext(), text, duration);
                toast.show();
            } else {
                Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                Drawable bg = getResources().getDrawable(R.drawable.edittext_bground);
                if ((edttaskprice.getText().toString().trim().equals(""))) {
                    edttaskprice.setBackground(errorbg);
                } else {
                    edttaskprice.setBackground(bg);
                }


                Toast.makeText(rootView.getContext(), r, Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected String doInBackground(String... params) {
            if (price.trim().equals(""))
                z = "Please fill in all details...";
            else {
                try {


                    String query = "Update [UserPropertyCostTask] set [cost]='" + price + "',[serviceid]='" + service + "',[propertycostcategoryid]='" + category + "' where [id]='" + taskid + "'";
                    ;
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();
                    z = "Updated Successfully";
                    isSuccess = true;
                    Fragment fragment = new HomeOwnershipFrag();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    isSuccess = false;
                    // z = "Exceptions";
                    z = "Check your network connection!!";
                    //  z=ex.getMessage();
                }
            }
            return z;
        }
    }


}



