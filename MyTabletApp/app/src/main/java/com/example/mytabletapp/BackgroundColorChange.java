package com.example.mytabletapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mytabletapp.api.devices.ApiDevicesResponse;
import com.example.mytabletapp.api.devices.Device;
import com.example.mytabletapp.api.devices.DeviceRepository;
import com.example.mytabletapp.api.distance.DistanceRepository;
import com.example.mytabletapp.api.interaction.InteractionRepository;
import com.example.mytabletapp.api.personality.ApiPersonalityResponse;
import com.example.mytabletapp.api.personality.Personality;
import com.example.mytabletapp.api.personality.PersonalityRepository;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Math.round;

public class BackgroundColorChange extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG = "BackgroundColorChange";
    final DistanceRepository distanceRepository = new DistanceRepository();
    final DeviceRepository deviceRepository = new DeviceRepository();
    final PersonalityRepository personalityRepository = new PersonalityRepository();
    final InteractionRepository interactionRepository = new InteractionRepository();

    private long initialMillisec = System.currentTimeMillis();
    private int counter = 1;
    private BeaconManager beaconManager;

    String beaconTagValue, deviceTypeValue, mascotNameValue, devicePersValue;
    Button redButton, greenButton;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_color_change);
        linearLayout = findViewById(R.id.linearLayout);

//        redButton = findViewById(R.id.button1);
//        greenButton = findViewById(R.id.button2);

//        redButton.setOnClickListener(v -> linearLayout.setBackgroundColor(getResources().getColor(R.color.red)));
//
//        greenButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                linearLayout.setBackgroundColor(BackgroundColorChange.this.getResources().getColor(R.color.green));
//            }
//        });

        setupActionBar();
        beaconManager = BeaconManager.getInstanceForApplication(this);

        // TODO: shared Preferences

        beaconTagValue = getIntent().getStringExtra("BEACONUUID");
        deviceTypeValue = getIntent().getStringExtra("DEVICETYPE");
        mascotNameValue = getIntent().getStringExtra("DEVICENAME");
        devicePersValue = getIntent().getStringExtra("PERSONALITY");
    }

    private void setupActionBar() {
        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Color Change");
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                // todo:
                //  - set the timer, every 3 seconds,
                //  - send GET distance request, if distance between this tablet and approaching mascot is ... cm
                //  - if yes, get personality of that

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
        long currentMillisec = System.currentTimeMillis();
        // here you specify the time, every second (1000 milliseconds)
        if ((currentMillisec - initialMillisec) >= 1000 * counter) {
            counter += 1;
            Log.d("test", "counter = " + counter + "; currentMillisec = " + currentMillisec + "; initialMillisec = " + initialMillisec);
            getDevice(beacons);
        }
    }

    private void getDevice(Collection<Beacon> beacons) {
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

}
