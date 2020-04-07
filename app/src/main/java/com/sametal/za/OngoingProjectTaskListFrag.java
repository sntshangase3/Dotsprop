package com.sametal.za;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
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
public class OngoingProjectTaskListFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip;


    ImageView back,newtask;
    Button btn_detail, btn_photo;

    MainActivity activity = MainActivity.instance;
    byte[] byteArray;
    String service = "";
    ListView lstgross;

    Spinner spinnercategory;
    ArrayAdapter adapter;
    TextView txtfees,txtoustanding,txttotaltask;
    Bundle bundles = new Bundle();
    FragmentManager fragmentManager;

    ArrayList<String> item_taskvalue = new ArrayList<String>();
    ArrayList<String> item_dec = new ArrayList<String>();
    ArrayList<String> item_status = new ArrayList<String>();
    ArrayList<Integer> item_taskid = new ArrayList<Integer>();


     String selectedtaskvalue ;
    String selectedtaskdesc ;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.ongoingprojecttasklist, container, false);
        lstgross = (ListView) rootView.findViewById(R.id.lstgross);

        spinnercategory = (Spinner) rootView.findViewById(R.id.spinnercategory);
        back = (ImageView) rootView.findViewById(R.id.back);
        newtask = (ImageView) rootView.findViewById(R.id.newtask);
        txtfees = (TextView) rootView.findViewById(R.id.txtfees);
        txtoustanding = (TextView) rootView.findViewById(R.id.txtoustanding);
        txttotaltask = (TextView) rootView.findViewById(R.id.txttotaltask);


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
        FillCategoryData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               Fragment fragment = new HomeOngoingProject();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, fragment).commit();

            }
        });

        newtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Fragment fragment = new OnGoingProjectTaskRegister();
                FragmentManager fragmentManager = getFragmentManager();
                bundles.putString("newtask", "newtask");
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, fragment).commit();

            }
        });





        spinnercategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // item.toString()
                if (!spinnercategory.getSelectedItem().toString().equals("Select Category...")) {
                    try {
                      int category = spinnercategory.getSelectedItemPosition() ;
                     FillData(category);


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
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

    public void FillData(int projectcategoryid) {
        //==============Initialize list=


        try {
            String query = "SELECT count (t.id) as c " +
                    "  FROM [Dotsprop].[sqaloits].[UserOnGoingProjectTask] t\n" +
                    "  inner join [UserOngoingProjectService] s on s.id=t.projectserviceid" +
                    " where (taskstarted='No' or taskapproved='No' or taskonquery='No') and projectcategoryid='"+projectcategoryid+"' and userid=" + activity.id+"  and [projectid]='" + activity.projectid+"'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();



            rs.next();
            txtoustanding.setText(rs.getString("c")+" Outstanding Task");






            query = "SELECT t.id as task_id,*\n" +
                    "  FROM [Dotsprop].[sqaloits].[UserOnGoingProjectTask] t\n" +
                    "  inner join [UserOngoingProjectService] s on s.id=t.projectserviceid" +
                    " where projectcategoryid='"+projectcategoryid+"' and userid=" + activity.id+"  and [projectid]='" + activity.projectid+"'";


            Log.d("ReminderService In", query);
            item_taskvalue.clear();
            item_dec.clear();
            item_status.clear();
            item_taskid.clear();


             ps = con.prepareStatement(query);
             rs = ps.executeQuery();

           int total=0;
            int totaltask=0;
            while (rs.next()) {

                String  query2 = "select sum(cost) c " +
                        "  FROM [Dotsprop].[sqaloits].[UserOnGoingProjectTask] t \n" +
                        "  inner join [UserOngoingProjectService] s on s.id=t.projectserviceid" +
                        " where [service]='" + rs.getString("service")+"' and [projectid]='" + activity.projectid+"'";
                Log.d("ReminderService In", query2);
                PreparedStatement ps2 = con.prepareStatement(query2);
                ResultSet rs2 = ps2.executeQuery();
                rs2.next();
                totaltask+=1;
                if(!item_taskvalue.contains(rs.getString("service"))){


                        query = "SELECT service from [UserOngoingProjectService] s " +
                                " where s.id='"+(Integer.parseInt(rs.getString("projectserviceid")))+"'";
                        PreparedStatement ps1 = con.prepareStatement(query);
                        ResultSet rs1 = ps1.executeQuery();
                        rs1.next();
                        item_taskvalue.add(rs1.getString("service"));

                  /*  if(Integer.parseInt(rs.getString("projectcategoryid"))==2){

                        query = "SELECT service from [UserOngoingProjectService] s " +
                                " where s.id='"+(Integer.parseInt(rs.getString("projectserviceid")))+"'";
                        PreparedStatement ps1 = con.prepareStatement(query);
                        ResultSet rs1 = ps1.executeQuery();
                        rs1.next();
                        item_taskvalue.add(rs1.getString("service"));
                    }
                    if(Integer.parseInt(rs.getString("projectcategoryid"))==3){

                        query = "SELECT service from [UserOngoingProjectService] s " +
                                " where s.id='"+(Integer.parseInt(rs.getString("projectserviceid")))+"'";
                        PreparedStatement ps1 = con.prepareStatement(query);
                        ResultSet rs1 = ps1.executeQuery();
                        rs1.next();
                        item_taskvalue.add(rs1.getString("service"));
                    }*/

                    item_taskid.add(rs.getInt("task_id"));
                   // item_taskvalue.add(rs.getString("service"));
                    item_dec.add("R"+rs2.getString("c"));


                    String  query1 = "select Count(projectserviceid) as c from [UserOnGoingProjectTask] " +
                            " where projectserviceid='"+rs.getInt("projectserviceid")+"' and [projectid]='" + activity.projectid+"'";
                    PreparedStatement ps3 = con.prepareStatement(query1);
                    ResultSet rs3 = ps3.executeQuery();
                    rs3.next();
                    item_status.add(rs3.getString("c")+" Tasks");
                }

                total+=Integer.parseInt(rs.getString("cost"));
            }

            txttotaltask.setText(totaltask+" Total Tasks");

txtfees.setText("R"+String.valueOf(total));


            OngoingProjectTaskFragListAdapter adapter = new OngoingProjectTaskFragListAdapter(this.getActivity(), item_taskvalue, item_dec,item_status);
            lstgross.setAdapter(adapter);
            lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {



                       selectedtaskvalue = item_taskvalue.get(position);
                        selectedtaskdesc = item_dec.get(position);
                        String  query1 = "select t.id as task_id " +
                                "  FROM [Dotsprop].[sqaloits].[UserOnGoingProjectTask] t \n" +
                                "  inner join [UserOngoingProjectService] s on s.id=t.projectserviceid" +
                                " where [service]='" + selectedtaskvalue+"' and [projectid]='" + activity.projectid+"'";


                        PreparedStatement ps1 = con.prepareStatement(query1);
                        ResultSet rs1 = ps1.executeQuery();
                        final ArrayList<Integer> item_taskid = new ArrayList<Integer>();
                       while(rs1.next()){
                          item_taskid.add(rs1.getInt("task_id") );
                       }



                        if (currentSelectedView != null && currentSelectedView != view) {
                            unhighlightCurrentRow(currentSelectedView);
                        }
                        currentSelectedView = view;
                        highlightCurrentRow(currentSelectedView);

                       bundles.putString("selectedtaskvalue", selectedtaskvalue);

                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                            builder.setTitle(selectedtaskvalue);
                            builder.setIcon(rootView.getResources().getDrawable(R.drawable.jobs));
                            builder.setMessage("Details/Delete?");


                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {
                                        for(int taskid:item_taskid){
                                            String commands = "delete from [UserOnGoingProjectTask]  where [id]='" + taskid + "'";
                                            PreparedStatement preStmt = con.prepareStatement(commands);
                                            preStmt.executeUpdate();
                                        }

                                    Toast ToastMessage = Toast.makeText(rootView.getContext(), "Task Deleted Successfully!!!", Toast.LENGTH_LONG);
                                    View toastView = ToastMessage.getView();
                                    toastView.setBackgroundResource(R.drawable.toast_bground);
                                    ToastMessage.show();


                                } catch (Exception ex) {
                                    Log.d("ReminderService In", ex.getMessage().toString());
                                }

                            }
                        });
                            builder.setNegativeButton("Details", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    OnGoingProjectTaskRegister fragment = new OnGoingProjectTaskRegister();
                                    fragment.setArguments(bundles);
                                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

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




}



