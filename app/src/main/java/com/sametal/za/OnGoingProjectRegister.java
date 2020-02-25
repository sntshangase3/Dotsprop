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
public class OnGoingProjectRegister extends Fragment {

    View rootView;

    //---------con--------
    Connection con;
    String un, pass, db, ip;
    Button btncreate,btndelete;
    MainActivity activity = MainActivity.instance;
    byte[] byteArray;
    String encodedImage;
    ImageView propertyprofileImage;

    FragmentManager fragmentManager;
    Spinner spinnerprojectname;
    EditText edtprojectname;
    int propertyexist=0;
    ArrayAdapter adapter;
    ArrayList<String> projectphotos = new ArrayList<String>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.ongoingprojectegister, container, false);


        edtprojectname = (EditText) rootView.findViewById(R.id.edtprojectname);


        propertyprofileImage = (ImageView) rootView.findViewById(R.id.propertyprofileImage);
       btncreate = (Button) rootView.findViewById(R.id.btn_create);

        btndelete = (Button) rootView.findViewById(R.id.btn_delete);
        spinnerprojectname = (Spinner) rootView.findViewById(R.id.spinnerprojectname);


        fragmentManager = getFragmentManager();
        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "Dotsprop";
        un = "sqaloits";
        pass = "422q5mfQzU";

        ConnectionClass cn = new ConnectionClass();
        con = cn.connectionclass(un, pass, db, ip);

        FillProjectData();

        spinnerprojectname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                // item.toString()
                if(!spinnerprojectname.getSelectedItem().toString().equals("Select Project...")){
                    try{

                       FillData(spinnerprojectname.getSelectedItem().toString());
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
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                   DeleteProject deleteProject = new DeleteProject();
                    deleteProject.execute("");


                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });

        propertyprofileImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                builder.setTitle("Project Photo");
                builder.setIcon(rootView.getResources().getDrawable(R.drawable.radio));
                builder.setMessage("Add more photo...?");
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectImage();


                    }
                });
                builder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.cancel();

                    }
                });
                builder.show();
            }
        });


        return rootView;
    }
    public void FillProjectData() {
        //==============Fill Data=
        try {

            String query = "select projectname from [UserOnGoingProject] where [userid]='" + activity.id + "'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();
            category.add("Select Project...");
            while (rs.next()) {
                category.add(rs.getString("projectname"));
                propertyexist = propertyexist + 1;
            }
            adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerprojectname.setAdapter(adapter);
            if (propertyexist != 0) {
                spinnerprojectname.setSelection(1);
                FillData(spinnerprojectname.getSelectedItem().toString());
            }

        } catch (Exception ex) {
            // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here",Toast.LENGTH_LONG).show();
        }
//==========
    }


    public void FillData(String name) {
        //==============Fill Data=
        try {

                    String  query = "select  * from [UserOnGoingProject] where [projectname]='" + name+"'";
                   PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    rs.next();

                    if (rs.getRow() != 0) {
                        activity.projectid=rs.getInt("id");
                        edtprojectname.setText(rs.getString("projectname"));
                        spinnerprojectname.setSelection(adapter.getPosition(name));
                        query = "select  * from [UserOnGoingProjectPhoto] where [id]='" + activity.projectid+"'";
                        ps = con.prepareStatement(query);
                        rs = ps.executeQuery();
                        rs.next();
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


        String name = edtprojectname.getText().toString();


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
            if (name.trim().equals(""))
                z = "Please fill in all required details...";
            else {
                try {



                                String query = "insert into [UserOnGoingProject]([projectname],[userid]) " +
                                        "values ('" + name + "','"+Integer.parseInt(activity.id)+"')";
                                PreparedStatement preparedStatement = con.prepareStatement(query);
                                preparedStatement.executeUpdate();

                    String query1 = "select MAX(id) as new_id from [UserOnGoingProject]";
                    PreparedStatement ps = con.prepareStatement(query1);
                    ResultSet rs = ps.executeQuery();
                    rs.next();

                    if(rs.getRow()!=0){
                        activity.projectid=rs.getInt("new_id");
                    }
                    for (String photo:projectphotos ) {
                        query = "insert into [UserOnGoingProjectPhotos]([image],[projectid]) " +
                                "values ('" + photo + "','" +  activity.projectid + "')";
                        preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                    }


                                z = "New Project Created!!!";
                                isSuccess = true;






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
    public class DeleteProject extends AsyncTask<String, String, String> {


        String z = "";
        Boolean isSuccess = false;


        String name = edtprojectname.getText().toString();


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

                try {



                        String query = "delete from UserOnGoingProject where [id]='"+activity.projectid+"'";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();

                    query = "delete from [UserOnGoingProjectPhotos] where projectid='" +  activity.projectid + "')";
                    preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();

                        z = "Project Deleted!!!";
                        isSuccess = true;






                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Check your network connection!!";
                    Log.d("ReminderService In", "An error GGGG: " + ex.getMessage());
                    // z=ex.getMessage();
                }

            return z;
        }
    }

    //===Upload profile
    private void selectImage() {


        final CharSequence[] options = {"Camera", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());

        builder.setTitle("Project Photo");

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
                        projectphotos.add(encodedImage);

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
                    projectphotos.add(encodedImage);
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



