package com.example.mytabletapp.api.devices;

import com.example.mytabletapp.api.personality.Personality;

public class Device {

    private final Integer deviceId;
    private final String deviceName;
    private final String deviceType;
    private final String beaconUuid;
    private final Personality devicePersonality;

    public Device(Integer id) {
        this(id, null, null, null, null);
    }

    Device(Integer deviceId, String deviceName, String deviceType, String beaconUuid, Personality devicePersonality) {
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