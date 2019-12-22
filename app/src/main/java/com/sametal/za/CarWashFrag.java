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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;




import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Sbusiso.
 */
public class CarWashFrag extends Fragment implements AdapterView.OnItemSelectedListener {
    View rootView;
    FragmentManager fragmentManager;

    ArrayList<String> item_payment_number = new ArrayList<String>();

    ArrayList<String> item_customer_name = new ArrayList<String>();
    ArrayList<String> item_transaction_type = new ArrayList<String>();
    ArrayList<String> item_total = new ArrayList<String>();
    ArrayList<String> item_changed_by = new ArrayList<String>();
    ArrayList<String> item_branch = new ArrayList<String>();
    Connection con;
    String un, pass, db, ip;

    ListView lstgross;
    ImageView edtlogoImage;
    Bundle bundle;

    MainActivity activity = MainActivity.instance;

    int userid;

    public CarWashFrag() {

        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.advancereport, container, false);
        lstgross = (ListView) rootView.findViewById(R.id.lstgross);
        fragmentManager = getFragmentManager();


        // Declaring Server ip, username, database name and password
        ip = "atlasms.sam.cpt";
        db = "Dotsprop";
        un = "PaymentsQuoting";
        pass = "PaymentsQuoting";
        ConnectionClass cn = new ConnectionClass();
        con = cn.connectionclass(un, pass, db, ip);
        if (con == null)
        {
            Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
        }

        bundle = this.getArguments();


        try {
            FillDataReleasePayment();

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
            Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
        }

        return rootView;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        //Toast.makeText(rootView.getContext(), "You've selected " + String.valueOf(position)+" "+pokeId+" "+l , Toast.LENGTH_LONG).show();
        // Log.d("POKEMON", "You've selected " + String.valueOf(position)+" "+pokeId+" "+l);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private View currentSelectedView;
    public void FillDataReleasePayment() {
        //==============Initialize list=
        try {

            String query = "SELECT  convert(varchar, AdjustmentDate, 120) as date,AdvanceNumber,Status,Branch,BalanceBefore,ByWho FROM [AdvanceAdjustments] " +
                    " where [Status] ='A'";

            item_branch.clear();
            item_changed_by.clear();
            item_customer_name.clear();

            item_payment_number.clear();
            item_total.clear();
            item_transaction_type.clear();

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               double cost=Double.valueOf(rs.getString("BalanceBefore").toString().trim());
                item_payment_number.add(rs.getString("date").toString().trim());
                item_transaction_type.add(rs.getString("AdvanceNumber").toString().trim());
                item_customer_name.add(rs.getString("Status").toString().trim());
                item_changed_by.add(rs.getString("ByWho").toString().trim());
                item_branch.add(rs.getString("Branch").toString().trim());
               item_total.add("R"+cost);
            }
            CarWashFragAdapter adapter = new CarWashFragAdapter(this.getActivity(), item_payment_number,item_customer_name,item_transaction_type,item_total,item_changed_by,item_branch);
            lstgross.setAdapter(adapter);
            lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {


                      final  String selectedpayno= item_transaction_type.get(position);
                        String query = "SELECT * FROM [AdvanceAdjustments] p " +
                                " where [Status] ='A' and  AdvanceNumber='" + selectedpayno +"'";
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();
                        Log.d("ReminderService In",query);
                        while (rs.next()) {
                            final String CustCodeBefore=rs.getString("AdvanceNumber").toString().trim();
                            if (currentSelectedView != null && currentSelectedView != view) {
                                unhighlightCurrentRow(currentSelectedView);
                            }
                            currentSelectedView = view;
                            highlightCurrentRow(currentSelectedView);

                            try {
                                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                builder.setTitle(selectedpayno);
                                builder.setIcon(rootView.getResources().getDrawable(R.drawable.add_search));
                                builder.setMessage("Approve/Decline Payment?");
                                builder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            String commands = "update [AdvanceAdjustments] set [Status] ='A' where AdvanceNumber='" + selectedpayno +"'";
                                            PreparedStatement preStmt = con.prepareStatement(commands);
                                           // preStmt.executeUpdate();

                                            Toast ToastMessage = Toast.makeText(rootView.getContext(),"Payment Approved Successfully!!!",Toast.LENGTH_LONG);
                                            View toastView = ToastMessage.getView();
                                            toastView.setBackgroundResource(R.drawable.toasttext_bground);
                                            ToastMessage.show();
                                            CarWashFrag fragment = new CarWashFrag();
                                            FragmentManager fragmentManager = getFragmentManager();
                                            fragmentManager.beginTransaction()
                                                    .replace(R.id.mainFrame, fragment).commit();
                                            dialog.cancel();
                                        }catch (Exception ex) {

                                        }
                                    }
                                });
                                builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            String commands = "update [AdvanceAdjustments] set [Status] ='D' where [Status] ='A'  and AdvanceNumber='" + selectedpayno +"'";
                                          PreparedStatement preStmt = con.prepareStatement(commands);
                                            // preStmt.executeUpdate();

                                            Toast ToastMessage = Toast.makeText(rootView.getContext(),"Payment Declined Successfully!!!",Toast.LENGTH_LONG);
                                            View toastView = ToastMessage.getView();
                                            toastView.setBackgroundResource(R.drawable.toasttext_bground);
                                            ToastMessage.show();
                                            CarWashFrag fragment = new CarWashFrag();
                                            FragmentManager fragmentManager = getFragmentManager();
                                            fragmentManager.beginTransaction()
                                                    .replace(R.id.mainFrame, fragment).commit();
                                            dialog.cancel();
                                        }catch (Exception ex) {

                                        }

                                    }
                                });
                                builder.show();



                            } catch (Exception ex) {
                               // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                Log.d("ReminderService In", "#####"+ex.getMessage().toString());
                            }


                        }
                    } catch(Exception ex) {
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

    private void highlightCurrentRow(View rowView) {

        rowView.setBackgroundColor(getResources().getColor(R.color.focus_box_frame));

    }


    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);

    }
}








