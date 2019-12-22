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
import android.graphics.drawable.Drawable;
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
public class RegisterPropertyFrag extends Fragment {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip;

    Button btncreate,btnsave;
    MainActivity activity = MainActivity.instance;
    byte[] byteArray;
    String encodedImage;
    ImageView propertyprofileImage;

    FragmentManager fragmentManager;
    Spinner spinnerpaystatus,spinnerprppertyname;
    EditText  edtlocation,edtaddress,edtpaymentamount, edtmunicipalityvaluation,edtindependentvaluation, edtrangemarketprice,edtnumberofbedroom,edtnumberofbathroom,
            edtotherrooms,edthasswimmingpool,edtlapaOrintertainment;
    int propertyexist=0;
    ArrayAdapter adapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.registerproperty, container, false);


        edtlocation = (EditText) rootView.findViewById(R.id.edtlocation);
        edtaddress = (EditText) rootView.findViewById(R.id.edtaddress);
        edtpaymentamount = (EditText) rootView.findViewById(R.id.edtpaymentamount);
        edtmunicipalityvaluation = (EditText) rootView.findViewById(R.id.edtmunicipalityvaluation);
        edtindependentvaluation = (EditText) rootView.findViewById(R.id.edtindependentvaluation);
        edtrangemarketprice = (EditText) rootView.findViewById(R.id.edtrangemarketprice);
        edtnumberofbedroom = (EditText) rootView.findViewById(R.id.edtnumberofbedroom);
        edtnumberofbathroom = (EditText) rootView.findViewById(R.id.edtnumberofbathroom);
        edtotherrooms = (EditText) rootView.findViewById(R.id.edtotherrooms);
        edthasswimmingpool = (EditText) rootView.findViewById(R.id.edthasswimmingpool);
        edtlapaOrintertainment = (EditText) rootView.findViewById(R.id.edtlapaOrintertainment);
        propertyprofileImage = (ImageView) rootView.findViewById(R.id.propertyprofileImage);
       btncreate = (Button) rootView.findViewById(R.id.btn_create);
        btnsave = (Button) rootView.findViewById(R.id.btn_save);
        spinnerpaystatus = (Spinner) rootView.findViewById(R.id.spinnerpaystatus);
        spinnerprppertyname = (Spinner) rootView.findViewById(R.id.spinnerprppertyname);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.status_arrays, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerpaystatus.setAdapter(adapter);
        fragmentManager = getFragmentManager();
        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "Dotsprop";
        un = "sqaloits";
        pass = "422q5mfQzU";

        ConnectionClass cn = new ConnectionClass();
        con = cn.connectionclass(un, pass, db, ip);

FillPropertyData();
        spinnerprppertyname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                // item.toString()
                if(!spinnerprppertyname.getSelectedItem().toString().equals("Select Property")){
                    try{

                   FillData(spinnerprppertyname.getSelectedItem().toString());
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
                    UpdateProfile updatePro = new UpdateProfile();
                    updatePro.execute("");
                    HomeFragment fragment = new HomeFragment();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });

        propertyprofileImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        return rootView;
    }

    public void FillPropertyData() {
        //==============Fill Data=
        try {

            String query = "select * from [UserProperty] where [userid]='" + activity.id + "'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();
            category.add("Select Property");
            while (rs.next()) {
                category.add(rs.getString("location"));
                propertyexist=propertyexist+1;
            }
            adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerprppertyname.setAdapter(adapter);
            if(propertyexist!=0){
                spinnerprppertyname.setVisibility(View.VISIBLE);
               // spinnerprppertyname.setSelection(1);
               //FillData(spinnerprppertyname.getSelectedItem().toString());
            }

        } catch (Exception ex) {
            // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here",Toast.LENGTH_LONG).show();
        }
//==========
    }

    public void FillData(String location) {
        //==============Fill Data=
        try {

                    String  query = "select  * from [UserProperty] where [location]='" + location+"'";

                   PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    rs.next();

                    if (rs.getRow() != 0) {
                        edtlocation.setText(rs.getString("location"));
                        edtaddress.setText(rs.getString("address").trim());
                           edtpaymentamount.setText(rs.getString("paymentamount"));
                        edtmunicipalityvaluation.setText(rs.getString("municipalityvaluation"));
                        edtindependentvaluation.setText(rs.getString("independentvaluation"));
                        edtrangemarketprice.setText(rs.getString("rangemarketprice"));
                        edtnumberofbedroom.setText(rs.getString("numberofbedroom"));
                        edtnumberofbathroom.setText(rs.getString("numberofbathroom").trim());
                        edtotherrooms.setText(rs.getString("otherrooms"));
                        edthasswimmingpool.setText(rs.getString("hasswimmingpool"));
                        edtlapaOrintertainment.setText(rs.getString("lapaOrintertainment"));

                        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.status_arrays, R.layout.spinner_item);
                        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinnerpaystatus.setSelection(adapter1.getPosition(rs.getString("paymentstatus")));
                        spinnerprppertyname.setSelection(adapter.getPosition(location));
                        if (rs.getString("image") != null) {
                            byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                            Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                            propertyprofileImage.setImageBitmap(decodebitmap);
                            encodedImage = rs.getString("image");
                        } else {

                            propertyprofileImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.profilephoto));

                        }

                    }

        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }
//==========
    }



    public class CreateProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;


        String location = edtlocation.getText().toString();
        String address = edtaddress.getText().toString();
        String paystatus = spinnerpaystatus.getSelectedItem().toString();
        String amountpay = edtpaymentamount.getText().toString();
        String municipality = edtmunicipalityvaluation.getText().toString();
        String independent = edtindependentvaluation.getText().toString();
        String marketprice = edtrangemarketprice.getText().toString();
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
                    ConnectionClass cn = new ConnectionClass();
                    con = cn.connectionclass(un, pass, db, ip);
                    if (con == null) {
                        z = "Check your network connection!!";
                    } else {


                                String query = "insert into [UserProperty]([location],[address],[paymentstatus],[paymentamount]\n\n" +
                                        "      ,[image]\n" +
                                        "      ,[municipalityvaluation]\n" +
                                        "      ,[independentvaluation]\n" +
                                        "      ,[rangemarketprice]\n" +
                                        "      ,[numberofbedroom]\n" +
                                        "      ,[numberofbathroom]\n" +
                                        "      ,[otherrooms]\n" +
                                        "      ,[hasswimmingpool]\n" +
                                        "      ,[lapaOrintertainment],[userid]) " +
                                        "values ('" + location + "','" + address + "','" + paystatus + "','" + amountpay + "','" + encodedImage + "','" + municipality + "','" + independent + "'," +
                                        "'" + marketprice + "','" + bedroomno + "','" + bathroomno + "','" + otherroom + "','" + swimming + "','" + lapa + "','"+Integer.parseInt(activity.id)+"')";
                                PreparedStatement preparedStatement = con.prepareStatement(query);
                                preparedStatement.executeUpdate();
                                z = "New Property Profile Created!!!";
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
    public class UpdateProfile extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;


        String location = edtlocation.getText().toString();
        String address = edtaddress.getText().toString();
        String paystatus = spinnerpaystatus.getSelectedItem().toString();
        String amountpay = edtpaymentamount.getText().toString();
        String municipality = edtmunicipalityvaluation.getText().toString();
        String independent = edtindependentvaluation.getText().toString();
        String marketprice = edtrangemarketprice.getText().toString();
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
    }

    //===Upload profile
    private void selectImage() {


        final CharSequence[] options = {"Camera", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());

        builder.setTitle("Property Photo");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Camera"))

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(rootView.getContext(), "You grant write external storage permission", Toast.LENGTH_LONG).show();
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getActivity(), "Reopen app and allow permission.", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }

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
                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

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
                        propertyprofileImage.setImageBitmap(bitmap);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
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



                try {
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    thumbnail = Bitmap.createScaledBitmap(thumbnail, thumbnail.getWidth(), thumbnail.getHeight(), true);
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

                    propertyprofileImage.setImageBitmap(thumbnail);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byteArray = stream.toByteArray();

                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    // encodedImage= byteArray.toString();
                } catch (IOException io) {

                }




            }

        }

    }
    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }


}



