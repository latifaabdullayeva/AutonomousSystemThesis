package com.example.autonomoussystemthesis;


import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.EditText;

import com.example.autonomoussystemthesis.network.HueRepository;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class RangingActivity extends Activity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
        beaconManager = BeaconManager.getInstanceForApplication(this);
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
        final HueRepository hueRepository = new HueRepository(
                "192.168.0.102",
                "vY5t4oArH-K0BUA7430cb1rJ8mC1DYMzkmBWRr91"
        );

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  " + beacons.size());

                    Beacon firstBeacon = beacons.iterator().next();
                    int brightness = (int) firstBeacon.getDistance() * 80;
                    if (brightness > 255) {
                        brightness = 255;
                    }

                    hueRepository.updateBrightness(brightness);

                    if (firstBeacon.getDistance() <= 0.45) { // intimate
                        logToDisplay("Intimate Zone!!!! " +
                                String.format("%.2f", firstBeacon.getDistance()) + " meters away.");
                        hueRepository.updateBrightness(255);
                    } else if (firstBeacon.getDistance() >= 0.46 && firstBeacon.getDistance() <= 1.21) { // personal
                        logToDisplay("Personal Zone!!!! " +
                                String.format("%.2f", firstBeacon.getDistance()) + " meters away.");
                        hueRepository.updateBrightness(180);
                    } else if (firstBeacon.getDistance() >= 1.22 && firstBeacon.getDistance() <= 3.70) { // social
                        logToDisplay("Social Zone!!!! " +
                                String.format("%.2f", firstBeacon.getDistance()) + " meters away.");
                        hueRepository.updateBrightness(90);
                    } else if (firstBeacon.getDistance() > 3.70) { // public
                        logToDisplay("Public Zone!!!! " +
                                String.format("%.2f", firstBeacon.getDistance()) + " meters away.");
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

    private void logToDisplay(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
                EditText editText = (EditText) RangingActivity.this.findViewById(R.id.rangingText);
                editText.append(line + "\n");
            }
        });
    }
}
