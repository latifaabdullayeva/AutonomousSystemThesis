package com.example.mytabletapp;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.LinearLayout;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.Objects;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class BackgroundColorChange extends AppCompatActivity implements BeaconConsumer {
//    final DistanceRepository distanceRepository = new DistanceRepository();
//    final DeviceRepository deviceRepository = new DeviceRepository();
//    final PersonalityRepository personalityRepository = new PersonalityRepository();
//    final InteractionRepository interactionRepository = new InteractionRepository();

    private BeaconManager beaconManager;

    String beaconTagValue, deviceTypeValue, mascotNameValue, devicePersValue;
    Button redButton, greenButton;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_color_change);

        redButton = findViewById(R.id.button1);
        greenButton = findViewById(R.id.button2);
        linearLayout = findViewById(R.id.linearLayout);

        redButton.setOnClickListener(v -> linearLayout.setBackgroundColor(getResources().getColor(R.color.red)));

        greenButton.setOnClickListener(v -> linearLayout.setBackgroundColor(getResources().getColor(R.color.green)));

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
        beaconManager.addRangeNotifier((beacons, region) -> getDeviceRequest(beacons));
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (
                RemoteException ignored) {
        }

    }

    // TODO: get color from DB -> Device, Distance, Personality, Interaction

}
