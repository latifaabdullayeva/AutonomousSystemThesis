package com.example.autonomoussystemthesis.network.api.devices;

public class Device {

    private final Integer deviceId;
    private final String deviceName;
    private final String beaconUuid;
    private final String devicePersonality;

    Device(Integer deviceId, String deviceName, String beaconUuid, String devicePersonality) {
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

    public String getDevicePersonality() {
        return devicePersonality;
    }
}