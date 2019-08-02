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
    Device myDevice;
    String beaconTagValue, deviceTypeValue, mascotNameValue, devicePersValue;

    private BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TestActivity", "ShowALlDist");
        setContentView(R.layout.activity_show_all_distances);

        Objects.requireNonNull(getSupportActionBar()).setTitle("All Distances");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                if (beacons.size() > 0) {
                    for (Beacon beacon : beacons) {
                        deviceRepository.getNetworkRequest(new Callback<ApiDevicesResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<ApiDevicesResponse> call,
                                                   @NonNull Response<ApiDevicesResponse> response) {
                                Log.d("DeviceRepository", "res: " + response);
                                if (!response.isSuccessful()) {
                                    Log.d("DeviceRepository", "Code: " +
                                            response.code());
                                    return;
                                }
                                ApiDevicesResponse devices = response.body();

                                // find my own phone (device id)
                                myDevice = null;
                                if (devices != null) {
                                    for (Device device : devices.getContent()) {
                                        if (device.getBeaconUuid().equals(beaconTagValue)) {
                                            myDevice = device;
                                            Log.d("DeviceRepository", "" +
                                                    myDevice.getDeviceId() + " " +
                                                    device.getDeviceId());
                                        }
                                    }
                                }

                                if (myDevice != null) {
                                    for (Device device :
                                            Objects.requireNonNull(devices).getContent()) {
                                        if (!myDevice.equals(device.getDeviceId())) {
                                            Log.d("DeviceRepository", "IF: myDevice " +
                                                    myDevice.getDeviceId() + " " +
                                                    device.getDeviceId() + " " +
                                                    round(beacon.getDistance() * 100));
                                            distanceRepository.sendNetworkRequest(
                                                    myDevice.getDeviceId(),
                                                    device.getDeviceId(),
                                                    round(beacon.getDistance() * 100)
                                            );
                                            // TODO: ERROR!!!! takoe oshusheniya kak budto on ne
                                            //  ponimaet kakoy imenno eto beacon
                                        } else {
                                            Log.d("DeviceRepository",
                                                    "ELSE: EQUALS myDevice " +
                                                            myDevice.getDeviceId() + " " +
                                                            device.getDeviceId() + " " +
                                                            round(beacon.getDistance() * 100));
                                        }
                                        // TODO: if there is beacon in DB, but our app is not able
                                        //  to find it, do not post distance
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

                        // TODO:
                        // Here the app starts measuring a distance to all other
                        // devices in the system

                    }
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId",
                    null, null, null));
        } catch (
                RemoteException ignored) {
        }
    }

}