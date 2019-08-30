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
import com.example.autonomoussystemthesis.network.api.distance.ApiDistanceResponse;
import com.example.autonomoussystemthesis.network.api.distance.Distance;
import com.example.autonomoussystemthesis.network.api.distance.DistanceRepository;
import com.example.autonomoussystemthesis.network.api.personality.ApiPersonalityResponse;
import com.example.autonomoussystemthesis.network.api.personality.Personality;
import com.example.autonomoussystemthesis.network.api.personality.PersonalityRepository;

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
    final PersonalityRepository personalityRepository = new PersonalityRepository();

    String beaconTagValue, deviceTypeValue, mascotNameValue, devicePersValue;

    private BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FLOW", "ShowAllDistances");
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

                        // find my own phone (device id)
                        Integer myDeviceID = null;
                        if (devices != null) {
                            boolean myBeaconIsInDB = false;
                            Log.d(TAG, "devices.getContent().size(): " + devices.getContent().size());
                            for (int i = 0; i < devices.getContent().size(); i++) {
                                if (devices.getContent().get(i).getBeaconUuid().contains(beaconTagValue)) {
                                    myBeaconIsInDB = true;
                                    myDeviceID = devices.getContent().get(i).getDeviceId();
                                    Log.d(TAG, "-- DB contains my beacon = " + myBeaconIsInDB + "; ID: " + myDeviceID);
                                }
                            }

                            if (myBeaconIsInDB) {
                                Log.d(TAG, "myBeaconIsInDB 2 = " + myBeaconIsInDB + "; beacons.size() = " + beacons.size());
                                if (beacons.size() > 0) {
                                    for (Beacon beacon : beacons) {
                                        if (!beaconTagValue.equals(beacon.getId1().toString())) {
                                            for (Device device : devices.getContent()) {
                                                if (device.getBeaconUuid().equals(beacon.getId1().toString())) {
                                                    distanceRepository.sendNetworkRequest(myDeviceID, device.getDeviceId(), round(beacon.getDistance() * 100));

                                                    // When the type of our device is Mascot, We get all other devices from DB that are Mascots,
                                                    // then we check if the distance from my Mascot to any other Mascots is less or equal to 45 cm,
                                                    // we vibrate my mascot according to the personality of other mascot

                                                    // If my device and other devices are mascots
                                                    Log.d(TAG, "------------------- deviceTypeValue = " + deviceTypeValue);
                                                    if (deviceTypeValue.equals("Mascot") && !device.getDeviceName().equals("Lamp") && !device.getDeviceName().equals("Speaker") && !device.getDeviceName().equals("Tablet")) {
                                                        // In Distances table we have "id, from, to, distance" columns, where
                                                        // From is the id of a device that measures the distance, and To is an id of a device to which we measure the distance
                                                        // Server ignores the requests where the id of From and To devices are equal
                                                        int FromMascotId = myDeviceID;
                                                        int ToMascotId = device.getDeviceId();
                                                        Log.d(TAG, "------------------- FromMascotId = " + FromMascotId + "; ToMascotId = " + ToMascotId + " = " + round(beacon.getDistance() * 100));
                                                        distanceRepository.getNetworkRequest(new Callback<ApiDistanceResponse>() {
                                                            @Override
                                                            public void onResponse(@NonNull Call<ApiDistanceResponse> call, @NonNull Response<ApiDistanceResponse> response) {
                                                                ApiDistanceResponse distanceResponse = response.body();
                                                                if (distanceResponse != null) {
                                                                    for (Distance distance : distanceResponse.getContent()) {
                                                                        // here we check if the Distance From my mascot To other mascot is less or equal to 45 cm,
                                                                        // vibrate my phone according to the personality of other mascot
                                                                        Log.d(TAG, "-------------------" + "distance.getFromDevice() = " + distance.getFromDevice() + "; FromMascotId = " + FromMascotId + "; distance.getToDevice()) = " + distance.getToDevice() + "; ToMascotId = " + ToMascotId + "; distance.getDistance() = " + distance.getDistance());
                                                                        if (distance.getFromDevice() == FromMascotId && distance.getToDevice() == ToMascotId && distance.getDistance() <= 45 && distance.getToDevice() != FromMascotId) {
                                                                            final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                                                            // Add a vibration level according to the personality of a device to whom we measure the distance
                                                                            personalityRepository.getNetworkRequest(new Callback<ApiPersonalityResponse>() {
                                                                                @Override
                                                                                public void onResponse(@NonNull Call<ApiPersonalityResponse> call, @NonNull Response<ApiPersonalityResponse> response) {
                                                                                    Log.d(TAG, response.toString());
                                                                                    if (!response.isSuccessful()) {
                                                                                        Log.d(TAG, "PersonalityRepository Code: " + response.code());
                                                                                        return;
                                                                                    }
                                                                                    ApiPersonalityResponse personalities = response.body();

                                                                                    if (personalities != null) {
                                                                                        for (Personality personality : personalities.getContent()) {
                                                                                            if (personality.getPersonality_name().equals(device.getDevicePersonality().toString())) {
                                                                                                vibrator.vibrate(100 * personality.getVibration_level());
                                                                                                Log.d(TAG, "------------------- " + "personality.getPersonality_name() = " + personality.getPersonality_name() + "; Vibration Level = " + personality.getVibration_level());
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onFailure(@NonNull Call<ApiPersonalityResponse> call, @NonNull Throwable t) {
                                                                                    Log.d(TAG, "error loading from API: " + t.getMessage());
                                                                                }
                                                                            });

                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(@NonNull Call<ApiDistanceResponse> call, @NonNull Throwable t) {

                                                            }
                                                        });

                                                    }
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