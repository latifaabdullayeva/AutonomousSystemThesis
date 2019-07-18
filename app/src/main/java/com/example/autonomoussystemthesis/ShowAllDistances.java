package com.example.autonomoussystemthesis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Objects;

public class ShowAllDistances extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TestActivity", "ShowALlDist");
        setContentView(R.layout.activity_show_all_distances);

        Objects.requireNonNull(getSupportActionBar()).setTitle("All Distances");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView beaconTag = findViewById(R.id.passBeaconUUID);
        String beaconTagValue = getIntent().getStringExtra("BEACONUUID");
        Log.d("qwerty", "" + beaconTagValue);
        beaconTag.setText(beaconTagValue);

        TextView deviceType = findViewById(R.id.passDeviceType);
        String deviceTypeValue = getIntent().getStringExtra("DEVICETYPE");
        Log.d("qwerty", "" + deviceTypeValue);
        deviceType.setText(deviceTypeValue);

        TextView mascotName = findViewById(R.id.passMascotName);
        String mascotNameValue = getIntent().getStringExtra("DEVICENAME");
        Log.d("qwerty", "" + mascotNameValue);
        mascotName.setText(mascotNameValue);

        TextView devicePers = findViewById(R.id.passPersonality);
        String devicePersValue = getIntent().getStringExtra("PERSONALITY");
        Log.d("qwerty", "" + devicePersValue);
        devicePers.setText(devicePersValue);
    }
}