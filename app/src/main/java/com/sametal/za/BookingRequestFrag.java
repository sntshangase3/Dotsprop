package com.sametal.za;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by sibusison on 2017/07/30.
 */
public class BookingRequestFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    View rootView;

    EditText edtaddress,edtaddresssametal;
    private Button confirm;
    private Button cancel;
    //---------con--------
    Connection con;
    String un, pass, db, ip, collectiondaydate, day, hour, minute;


    Calendar date;
    String  selectedcarwash="";

    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;
    ArrayList<String> scheduleid = new ArrayList<String>();
    ArrayList<String> deleiveryday = new ArrayList<String>();
    ArrayList<String> deliveryhourrange = new ArrayList<String>();
    ArrayList<Bitmap> timer = new ArrayList<Bitmap>();
    ListView lstgross;
    Spinner spinnercarwash;
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";
    Bundle bundles = new Bundle();
    Calendar c;
    TextView txtorderno, edtdistance;


    ArrayAdapter adapter1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.collectionrequest, container, false);


        edtaddress = (EditText) rootView.findViewById(R.id.edtaddress);
        edtaddresssametal = (EditText) rootView.findViewById(R.id.edtaddresssametal);
        edtdistance = (TextView) rootView.findViewById(R.id.edtdistance);
        confirm = (Button) rootView.findViewById(R.id.confirm);
        cancel = (Button) rootView.findViewById(R.id.cancel);
        spinnercarwash = (Spinner) rootView. findViewById(R.id.spinnercarwash);
        lstgross = (ListView) rootView.findViewById(R.id.lstgross);
        txtorderno = (TextView) rootView.findViewById(R.id.txtorderno);
        fragmentManager = getFragmentManager();

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "Dotsprop";
        un = "sqaloits";
        pass = "422q5mfQzU";
        ConnectionClass cn = new ConnectionClass();
        con = cn.connectionclass(un, pass, db, ip);
        bundle = this.getArguments();


        try {
            String query;
            if (activity.position.equals("carwash")) {
                query = "select  * from [Collection] where Datediff(D,convert(varchar,getDate(),112),Cast([collectionsdate] as date))>=0 and [collectionstatus]='New' and [carwashid]='" + activity.id+"'";
            } else {
                query = "select  * from [Collection] where Datediff(D,convert(varchar,getDate(),112),Cast([collectionsdate] as date))>=0 and [carownerid]=" + activity.id;
            }
            PreparedStatement ps1 = con.prepareStatement(query);
            ResultSet rs1 = ps1.executeQuery();
            int total = 0;
            while (rs1.next()) {
                total = total + 1;
            }
            txtorderno.setText(String.valueOf(total));

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage() + "######");
        }
        FillScheduleData();
        FillCarWashData();
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);

                if ((edtaddress.getText().toString().trim().equals(""))) {
                    edtaddress.setBackground(errorbg);
                } else if (collectiondaydate.equals("")) {
                    Toast.makeText(rootView.getContext(), "Select Delivery Day!!", Toast.LENGTH_LONG).show();
                } else {

                    try {


                            Date today = new Date();
                            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                            String todaydate = date_format.format(today);
                            String refno = "SITS" + today.getHours() + "" + today.getMinutes() + "" + today.getSeconds();
                        if (todaydate.trim().equals("")|| collectiondaydate.trim().equals("")|| edtdistance.getText().toString().equals("")|| edtaddress.getText().toString().equals("")|| selectedcarwash.trim().equals("") )
                          {
                            Toast ToastMessage = Toast.makeText(rootView.getContext(), "Please fill in all required details...", Toast.LENGTH_LONG);
                            View toastView = ToastMessage.getView();
                            toastView.setBackgroundResource(R.drawable.toast_bground);
                            ToastMessage.show();
                        }
                        else {
                            Log.d("ReminderService In", refno+" "+activity.id+ " "+selectedcarwash);
                            String command = "insert into [Collection]([createddate],[collectionsdate],[daysleft],[collectionstatus],[collectiondistance],[location],[carownerid],[carwashid],[driverid],[ref],[warning] ,[prepdate]\n" +
                                    "      ,[transitdate]\n" +
                                    "      ,[deliverdate],[isread]) " +
                                    "values ('" + todaydate + "','" + collectiondaydate + "','1 Days','New','" + edtdistance.getText().toString() +"','" + edtaddress.getText().toString() + "','" + activity.id + "','" + selectedcarwash + "',0,'" + refno + "','No Reported Possible Delay','#####','#####','#####','No')";
                            PreparedStatement preparedStatement = con.prepareStatement(command);
                            preparedStatement.executeUpdate();

                            Toast ToastMessage = Toast.makeText(rootView.getContext(), "Booking Successfully!!!", Toast.LENGTH_LONG);
                            View toastView = ToastMessage.getView();
                            toastView.setBackgroundResource(R.drawable.toast_bground);
                            ToastMessage.show();
                            Fragment frag = new HomeFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.mainFrame, frag).commit();
                        }



                    } catch (Exception ex) {
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }

                }

            }


        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag = new HomeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();


            }
        });

        txtorderno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    BookingAllOrdersFrag fragment = new BookingAllOrdersFrag();
                    fragment.setArguments(bundles);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                } catch (Exception ex) {
                    Log.d("ReminderService In", ex.getMessage().toString());
                }
            }
        });

        edtaddress.addTextChangedListener(new TextWatcher() {

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
                    if((s.length() >= 10)){
                        GPSTracker mGPS = new GPSTracker(rootView.getContext());
                        if (mGPS.canGetLocation) {

                            Geocoder geocoder;
                            geocoder = new Geocoder(rootView.getContext(), Locale.getDefault());
                            double Lat = geocoder.getFromLocationName(edtaddress.getText().toString(), 1).get(0).getLatitude();
                            double Lon = geocoder.getFromLocationName(edtaddress.getText().toString(), 1).get(0).getLongitude();
                            double Lat1 = geocoder.getFromLocationName(edtaddresssametal.getText().toString(), 1).get(0).getLatitude();
                            double Lon1 = geocoder.getFromLocationName(edtaddresssametal.getText().toString(), 1).get(0).getLongitude();

                            double distance;
                            Location locationA = new Location("");
                            locationA.setLatitude(Lat);
                            locationA.setLongitude(Lon);

                            Location locationB = new Location("");
                            locationB.setLatitude(Lat1);
                            locationB.setLongitude(Lon1);


                            distance = locationA.distanceTo(locationB) / 1000;   // in km
                            edtdistance.setText((double) Math.round(distance) + "Km");
                            // Get reults




                        }else {
                            Log.d("ReminderService In", "GPS OFF");
                            mGPS.showSettingsAlert();
                        }

                    }
                }catch (Exception ex){
                    Log.d("ReminderService In", ex.getMessage());
                }
            }
        });
        spinnercarwash.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                // item.toString()
                if(!spinnercarwash.getSelectedItem().toString().equals("Select Car Wash...")){
                    try{
                        selectedcarwash = String.valueOf(spinnercarwash.getSelectedItemPosition()+1);

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


    public void showDateTimePicker() {

        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(rootView.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(rootView.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                       SimpleDateFormat date_format = new SimpleDateFormat(DATE_TIME_FORMAT);

                        try {
                            collectiondaydate = date_format.format(date.getTime());
                            Log.d("ReminderService In", "#######" + collectiondaydate);
                        } catch (Exception e) {
                            Log.d("ReminderService In", e.getMessage().toString());
                        }
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    private View currentSelectedView;
    public void FillCarWashData() {
        //==============Fill Data=
        try {

            String query = "select * from [CarWash]";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> category = new ArrayList<String>();
            category.add("Select Car Wash...");
            while (rs.next()) {
                category.add(rs.getString("carwash"));
            }
            adapter1 = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, category);
            adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnercarwash.setAdapter(adapter1);

        } catch (Exception ex) {
            // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here",Toast.LENGTH_LONG).show();
        }
//==========
    }
    public void FillScheduleData() {
        //==============Fill Data=FillScheduleData

        try {
                String query = "select * from [CollectionSchedule]";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                ArrayList<String> category = new ArrayList<String>();

                scheduleid.clear();
                deleiveryday.clear();
                deliveryhourrange.clear();
                timer.clear();

                while (rs.next()) {
                    scheduleid.add(rs.getString("id"));
                    deleiveryday.add(rs.getString("deliveryday"));
                    deliveryhourrange.add(rs.getString("deliveryhourrange"));
                    Bitmap preptime = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.prep_time);
                    timer.add(preptime);

                }
                BookingRequestAdapter adapter = new BookingRequestAdapter(this.getActivity(), scheduleid, deleiveryday, deliveryhourrange, timer);
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
                            day = deleiveryday.get(position).toString();
                            hour = deliveryhourrange.get(position).toString();
                            hour = hour.substring(hour.indexOf("-") + 1);
                            hour = hour.substring(0, hour.indexOf("H")).trim();
                            minute = deliveryhourrange.get(position).toString();
                            minute = minute.substring(minute.length() - 2).trim();
                            Log.d("ReminderService In", day + " " + hour + " " + minute + " " + collectiondaydate);
                            String message = "Specific delivery time?";
                            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                            builder.setTitle("Select");

                            builder.setMessage(message);
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        // Get Current Date
                                        showDateTimePicker();


                                    } catch (Exception ex) {
                                        Log.d("ReminderService In", ex.getMessage().toString());
                                    }
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // dialog.cancel();
                                    c = Calendar.getInstance();

                                    switch (day) {
                                        case "Sunday":
                                            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                                            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                                            c.set(Calendar.MINUTE, Integer.parseInt(minute));
                                            break;
                                        case "Monday":
                                            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                                            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                                            c.set(Calendar.MINUTE, Integer.parseInt(minute));
                                            break;
                                        case "Tuesday":
                                            c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                                            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                                            c.set(Calendar.MINUTE, Integer.parseInt(minute));
                                            break;
                                        case "Wednesday":
                                            c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                                            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                                            c.set(Calendar.MINUTE, Integer.parseInt(minute));
                                            break;
                                        case "Thursday":
                                            c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                                            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                                            c.set(Calendar.MINUTE, Integer.parseInt(minute));
                                            break;
                                        case "Friday":
                                            c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                                            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                                            c.set(Calendar.MINUTE, Integer.parseInt(minute));
                                            break;
                                        case "Saturday":
                                            c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                                            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                                            c.set(Calendar.MINUTE, Integer.parseInt(minute));
                                            break;

                                    }

                                    c.set(Calendar.SECOND, 0);
                                    SimpleDateFormat date_format = new SimpleDateFormat(DATE_TIME_FORMAT);
                                    collectiondaydate = date_format.format(c.getTime());
                                }
                            });
                            builder.show();

                            Log.d("ReminderService In", day + " " + hour + " " + minute + " " + collectiondaydate);

                        } catch (Exception ex) {
                            Log.d("ReminderService In", ex.getMessage().toString());
                        }


                    }


                });

        } catch (Exception ex) {
            Log.d("ReminderService In", ex.getMessage().toString());
        }
//==========
    }
    private void highlightCurrentRow(View rowView) {

        rowView.setBackgroundColor(getResources().getColor(R.color.focus_box_frame));

    }


    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);

    }
}


