package com.example.mac.airnow;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DashboardFragment extends Fragment {

    private ProgressBar progressInfoBar;
    private ProgressBar progressSearchBar;
    private ListView listView;
    private TinyDB tinydb;
    public LocationProvider locationProvider;
    private Location currentLocation;
    public ConnectivityCheck connectivityCheck;
    private Context context;
    private Activity activity;
    private Toolbar toolbar;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getContext();
        activity = getActivity();

        View dashboardView  = inflater.inflate(R.layout.fragment_dashboard, container, false);
        toolbar = activity.findViewById(R.id.maintoolbar);

        progressInfoBar = dashboardView.findViewById(R.id.main_info_progress);
        progressSearchBar = activity.findViewById(R.id.main_progress_bar);

        tinydb = new TinyDB(context);
        Log.e("E", "Removed");
        tinydb.removeAll();

        listView = dashboardView.findViewById(R.id.main_aqi_list);
        Button refreshBtn = dashboardView.findViewById(R.id.main_footer_refresh);

        connectivityCheck = new ConnectivityCheck(context, activity);
        locationProvider = new LocationProvider(context, activity);

        if(connectivityCheck.isConnected()){
            setUpDashboard();
        }else {
            connectivityCheck.showDialog();
        }

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer i = tinydb.getInt("lastLocationPosition");
                if(i > 0){ //0 means no search has been done.
                    LocationItem l = tinydb.getObject("LocationItem " + i, LocationItem.class);
                    makeApiRequest(l.getLat(), l.getLon());
                }else {
                    makeApiRequest(String.valueOf(currentLocation.getLatitude()),
                            String.valueOf(currentLocation.getLongitude()));
                }

            }
        });
        return dashboardView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Log.e("E", "Got selected location");
            Integer selectedLocationPosition = data.getIntExtra("selectedLocation", 0);

            tinydb.putInt("lastLocationPosition", selectedLocationPosition);

            LocationItem l = tinydb.getObject("LocationItem " + selectedLocationPosition, LocationItem.class);
            makeApiRequest(l.getLat(), l.getLon());
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    locationProvider.makeLocationRequest();
                } else {
                    // permission denied, boo!
                    Toast.makeText(activity, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void setUpDashboard(){
        progressInfoBar.setVisibility(View.VISIBLE);

        Callback<Location> callback = new Callback<Location>() {
            @Override
            public void perform(Location location){
                currentLocation = location;
                Log.e("E", "Location! Found");
                String lat = String.valueOf(location.getLatitude());
                String lon = String.valueOf(location.getLongitude());
                makeApiRequest(lat, lon);
            }
        };
        locationProvider.setCallback(callback);
        locationProvider.makeLocationRequest();

    }



    public void getQueryLocation(String searchLocation, final Activity activity) {

        progressSearchBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://api.waqi.info/search/?keyword=" + searchLocation + "&token=278800aef13f313c5b7b006ec0db07742ef504d3";

        StringRequest locationRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        tinydb.removeAll();
                        Gson gson = new Gson();
                        SearchResponse res = gson.fromJson(response, SearchResponse.class);
                        Integer count = 0;
                        for (SearchResponse.Datum location : res.getData()) {
                            SearchResponse.Station station = location.getStation();
                            List<String> geo = station.getGeo();
                            LocationItem thisLocationItem = new LocationItem();
                            thisLocationItem.setLocation(station.getName(), geo.get(0), geo.get(1));
                            count++;
                            tinydb.putObject("LocationItem " + count, thisLocationItem);
                        }
                        tinydb.putInt("count", count);
                        progressSearchBar.setVisibility(View.INVISIBLE);
                        //startLocationSelection
                        Intent selectionIntent = new Intent(activity, LocationActivity.class);
                        startActivityForResult(selectionIntent, 1);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error from getLocation().", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(locationRequest);
    }

    public void makeApiRequest(String lat, String lon) {
        progressInfoBar.setVisibility(View.VISIBLE);

        String geo = "geo:" + lat + ";" + lon;
        String url = "https://api.waqi.info/feed/" + geo + "/?token=278800aef13f313c5b7b006ec0db07742ef504d3";
        final ArrayList<String> keys = new ArrayList<>();
        final ArrayList<String> values = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest apiRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                final JsonObject data = jsonObject.getAsJsonObject("data");
                String cityName = data.getAsJsonObject("city").get("name").getAsString();
                String mainAQI = data.get("aqi").getAsString();
                String timestamp = data.getAsJsonObject("time").get("v").getAsString();
                String timezone = data.getAsJsonObject("time").get("tz").getAsString();

                JsonObject infoAqi = data.getAsJsonObject("iaqi");

                for (String key : infoAqi.keySet()) {
                    JsonObject k = infoAqi.getAsJsonObject(key);
                    String value = k.get("v").getAsString();
                    keys.add(key);
                    values.add(value);
                }

                setMainCardText(cityName, mainAQI, timestamp, timezone);
                setListItems(keys, values);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressInfoBar.setVisibility(View.INVISIBLE);
                Toast.makeText(context, "No internet connection.", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(apiRequest);
    }

    private void setMainCardText(String city, String aqi, String timestamp, String timezone) {
        RelativeLayout mainCard = activity.findViewById(R.id.main_layout_color);
        TextView mainLocationText = activity.findViewById(R.id.main_location_text);
        mainLocationText.setText(city);
        TextView mainAqiText = activity.findViewById(R.id.main_aqi_text);
        mainAqiText.setText(aqi);
        TextView mainCondition = activity.findViewById(R.id.main_condition_text);

        Integer aqiInt = Integer.parseInt(aqi);
        if (aqiInt <= 50) {
            mainCondition.setText("Good");
            mainCard.setBackgroundColor(getResources().getColor(R.color.colorGood));
        } else if (50 < aqiInt && aqiInt <= 100) {
            mainCondition.setText("Moderate");
            mainCard.setBackgroundColor(getResources().getColor(R.color.colorModerate));
        } else if (10 < aqiInt && aqiInt <= 150) {
            mainCondition.setText("Poor");
            mainCard.setBackgroundColor(getResources().getColor(R.color.colorPoor));
        } else if (150 < aqiInt && aqiInt <= 200) {
            mainCondition.setText("Unhealthy");
            mainCard.setBackgroundColor(getResources().getColor(R.color.colorUnhealthy));
        } else if (200 < aqiInt && aqiInt <= 300) {
            mainCondition.setText("Very\nUnhealthy");
            mainCard.setBackgroundColor(getResources().getColor(R.color.colorVUnhealthy));
        } else if (aqiInt > 300) {
            mainCondition.setText("Hazardous");
            mainCard.setBackgroundColor(getResources().getColor(R.color.colorHazardous));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone(timezone));
        Date timeMs = new Date((long) Integer.parseInt(timestamp) * 1000);
        String updateTime = sdf.format(timeMs);
        TextView timeText = activity.findViewById(R.id.main_footer_text);
        timeText.setText("Updated on "+updateTime.toLowerCase()+" ( GMT: "+timezone+" )");

    }

    private void setListItems(ArrayList<String> params, ArrayList<String> values) {
        CustomInfoAdapter customAdapter = new CustomInfoAdapter(activity, params, values);
        listView.setAdapter(customAdapter);
        progressInfoBar.setVisibility(View.INVISIBLE);
    }


}
