package com.example.mytabletapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import org.altbeacon.beacon.BeaconManager;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActionBar();
        setupTablet();

        // if tablet is new in a system, then go to Tablet Initialisation activity
        // If it is already initialised, then go to Change Screen Colour activity

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String beaconValue = sharedPreferences.getString("beaconTag", "");
        String deviceTypeValue = sharedPreferences.getString("deviceType", "");

        if (TextUtils.isEmpty(beaconValue) || TextUtils.isEmpty(deviceTypeValue)) {
            verifyBluetooth();
            askPermission();
        } else {
            Intent intent = new Intent(MainActivity.this, ScreenColorChange.class);
            intent.putExtra("BEACONUUID", beaconValue);
            intent.putExtra("DEVICETYPE", deviceTypeValue);
            startActivity(intent);
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Home");
    }

    private void setupTablet() {
        Button initialiseTablet = findViewById(R.id.initialiseTablet);
        initialiseTablet.setOnClickListener(button -> {
            startActivity(new Intent(MainActivity.this, TabletInitialisation.class));
        });
    }

    private void verifyBluetooth() {
        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Bluetooth not enabled");
                builder.setMessage("Please enable bluetooth in settings and restart this application.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(dialog -> {
                    finish();
                    System.exit(0);
                });
                builder.show();
            }
        } catch (RuntimeException e) {
            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(dialog -> {
            });
            builder.show();
        }
    }

    // If targeting Android SDK 23+ (Marshmallow), in our case we have "targetSdkVersion: 28"
    // the app must also request permission from the user to get location access.
    public void askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // permission check
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access!");
                builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults
    ) {
        if (requestCode == PERMISSION_REQUEST_COARSE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Location permission is granted");
            } else {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

                builder.setTitle("Functionality limited");
                builder.setMessage("Since location access has not been granted, this app " +
                        "will not be able to discover beacons when in the background.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(dialog -> {
                });
                builder.show();
            }
        }
    }
}
