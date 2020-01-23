package com.sametal.za;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
public class MyTaskAllFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip;


    ImageView setting,imgpro;
    Button btn_detail, btn_photo,btn_complete,btn_incomplete,btn_new;

    MainActivity activity = MainActivity.instance;
    byte[] byteArray;
    String service = "";
    ListView lstgross;
    ListView lstgross1;
    Spinner spinnerservice;
    ArrayAdapter adapter;
    TextView txtback, txtpro;
    Bundle bundles = new Bundle();
    FragmentManager fragmentManager;

    ArrayList<String> item_taskvalue = new ArrayList<String>();
    ArrayList<String> item_dec = new ArrayList<String>();
    ArrayList<String> item_status = new ArrayList<String>();
    ArrayList<Integer> item_taskid = new ArrayList<Integer>();

    ArrayList<String> item_task = new ArrayList<String>();
    ArrayList<String> item_taskall = new ArrayList<String>();
    ArrayList<Bitmap> item_photo = new ArrayList<Bitmap>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.mytaskall_detail, container, false);
        lstgross = (ListView) rootView.findViewById(R.id.lstgross);
        lstgross1 = (ListView) rootView.findViewById(R.id.lstgross1);
        spinnerservice = (Spinner) rootView.findViewById(R.id.spinnerservice);
        txtback = (TextView) rootView.findViewById(R.id.txtback);
        txtpro = (TextView) rootView.findViewById(R.id.txtpro);
        btn_detail = (Button) rootView.findViewById(R.id.btn_detail);
        btn_photo = (Button) rootView.findViewById(R.id.btn_photo);
        btn_complete = (Button) rootView.findViewById(R.id.btn_complete);
        btn_incomplete = (Button) rootView.findViewById(R.id.btn_incomplete);
        btn_new = (Button) rootView.findViewById(R.id.btn_new);
        setting = (ImageView) rootView.findViewById(R.id.setting);
        imgpro = (ImageView) rootView.findViewById(R.id.imgpro);

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
        FillServiceData();

        imgpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               /* Fragment fragment = new HomeContractor();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, fragment).commit();*/
                if (!spinnerservice.getSelectedItem().toString().equals("Select Service")) {
                    MyTaskPro fragment = new MyTaskPro();
                    bundles.putStringArrayList("item_taskall",item_taskall);

                    fragment.setArguments(bundles);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                }else{
                    Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                        spinnerservice.setBackground(errorbg);
                }



            }
        });

        btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // spinnerservice.setSelection(0);
                spinnerservice.performClick();
               service = spinnerservice.getSelectedItem().toString() ;
                FillData(service);

            }
        });

        btn_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
FillDataPhotos();

            }
        });




        spinnerservice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // item.toString()
                if (!spinnerservice.getSelectedItem().toString().equals("Select Service")) {
                    try {
                        service = spinnerservice.getSelectedItem().toString() ;
                        FillData(service);
                        spinnerservice.setBackgroundColor(Color.WHITE);

                    } catch (Exception ex) {
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }
                }else{
                    spinnerservice.setSelection(0);
                    service = spinnerservice.getSelectedItem().toString() ;
                    FillData(service);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });


        btn_complete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {

                    for (int i : item_taskid) {
                        String commands = "update [UserPropertyCostTask] set [status]='Completed' where [id]='" + i + "'";
                        PreparedStatement preStmt = con.prepareStatement(commands);
                        preStmt.executeUpdate();
                    }
                    Toast ToastMessage = Toast.makeText(rootView.getContext(),"Task Completed!!!", Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();
                    MyTaskAllFrag fragment = new MyTaskAllFrag();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });
        btn_incomplete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {

                    for (int i : item_taskid) {
                        String commands = "update [UserPropertyCostTask] set [status]='Incomplete' where [id]='" + i + "'";
                        PreparedStatement preStmt = con.prepareStatement(commands);
                        preStmt.executeUpdate();
                    }
                    Toast ToastMessage = Toast.makeText(rootView.getContext(),"Task Incompleted!!!", Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();
                    MyTaskAllFrag fragment = new MyTaskAllFrag();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });
        btn_new.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {

                    for (int i : item_taskid) {
                        String commands = "update [UserPropertyCostTask] set [status]='New' where [id]='" + i + "'";
                        PreparedStatement preStmt = con.prepareStatement(commands);
                        preStmt.executeUpdate();
                    }
                    Toast ToastMessage = Toast.makeText(rootView.getContext(),"Task New!!!", Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_bground);
                    ToastMessage.show();
                    MyTaskAllFrag fragment = new MyTaskAllFrag();
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


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

        rowView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryBackGround));

    }


    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);

    }

    public void FillData(String service) {
        //==============Initialize list=
        lstgross.setVisibility(View.VISIBLE);
        lstgross1.setVisibility(View.GONE);
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
            } else if (service.equals("Maintanance"))  {
                query = "select upct.id as task_id,*  from [UserPropertyCostTask] upct\n" +
                        "                    inner join [Service] s on s.ID=upct.serviceid" +
                        " inner join [UserPropertyCostCategory] c on c.id=upct.propertycostcategoryid" +
                        " where propertycostcategoryid in(7,8,9,10) and userid=" + activity.id;
            }else{
                query = "select upct.id as task_id,*  from [UserPropertyCostTask] upct\n" +
                        "                    inner join [Service] s on s.ID=upct.serviceid" +
                        " inner join [UserPropertyCostCategory] c on c.id=upct.propertycostcategoryid" +
                        " where  userid=" + activity.id;
            }

            Log.d("ReminderService In", query);
            item_taskvalue.clear();
            item_dec.clear();
            item_status.clear();
            item_taskid.clear();
            item_taskall.clear();

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            String Insurance = "", Bond = "", Association = "", Taxrates = "", Electricity = "", Water = "", Cleaning = "", Security = "", Gardening = "", Homerepairs = "";

            while (rs.next()) {
                item_taskid.add(rs.getInt("task_id"));
                item_status.add(rs.getString("status"));
                item_taskall.add(rs.getString("service"));
                if (rs.getString("category").contains("Insurance")) {
                    Insurance = Insurance + rs.getString("service")+"\n";
                } else if (rs.getString("category").contains("Bond")) {
                    Bond = Bond + rs.getString("service")+"\n";
                } else if (rs.getString("category").contains("Association")) {
                    Association = Association + rs.getString("service")+"\n";
                } else if (rs.getString("category").contains("Taxrates")) {
                    Taxrates = Taxrates + rs.getString("service")+"\n";
                } else if (rs.getString("category").contains("Electricity")) {
                    Electricity = Electricity + rs.getString("service")+"\n";
                } else if (rs.getString("category").contains("Water")) {
                    Water = Water + rs.getString("service")+"\n";
                } else if (rs.getString("category").contains("Cleaning")) {
                    Cleaning = Cleaning + rs.getString("service")+"\n";
                } else if (rs.getString("category").contains("Security")) {
                    Security = Security + rs.getString("service")+"\n";
                } else if (rs.getString("category").contains("Gardening")) {
                    Gardening = Gardening + rs.getString("service")+"\n";
                } else if (rs.getString("category").contains("Homerepairs")) {
                    Homerepairs = Homerepairs + rs.getString("service")+"\n";
                }

            }
            //Count pro around
            int proaround = 0;
            ArrayList<Integer>id=new ArrayList<Integer>();
            for (String tsk : item_taskall) {
                String query1 = "SELECT *\n" +
                        "  FROM [Contractor] \n" +
                        " where  [service] like '%" + tsk + "%'";

                PreparedStatement ps1 = con.prepareStatement(query1);
                ResultSet rs1 = ps1.executeQuery();


                while (rs1.next()) {
                    int proid=rs1.getInt("id");
                    if(!id.contains(proid)){
                        Log.d("ReminderService In","Not" );
                        id.add(proid);
                        proaround+=1;
                    }
                }
            }
            txtpro.setText(String.valueOf(proaround));
            if (!Insurance.equals("")) {
                item_taskvalue.add("Type of Task/s Insurance");
                int count = Insurance.length() - Insurance.replaceAll("\\n", "").length();
                int index=Insurance.lastIndexOf("\n");
                if(count>=2){
                    StringBuilder a=new StringBuilder(Insurance);
                    a.setCharAt(index,' ');
                    item_dec.add(a.toString());


                }else{
                    item_dec.add(Insurance.replaceAll("\n", ""));
                }
            }
            if (!Bond.equals("")) {
                item_taskvalue.add("Type of Task/s Bond");
                int count = Bond.length() - Bond.replaceAll("\\n", "").length();
                int index=Bond.lastIndexOf("\n");
                if(count>=2){
                    StringBuilder a=new StringBuilder(Bond);
                    a.setCharAt(index,' ');
                    item_dec.add(a.toString());

                    Log.d("ReminderService In",count+" ###" );
                }else{
                    item_dec.add(Bond.replaceAll("\n", ""));
                }
            }
            if (!Taxrates.equals("")) {
                item_taskvalue.add("Type of Task/s Taxrates");
                int count = Taxrates.length() - Taxrates.replaceAll("\\n", "").length();
                int index=Taxrates.lastIndexOf("\n");
                if(count>=2){
                    StringBuilder a=new StringBuilder(Taxrates);
                    a.setCharAt(index,' ');
                    item_dec.add(a.toString());

                    Log.d("ReminderService In",count+" ###" );
                }else{
                    item_dec.add(Taxrates.replaceAll("\n", ""));
                }
            }
            if (!Association.equals("")) {
                item_taskvalue.add("Type of Task/s Association");
                int count = Association.length() - Association.replaceAll("\\n", "").length();
                int index=Association.lastIndexOf("\n");
                if(count>=2){
                    StringBuilder a=new StringBuilder(Association);
                    a.setCharAt(index,' ');
                    item_dec.add(a.toString());

                    Log.d("ReminderService In",count+" ###" );
                }else{
                    item_dec.add(Association.replaceAll("\n", ""));
                }
            }
            if (!Electricity.equals("")) {
                item_taskvalue.add("Type of Task/s Electricity");
                int count = Electricity.length() - Electricity.replaceAll("\\n", "").length();
                int index=Electricity.lastIndexOf("\n");
                if(count>=2){
                    StringBuilder a=new StringBuilder(Electricity);
                    a.setCharAt(index,' ');
                    item_dec.add(a.toString());

                    Log.d("ReminderService In",count+" ###" );
                }else{
                    item_dec.add(Electricity.replaceAll("\n", ""));
                }
            }
            if (!Water.equals("")) {
                item_taskvalue.add("Type of Task/s Water");
                int count = Water.length() - Water.replaceAll("\\n", "").length();
                int index=Water.lastIndexOf("\n");
                if(count>=2){
                    StringBuilder a=new StringBuilder(Water);
                    a.setCharAt(index,' ');
                    item_dec.add(a.toString());

                    Log.d("ReminderService In",count+" ###" );
                }else{
                    item_dec.add(Water.replaceAll("\n", ""));
                }
            }
            if (!Cleaning.equals("")) {
                item_taskvalue.add("Type of Task/s Cleaning");
                int count = Cleaning.length() - Cleaning.replaceAll("\\n", "").length();
                int index=Cleaning.lastIndexOf("\n");
                if(count>=2){
                    StringBuilder a=new StringBuilder(Cleaning);
                    a.setCharAt(index,' ');
                    item_dec.add(a.toString());

                    Log.d("ReminderService In",count+" ###" );
                }else{
                    item_dec.add(Cleaning.replaceAll("\n", ""));
                }
            }
            if (!Security.equals("")) {
                item_taskvalue.add("Type of Task/s Security");
                int count = Security.length() - Security.replaceAll("\\n", "").length();
                int index=Security.lastIndexOf("\n");
                if(count>=2){
                    StringBuilder a=new StringBuilder(Security);
                    a.setCharAt(index,' ');
                    item_dec.add(a.toString());

                    Log.d("ReminderService In",count+" ###" );
                }else{
                    item_dec.add(Security.replaceAll("\n", ""));
                }
            }
            if (!Gardening.equals("")) {
                item_taskvalue.add("Type of Task/s Gardening");
                int count = Gardening.length() - Gardening.replaceAll("\\n", "").length();
                int index=Gardening.lastIndexOf("\n");
                if(count>=2){
                    StringBuilder a=new StringBuilder(Gardening);
                    a.setCharAt(index,' ');
                    item_dec.add(a.toString());

                    Log.d("ReminderService In",count+" ###" );
                }else{
                    item_dec.add(Gardening.replaceAll("\n", ""));
                }
            }
            if (!Homerepairs.equals("")) {
                item_taskvalue.add("Type of Task/s Homerepairs");
                int count = Homerepairs.length() - Homerepairs.replaceAll("\\n", "").length();
                int index=Homerepairs.lastIndexOf("\n");
                if(count>=2){
                   StringBuilder a=new StringBuilder(Homerepairs);
                   a.setCharAt(index,' ');
                    item_dec.add(a.toString());

                    Log.d("ReminderService In",count+" ###" );
                }else{
                    item_dec.add(Homerepairs.replaceAll("\n", ""));
                }
            }

            MyTaskAll_DetailAdapter adapter = new MyTaskAll_DetailAdapter(this.getActivity(), item_taskvalue, item_dec,item_status);
            lstgross.setAdapter(adapter);
            lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {



                        final String selectedtaskvalue = item_taskvalue.get(position);
                        final String selectedtaskdesc = item_dec.get(position);

                        if (currentSelectedView != null && currentSelectedView != view) {
                            unhighlightCurrentRow(currentSelectedView);
                        }
                        currentSelectedView = view;
                        highlightCurrentRow(currentSelectedView);

                       bundles.putString("selectedtaskvalue", selectedtaskvalue);

                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                            builder.setTitle(selectedtaskvalue);
                            builder.setIcon(rootView.getResources().getDrawable(R.drawable.radio));
                            builder.setMessage("Details/Pro/Delete?");
                            builder.setPositiveButton("Details", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                       HomeOwnershipFrag fragment = new HomeOwnershipFrag();
                        fragment.setArguments(bundles);
                        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                }
                            });
                        builder.setNeutralButton("Pro", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    if (txtpro.getText().toString().equals("0")) {
                                       Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                                        txtpro.setBackground(errorbg);
                                    }else {
                                        if (!spinnerservice.getSelectedItem().toString().equals("Select Service")) {
                                            MyTaskPro fragment = new MyTaskPro();
                                            bundles.putStringArrayList("item_dec",item_dec);
                                            bundles.putString("selectedtaskvalue", selectedtaskvalue);
                                            fragment.setArguments(bundles);
                                            fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                                        }else{
                                            Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                                            spinnerservice.setBackground(errorbg);
                                        }
                                    }


                                } catch (Exception ex) {
                                    Log.d("ReminderService In", ex.getMessage().toString());
                                }

                            }
                        });
                            builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        if (!spinnerservice.getSelectedItem().toString().equals("Select Service")) {
                                            for (int i : item_taskid) {
                                                String commands = "delete from [UserPropertyCostTask]  where [id]='" + i + "'";
                                                PreparedStatement preStmt = con.prepareStatement(commands);
                                                preStmt.executeUpdate();
                                                commands = "delete from [UserPropertyCostTaskPhotos]  where [taskid]='" + i + "'";
                                                preStmt = con.prepareStatement(commands);
                                                preStmt.executeUpdate();
                                            }

                                            Toast ToastMessage = Toast.makeText(rootView.getContext(), "Task Deleted Successfully!!!", Toast.LENGTH_LONG);
                                            View toastView = ToastMessage.getView();
                                            toastView.setBackgroundResource(R.drawable.toast_bground);
                                            ToastMessage.show();
                                        }else{
                                            Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                                            spinnerservice.setBackground(errorbg);
                                        }


                                    } catch (Exception ex) {
                                        Log.d("ReminderService In", ex.getMessage().toString());
                                    }

                                }
                            });
                            builder.show();




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
lstgross.setVisibility(View.GONE);
        lstgross1.setVisibility(View.VISIBLE);
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
            lstgross1.setAdapter(adapter);
            lstgross1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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



