package com.sametal.za;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by sibusiso
 */
public class UserProfileFrag extends Fragment {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip;
    EditText  edtfirstname,edtlastname,edtusername,edtemail ;
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> username = new ArrayList<String>();
    ArrayList<String> department = new ArrayList<String>();

    ArrayList<String> description = new ArrayList<String>();
    ArrayList<Boolean> isselected = new ArrayList<Boolean>();

    ArrayList<String> descriptioncommand = new ArrayList<String>();
    ArrayList<String> descriptionsystem = new ArrayList<String>();

    Button btncreate, btnsave;
    MainActivity activity = MainActivity.instance;
    ListView lstgross,lstroles,lstfuction;
    Spinner spinnertitle,spinnerdeparment;
    ArrayAdapter adapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.userprofile, container, false);

        lstgross = (ListView) rootView.findViewById(R.id.lstgross);
        lstroles = (ListView) rootView.findViewById(R.id.lstroles);
        lstfuction = (ListView) rootView.findViewById(R.id.lstfuction);

        edtfirstname = (EditText) rootView.findViewById(R.id.edtfirstname);
        edtlastname = (EditText) rootView.findViewById(R.id.edtlastname);
        edtemail = (EditText) rootView.findViewById(R.id.edtemail);
        edtusername = (EditText) rootView.findViewById(R.id.edtusername);

        spinnertitle = (Spinner) rootView. findViewById(R.id.spinnertitle);
        spinnerdeparment = (Spinner) rootView. findViewById(R.id.spinnerdeparment);

       btncreate = (Button) rootView.findViewById(R.id.btn_create);
        btnsave = (Button) rootView.findViewById(R.id.btn_save);


        // Declaring Server ip, username, database name and password
        ip = "192.168.100.97";
        db = "SAMDB2";
        un = "sa";
        pass = "ackentruselmde";
        try {
        ConnectionClass cn = new ConnectionClass();
        con = cn.connectionclass(un, pass, db, ip);
        if (con == null) {
            Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
        } } catch (Exception ex) {
        Log.d("ReminderService In", ex.getMessage() + "DSDSD");
    }


        FillData();
FillTitleData();
FillDepartmentData();
        edtemail.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                try{
                    Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                    Drawable bg = getResources().getDrawable(R.drawable.edittext_bground);
                    if((s.length() != 0) && (emailValidator(edtemail.getText().toString()))){
                        edtemail.setBackground(bg);
                    } else{
                        edtemail.setBackground(errorbg);
                    }
                }catch (Exception ex){

                }

            }
        });



        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   // CreateProfile addPro = new CreateProfile();
                   // addPro.execute("");



            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               // UpdateProfile updatePro = new UpdateProfile();
               // updatePro.execute("");

            }
        });


        return rootView;
    }


    private View currentSelectedView;

    public void FillTitleData() {
        //==============Fill Data=
        try {

            String query = "select [Description] from [Titles]";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();
            while (rs.next()) {
                category.add(rs.getString("Description"));
            }
            adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnertitle.setAdapter(adapter);

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
        }
//==========
    }
    public void FillDepartmentData() {
        //==============Fill Data=
        try {

            String query = "select [Description] from [Departments]";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();
            while (rs.next()) {
                category.add(rs.getString("Description"));
            }
            adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerdeparment.setAdapter(adapter);

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage());
        }
//==========
    }



    public void FillData() {
        //==============Fill Data=
        try {



                    String query = "  select FirstName,Username,LastName,t.Description,d.Description as Department,Email from SamUsers su\n" +
                            "  inner join Login l on l.ID=su.LoginID\n" +
                            "    inner join Titles t on t.ID=su.TitleID\n" +
                            "\t  inner join Departments d on d.ID=su.DepartmentID\n";

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                name.clear();
                username.clear();
                department.clear();
                    while(rs.next()){
                        Log.d("ReminderService In", "#####"+rs.getString("FirstName").toString().trim()+" "+rs.getString("LastName").toString().trim());
                        name.add(rs.getString("FirstName").toString().trim()+" "+rs.getString("LastName").toString().trim());
                        username.add(rs.getString("Username").toString().trim());
                        department.add(rs.getString("Department").toString().trim());
                    }
                final Context c=this.getActivity();
                UserProfileAdapter adapter = new UserProfileAdapter(this.getActivity(), name,username,department);
                lstgross.setAdapter(adapter);
                lstgross.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        // TODO Auto-generated method stub

                        try {

                            final  String selectedusername= username.get(position);
                            String query = "  select FirstName,LastName,l.Username ,t.Description,TitleID,d.ID as Department,Email from SamUsers su\n" +
                                    "  inner join Login l on l.ID=su.LoginID\n" +
                                    "    inner join Titles t on t.ID=su.TitleID\n" +
                                    "\t  inner join Departments d on d.ID=su.DepartmentID\n" +
                                    "\t   where Username='"+selectedusername+"'";
                            PreparedStatement ps = con.prepareStatement(query);
                            ResultSet rs = ps.executeQuery();
                            Log.d("ReminderService In",query);
                            while (rs.next()) {
                                if (currentSelectedView != null && currentSelectedView != view) {
                                    unhighlightCurrentRow(currentSelectedView);
                                }
                                currentSelectedView = view;
                                highlightCurrentRow(currentSelectedView);

                                try {

                                    edtfirstname.setText(rs.getString("FirstName").trim());
                                    edtlastname.setText(rs.getString("LastName").trim());
                                    edtemail.setText(rs.getString("email").trim());
                                    edtusername.setText(rs.getString("Username").trim());
                                    spinnertitle.setSelection(rs.getInt("TitleID"));
                                    spinnerdeparment.setSelection(rs.getInt("Department"));
                                } catch (Exception ex) {
                                    // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                    Log.d("ReminderService In", "#####"+ex.getMessage().toString());
                                }
                            }

                            //#######Populate roles########
                            description.clear();
                            isselected.clear();
                            query="select * from Roles";
                            ps = con.prepareStatement(query);
                            rs = ps.executeQuery();
                            while(rs.next()){
                                description.add(rs.getString("Description").toString().trim());
                               String query1="  select FirstName,r.Description,r.ID from SamUsers su\n" +
                                       "\t    full outer join UserRoles ur on ur.UserId=su.ID\n" +
                                       "\t\t full outer join Roles r on r.ID=ur.RoleId\n" +
                                       "\t\t where su.Email='"+edtemail.getText().toString().trim()+"'";
                                PreparedStatement ps1 = con.prepareStatement(query1);
                                ResultSet rs1 = ps1.executeQuery();
                                Boolean isselectedfound=false;
                                while(rs1.next()){
                                    if(rs.getInt("id")==rs1.getInt("id")){
                                        isselectedfound=true;
                                    }
                                }
                                isselected.add(isselectedfound);
                            }
                            RolesAdapter adapter1 = new RolesAdapter(getActivity(), description,isselected);
                            lstroles.setAdapter(adapter1);
                            lstroles.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        final int position, long id) {
                                    Boolean sel=isselected.get(position);
                                    if(sel){
                                        isselected.set(position,false);
                                    }else{
                                        isselected.set(position,true);
                                    }

                                }
                            });
                     //#################
                            descriptioncommand.clear();
                            descriptionsystem.clear();
                            String query1="  select f.Description as Description,a.Description as system from SamUsers su\n" +
                                    "\t    inner join UserFunctions uf on uf.UserId=su.ID\n" +
                                    "\t\t inner join Functions f on f.ID=uf.functionID\n" +
                                    "\t\t inner join Applications a on a.ID=f.ApplicationID" +
                                    "\t\t where su.Email='"+edtemail.getText().toString().trim()+"'";
                            PreparedStatement ps1 = con.prepareStatement(query1);
                            ResultSet rs1 = ps1.executeQuery();
                            while(rs1.next()){
                                descriptioncommand.add(rs.getString("Description").toString().trim());
                                descriptionsystem.add(rs.getString("system").toString().trim());
                            }
                            FunctionsAdapter adapter2 = new FunctionsAdapter(getActivity(), descriptioncommand,descriptionsystem);
                            lstfuction.setAdapter(adapter2);


                        } catch(Exception ex) {
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

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    /*
    public class CreateProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;


        String firstname = edtstaff.getText().toString();
        String position = edtposition.getText().toString();
        String email = edtemail.getText().toString();
        String password = edtpassword.getText().toString();
        String contact = edtsms.getText().toString();





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

                Intent i = new Intent(rootView.getContext(), MainActivity.class);
                startActivity(i);

            } else {
                Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                Drawable bg = getResources().getDrawable(R.drawable.edittext_bground);
                if ((edtstaff.getText().toString().trim().equals(""))) {
                    edtstaff.setBackground(errorbg);
                } else {
                    edtstaff.setBackground(bg);
                }

                if ((edtposition.getText().toString().trim().equals(""))) {
                    edtposition.setBackground(errorbg);
                } else {
                    edtposition.setBackground(bg);
                }


                if ((edtemail.getText().toString().trim().equals("")) ||!emailValidator(edtemail.getText().toString())) {
                    edtemail.setBackground(errorbg);
                } else {
                    edtemail.setBackground(bg);
                }

                if ((edtpassword.getText().toString().trim().equals(""))) {
                    edtpassword.setBackground(errorbg);
                } else {
                    edtpassword.setBackground(bg);
                }

                if ((edtsms.getText().toString().trim().equals(""))) {
                    edtsms.setBackground(errorbg);
                } else {
                    edtsms.setBackground(bg);
                }




                Toast.makeText(rootView.getContext(), r, Toast.LENGTH_SHORT).show();
            }

        }



        @Override
        protected String doInBackground(String... params) {
            if (firstname.trim().equals("")|| position.trim().equals("") || email.trim().equals("") || password.trim().equals("") || contact.trim().equals(""))
                z = "Please fill in all required details...";
            else {
                try {
                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    if (con == null) {
                        z = "Check your network connection!!";
                    } else {

                        if (!emailValidator(email)) {
                            z = "InValid Email Address";
                        }else{

                                String query = "insert into [sastaff]([password],[staff],[email],[sms],[position]) " +
                                        "values ('" + password + "','" + firstname + "','" + email + "','" + contact + "','" + position + "')";
                                PreparedStatement preparedStatement = con.prepareStatement(query);
                                preparedStatement.executeUpdate();
                                z = "Profile Created,please Login";
                                isSuccess = true;


                        }


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

    class UpdateProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;


        String firstname = edtstaff.getText().toString();
        String position = edtposition.getText().toString();
        String email = edtemail.getText().toString();
        String password = edtpassword.getText().toString();
        String contact = edtsms.getText().toString();



        String userid = edtappuserId.getText().toString();

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
                    if ((edtstaff.getText().toString().trim().equals(""))) {
                        edtstaff.setBackground(errorbg);
                    } else {
                        edtstaff.setBackground(bg);
                    }

                    if ((edtposition.getText().toString().trim().equals(""))) {
                        edtposition.setBackground(errorbg);
                    } else {
                        edtposition.setBackground(bg);
                    }


                    if ((edtemail.getText().toString().trim().equals("")) ||!emailValidator(edtemail.getText().toString())) {
                        edtemail.setBackground(errorbg);
                    } else {
                        edtemail.setBackground(bg);
                    }



                    if ((edtsms.getText().toString().trim().equals(""))) {
                        edtsms.setBackground(errorbg);
                    } else {
                        edtsms.setBackground(bg);
                    }
                    Toast.makeText(rootView.getContext(), r, Toast.LENGTH_SHORT).show();
                }


        }

        @Override
        protected String doInBackground(String... params) {
            if (firstname.trim().equals("")||   contact.trim().equals("")|| position.trim().equals("")|| email.trim().equals(""))
                z = "Please fill in all details...";
            else {
                try {
                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    if (con == null) {
                        z = "Check your network connection!!";

                    } else {

                            String query = "Update [sastaff] set [staff]='" + firstname + "',[email]='" + email + "',[position]='" + position + "',[sms]='" + contact + "' where [password]='" + userid+"'";;
                            PreparedStatement preparedStatement = con.prepareStatement(query);
                            preparedStatement.executeUpdate();
                            z = "Updated Successfully";
                            isSuccess = true;
                        }

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
*/


}



