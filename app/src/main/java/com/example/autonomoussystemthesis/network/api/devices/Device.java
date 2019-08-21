package com.example.autonomoussystemthesis.network.api.devices;

import com.example.autonomoussystemthesis.network.api.personality.Personality;

public class Device {

    private final Integer deviceId;
    private final String deviceName;
    private final String beaconUuid;
    private final Personality devicePersonality;

    Device(Integer deviceId, String deviceName, String beaconUuid, Personality devicePersonality) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.beaconUuid = beaconUuid;
        this.devicePersonality = devicePersonality;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getBeaconUuid() {
        return beaconUuid;
    }

    public Personality getDevicePersonality() {
        return devicePersonality;
    }
}