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
    boolean myBeaconIsInDB = false;
    boolean isProxemicsForMascot = false;
    boolean beaconIsStillActive = false;
    Integer myDeviceID = null;
    int FromMascotId, ToMascotId;
    ApiDevicesResponse passDevicesResponse = null;
    ApiDistanceResponse passDistanceResponse = null;
    ApiPersonalityResponse passPersonalityResponse = null;
    Device passDeviceForBeacon = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FLOW", "ShowAllDistances");
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

//    // This method checks if the beacon that we have choose is exists in Device table or not
//    public void chosenBeaconIsInDB() {
//        for (int i = 0; i < passDevicesResponse.getContent().size(); i++) {
//            if (passDevicesResponse.getContent().get(i).getBeaconUuid().contains(beaconTagValue)) {
//                myBeaconIsInDB = true;
//                myDeviceID = passDevicesResponse.getContent().get(i).getDeviceId();
//            }
//        }
//    }

    public void deviceReq(Beacon beacon) {
        deviceRepository.getNetworkRequest(new Callback<ApiDevicesResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiDevicesResponse> call, @NonNull Response<ApiDevicesResponse> response) {
                Log.d(TAG, "deviceReq");
                if (!response.isSuccessful()) {
                    Log.d(TAG, "PersonalityRepository Code: " + response.code());
                    return;
                }

                ApiDevicesResponse devicesResponse = response.body();
                passDevicesResponse = devicesResponse;
                if (devicesResponse != null) {
                    for (Device device : devicesResponse.getContent()) {
                        if (device.getBeaconUuid().equals(beacon.getId1().toString())) {
                            distanceRepository.sendNetworkRequest(myDeviceID, device.getDeviceId(), round(beacon.getDistance() * 100));
                        }
                        passDeviceForBeacon = device;
                        vibrationBasedOnPersonality(device);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiDevicesResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "error loading from API: " + t.getMessage());
            }
        });
    }

    public void distanceReq() {
        distanceRepository.getNetworkRequest(new Callback<ApiDistanceResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiDistanceResponse> call, @NonNull Response<ApiDistanceResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "PersonalityRepository Code: " + response.code());
                    return;
                }
                Log.d(TAG, "distanceReq");
                ApiDistanceResponse distanceResponse = response.body();

                if (distanceResponse != null) {
                    for (Distance distance : distanceResponse.getContent()) {
                        checkProxemicsForMascot(distance);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiDistanceResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "error loading from API: " + t.getMessage());
            }
        });
    }

    // This method checks if the Theory of Proxemics for Mascot-Mascot interaction is followed
    // for that our PhoneId and the deviceId of the measured distance should be in Distance table in from and to columns respectively
    // and the distance between these two points needs to be less or equal than 45 cm
    public void checkProxemicsForMascot(Distance distance) {
        Log.d(TAG, "checkProxemicsForMascot");
        if (distance.getFromDevice().equals(FromMascotId) && distance.getToDevice().equals(ToMascotId) && distance.getDistance() <= 45) {
            isProxemicsForMascot = true;
        }
    }

    // This method check if both devices are Mascots, then
    // vibrate our phone according to the personality of the device to which we measured the distance
    public void vibrationBasedOnPersonality(Device device) {
        personalityRepository.getNetworkRequest(new Callback<ApiPersonalityResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiPersonalityResponse> call, @NonNull Response<ApiPersonalityResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "PersonalityRepository Code: " + response.code());
                    return;
                }
                Log.d(TAG, "vibrationBasedOnPersonality");
                ApiPersonalityResponse personalities = response.body();

                if (personalities != null) {
                    for (Personality personality : personalities.getContent()) {
                        if (deviceTypeValue.equals("Mascot")) {
                            if (isProxemicsForMascot) {
                                final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                if (personality.getPersonality_name().equals(device.getDevicePersonality().toString())) {
                                    vibrator.vibrate(100 * personality.getVibration_level());
                                }
                            }
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

    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG, "onBeaconServiceConnect");
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Log.d(TAG, "didRangeBeaconsInRegion");
                if (beacons.size() > 0) {
                    for (Beacon beacon : beacons) {
                        if (!beaconTagValue.equals(beacon.getId1().toString())) {
                            deviceReq(beacon);
                            distanceReq();
                        } else {
                            Log.d(TAG, "= myDevice (" + beaconTagValue + "); device (" + beacon.getId1().toString() + ") = same");
                        }
                    }
                }
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


/*
package com.example.autonomoussystemthesis.network.api.personality;

import android.os.RemoteException;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.autonomoussystemthesis.network.api.devices.ApiDevicesResponse;
import com.example.autonomoussystemthesis.network.api.devices.Device;
import com.example.autonomoussystemthesis.network.api.distance.ApiDistanceResponse;
import com.example.autonomoussystemthesis.network.api.distance.Distance;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Math.round;

public class ApiPersonalityResponse {
    protected static final String TAG = "ApiPersonalityResponse";

    private List<Personality> content;

    public ApiPersonalityResponse(List<Personality> content) {
        Log.d("FLOW", "ApiPersonalityResponse");
        this.content = content;
    }

    public List<Personality> getContent() {
        return content;
    }
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
                            boolean myBeaconIsInDB = false;
                            for (int i = 0; i < devices.getContent().size(); i++) {
                                if (devices.getContent().get(i).getBeaconUuid().contains(beaconTagValue)) {
                                    myBeaconIsInDB = true;
                                    myDeviceID = devices.getContent().get(i).getDeviceId();
                                }
                            }

                            if (myBeaconIsInDB) {
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
                                                    if (deviceTypeValue.equals("Mascot") && !device.getDeviceName().equals("Lamp") && !device.getDeviceName().equals("Speaker") && !device.getDeviceName().equals("Tablet")) {
                                                        // In Distances table we have "id, from, to, distance" columns, where
                                                        // From is the id of a device that measures the distance, and To is an id of a device to which we measure the distance
                                                        // Server ignores the requests where the id of From and To devices are equal
                                                        int FromMascotId = myDeviceID;
                                                        int ToMascotId = device.getDeviceId();
                                                        distanceRepository.getNetworkRequest(new Callback<ApiDistanceResponse>() {
                                                            @Override
                                                            public void onResponse(@NonNull Call<ApiDistanceResponse> call, @NonNull Response<ApiDistanceResponse> response) {
                                                                ApiDistanceResponse distanceResponse = response.body();
                                                                if (distanceResponse != null) {
                                                                    for (Distance distance : distanceResponse.getContent()) {
                                                                        // here we check if the Distance From my mascot To other mascot is less or equal to 45 cm,
                                                                        // vibrate my phone according to the personality of other mascot
                                                                        if (distance.getFromDevice() == FromMascotId && distance.getToDevice() == ToMascotId && distance.getDistance() <= 45 && distance.getToDevice() != FromMascotId) {
                                                                            final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                                                            // Add a vibration level according to the personality of a device to whom we measure the distance
                                                                            personalityRepository.getNetworkRequest(new Callback<ApiPersonalityResponse>() {
                                                                                @Override
                                                                                public void onResponse(@NonNull Call<ApiPersonalityResponse> call, @NonNull Response<ApiPersonalityResponse> response) {
                                                                                    if (!response.isSuccessful()) {
                                                                                        Log.d(TAG, "PersonalityRepository Code: " + response.code());
                                                                                        return;
                                                                                    }
                                                                                    ApiPersonalityResponse personalities = response.body();

                                                                                    if (personalities != null) {
                                                                                        for (Personality personality : personalities.getContent()) {
                                                                                            if (personality.getPersonality_name().equals(device.getDevicePersonality().toString())) {
                                                                                                vibrator.vibrate(100 * personality.getVibration_level());
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
 */