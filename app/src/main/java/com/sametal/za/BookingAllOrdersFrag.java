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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * Created by sibusison on 2017/07/30.
 */
public class BookingAllOrdersFrag extends Fragment implements AdapterView.OnItemSelectedListener {


    View rootView;
    String m_Text_donate = "";
    String firstname = "", position = "";
    //---------con--------
    Connection con;
    String un, pass, db, ip;
    ListView lstgross;
    String currentid;

    int qty, id;

    String userid;


    ArrayList<String> itemid= new ArrayList<String>();;
    ArrayList<String> itemname= new ArrayList<String>();;
    ArrayList<String> itemdistance= new ArrayList<String>();;
    ArrayList<String> itemcollectiondate= new ArrayList<String>();;
    ArrayList<String> itemcollectionstatus= new ArrayList<String>();;


    ImageButton search;
    String search_product = "";
    Bundle bundles = new Bundle();

    SpinnerAdapter adapter = null;

    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;
    EditText edtsearch;
    TextView txtorderno, txtback;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.collectionallorderslist, container, false);


        lstgross = (ListView) rootView.findViewById(R.id.lstgross);
        edtsearch = (EditText) rootView.findViewById(R.id.edtsearch);
        txtorderno = (TextView) rootView.findViewById(R.id.txtorderno);
        txtback = (TextView) rootView.findViewById(R.id.txtback);
        search = (ImageButton) rootView.findViewById(R.id.search);


        fragmentManager = getFragmentManager();
        bundle = this.getArguments();
        // Declaring Server ip, username, database name and password
        ip = "atlasms.sam.cpt";
        db = "Dotsprop";
        un = "PaymentsQuoting";
        pass = "PaymentsQuoting";
        ConnectionClass cn = new ConnectionClass();
        con = cn.connectionclass(un, pass, db, ip);
        if (con == null) {
            Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
        }

        try {
            userid = activity.edthidenuserid.getText().toString();
            String query = "select * from [sastaff] where [password]='" + userid+"'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            rs.next();
            firstname = rs.getString("staff").trim();
            position = rs.getString("position").trim();

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage() + "######");
        }


        try {
            FillDataOrderByStatus(search_product);

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage() + "######");

        }
        try {
            if (bundle != null) {
                if (!bundle.getString("name").equals("")) {
                    search_product = bundle.getString("name");
                    FillDataOrderByStatus(search_product);
                }
            }
        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage() + "######");

        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_product = edtsearch.getText().toString();
                FillDataOrderByStatus(search_product);
            }
        });


        txtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag = new BookingRequestFrag();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();
            }
        });

        return rootView;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        // Toast.makeText(rootView.getContext(), "You've selected " + name.get(position) , Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date2.getTime() - date1.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }

    private View currentSelectedView;

    public void FillDataOrderByStatus(String search) {
        //==============Initialize list=
        try {
            ip = "winsqls01.cpt.wa.co.za";
            db = "SqaloITSolutionsTest";
            un = "sqaloits";
            pass = "422q5mfQzU";
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            String query;

            if (position.equals("Administrator")) {
                query = "select  * from [Collection] where [collectionstatus]='New' order by [collectionstatus] asc";
                if (!search.equals("")) {
                    query = "select  * from [Collection] where ([customername] like '%" + search + "%' or [location] like '%" + search + "%') order by [collectionstatus] asc";
                }
            } else {
                query = "select  * from [Collection] where [custno]='" + userid+"' order by [collectionstatus] asc";
                if (!search.equals("")) {
                    query = "select  * from [Collection]  where ([customername] like '%" + search + "%' or [location] like '%" + search + "%') and custno='" + userid + "' order by [collectionstatus] asc";
                }
            }
            //Co-user login

            PreparedStatement ps1 = con.prepareStatement(query);
            ResultSet rs1 = ps1.executeQuery();
            int total = 0;
            while (rs1.next()) {
                total = total + 1;
            }
            txtorderno.setText(String.valueOf(total));


            Log.d("ReminderService In", query);
            itemid.clear();
            itemname.clear();
            itemdistance.clear();
            itemcollectiondate.clear();
            itemcollectionstatus.clear();

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                itemid.add(rs.getString("id").toString());
                itemname.add(rs.getString("customername").toString());
                itemdistance.add(rs.getString("collectiondistance").toString());
                itemcollectiondate.add(rs.getString("collectionsdate").toString());
                itemcollectionstatus.add(rs.getString("collectionstatus").toString());

            }
            BookingAllOrderAdapter adapter = new BookingAllOrderAdapter(this.getActivity(), itemid, itemname, itemdistance, itemcollectiondate, itemcollectionstatus);
            lstgross.setAdapter(adapter);
            lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    // TODO Auto-generated method stub

                    try {

                        String selectedname = itemname.get(position);
                        final String selectedid = itemid.get(position);


                        bundles.putString("id", selectedid);
                        bundles.putString("name", selectedname);


                        if (currentSelectedView != null && currentSelectedView != view) {
                            unhighlightCurrentRow(currentSelectedView);
                        }
                        currentSelectedView = view;
                        highlightCurrentRow(currentSelectedView);

                        try {
                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                            builder.setTitle(selectedname);
                            builder.setIcon(rootView.getResources().getDrawable(R.drawable.addcart));
                            builder.setMessage("View/Delete Collection Details?");
                            builder.setPositiveButton("View Details", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                            CollectionFinalFrag fragment = new CollectionFinalFrag();
                                           fragment.setArguments(bundles);
                                            fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                                }
                            });
                            builder.setNegativeButton("Delete Request", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        String commands = "delete from [Collection]  where [id]='" + selectedid + "'";
                                        PreparedStatement preStmt = con.prepareStatement(commands);
                                        preStmt.executeUpdate();

                                        Toast ToastMessage = Toast.makeText(rootView.getContext(), "Collection Deleted Successfully!!!", Toast.LENGTH_LONG);
                                        View toastView = ToastMessage.getView();
                                        toastView.setBackgroundResource(R.drawable.toast_bground);
                                        ToastMessage.show();
                                    } catch (Exception ex) {

                                    }

                                }
                            });
                            builder.show();


                        } catch (Exception ex) {
                            //Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                            Log.d("ReminderService In", ex.getMessage().toString());
                        }


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

    private void highlightCurrentRow(View rowView) {

        rowView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }


    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);

    }


}


