package com.example.mytablet;

import android.app.Application;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;

public class LaunchScreen extends Application {

    public void onCreate() {
        super.onCreate();
        setUpBeaconManager();
    }

    private void setUpBeaconManager() {
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().clear();

        beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
        );
        new BackgroundPowerSaver(this);
    }
}
