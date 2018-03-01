package com.example.mac.airnow;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class LocationProvider {

    Context context;
    Activity activity;
    Location currentLocation;
    LocationRequest locationRequest;
    LocationCallback newLocationCallback;
    FusedLocationProviderClient fusedLocationClient;
    Callback<Location> callback;

    public LocationProvider(Context context, final Activity activity) {
        this.context = context;
        this.activity = activity;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
//        makeLocationRequest();

    }

    public void makeLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(1000);

        newLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.e("E", "Location Updated");
                for (Location location : locationResult.getLocations()) {
                    currentLocation = location;
                    fusedLocationClient.removeLocationUpdates(newLocationCallback);
                }
                if(callback != null){
                    callback.perform(currentLocation);
                }
            }
        };

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.e("E", "Success Requesting Location");
                Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.main_layout), "Showing results for current location", 1500);
                snackbar.show();
                checkPermission();
                fusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location == null) {
                            Log.e("E", "Null location");
                            requestLocation();
                        }
                        if(callback != null && location!= null){
                            currentLocation = location;
                            callback.perform(currentLocation);
                        }
                    }
                });
            }
        });

        task.addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    Log.e("E", "failure");
//                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    activity.startActivityForResult(intent, 2);

                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(activity, 2);
                        Log.e("E", "resolved");
                    } catch (Exception e1) {
                        // Ignore the error.
                        Toast.makeText(context, "Intent error", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void checkPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1 );
            Log.e("E", "Permission denied!");
            Log.e("E", "Requesting permission!");
            return;
        }
    }
    public void requestLocation() {
        checkPermission();
        fusedLocationClient.requestLocationUpdates(locationRequest, newLocationCallback, null);
    }

    public void setCallback(Callback<Location> callback) {
        this.callback = callback;
    }

}
