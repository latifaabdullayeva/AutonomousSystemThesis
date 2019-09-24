package com.example.mytabletapp;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Math.round;

public class TabletInitialisation extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG = "TabletInitialisation";
    private BeaconManager beaconManager;
    private TextView numbOfBeacons;

    //-----
    private ArrayList<String> beaconList;
    private ListView beaconListView;
    private ArrayAdapter<String> adapter;
    //-----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TestActivity", "TabletInitialisation");
        setContentView(R.layout.activity_tablet_initialisation);
        Log.d(TAG, "TabletInitialisation started up");

        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Tablet Initialisation");
        actionBar.setDisplayHomeAsUpEnabled(true);
        // TODO: add back Button and functionality for it

        beaconManager = BeaconManager.getInstanceForApplication(this);

        //-----
        // Choose the Beacon Device out of list
        this.beaconList = new ArrayList<>();
        this.beaconListView = findViewById(R.id.listView);
        this.adapter = new ArrayAdapter<>(this, R.layout.my_listview_radiobutton_layout, this.beaconList);
        this.beaconListView.setAdapter(adapter);
        //-----
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
//        final DistanceRepository distanceRepository = new DistanceRepository();
//        final DeviceRepository deviceRepository = new DeviceRepository();


        beaconManager.addRangeNotifier((beacons, region) -> {

            if (beacons.size() > 0) {

                // Show the List of all beacons
                beaconList.clear();
                for (Beacon beacon : beacons) {
                    if (!beaconList.contains(beacon.getId1().toString())) {
                        beaconList.add(beacon.getId1().toString());
                    }
                }
                runOnUiThread(() -> adapter.notifyDataSetChanged());

                // Bind onclick event handler
                beaconListView.setOnItemClickListener((parent, view, position, id) -> {

                    Toast.makeText(TabletInitialisation.this, "Selected Beacon: " +
                            beaconList.get(position), Toast.LENGTH_SHORT).show();

                    // Action for Save button
                    Button buttonSave = findViewById(R.id.buttonBeaconSave);
                    buttonSave.setOnClickListener(v -> {
                        // deviceRepository.sendNetworkRequest("Nexus", beaconList.get(position), "intimate");
//                                    Intent myIntent = new Intent(TabletInitialisation.this, DeviceInitialisation.class);

//                                    String deviceValue = beaconList.get(position);
//                                    myIntent.putExtra("BEACONUUID", deviceValue);
//
//                                    startActivity(myIntent);
                    });

                });

                Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  " + beacons.size());

                for (Beacon beacon : beacons) {
//                        deviceRepository.sendNetworkRequest("Nexus", beacon.toString(), "intimate");
                    Log.d(TAG, "The beacon " + beacon.toString());
//                        distanceRepository.sendNetworkRequest(1, 2, round(beacon.getDistance() * 100));

                    Log.d(TAG, "DISTANCE" + round(beacon.getDistance() * 100) + " cm away.");

                }
                Log.d(TAG, "------------------------------------------------------------");
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (
                RemoteException ignored) {
        }
    }
}