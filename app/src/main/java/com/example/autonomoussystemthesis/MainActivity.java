package com.example.autonomoussystemthesis;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.autonomoussystemthesis.network.api.devices.DeviceRepository;

import org.altbeacon.beacon.BeaconManager;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    protected static final String TAG = "MonitoringMainActivity";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    final DeviceRepository deviceRepository = new DeviceRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TestActivity", "MainAct");

//      When run first time -> MainActivity, When quit.. -> ShowAllDistances
        String devType = getIntent().getStringExtra("DEVICETYPE");
        String devName = getIntent().getStringExtra("DEVICENAME");
        String pers = getIntent().getStringExtra("PERSONALITY");

        String deviceTypeValue = getSharedPreferences("sharedPrefs", MODE_PRIVATE).getString("text2", devType);
        String devNameValue = getSharedPreferences("sharedPrefs", MODE_PRIVATE).getString("text3", devName);
        String persValue = getSharedPreferences("sharedPrefs", MODE_PRIVATE).getString("text4", pers);

        // TODO: change to beacon not deviceTypeValue
        if (deviceTypeValue == null || deviceTypeValue.equals("")) {
            Log.d("test", "MainAct if --> deviceTypeValue: " + deviceTypeValue);
            Toast.makeText(MainActivity.this, "First Run", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_main);
            Objects.requireNonNull(getSupportActionBar()).setTitle("Home");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
            verifyBluetooth();

//         If targeting Android SDK 23+ (Marshmallow), in our case we have "targetSdkVersion: 28"
//         the app must also request permission from the user to get location access.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//             Android M Permission check
                if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            Button buttonSave = findViewById(R.id.gotoQuestionnare);
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, Initialisation.class));
                }
            });

//        deviceRepository.getNetworkRequest();
        } else {
            Log.d("test", "MainAct else --> deviceTypeValue: " + deviceTypeValue);
            Intent myIntent = new Intent(MainActivity.this, ShowAllDistances.class);
            myIntent.putExtra("DEVICETYPE", deviceTypeValue);
            startActivity(myIntent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }
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
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                        System.exit(0);
                    }
                });
                builder.show();
            }
        } catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            builder.show();

        }

    }

}
