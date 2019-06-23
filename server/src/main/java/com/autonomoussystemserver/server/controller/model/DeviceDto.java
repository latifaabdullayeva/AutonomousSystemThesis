package com.autonomoussystemserver.server.controller.model;

public class DeviceDto {

    private String deviceName;
    private String beaconUuid;
    private String devicePersonality;

    public DeviceDto(String deviceName, String beaconUuid, String devicePersonality) {
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