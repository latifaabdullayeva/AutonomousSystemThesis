package com.example.autonomoussystemthesis;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.autonomoussystemthesis.network.api.devices.ApiDevicesResponse;
import com.example.autonomoussystemthesis.network.api.devices.Device;
import com.example.autonomoussystemthesis.network.api.devices.DeviceRepository;
import com.example.autonomoussystemthesis.network.api.distance.DistanceRepository;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Math.round;

public class ShowAllDistances extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG = "ShowAllDistances";

    final DistanceRepository distanceRepository = new DistanceRepository();
    final DeviceRepository deviceRepository = new DeviceRepository();
    String beaconTagValue, deviceTypeValue, mascotNameValue, devicePersValue;

    private BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ShowAllDistances onCreate()");
        setContentView(R.layout.activity_show_all_distances);

        Objects.requireNonNull(getSupportActionBar()).setTitle("All Distances");
        // getSupportActionBar().setDisplayShowHomeEnabled(true);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        beaconManager = BeaconManager.getInstanceForApplication(this);

        TextView beaconTag = findViewById(R.id.passBeaconUUID);
        beaconTagValue = getIntent().getStringExtra("BEACONUUID");
        beaconTag.setText(beaconTagValue);
        Log.d(TAG, "beaconTagValue = " + beaconTagValue);

        TextView deviceType = findViewById(R.id.passDeviceType);
        deviceTypeValue = getIntent().getStringExtra("DEVICETYPE");
        deviceType.setText(deviceTypeValue);
        Log.d(TAG, "deviceTypeValue = " + deviceTypeValue);

        TextView mascotName = findViewById(R.id.passMascotName);
        mascotNameValue = getIntent().getStringExtra("DEVICENAME");
        mascotName.setText(mascotNameValue);
        Log.d(TAG, "mascotNameValue = " + mascotNameValue);

        TextView devicePers = findViewById(R.id.passPersonality);
        devicePersValue = getIntent().getStringExtra("PERSONALITY");
        devicePers.setText(devicePersValue);
        Log.d(TAG, "devicePersValue = " + devicePersValue);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Log.d(TAG, "didRangeBeaconsInRegion()");
                deviceRepository.getNetworkRequest(new Callback<ApiDevicesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiDevicesResponse> call, @NonNull Response<ApiDevicesResponse> response) {
                        Log.d(TAG, "onResponse()");
                        if (!response.isSuccessful()) {
                            Log.d(TAG, "Code: " + response.code());
                            return;
                        }
                        ApiDevicesResponse devices = response.body();
                        Log.d(TAG, "devices = " + devices);

                        // find my own phone (device id)
                        Integer myDeviceID = null;
                        Log.d(TAG, "devices: " + devices + "; myDeviceID = " + myDeviceID);
                        if (devices != null) {
                            boolean myBeaconIsInDB = false;
                            Log.d(TAG, "devices.getContent().size(): " + devices.getContent().size());
                            for (int i = 0; i < devices.getContent().size(); i++) {
                                Log.d(TAG, "i = " + i + "; getBeaconUuid = " + devices.getContent().get(i).getBeaconUuid());
                                if (devices.getContent().get(i).getBeaconUuid().contains(beaconTagValue)) {
                                    myBeaconIsInDB = true;
                                    Log.d(TAG, "-- DB contains my beacon: " + myBeaconIsInDB);
                                    myDeviceID = devices.getContent().get(i).getDeviceId();
                                    Log.d(TAG, "-- CONTAINS: " + myBeaconIsInDB + "; ID: " + myDeviceID);
                                }
                            }
                            Log.d(TAG, "myBeaconIsInDB = " + myBeaconIsInDB);

                            if (myBeaconIsInDB) { // eto uslovie ne proveray v sluchae s lampoy i planshetom, tolko v sluchae s mascotom(vibraciya)
                                Log.d(TAG, "myBeaconIsInDB 2 = " + myBeaconIsInDB + "; beacons.size() = " + beacons.size());
                                if (beacons.size() > 0) {
                                    Log.d(TAG, "beacons.size() = " + beacons.size());
                                    for (Beacon beacon : beacons) {
                                        Log.d(TAG, "beacon = " + beacon);

                                        Log.d(TAG, "1.DISTANCE: " + round(beacon.getDistance() * 100) + "; BEACON: " + beacon.getId1());
                                        if (!beaconTagValue.equals(beacon.getId1().toString())) {
                                            for (Device device : devices.getContent()) {
                                                if (device.getBeaconUuid().equals(beacon.getId1().toString())) {
                                                    Log.d(TAG, "!= myDevID (" + myDeviceID + "); deviceID (" + device.getDeviceId() + ") = " + round(beacon.getDistance() * 100));
                                                    distanceRepository.sendNetworkRequest(myDeviceID, device.getDeviceId(), round(beacon.getDistance() * 100));
                                                }
                                            }

                                            if (deviceTypeValue.equals("Mascot")) {
                                                if (round(beacon.getDistance() * 100) <= 45) {
                                                    // TODO: vibrate the phone (this specific phone)
                                                    final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                                    vibrator.vibrate(200);
                                                    // todo personality
                                                    // TODO: vibrate according Personality
                                                    // String personalityNameofDev = devNameFrom.getDevicePersonality().getPersonality_name();
                                                    // Personality personality = personalityRepository.findByPersonalityName(personalityNameofDev);
                                                }
                                            }
                                        } else {
                                            Log.d(TAG, "= myDevice (" + beaconTagValue + "); device (" + beacon.getId1().toString() + ") = same");
                                        }

                                    }
                                }
                            } else {
                                Log.d(TAG, "Your Beacon DOES NOT exists in DB");
                            }

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiDevicesResponse> call,
                                          @NonNull Throwable t) {
                        Log.d(TAG, "error loading from API... " + t.getMessage());
                    }
                });
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (
                RemoteException ignored) {
        }
    }

    // Disabled back button, so in this Activity user will not allowed to change anything
    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
        Log.d(TAG, "onDestroy() beaconManager = " + beaconManager + "; Consumer = " + this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        beaconManager.unbind(this);
        Log.d(TAG, "onPause() beaconManager = " + beaconManager + "; Consumer = " + this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        beaconManager.bind(this);
        Log.d(TAG, "onResume() beaconManager = " + beaconManager + "; Consumer = " + this);
    }

}