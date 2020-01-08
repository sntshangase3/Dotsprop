package com.sametal.za;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;

/**
 * Created by Sbusiso.
 */
public class HomeContractor extends Fragment {
    private static final int REQUEST_CODE = 1;
    Button btnBook,btnCall;
    TextView txttotalyears, txttotalrequest, txttotalemployees, txttprovince;
    ImageView  profilePic;
    View rootView;
    Bundle bundle;
    MainActivity activity =   MainActivity.instance;

    //---------con--------
    Connection con;
    String un,pass,db,ip;

    public HomeContractor(){

        super();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.homecontractor,
                container, false);


        btnBook = (Button) rootView.findViewById(R.id.btn_book);
        btnCall = (Button) rootView.findViewById(R.id.btn_call);

        txttotalyears = (TextView) rootView.findViewById(R.id.txttotalyears);
        txttotalrequest = (TextView) rootView.findViewById(R.id.txttotalrequest);
        txttotalemployees = (TextView) rootView.findViewById(R.id.txttotalemployees);
        txttprovince = (TextView) rootView.findViewById(R.id.txttprovince);

        profilePic = (ImageView) rootView.findViewById(R.id.profilePic);

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




        btnBook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });



        return rootView;
    }


}
