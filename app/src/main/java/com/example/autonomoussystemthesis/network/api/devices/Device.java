package com.example.autonomoussystemthesis.network.api.devices;

public class Device {
    private final String deviceName;

    private final String beaconUuid;

    private final String devicePersonality;


    public Device(String deviceName, String beaconUuid, String devicePersonality) {
        this.deviceName = deviceName;
        this.beaconUuid = beaconUuid;
        this.devicePersonality = devicePersonality;
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
