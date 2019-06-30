package com.example.autonomoussystemthesis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.autonomoussystemthesis.network.api.devices.DeviceRepository;
import com.example.autonomoussystemthesis.network.api.distance.DistanceRepository;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class ShowAllDistances extends AppCompatActivity implements BeaconConsumer {
    final DeviceRepository deviceRepository = new DeviceRepository();
    TextView beaconUuid, deviceType, deviceName, devicePersonality;
    final DistanceRepository distanceRepository = new DistanceRepository();
    private BeaconManager beaconManager;

    TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_distances);
        beaconManager = BeaconManager.getInstanceForApplication(this);

        deviceType = findViewById(R.id.passDeviceType);
        String deviceTypeReq = getIntent().getStringExtra("DEVICETYPE");
        deviceName = findViewById(R.id.passMascotName);
        String deviceNameReq = getIntent().getStringExtra("DEVICENAME");
        devicePersonality = findViewById(R.id.passPersonality);
        String devicePersonalityReq = getIntent().getStringExtra("PERSONALITY");
        if (deviceTypeReq.equals("Mascot")) {
            deviceType.setText("Device Type: \n" + deviceTypeReq);
            deviceName.setText("Mascot Name: \n" + deviceNameReq);
            devicePersonality.setText("Device Personality: \n" + devicePersonalityReq);
        } else {
            deviceName.setText("Device Type: \n" + deviceTypeReq);
        }


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
                        String beaconUuidReq = getIntent().getStringExtra("BEACONUUID");
                        // TODO: on ojidaet fromDevice = Id etoqo beaconUuid, a ne samomu beaconUuid
                        // TODO: 2, should be real ID of a second device
//                        distanceRepository.sendNetworkRequest(beaconUuidReq, 2, round(beacon.getDistance() * 100));

                    }

                    deviceRepository.getNetworkRequest();

                }
            }
        });
    }
}