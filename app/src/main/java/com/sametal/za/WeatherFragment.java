package com.sametal.za;



import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;



import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class WeatherFragment extends Fragment {
    View rootView;
    Typeface weatherFont;

    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;
String address;
    Handler handler;
    Bundle bundle;
ImageView map;
    String latlon = "";
    public WeatherFragment(){
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.fragment_weather, container, false);



       bundle = this.getArguments();
        cityField = (TextView)rootView.findViewById(R.id.city_field);
        updatedField = (TextView)rootView.findViewById(R.id.updated_field);
        detailsField = (TextView)rootView.findViewById(R.id.details_field);
        currentTemperatureField = (TextView)rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView)rootView.findViewById(R.id.weather_icon);
        map = (ImageView) rootView.findViewById(R.id.map);
        weatherIcon.setTypeface(weatherFont);

//=========
        try {
            if (bundle != null) {

                address =bundle.getString("address").trim();
              String  latlonaddress =bundle.getString("latlon").trim();
                Log.d("ReminderService In", "##" + address);

                address=address.substring(address.indexOf(',')+1).trim();
                address=address.substring(address.indexOf(',')+1).trim();
                address = address.substring(0,address.indexOf(','));

                String lat = latlonaddress.substring(10,latlonaddress.indexOf(','));
                String lon = latlonaddress.substring(latlonaddress.indexOf(',') + 1, latlonaddress.indexOf(')'));
                latlon = lat + "," + lon;

                Log.d("ReminderService In", "##" + address);

            }
        } catch (Exception ex) {
            Log.d("ReminderService In", "####1" + ex.getMessage());
        }

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGoogleMapsInstalled()) {
                    try {
                        if (!latlon.equals("")) {
                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latlon);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            getActivity().startActivity(mapIntent);
                        } else {

                            try {
                                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + bundle.getString("address").trim());
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            } catch (Exception ex) {
                                Toast.makeText(rootView.getContext(), "Address/Google Maps Not Found...", Toast.LENGTH_LONG).show();
                            }
                        }

                    } catch (Exception ex) {
                        Toast.makeText(rootView.getContext(), "Enable Device GPS...", Toast.LENGTH_LONG).show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                    builder.setMessage("Install Google Maps");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Install", getGoogleMapsListener());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });


        //======


        return rootView;
    }

    public boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public DialogInterface.OnClickListener getGoogleMapsListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en"));
                startActivity(intent);

                //Finish the activity so they can't circumvent the check
                // finish();
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
        try {
            if (bundle != null) {
                 address =bundle.getString("address").trim();
                Log.d("ReminderService In", "##" + address);

               address=address.substring(address.indexOf(',')+1).trim();
                address=address.substring(address.indexOf(',')+1).trim();
                address = address.substring(0,address.indexOf(','));

                Log.d("ReminderService In", "##" + address);

            }
        } catch (Exception ex) {
            Log.d("ReminderService In", "####1" + ex.getMessage());
        }
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "weather.ttf");
        updateWeatherData(address);
    }
    private void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = RemoteFetch.getJSON(getActivity(), city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }
    private void renderWeather(JSONObject json){
        try {
            cityField.setText(json.getString("name").toUpperCase(Locale.US) +
                    ", " +
                    json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            detailsField.setText(
                    details.getString("description").toUpperCase(Locale.US) +
                            "\n" + "Humidity: " + main.getString("humidity") + "%" +
                            "\n" + "Pressure: " + main.getString("pressure") + " hPa");

            currentTemperatureField.setText(
                    String.format("%.2f", main.getDouble("temp"))+ " ℃");

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            updatedField.setText("Last update: " + updatedOn);

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }
    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 5 : icon = getActivity().getString(R.string.weather_rainy);
                    break;
            }
        }
        weatherIcon.setText(icon);
    }
}
