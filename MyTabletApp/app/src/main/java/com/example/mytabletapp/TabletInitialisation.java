package com.example.mytabletapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;

import java.util.ArrayList;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TabletInitialisation extends AppCompatActivity implements BeaconConsumer, RecyclerViewAdapter.ItemClickListener {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT1 = "beaconTag";
    public static final String TEXT2 = "deviceType";

    Button saveTabletButton;
    TextView textViewSelectedBeacon;

    private BeaconManager beaconManager;
    private ArrayList<String> beaconList, deviceList, tempBeaconList;
    private RecyclerViewAdapter adapter;
    private String beaconValue, deviceTypeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabletinitialisation);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Tablet Initialisation");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.deviceList = new ArrayList<>();
        this.tempBeaconList = new ArrayList<>();
        textViewSelectedBeacon = findViewById(R.id.showSelectedBeacon);

        beaconManager = BeaconManager.getInstanceForApplication(this);

        // Choose the Beacon Device out of list
        this.beaconList = new ArrayList<>();
        RecyclerView beaconListView = findViewById(R.id.listViewBeacon);
        this.adapter = new RecyclerViewAdapter(this, this.beaconList);
        beaconListView.setAdapter(adapter);

        saveTabletButton = findViewById(R.id.saveTabletButton);
        saveButtonListener();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(TabletInitialisation.this, ScreenColorChange.class);
        beaconValue = beaconList.get(position);
        Toast.makeText(TabletInitialisation.this, "Selected Beacon: " + beaconValue, Toast.LENGTH_SHORT).show();
        textViewSelectedBeacon.setText(getString(R.string.selectedBeacon, beaconValue));
    }

    public void checkBeaconButton() {
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.listViewBeacon);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, beaconList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        beaconManager.unbind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        beaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        // TODO:

    }

    public void saveButtonListener() {
        saveTabletButton.setOnClickListener(v -> {
            // TODO:
        });
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT1, beaconValue);
        editor.putString(TEXT2, deviceTypeValue);
        editor.apply();
        Toast.makeText(TabletInitialisation.this, "Data SAVED!", Toast.LENGTH_SHORT).show();
    }
}
