package com.example.autonomoussystemthesis;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.autonomoussystemthesis.network.api.devices.ApiDevicesResponse;
import com.example.autonomoussystemthesis.network.api.devices.Device;
import com.example.autonomoussystemthesis.network.api.devices.DeviceRepository;
import com.example.autonomoussystemthesis.network.api.distance.DistanceRepository;
import com.example.autonomoussystemthesis.network.hue.HueRepository;

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
    final DistanceRepository distanceRepository = new DistanceRepository();
    final DeviceRepository deviceRepository = new DeviceRepository();
    String beaconTagValue, deviceTypeValue, mascotNameValue, devicePersValue;

    private BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TestActivity", "ShowALlDist");
        setContentView(R.layout.activity_show_all_distances);

        Objects.requireNonNull(getSupportActionBar()).setTitle("All Distances");
        // getSupportActionBar().setDisplayShowHomeEnabled(true);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        beaconManager = BeaconManager.getInstanceForApplication(this);

        TextView beaconTag = findViewById(R.id.passBeaconUUID);
        beaconTagValue = getIntent().getStringExtra("BEACONUUID");
        beaconTag.setText(beaconTagValue);
        Log.d("TestActivity", "beaconTagValue " + beaconTagValue);

        TextView deviceType = findViewById(R.id.passDeviceType);
        deviceTypeValue = getIntent().getStringExtra("DEVICETYPE");
        deviceType.setText(deviceTypeValue);
        Log.d("TestActivity", "deviceTypeValue " + deviceTypeValue);

        TextView mascotName = findViewById(R.id.passMascotName);
        mascotNameValue = getIntent().getStringExtra("DEVICENAME");
        mascotName.setText(mascotNameValue);
        Log.d("TestActivity", "mascotNameValue " + mascotNameValue);

        TextView devicePers = findViewById(R.id.passPersonality);
        devicePersValue = getIntent().getStringExtra("PERSONALITY");
        devicePers.setText(devicePersValue);
        Log.d("TestActivity", "devicePersValue " + devicePersValue);
    }

    // Disabled back button, so in this Activity user will not allowed to change anything
    @Override
    public void onBackPressed() {
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
                "192.168.0.100",
                "vY5t4oArH-K0BUA7430cb1rJ8mC1DYMzkmBWRr91"
        );
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                deviceRepository.getNetworkRequest(new Callback<ApiDevicesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiDevicesResponse> call, @NonNull Response<ApiDevicesResponse> response) {
                        if (!response.isSuccessful()) {
                            Log.d("DeviceRepository", "Code: " + response.code());
                            return;
                        }
                        ApiDevicesResponse devices = response.body();

                        // find my own phone (device id)
                        Integer myDeviceID = null;
                        if (devices != null) {
                            boolean myBeaconIsInDB = false;
                            for (int i = 0; i < devices.getContent().size(); i++) {
                                if (devices.getContent().get(i).getBeaconUuid().contains(beaconTagValue)) {
                                    myBeaconIsInDB = true;
                                    Log.d("DeviceRepository", "-- DB contains my beacon: " + myBeaconIsInDB);
                                    myDeviceID = devices.getContent().get(i).getDeviceId();
                                    Log.d("DeviceRepository", "-- CONTAINS: " + myBeaconIsInDB + "; ID: " + myDeviceID);
                                }
                            }
                            if (myBeaconIsInDB) {
                                if (beacons.size() > 0) {
                                    for (Beacon beacon : beacons) {
                                        int brightness = (int) beacon.getDistance() * 80;
                                        if (brightness > 255) {
                                            brightness = 255;
                                        }
                                        hueRepository.updateBrightness(brightness);

                                        Log.d("DeviceRepository", "1.DISTANCE: " + round(beacon.getDistance() * 100) + "; BEACON: " + beacon.getId1());
                                        if (!beaconTagValue.equals(beacon.getId1().toString())) {
                                            for (Device device : devices.getContent()) {
                                                if (device.getBeaconUuid().equals(beacon.getId1().toString())) {
                                                    Log.d("DeviceRepository", "!= myDevID (" + myDeviceID + "); deviceID (" + device.getDeviceId() + ") = " + round(beacon.getDistance() * 100));
                                                    distanceRepository.sendNetworkRequest(myDeviceID, device.getDeviceId(), round(beacon.getDistance() * 100));

                                                }
                                            }
                                        } else {
                                            Log.d("DeviceRepository", "= myDevice (" + beaconTagValue + "); device (" + beacon.getId1().toString() + ") = same");
                                        }
                                    }
                                }
                            } else {
                                Log.d("DeviceRepository", "Your Beacon DOES NOT exists in DB");
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiDevicesResponse> call,
                                          @NonNull Throwable t) {
                        Log.d("DeviceRepository", "error loading from API");
                        Log.d("DeviceRepository", t.getMessage());
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
}