package com.example.mymascotapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;

import org.altbeacon.beacon.BeaconManager;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    protected static final String TAG = "MonitoringMainActivity";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("FLOW", "MainActivity");
        Log.d(TAG, "MainActivity");

        setupActionBar();
        setupQuestionnaireButton();

        SharedPreferences preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        String beaconValue = preferences.getString("text1", "");
        String deviceTypeValue = preferences.getString("text2", "");
        String devNameValue = preferences.getString("text3", "");
        String persValue = preferences.getString("text4", "");

        // TODO: change to beacon not deviceTypeValue
        // here on app start we check if beacon is already saved in your phone, according to that it goes
        // either to device Initialisation, or if exists -> it starts measuring all distances
        if (TextUtils.isEmpty(deviceTypeValue)) { // beaconValue, when you change it, test It
            Log.d(TAG, "MainAct if --> " + "deviceTypeValue: " + deviceTypeValue +
                    "; devNameValue " + devNameValue + "; persValue " + persValue + "; beaconValue " + beaconValue);
            verifyBluetooth();
            askPermission();
        } else {
            Log.d(TAG, "MainAct else -->" + "deviceTypeValue: " + deviceTypeValue +
                    "; devNameValue " + devNameValue + "; persValue " + persValue + "; beaconValue " + beaconValue);
            Intent myIntent = new Intent(MainActivity.this, ShowAllDistances.class);
            myIntent.putExtra("BEACONUUID", beaconValue);
            myIntent.putExtra("DEVICETYPE", deviceTypeValue);
            myIntent.putExtra("DEVICENAME", devNameValue);
            myIntent.putExtra("PERSONALITY", persValue);
            startActivity(myIntent);
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Home");
    }

    private void setupQuestionnaireButton() {
        Button gotoQuestionnare = findViewById(R.id.gotoQuestionnare);
        gotoQuestionnare.setOnClickListener(button -> {
            startActivity(new Intent(MainActivity.this, Initialisation.class));
        });
    }

    private void askPermission() {
        // If targeting Android SDK 23+ (Marshmallow), in our case we have "targetSdkVersion: 28"
        // the app must also request permission from the user to get location access.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
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
                Log.d(TAG, "coarse location permission granted");
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);

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

    private void verifyBluetooth() {
        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(dialog -> {
            });
            builder.show();
        }
    }
}
