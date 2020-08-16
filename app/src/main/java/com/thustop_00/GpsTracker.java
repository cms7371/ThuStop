package com.thustop_00;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;

import androidx.core.content.ContextCompat;

import butterknife.OnClick;

import static android.content.Context.LOCATION_SERVICE;

public class GpsTracker implements LocationListener {
    private final Context mContext;

    // use or not about using GPS, NETWORK
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    // state of GPS


    Location location;
    double latitude;   // 위도
    double longitude;   // 경도

    //minimum distance for updating gps location
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    // minimum time for updating gps location
    private static final long MIN_TIME_BW_UPDATES = 60*1000;

    // class to get gps info
    protected LocationManager locationManager;

    public GpsTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGPSEnabled && !isNetworkEnabled){
                // when NETWORK and GPS is unavailable
            } else {
                // Check permission
                int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
                int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
                } else
                    return null;


                if(isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location != null) {
                            // Get latitude and longitude
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }

                    }
                }

                if(isGPSEnabled) {
                    if(location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if(location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();;
        }
        return location;
    }

    // GPS shutdown
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GpsTracker.this);
        }
    }

    public double getLatitude() {
        if(location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if(location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }


    @Override
    public void onLocationChanged(Location location) {
    }

}
