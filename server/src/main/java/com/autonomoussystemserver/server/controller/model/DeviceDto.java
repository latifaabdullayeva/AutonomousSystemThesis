package com.autonomoussystemserver.server.controller.model;

import com.autonomoussystemserver.server.database.model.Personality;

public class DeviceDto {

    private String deviceName;
    private String deviceType;
    private String beaconUuid;
    private Personality devicePersonality;

    // for deserialisation
    public DeviceDto() {
    }

    public DeviceDto(String deviceName, String deviceType, String beaconUuid, Personality devicePersonality) {
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.beaconUuid = beaconUuid;
        this.devicePersonality = devicePersonality;
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