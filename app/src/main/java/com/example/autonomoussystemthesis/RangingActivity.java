package com.example.autonomoussystemthesis;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.autonomoussystemthesis.network.api.distance.DistanceRepository;
import com.example.autonomoussystemthesis.network.hue.HueRepository;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

import static java.lang.Math.round;

// import com.example.autonomoussystemthesis.data.DatabaseHelper;

public class RangingActivity extends Activity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager;

//     database
//     DatabaseHelper autonomousSystemDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "RangingActivity started up");

        beaconManager = BeaconManager.getInstanceForApplication(this);

//     autonomousSystemDatabase = new DatabaseHelper(this);
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
//         in order to discover the IpAdress of the bridge, we used https://discovery.meethue.com/
//         Once we have the address load the test app by visiting https://<bridge ip address>/debug/clip.html
//         We need to use the randomly generated username that the bridge creates for you.
//         Fill in the info below and press the POST button, after that press the link button on bridge.
//         URL	/api
//         Body	{"devicetype":"newDeveloper"}
//         Method	POST
//         the Command Response will show you a username
//         The documentation of Hue Api is "https://developers.meethue.com/develop/get-started-2/"
        final HueRepository hueRepository = new HueRepository(
                "192.168.0.100",
                "vY5t4oArH-K0BUA7430cb1rJ8mC1DYMzkmBWRr91"
        );
        final DistanceRepository distanceRepository = new DistanceRepository();


        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
//                  Show in logs the number of beacons that app found
                    Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  " + beacons.size());

                    for (Beacon beacon : beacons) {
//                      beacon = beacons.iterator().next(); --
                        Log.d(TAG, "---- The beacon " + beacon.toString() + " is about " + beacon.getDistance() + " meters away.");
                        int brightness = (int) beacon.getDistance() * 80;
                        if (brightness > 255) {
                            brightness = 255;
                        }

                        hueRepository.updateBrightness(brightness);

                        if (beacon.getDistance() <= 0.45) { // intimate
                            Log.d(TAG, "Intimate Zone!!!! " + round(beacon.getDistance() * 100) + " cm away.");

//                          TODO: it should send the distance to all mascots to the DATABASE
                            distanceRepository.sendNetworkRequest(3, 1, round(beacon.getDistance() * 100));
                            // TODO: vibration
                            // START: When you click on VIBRATE button, phone vibrates */
                            Button vibrationButton = findViewById(R.id.vibtationButton);
                            final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                            vibrationButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                                    } else {
                                        vibrator.vibrate(200);
                                    }
                                }
                            });
                            //
                            hueRepository.updateBrightness(255);

                        } else if (beacon.getDistance() >= 0.46 && beacon.getDistance() <= 1.21) { // personal
                            Log.d(TAG, "Personal Zone!!!! " + round(beacon.getDistance() * 100) + " cm away.");
                            // TODO: tablet color

                        } else if (beacon.getDistance() >= 1.22 && beacon.getDistance() <= 3.70) { // social
                            Log.d(TAG, "Social Zone!!!! " + round(beacon.getDistance() * 100) + " cm away.");
                            // TODO: bench is here, lights

                            hueRepository.updateBrightness(90);
                        } else if (beacon.getDistance() > 3.70) { // public
                            Log.d(TAG, "Public Zone!!!! " + round(beacon.getDistance() * 100) + " cm away."); // !!! a bilo String.format("%.2f", firstBeacon.getDistance())
                            // TODO: speakers
                        }
                    }
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }
}