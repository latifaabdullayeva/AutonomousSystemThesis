package com.example.autonomoussystemthesis.network.api.devices;

import android.util.Log;

import com.example.autonomoussystemthesis.network.api.personality.Personality;

public class Device {

    private final Integer deviceId;
    private final String deviceName;
    private final String deviceType;
    private final String beaconUuid;
    private final Personality devicePersonality;

    Device(Integer deviceId, String deviceName, String deviceType, String beaconUuid, Personality devicePersonality) {
        Log.d("FLOW", "Device");

        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.beaconUuid = beaconUuid;
        this.devicePersonality = devicePersonality;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getBeaconUuid() {
        return beaconUuid;
    }

    public Personality getDevicePersonality() {
        return devicePersonality;
    }
}