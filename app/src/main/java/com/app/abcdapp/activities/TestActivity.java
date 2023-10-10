package com.app.abcdapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.app.abcdapp.R;

public class TestActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_ENABLE_GPS = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
// Check if the ACCESS_FINE_LOCATION permission has been granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted, so check if GPS is enabled
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // GPS is enabled, so get the user's location
                Toast.makeText(TestActivity.this, "0", Toast.LENGTH_SHORT).show();
                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Toast.makeText(TestActivity.this, ""+latitude, Toast.LENGTH_SHORT).show();
                        // Do something with the latitude and longitude
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        Toast.makeText(TestActivity.this, "2", Toast.LENGTH_SHORT).show();
                    }

                    public void onProviderEnabled(String provider) {
                        Toast.makeText(TestActivity.this, "2", Toast.LENGTH_SHORT).show();
                    }

                    public void onProviderDisabled(String provider) {
                        Toast.makeText(TestActivity.this, "2", Toast.LENGTH_SHORT).show();
                    }
                };
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            } else {
                // GPS is not enabled, so prompt the user to enable it
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, REQUEST_ENABLE_GPS);
            }
        } else {
            // Permission is not granted, so request it from the user
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_LOCATION_PERMISSION);
        }


    }
    // Handle the user's permission and GPS enablement responses
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, so check if GPS is enabled
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // GPS is enabled, so get the user's location
                    // ...
                } else {
                    // GPS is not enabled, so prompt the user to enable it
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, REQUEST_ENABLE_GPS);
                }
            } else {
                Toast.makeText(this, "Please Give Location", Toast.LENGTH_SHORT).show();
                // Permission is not granted, so handle the error
                // ...
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_GPS) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // GPS is now enabled, so get the user's location
                // ...
            } else {
                // GPS is still not enabled, so handle the error
                // ...
            }
        }
    }


}