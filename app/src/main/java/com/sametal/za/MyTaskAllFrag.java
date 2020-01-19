package com.sametal.za;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


/**
 * Created by sibusison on 2017/07/30.
 */
public class MyTaskAllFrag extends Fragment {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip;


    ImageView setting;
    Button btn_detail, btn_photo;

    MainActivity activity = MainActivity.instance;
    byte[] byteArray;
    String service = "";
    ListView lstgross;
    Spinner spinnerservice;
    ArrayAdapter adapter;
    TextView txtback, txtdate;
    Bundle bundles = new Bundle();
    FragmentManager fragmentManager;

    ArrayList<String> item_taskvalue = new ArrayList<String>();
    ArrayList<String> item_dec = new ArrayList<String>();
    ArrayList<Integer> item_taskid = new ArrayList<Integer>();

    ArrayList<String> item_task = new ArrayList<String>();
    ArrayList<String> item_taskall = new ArrayList<String>();
    ArrayList<Bitmap> item_photo = new ArrayList<Bitmap>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.mytaskall_detail, container, false);
        lstgross = (ListView) rootView.findViewById(R.id.lstgross);
        spinnerservice = (Spinner) rootView.findViewById(R.id.spinnerservice);
        txtback = (TextView) rootView.findViewById(R.id.txtback);
        btn_detail = (Button) rootView.findViewById(R.id.btn_detail);
        btn_photo = (Button) rootView.findViewById(R.id.btn_photo);
        setting = (ImageView) rootView.findViewById(R.id.setting);

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


        Bundle bundle = this.getArguments();


        txtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               /* Fragment fragment = new HomeContractor();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, fragment).commit();*/
                MyTaskPro fragment = new MyTaskPro();
                bundles.putStringArrayList("item_taskall",item_taskall);
                fragment.setArguments(bundles);
                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


            }
        });

        btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

spinnerservice.performClick();
            }
        });

        btn_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
FillDataPhotos();

            }
        });


        FillServiceData();
        spinnerservice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // item.toString()
                if (!spinnerservice.getSelectedItem().toString().equals("Select Service")) {
                    try {
                        service = spinnerservice.getSelectedItem().toString() ;
                        FillData(service);

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
        return rootView;
    }

    public void FillServiceData() {
        //==============Fill Data=
        try {


            ArrayList<String> category = new ArrayList<String>();
            category.add("Select Service");
            category.add("Property");
            category.add("Municipality");
            category.add("Maintanance");


            adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerservice.setAdapter(adapter);


        } catch (Exception ex) {
            // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here",Toast.LENGTH_LONG).show();
        }
//==========
    }


    private View currentSelectedView;

    private void highlightCurrentRow(View rowView) {

        rowView.setBackgroundColor(getResources().getColor(R.color.focus_box_frame));

    }


    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);

    }

    public void FillData(String service) {
        //==============Initialize list=
        try {
            String query;

            if (service.equals("Property")) {
                query = "select upct.id as task_id, *  from [UserPropertyCostTask] upct\n" +
                        " inner join [Service] s on s.ID=upct.serviceid" +
                        " inner join [UserPropertyCostCategory] c on c.id=upct.propertycostcategoryid" +
                        " where propertycostcategoryid in(1,2,3) and userid=" + activity.id;
            } else if (service.equals("Municipality")) {
                query = "select upct.id as task_id, *  from [UserPropertyCostTask] upct\n" +
                        "                    inner join [Service] s on s.ID=upct.serviceid" +
                        " inner join [UserPropertyCostCategory] c on c.id=upct.propertycostcategoryid" +
                        " where propertycostcategoryid in(4,5,6) and userid=" + activity.id;
            } else {
                query = "select upct.id as task_id,*  from [UserPropertyCostTask] upct\n" +
                        "                    inner join [Service] s on s.ID=upct.serviceid" +
                        " inner join [UserPropertyCostCategory] c on c.id=upct.propertycostcategoryid" +
                        " where propertycostcategoryid in(7,8,9,10) and userid=" + activity.id;
            }

            Log.d("ReminderService In", query);
            item_taskvalue.clear();
            item_dec.clear();
            item_taskid.clear();
            item_taskall.clear();

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            String Insurance = "", Bond = "", Association = "", Taxrates = "", Electricity = "", Water = "", Cleaning = "", Security = "", Gardening = "", Homerepairs = "";

            while (rs.next()) {
                item_taskid.add(rs.getInt("task_id"));
                item_taskall.add(rs.getString("service"));
                if (rs.getString("category").contains("Insurance")) {
                    Insurance = Insurance + rs.getString("service");
                } else if (rs.getString("category").contains("Bond")) {
                    Bond = Bond + rs.getString("service")+"\n";
                } else if (rs.getString("category").contains("Association")) {
                    Association = Association + rs.getString("service");
                } else if (rs.getString("category").contains("Taxrates")) {
                    Taxrates = Taxrates + rs.getString("service");
                } else if (rs.getString("category").contains("Electricity")) {
                    Electricity = Electricity + rs.getString("service");
                } else if (rs.getString("category").contains("Water")) {
                    Water = Water + rs.getString("service");
                } else if (rs.getString("category").contains("Cleaning")) {
                    Cleaning = Cleaning + rs.getString("service");
                } else if (rs.getString("category").contains("Security")) {
                    Security = Security + rs.getString("service");
                } else if (rs.getString("category").contains("Gardening")) {
                    Gardening = Gardening + rs.getString("service");
                } else if (rs.getString("category").contains("Homerepairs")) {
                    Homerepairs = Homerepairs + rs.getString("service");
                }

               /* if (rs.getString("image") != null) {
                    byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    item_profile.add(decodebitmap);

                } else {
                    Bitmap im = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.profil);
                    item_profile.add(im);
                }*/

            }
            if (!Insurance.equals("")) {
                item_taskvalue.add("Type of Task/s Insurance");
                item_dec.add(Insurance);
            }
            if (!Bond.equals("")) {
                item_taskvalue.add("Type of Task/s Bond");
                item_dec.add(Bond);
            }
            if (!Taxrates.equals("")) {
                item_taskvalue.add("Type of Task/s Taxrates");
                item_dec.add(Taxrates);
            }
            if (!Association.equals("")) {
                item_taskvalue.add("Type of Task/s Association");
                item_dec.add(Association);
            }
            if (!Electricity.equals("")) {
                item_taskvalue.add("Type of Task/s Electricity");
                item_dec.add(Electricity);
            }
            if (!Water.equals("")) {
                item_taskvalue.add("Type of Task/s Water");
                item_dec.add(Water);
            }
            if (!Cleaning.equals("")) {
                item_taskvalue.add("Type of Task/s Cleaning");
                item_dec.add(Cleaning);
            }
            if (!Security.equals("")) {
                item_taskvalue.add("Type of Task/s Security");
                item_dec.add(Security);
            }
            if (!Gardening.equals("")) {
                item_taskvalue.add("Type of Task/s Gardening");
                item_dec.add(Gardening);
            }
            if (!Homerepairs.equals("")) {
                item_taskvalue.add("Type of Task/s Homerepairs");
                item_dec.add(Homerepairs);
            }

            MyTaskAll_DetailAdapter adapter = new MyTaskAll_DetailAdapter(this.getActivity(), item_taskvalue, item_dec);
            lstgross.setAdapter(adapter);
            lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {


                        final String selecteddesc = item_dec.get(position);

                        if (currentSelectedView != null && currentSelectedView != view) {
                            unhighlightCurrentRow(currentSelectedView);
                        }
                        currentSelectedView = view;
                        highlightCurrentRow(currentSelectedView);

                        bundles.putString("desc", selecteddesc);
                        HomeOwnershipFrag fragment = new HomeOwnershipFrag();
                        fragment.setArguments(bundles);
                        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


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

    public void FillDataPhotos() {

        //==============Initialize list=
        try {
            item_task.clear();
            item_photo.clear();

            for (int i : item_taskid) {
                String query = "select *  from [UserPropertyCostTask] upct\n" +
                        " inner join [UserPropertyCostTaskPhotos] p on p.taskid=upct.id\n" +
                        " inner join [Service] s on s.ID=upct.serviceid" +
                        " where  upct.id=" + i;

                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    item_task.add(rs.getString("service"));
                    byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    item_photo.add(decodebitmap);

                }
            }
            MyTaskAll_PhotoListAdapter adapter = new MyTaskAll_PhotoListAdapter(this.getActivity(), item_task, item_photo);
            lstgross.setAdapter(adapter);
            lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {




                        if (currentSelectedView != null && currentSelectedView != view) {
                            unhighlightCurrentRow(currentSelectedView);
                        }
                        currentSelectedView = view;
                        highlightCurrentRow(currentSelectedView);

                        HomeOwnershipFrag fragment = new HomeOwnershipFrag();
                        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


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


}



