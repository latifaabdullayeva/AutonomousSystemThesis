package com.example.autonomoussystemthesis;


import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.autonomoussystemthesis.data.DatabaseHelper;
import com.example.autonomoussystemthesis.network.HueRepository;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

import static java.lang.Math.round;

public class RangingActivity extends Activity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager;

    // database
    DatabaseHelper autonomousSystemDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
        beaconManager = BeaconManager.getInstanceForApplication(this);

        autonomousSystemDatabase = new DatabaseHelper(this);
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
        // in order to discover the IpAdress of the bridge, we used https://discovery.meethue.com/
        // Once we have the address load the test app by visiting https://<bridge ip address>/debug/clip.html
        // We need to use the randomly generated username that the bridge creates for you.
        // Fill in the info below and press the POST button, after that press the link button on bridge.
        // URL	/api
        // Body	{"devicetype":"newDeveloper"}
        // Method	POST
        // the Command Response will show you a username
        // The documentation of Hue Api is "https://developers.meethue.com/develop/get-started-2/"
        final HueRepository hueRepository = new HueRepository(
                "192.168.0.102",
                "vY5t4oArH-K0BUA7430cb1rJ8mC1DYMzkmBWRr91"
        );

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    //Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  " + beacons.size());

                    Beacon firstBeacon = beacons.iterator().next();
                    int brightness = (int) firstBeacon.getDistance() * 80;
                    if (brightness > 255) {
                        brightness = 255;
                    }

                    hueRepository.updateBrightness(brightness);

                    if (firstBeacon.getDistance() <= 0.45) { // intimate
                        Log.d(TAG, "Intimate Zone!!!! " + round(firstBeacon.getDistance() * 100) + " cm away.");
                        hueRepository.updateBrightness(255);
                        if (firstBeacon.getDistance() >= 0.10 && firstBeacon.getDistance() <= 0.12) {
                            AddData((int) round(firstBeacon.getDistance() * 100));
                        }
                    } else if (firstBeacon.getDistance() >= 0.46 && firstBeacon.getDistance() <= 1.21) { // personal
                        Log.d(TAG, "Personal Zone!!!! " + round(firstBeacon.getDistance() * 100) + " cm away.");
                        hueRepository.updateBrightness(180);
                    } else if (firstBeacon.getDistance() >= 1.22 && firstBeacon.getDistance() <= 3.70) { // social
                        Log.d(TAG, "Social Zone!!!! " +
                                round(firstBeacon.getDistance() * 100) + " cm away.");
                        hueRepository.updateBrightness(90);
                    } else if (firstBeacon.getDistance() > 3.70) { // public
                        Log.d(TAG, "Public Zone!!!! " +
                                round(firstBeacon.getDistance() * 100) + " cm away."); // !!!a bilo String.format("%.2f", firstBeacon.getDistance())
                        hueRepository.updateBrightness(10);
                    }
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }

    public void AddData(int distance) {
        boolean insertData = autonomousSystemDatabase.addData(
                "openness", "pink",
                "kpop", distance);

        if (insertData) {
            Toast.makeText(RangingActivity.this, "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
            ViewData();
        } else {
            Toast.makeText(RangingActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
        }
    }

    public void ViewData() {
        Cursor viewData = autonomousSystemDatabase.showData();
        Log.d(TAG, "DATABASE!!! " + viewData);
        if (viewData.getCount() == 0) {
            Log.d(TAG, "Error" + "No Data Found.");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (viewData.moveToNext()) {
            buffer.append("_id: " + viewData.getString(0) + "\n");
            buffer.append("personaType: " + viewData.getString(1) + "\n");
            buffer.append("color: " + viewData.getString(2) + "\n");
            buffer.append("musicGenre: " + viewData.getString(3) + "\n");
            buffer.append("distance: " + viewData.getString(4) + "\n");

            Log.d(TAG, buffer.toString());
        }
    }
}


