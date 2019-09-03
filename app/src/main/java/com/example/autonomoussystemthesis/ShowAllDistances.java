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
    int fromMascotId, toMascotId;

    private BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FLOW", "ShowAllDistances");
        setContentView(R.layout.activity_show_all_distances);

        Objects.requireNonNull(getSupportActionBar()).setTitle("All Distances");

        beaconManager = BeaconManager.getInstanceForApplication(this);

        TextView beaconTag = findViewById(R.id.passBeaconUUID);
        beaconTagValue = getIntent().getStringExtra("BEACONUUID");
        beaconTag.setText(beaconTagValue);

        TextView deviceType = findViewById(R.id.passDeviceType);
        deviceTypeValue = getIntent().getStringExtra("DEVICETYPE");
        deviceType.setText(deviceTypeValue);

        TextView mascotName = findViewById(R.id.passMascotName);
        mascotNameValue = getIntent().getStringExtra("DEVICENAME");
        mascotName.setText(mascotNameValue);

        TextView devicePers = findViewById(R.id.passPersonality);
        devicePersValue = getIntent().getStringExtra("PERSONALITY");
        devicePers.setText(devicePersValue);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                deviceRepository.getNetworkRequest(new Callback<ApiDevicesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiDevicesResponse> call, @NonNull Response<ApiDevicesResponse> response) {
                        if (!response.isSuccessful()) {
                            Log.d(TAG, "Code: " + response.code());
                            return;
                        }
                        ApiDevicesResponse devices = response.body();

                        // find my own phone (device id)
                        Integer myDeviceID = null;
                        if (devices != null) {
                            // we check if the beacon that the user has chosen exists in DB
                            boolean myBeaconIsInDB = false;
                            for (int i = 0; i < devices.getContent().size(); i++) {
                                if (devices.getContent().get(i).getBeaconUuid().contains(beaconTagValue)) {
                                    myBeaconIsInDB = true;
                                    myDeviceID = devices.getContent().get(i).getDeviceId();
                                }
                            }
                            // if the beacon exists in DB, continue, otherwise Log the message that "Your Beacon DOES NOT exists in DB"
                            if (myBeaconIsInDB) {
                                if (beacons.size() > 0) {
                                    // if the numb of beacons in the room is not 0,
                                    // for every beacon check ..
                                    for (Beacon beacon : beacons) {
                                        // .. if beacon that user choose is not the same as beacon in the room, then we sendRequest with (from, to, distance)
                                        // if two endpoints are the same, then show the message that the distance between two identical endpoints are the same
                                        if (!beaconTagValue.equals(beacon.getId1().toString())) {
                                            for (Device device : devices.getContent()) {
                                                if (device.getBeaconUuid().equals(beacon.getId1().toString())) {
                                                    distanceRepository.sendNetworkRequest(myDeviceID, device.getDeviceId(), round(beacon.getDistance() * 100));

                                                    // When the type of our device is Mascot, We get all other devices from DB that are Mascots,
                                                    // then we check if the distance from my Mascot to any other Mascots is less or equal to 45 cm,
                                                    // we vibrate my mascot according to the personality of other mascot

                                                    // If my device is mascot
                                                    if (deviceTypeValue.equals("Mascot")) {
                                                        fromMascotId = myDeviceID;
                                                        toMascotId = device.getDeviceId();
                                                        // check if approaching device is also mascot (we check it by gating value from "Todevice" column in Distance table
                                                        distanceRepository.getNetworkRequest(new Callback<ApiDistanceResponse>() {
                                                            @Override
                                                            public void onResponse(Call<ApiDistanceResponse> call, Response<ApiDistanceResponse> response) {
                                                                if (!response.isSuccessful()) {
                                                                    Log.d(TAG, "DistanceRepository Code: " + response.code());
                                                                    return;
                                                                }
                                                                ApiDistanceResponse distanceResponse = response.body();
                                                                if (distanceResponse != null) {
                                                                    for (Distance distance : distanceResponse.getContent()) {
                                                                        // In Distances table we have "id, from, to, distance" columns, where
                                                                        // From is the id of a device that measures the distance, and To is an id of a device to which we measure the distance
                                                                        // Server ignores the requests where the id of From and To devices are equal
                                                                        if (distance.getFromDevice().equals(fromMascotId) && distance.getToDevice().equals(toMascotId) && distance.getDistance() <= 45) {
                                                                            Log.d(TAG, "");
                                                                            final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                                                            // Add a vibration level according to the personality of a device to whom we measure the distance
                                                                            personalityRepository.getNetworkRequest(new Callback<ApiPersonalityResponse>() {
                                                                                @Override
                                                                                public void onResponse(Call<ApiPersonalityResponse> call, Response<ApiPersonalityResponse> response) {
                                                                                    if (!response.isSuccessful()) {
                                                                                        Log.d(TAG, "PersonalityRepository Code: " + response.code());
                                                                                        return;
                                                                                    }
                                                                                    ApiPersonalityResponse personalities = response.body();

                                                                                    if (personalities != null) {
                                                                                        for (Personality personality : personalities.getContent()) {
                                                                                            if (personality.getPersonality_name().equals(device.getDevicePersonality().getPersonality_name())) {
                                                                                                vibrator.vibrate(100 * personality.getVibration_level());
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<ApiPersonalityResponse> call, Throwable t) {
                                                                                    Log.d(TAG, "error loading from API: " + t.getMessage());
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<ApiDistanceResponse> call, Throwable t) {

                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        } else {
                                            // if two endpoints are the same, then show the message that the distance between two identical endpoints are the same
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