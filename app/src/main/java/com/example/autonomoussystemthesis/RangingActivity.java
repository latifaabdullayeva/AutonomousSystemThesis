package com.example.autonomoussystemthesis;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autonomoussystemthesis.network.api.devices.DeviceRepository;
import com.example.autonomoussystemthesis.network.api.distance.DistanceRepository;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static java.lang.Math.round;

public class RangingActivity extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
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
        Log.d("TestActivity", "Rang");
        setContentView(R.layout.activity_ranging);
        Log.d(TAG, "RangingActivity started up");
        Objects.requireNonNull(getSupportActionBar()).setTitle("Ranging Activity");

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
//         in order to discover the IpAdress of the bridge, we used https://discovery.meethue.com/
//         Once we have the address load the test app by visiting https://<bridge ip address>/debug/clip.html
//         We need to use the randomly generated username that the bridge creates for you.
//         Fill in the info below and press the POST button, after that press the link button on bridge.
//         URL	/api
//         Body	{"devicetype":"newDeveloper"}
//         Method	POST
//         the Command Response will show you a username
//         The documentation of Hue Api is "https://developers.meethue.com/develop/get-started-2/"
        final DistanceRepository distanceRepository = new DistanceRepository();
        final DeviceRepository deviceRepository = new DeviceRepository();


        beaconManager.addRangeNotifier(new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                if (beacons.size() > 0) {

                    // Show the List of all beacons
                    beaconList.clear();
                    for (Beacon beacon : beacons) {
                        if (!beaconList.contains(beacon.getId1().toString())) {
                            beaconList.add(beacon.getId1().toString()); // if you want to get ID of beacon -> .getId1();
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                    numbOfBeacons = findViewById(R.id.numbOfBeacons);
                    numbOfBeacons.setText("Number of Beacon devices: " + beacons.size());

                    // Bind onclick event handler
                    beaconListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                            Toast.makeText(RangingActivity.this, "Selected Beacon: " +
                                    beaconList.get(position), Toast.LENGTH_SHORT).show();

                            // Action for Save button
                            Button buttonSave = findViewById(R.id.buttonBeaconSave);
                            // TODO: write a logic, when this Beacon ID is already choosen by someone
                            //  (get ID from table, if new == none of table, then choose, else show message)
                            buttonSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // deviceRepository.sendNetworkRequest("Nexus", beaconList.get(position), "intimate");
//                                    Intent myIntent = new Intent(RangingActivity.this, DeviceInitialisation.class);

//                                    String deviceValue = beaconList.get(position);
//                                    myIntent.putExtra("BEACONUUID", deviceValue);
//
//                                    startActivity(myIntent);
                                }
                            });

                            //-----

                            // TODO: Logic for:
                            // 1) if user chooses second time
                            //      -> show message -> app ignore in DB new value
                            // 2) if this UUID already exists in DB, so make get request first

                        }
                    });


//                  Show in logs the number of beacons that app found
                    Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  " + beacons.size());

                    for (Beacon beacon : beacons) {

//                        deviceRepository.sendNetworkRequest("Nexus", beacon.toString(), "intimate");
                        Log.d(TAG, "The beacon " + beacon.toString());
//                        distanceRepository.sendNetworkRequest(1, 2, round(beacon.getDistance() * 100));

                        int brightness = (int) beacon.getDistance() * 80;
                        if (brightness > 255) {
                            brightness = 255;
                        }
//                        hueRepository.updateBrightness(brightness);

                        // set extra data field for beacon, name each beacon according to device
//                        beacon = new Beacon.Builder().setExtraDataFields(Arrays
//                                .asList(1L, 2L, 3L, 4L)).build();

                        // get from DB all devices in the system, and beacon.toString()
//                        measurementBeaconList = (TextView) findViewById(R.id.measurementText);
//                        measurementBeaconList.setText("The number of beacons: " + beacons.size() +
//                                "\nThe beacon [" + beacon + "] is about " + round(beacon.getDistance() * 100) + " cm away.\n" +
//                                beacon.getBluetoothAddress() + "\n" +
//                                beacon.getExtraDataFields());

                        if (beacon.getDistance() <= 0.45) { // intimate
                            Log.d(TAG, "Intimate Zone!!!! " + round(beacon.getDistance() * 100) + " cm away.");
                            Log.d(TAG, "-");
//                          TODO: it should send the distance to all mascots to the DATABASE

//                            deviceRepository.getNetworkRequest();

                            // TODO: vibration
                            // START: When you click on VIBRATE button, phone vibrates */
//                            Button vibrationButton = findViewById(R.id.vibtationButton);
//                            final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//                            vibrationButton.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    if (Build.VERSION.SDK_INT >= 26) {
//                                        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
//                                    } else {
//                                        vibrator.vibrate(200);
//                                    }
//                                }
//                            });

                        } else if (beacon.getDistance() >= 0.46 && beacon.getDistance() <= 1.21) { // personal
                            Log.d(TAG, "Personal Zone!!!! " + round(beacon.getDistance() * 100) + " cm away.");
                            Log.d(TAG, "-");
                            // TODO: tablet color
                        } else if (beacon.getDistance() >= 1.22 && beacon.getDistance() <= 3.70) { // social
                            Log.d(TAG, "Social Zone!!!! " + round(beacon.getDistance() * 100) + " cm away.");
                            Log.d(TAG, "-");
                            // TODO: bench is here, lights
//                            hueRepository.updateBrightness(90);
                        } else if (beacon.getDistance() > 3.70) { // public
                            Log.d(TAG, "Public Zone!!!! " + round(beacon.getDistance() * 100) + " cm away."); // !!! a bilo String.format("%.2f", firstBeacon.getDistance())
                            Log.d(TAG, "-");
                            // TODO: speakers
                        }
                    }
                    Log.d(TAG, "------------------------------------------------------------");
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (
                RemoteException ignored) {
        }
    }
}