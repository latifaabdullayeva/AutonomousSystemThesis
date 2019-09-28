package com.example.mytabletapp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mytabletapp.api.devices.Device;
import com.example.mytabletapp.api.devices.DeviceRepository;
import com.example.mytabletapp.api.distance.ApiDistanceResponse;
import com.example.mytabletapp.api.distance.Distance;
import com.example.mytabletapp.api.distance.DistanceRepository;
import com.example.mytabletapp.api.interaction.InteractionRepository;
import com.example.mytabletapp.api.personality.ApiPersonalityResponse;
import com.example.mytabletapp.api.personality.Personality;
import com.example.mytabletapp.api.personality.PersonalityRepository;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackgroundColorChange extends AppCompatActivity {
    protected static final String TAG = "BackgroundColorChange";
    final DistanceRepository distanceRepository = new DistanceRepository();
    final DeviceRepository deviceRepository = new DeviceRepository();
    final PersonalityRepository personalityRepository = new PersonalityRepository();
    final InteractionRepository interactionRepository = new InteractionRepository();

    private long initialMillisec = System.currentTimeMillis();
    private int counter = 1;

    String beaconTagValue, deviceTypeValue;
    Button redButton, greenButton;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_color_change);
        linearLayout = findViewById(R.id.linearLayout);

        setupActionBar();

        beaconTagValue = getIntent().getStringExtra("BEACONUUID");
        deviceTypeValue = getIntent().getStringExtra("DEVICETYPE");
        Log.d(TAG, "beaconTagValue = " + beaconTagValue + "; deviceTypeValue = " + deviceTypeValue);

        long currentMillisec = System.currentTimeMillis();
        // here you specify the time, every second (1000 milliseconds)
        // TODO: instead of IF, which will check only once, use WHILE
        if ((currentMillisec - initialMillisec) <= 1000 * counter) {
            Log.d(TAG, "counter = " + counter + "; currentMillisec = " + currentMillisec + "; initialMillisec = " + initialMillisec + "; currentMillisec - initialMillisec = " + (currentMillisec - initialMillisec));
            checkProxemicsTheory();
            counter += 1;
        }
    }

    private void checkProxemicsTheory() {
        Log.d(TAG, "checkDistanceForProxemics()...");
        distanceRepository.getNetworkRequest(new Callback<ApiDistanceResponse>() {
            @Override
            public void onResponse(Call<ApiDistanceResponse> call, Response<ApiDistanceResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Code: " + response.code());
                    return;
                }
                ApiDistanceResponse distanceResponse = response.body();
                if (distanceResponse != null) {
                    for (Distance distance : distanceResponse.getContent()) {
                        Log.d(TAG, "distance.getDistance() = " + distance.getDistance() +
                                "; getFromDevice = " + distance.getFromDevice().getDeviceId() + "(" + distance.getFromDevice().getDeviceType() + ")" +
                                "; getToDevice = " + distance.getToDevice().getDeviceId() + "(" + distance.getToDevice().getDeviceType() + ")");
                        if (distance.getDistance() >= 46 && distance.getDistance() <= 120) {
                            Log.d(TAG, "Distance fits Proxemics -> ");
                            if (distance.getFromDevice().getDeviceType().equals("Mascot") && distance.getToDevice().getDeviceType().equals("Tablet")) {
                                Log.d(TAG, "Device type fits Proxemics -> ");
                                getPersonalityOfApproachingMascot(distance.getFromDevice(), distance.getToDevice().getDeviceId());
                            } else {
                                Log.d(TAG, "Device type does NOT fit Proxemics");
                            }
                        } else {
                            Log.d(TAG, "Distance does NOT fit Proxemics");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiDistanceResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private void getPersonalityOfApproachingMascot(Device device, Integer myTabletID) {
        Log.d(TAG, "getPersonalityOfApproachingMascot()...");
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
                        if (personality.getPersonality_name().equals(device.getDevicePersonality().getPersonality_name())) {
                            String myColor = personality.getScreen_color();
                            Log.d(TAG, "myColor = " + myColor);
                            linearLayout.setBackgroundColor(Color.parseColor(myColor));
                            interactionRepository.sendNetworkRequest(myTabletID);
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

    private void setupActionBar() {
        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Color Change");
    }

}


/*
private void checkDeviceTypeForProxemics() {
        Log.d(TAG, "checkDeviceTypeForProxemics()");
        deviceRepository.getNetworkRequest(new Callback<ApiDevicesResponse>() {
            @Override
            public void onResponse(Call<ApiDevicesResponse> call, Response<ApiDevicesResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Code: " + response.code());
                    return;
                }
                ApiDevicesResponse devices = response.body();

                // find this Tablet (by device id)
                Integer myTabletID = null;
                if (devices != null) {
                    // we check if the beacon that the user has chosen exists in DB
                    boolean myBeaconIsInDB = false;
                    for (int i = 0; i < devices.getContent().size(); i++) {
                        if (devices.getContent().get(i).getBeaconUuid().contains(beaconTagValue)) {
                            myBeaconIsInDB = true;
                            myTabletID = devices.getContent().get(i).getDeviceId();
                            Log.d(TAG, "myTabletID = " + myTabletID);
                            for (Device device : devices.getContent()) {
                                Log.d(TAG, "deviceTypeValue = " + deviceTypeValue + "; device.getDeviceType() = " + device.getDeviceType());
                                if (device.getDeviceType().equals("Mascot")) {
                                    getPersonalityOfApproachingMascot(device, myTabletID);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiDevicesResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "error loading from API: " + t.getMessage());
            }
        });
    }

 */

/*
@Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                checkTheTime(beacons);
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (
                RemoteException ignored) {
        }
    }

    private void checkTheTime(Collection<Beacon> beacons) {
        Log.d(TAG, "checkTheTime()");
        long currentMillisec = System.currentTimeMillis();
        // here you specify the time, every second (1000 milliseconds)
        if ((currentMillisec - initialMillisec) >= 1000 * counter) {
            counter += 1;
            Log.d(TAG, "counter = " + counter + "; currentMillisec = " + currentMillisec + "; initialMillisec = " + initialMillisec);
            getDevice(beacons);
        }
    }

    private void getDevice(Collection<Beacon> beacons) {
        Log.d(TAG, "getDevice()");
        deviceRepository.getNetworkRequest(new Callback<ApiDevicesResponse>() {
            @Override
            public void onResponse(Call<ApiDevicesResponse> call, Response<ApiDevicesResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Code: " + response.code());
                    return;
                }
                ApiDevicesResponse devices = response.body();

                // find this Tablet (by device id)
                Integer myTabletID = null;
                if (devices != null) {
                    // we check if the beacon that the user has chosen exists in DB
                    boolean myBeaconIsInDB = false;
                    for (int i = 0; i < devices.getContent().size(); i++) {
                        if (devices.getContent().get(i).getBeaconUuid().contains(beaconTagValue)) {
                            myBeaconIsInDB = true;
                            myTabletID = devices.getContent().get(i).getDeviceId();
                        }
                    }
                    // if the beacon exists in DB, continue, otherwise Log the message that "Your Beacon DOES NOT exists in DB"
                    if (myBeaconIsInDB && beacons.size() > 0) {
                        // if the numb of beacons in the room is not 0,
                        // for every beacon check ..
                        for (Beacon beacon : beacons) {
                            // .. if beacon that user choose is not the same as beacon in the room, then we sendRequest with (from, to, distance)
                            // if two endpoints are the same, then show the message that the distance between two identical endpoints are the same
                            if (!beaconTagValue.equals(beacon.getId1().toString())) {
                                for (Device device : devices.getContent()) {
                                    if (device.getBeaconUuid().equals(beacon.getId1().toString())) {
                                        distanceRepository.sendNetworkRequest(myTabletID, device.getDeviceId(), round(beacon.getDistance() * 100));
                                        mascotTabletInteraction(beacon, device, myTabletID);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiDevicesResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "error loading from API: " + t.getMessage());
            }
        });
    }

    private void mascotTabletInteraction(Beacon beacon, Device device, Integer myTabletID) {
        Log.d(TAG, "mascotTabletInteraction()");

        // When the type of our device is Tablet, We get all other devices from DB that are Tablets,
        // then we check if the distance from  Mascot to our Tablet is between 46-120 cm,
        // we change the background color of our tablet according to the personality of approaching mascot

        // We check the distance and whether the devices have Tablet and Mascot types
        if (round(beacon.getDistance() * 100) <= 46 && round(beacon.getDistance() * 100) <= 120) {
            if (deviceTypeValue.equals("Tablet") && device.getDeviceType().equals("Mascot")) {
                // Add color according to the personality of a mascot approaching our tablet
                personalityRepository.getNetworkRequest(new Callback<ApiPersonalityResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiPersonalityResponse> call, @NonNull Response<ApiPersonalityResponse> response) {
                        if (!response.isSuccessful()) {
                            Log.d(TAG, "PersonalityRepository Code: " + response.code());
                            return;
                        }
                        ApiPersonalityResponse personalities = response.body();
                        tabletBackgroundChange(personalities, device, myTabletID);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiPersonalityResponse> call, @NonNull Throwable t) {
                        Log.d(TAG, "error loading from API: " + t.getMessage());
                    }
                });
            }
        }
    }

    private void tabletBackgroundChange(ApiPersonalityResponse personalities, Device device, Integer myMascotId) {
        Log.d(TAG, "tabletBackgroundChange()");
        if (personalities != null) {
            for (Personality personality : personalities.getContent()) {
                if (personality.getPersonality_name().equals(device.getDevicePersonality().getPersonality_name())) {
                    String myColor = personality.getScreen_color();
                    Log.d(TAG, "myColor = " + myColor);
                    linearLayout.setBackgroundColor(Color.parseColor(myColor));
//                    linearLayout.setBackgroundColor(BackgroundColorChange.this.getResources().getColor(R.color.green));
                    interactionRepository.sendNetworkRequest(myMascotId);
                }
            }
        }
    }
 */