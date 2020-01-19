package com.sametal.za;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;


/**
 * Created by sibusison on 2017/07/30.
 */
public class RegisterContractorFrag extends Fragment {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip;

    EditText edtcontactname, edtbusinessname,edtaddress,edtservice,  edtemail,edtpassword, edtcontactno,edtrelatedfield,edtnumberofserviceyears,edtnumberofemployees;
    ImageView edtprofileImage;
    Button btncreate, btnsave;

    MainActivity activity = MainActivity.instance;
    byte[] byteArray;
    String encodedImage,service="";

    Spinner spinnerprovince,spinnerservice;
    ArrayAdapter adapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.registercontructor, container, false);

        edtcontactname = (EditText) rootView.findViewById(R.id.edtcontactname);
        edtbusinessname = (EditText) rootView.findViewById(R.id.edtbusinessname);
        edtaddress = (EditText) rootView.findViewById(R.id.edtaddress);
        spinnerprovince = (Spinner) rootView.findViewById(R.id.spinnerprovince);
        spinnerservice = (Spinner) rootView.findViewById(R.id.spinnerservice);
        edtcontactno = (EditText) rootView.findViewById(R.id.edtcontact);
        edtemail = (EditText) rootView.findViewById(R.id.edtemail);
        edtpassword = (EditText) rootView.findViewById(R.id.edtpassword);
        edtrelatedfield = (EditText) rootView.findViewById(R.id.edtrelatedfield);

        edtnumberofserviceyears = (EditText) rootView.findViewById(R.id.edtnumberofserviceyears);
        edtnumberofemployees = (EditText) rootView.findViewById(R.id.edtnumberofemployees);
        edtservice = (EditText) rootView.findViewById(R.id.edtservice);
        btncreate = (Button) rootView.findViewById(R.id.btn_create);
        btnsave = (Button) rootView.findViewById(R.id.btn_save);
        edtprofileImage = (ImageView) rootView.findViewById(R.id.propertyprofileImage);


        ArrayAdapter adapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.province_arrays, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerprovince.setAdapter(adapter);


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

        FillData();
        Bundle bundle = this.getArguments();


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

                    CreateProfile addPro = new CreateProfile();
                    addPro.execute("");



            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                UpdateProfile updatePro = new UpdateProfile();
                updatePro.execute("");

            }
        });
        edtprofileImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

FillServiceData();
        spinnerservice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                      // item.toString()
                if(!spinnerservice.getSelectedItem().toString().equals("Select Service")){
                    try{
                        service=service+spinnerservice.getSelectedItem().toString()+",";
                        edtservice.setText(service);

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

            String query = "select * from [Service]";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();
            category.add("Select Service");
            while (rs.next()) {
                category.add(rs.getString("service"));

            }
            adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerservice.setAdapter(adapter);


        } catch (Exception ex) {
            // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here",Toast.LENGTH_LONG).show();
        }
//==========
    }
    //===Upload profile
    private void selectImage() {


        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                    startActivityForResult(intent, 1);

                } else if (options[item].equals("Choose from Gallery"))

                {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);


                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }


    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {

                    if (temp.getName().equals("temp.jpg")) {

                        f = temp;

                        break;

                    }

                }

                try {

                    Bitmap bitmap;

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();


                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);

                    try {
                        ExifInterface ei = new ExifInterface(f.getAbsolutePath());
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        switch (orientation) {

                            case ExifInterface.ORIENTATION_ROTATE_90:
                                bitmap = rotateImage(bitmap, 90);
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                bitmap = rotateImage(bitmap, 90);
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                bitmap = rotateImage(bitmap, 270);

                        }

                    } catch (IOException io) {

                    }


                    try {
                        edtprofileImage.setImageBitmap(bitmap);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byteArray = stream.toByteArray();
                        encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);


                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor c = rootView.getContext().getApplicationContext().getContentResolver().query(selectedImage, filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);

                c.close();


                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                thumbnail = Bitmap.createScaledBitmap(thumbnail, 100, 100, true);
                try {
                    ExifInterface ei = new ExifInterface(picturePath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            thumbnail = rotateImage(thumbnail, 90);
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            thumbnail = rotateImage(thumbnail, 90);
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            thumbnail = rotateImage(thumbnail, 270);


                    }

                } catch (IOException io) {

                }


                edtprofileImage.setImageBitmap(thumbnail);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
                encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            }

        }

    }

    //======
    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        //Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }

    public void FillData() {
        //==============Fill Data=
        try {

                    String query = "select * from [Contractor] where [id]=" + Integer.parseInt(activity.id);
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    rs.next();


                    if (rs.getRow() != 0) {

                        edtcontactname.setText(rs.getString("contactname"));
                        edtbusinessname.setText(rs.getString("businessname"));
                        edtemail.setText(rs.getString("email"));
                        edtpassword.setText(rs.getString("password"));
                        edtcontactno.setText(rs.getString("contact"));
                        edtaddress.setText(rs.getString("address"));

                        edtservice.setText(rs.getString("service"));
                        edtrelatedfield.setText(rs.getString("relatedfield"));
                        edtnumberofserviceyears.setText(rs.getString("numberofserviceyears"));
                        edtnumberofemployees.setText(rs.getString("numberofemployees"));

                        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.status_arrays, R.layout.spinner_item);
                        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinnerprovince.setSelection(adapter1.getPosition(rs.getString("province")));


                        if (rs.getString("image") != null) {
                            byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                            Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                            edtprofileImage.setImageBitmap(decodebitmap);
                            encodedImage = rs.getString("image");
                        } else {

                            edtprofileImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.profilephoto));

                        }



                    }

        } catch (Exception ex) {
            // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here",Toast.LENGTH_LONG).show();
        }
//==========
    }
    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public class CreateProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;


        String contacttname = edtcontactname.getText().toString();
        String businessname = edtbusinessname.getText().toString();
        String province = spinnerprovince.getSelectedItem().toString();
        String address = edtaddress.getText().toString();
        String email = edtemail.getText().toString();
        String password = edtpassword.getText().toString();


        String numberofyears = edtnumberofserviceyears.getText().toString();
        String numberofemployees = edtnumberofemployees.getText().toString();
        String contact = edtcontactno.getText().toString();
        String relatedfield = edtrelatedfield.getText().toString();
        String service = edtservice.getText().toString().substring(0, edtservice.getText().toString().length() - 1);


        // int birthyear =Integer.parseInt( spinnerbirthyear.getSelectedItem().toString());
        // String gender = spinnergender.getSelectedItem().toString();
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
                if ((edtcontactname.getText().toString().trim().equals(""))) {
                    edtcontactname.setBackground(errorbg);
                } else {
                    edtcontactname.setBackground(bg);
                }

                if ((edtbusinessname.getText().toString().trim().equals(""))) {
                    edtbusinessname.setBackground(errorbg);
                } else {
                    edtbusinessname.setBackground(bg);
                }
                if (( spinnerprovince.getSelectedItem().toString().equals("Select Province"))) {
                    spinnerprovince.setBackground(errorbg);
                } else {
                    spinnerprovince.setBackground(bg);
                }
                if ((edtaddress.getText().toString().trim().equals(""))) {
                    edtaddress.setBackground(errorbg);
                } else {
                    edtaddress.setBackground(bg);
                }
                if ((edtpassword.getText().toString().trim().equals(""))) {
                    edtpassword.setBackground(errorbg);
                } else {
                    edtpassword.setBackground(bg);
                }

                if ((edtemail.getText().toString().trim().equals("")) ||!emailValidator(edtemail.getText().toString())) {
                    edtemail.setBackground(errorbg);
                } else {
                    edtemail.setBackground(bg);
                }

                if ((edtnumberofemployees.getText().toString().trim().equals(""))) {
                    edtnumberofemployees.setBackground(errorbg);
                } else {
                    edtnumberofemployees.setBackground(bg);
                }

                if ((edtcontactno.getText().toString().trim().equals(""))) {
                    edtcontactno.setBackground(errorbg);
                } else {
                    edtcontactno.setBackground(bg);
                }
                if ((edtnumberofserviceyears.getText().toString().trim().equals(""))) {
                    edtnumberofserviceyears.setBackground(errorbg);
                } else {
                    edtnumberofserviceyears.setBackground(bg);
                }



                if ((z.equals("Email Address Already Exist"))) {
                    edtemail.setBackground(errorbg);
                } else {
                    edtemail.setBackground(bg);
                }

                Toast.makeText(rootView.getContext(), r, Toast.LENGTH_LONG).show();
            }

        }



        @Override
        protected String doInBackground(String... params) {
            if (contacttname.trim().equals("")|| businessname.trim().equals("")|| province.trim().equals("")||email.trim().equals("")||password.trim().equals("")||contact.trim().equals("")|| service.trim().equals("")|| numberofemployees.trim().equals("") || numberofyears.trim().equals("") || relatedfield.trim().equals("") )
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
                            String query1 = "select * from [AppUser] where [email]='" + email+"'";
                            PreparedStatement ps = con.prepareStatement(query1);
                            ResultSet rs = ps.executeQuery();
                            rs.next();
                            if (rs.getRow() != 0) {
                                z = "Email Address Already Exist";
                            }else{



                                    String query = "insert into [Contractor]([contactname],[businessname],[province],[address],[email],[contact],[service],[numberofserviceyears],[numberofemployees],[relatedfield],[image],[userid]) " +
                                            "values ('" + contacttname + "','" + businessname + "','" + province + "','" + address + "','" + email + "','" + password + "','" + contact + "','" + service + "','" +numberofyears + "','" + numberofemployees + "','" + relatedfield + "','" + encodedImage + "')";
                                    PreparedStatement preparedStatement = con.prepareStatement(query);
                                    preparedStatement.executeUpdate();
                                    z = "Profile Created";
                                    isSuccess = true;



                            }


                        }


                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    // z = "Check your network connection!!";
                    z=ex.getMessage();
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
            return z;
        }
    }

    //===================

    class UpdateProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;


        String contacttname = edtcontactname.getText().toString();
        String businessname = edtbusinessname.getText().toString();
        String province = spinnerprovince.getSelectedItem().toString();
        String address = edtaddress.getText().toString();
        String email = edtemail.getText().toString();
        String password = edtemail.getText().toString();


        String numberofyears = edtnumberofserviceyears.getText().toString();
        String numberofemployees = edtnumberofemployees.getText().toString();
        String contact = edtcontactno.getText().toString();
        String relatedfield = edtrelatedfield.getText().toString();
        String service = edtservice.getText().toString();

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
                if ((edtcontactname.getText().toString().trim().equals(""))) {
                    edtcontactname.setBackground(errorbg);
                } else {
                    edtcontactname.setBackground(bg);
                }

                if ((edtbusinessname.getText().toString().trim().equals(""))) {
                    edtbusinessname.setBackground(errorbg);
                } else {
                    edtbusinessname.setBackground(bg);
                }
                if (( spinnerprovince.getSelectedItem().toString().equals("Select Province"))) {
                    spinnerprovince.setBackground(errorbg);
                } else {
                    spinnerprovince.setBackground(bg);
                }
                if ((edtaddress.getText().toString().trim().equals(""))) {
                    edtaddress.setBackground(errorbg);
                } else {
                    edtaddress.setBackground(bg);
                }

                if ((edtpassword.getText().toString().trim().equals(""))) {
                    edtpassword.setBackground(errorbg);
                } else {
                    edtpassword.setBackground(bg);
                }

                if ((edtemail.getText().toString().trim().equals("")) ||!emailValidator(edtemail.getText().toString())) {
                    edtemail.setBackground(errorbg);
                } else {
                    edtemail.setBackground(bg);
                }

                if ((edtnumberofemployees.getText().toString().trim().equals(""))) {
                    edtnumberofemployees.setBackground(errorbg);
                } else {
                    edtnumberofemployees.setBackground(bg);
                }

                if ((edtcontactno.getText().toString().trim().equals(""))) {
                    edtcontactno.setBackground(errorbg);
                } else {
                    edtcontactno.setBackground(bg);
                }
                if ((edtnumberofserviceyears.getText().toString().trim().equals(""))) {
                    edtnumberofserviceyears.setBackground(errorbg);
                } else {
                    edtnumberofserviceyears.setBackground(bg);
                }



                if ((z.equals("Email Address Already Exist"))) {
                    edtemail.setBackground(errorbg);
                } else {
                    edtemail.setBackground(bg);
                }
                Toast.makeText(rootView.getContext(), r, Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected String doInBackground(String... params) {
            if (contacttname.trim().equals("")|| businessname.trim().equals("")|| province.trim().equals("")||email.trim().equals("")||password.trim().equals("")||contact.trim().equals("")|| service.trim().equals("")|| numberofemployees.trim().equals("") || numberofyears.trim().equals("") || relatedfield.trim().equals("") )
                z = "Please fill in all details...";
            else {
                try {

                    String query = "update [Contractor] set [contactname]='" + contacttname + "',[businessname]='" + businessname + "',[province]='" + province + "',[address]='" + address + "',[email]='" + email + "',[password]='" + password + "',[contact]='" + contact + "',[service]='" + service + "',[numberofserviceyears]='" +numberofyears + "',[numberofemployees]='" + numberofemployees + "',[relatedfield]='" + relatedfield + "',[image]='" + encodedImage + "' where [userid]='" + activity.id + "'";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                            preparedStatement.executeUpdate();
                            z = "Updated Successfully";
                            isSuccess = true;

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


  /*  public void LatLon(String address){
        try{
            Geocoder geocoder;
            geocoder = new Geocoder(rootView.getContext(), Locale.getDefault());
            double Lat = geocoder.getFromLocationName(address, 1).get(0).getLatitude();
            double Lon = geocoder.getFromLocationName(address, 1).get(0).getLongitude();
            lonlat="lat/lng: ("+String.valueOf(Lat)+","+String.valueOf(Lon)+")";

        }catch (Exception e){
            Toast.makeText(rootView.getContext(), "Slow network connection,wait...!!", Toast.LENGTH_LONG).show();
        }

    }*/

}



