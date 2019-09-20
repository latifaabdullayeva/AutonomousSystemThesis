package com.example.mytablet;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.altbeacon.beacon.BeaconManager;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Home");

        // if tablet is new in a system, then go to Tablet Initialisation activity
        // If it is already initialised, then go to Change Screen Colour activity

    }
}