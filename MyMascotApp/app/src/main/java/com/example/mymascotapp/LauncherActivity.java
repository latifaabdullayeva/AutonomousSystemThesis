package com.example.mymascotapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Log.d("FLOW", "LauncherActivity");

        NsdHelper nsdHelper = new NsdHelper(this);
        nsdHelper.discoverServices();
    }
}
