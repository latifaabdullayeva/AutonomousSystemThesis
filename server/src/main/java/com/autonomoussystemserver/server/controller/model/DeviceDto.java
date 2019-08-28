package com.autonomoussystemserver.server.controller.model;

import com.autonomoussystemserver.server.database.model.Personality;

public class DeviceDto {

    private String deviceName;
    private String beaconUuid;
    private Personality devicePersonality;

    // for deserialisation
    public DeviceDto() {
    }

    public DeviceDto(String deviceName, String beaconUuid, Personality devicePersonality) {
        System.out.println("Backend: " + "DeviceDTO constructor");
        this.deviceName = deviceName;
        this.beaconUuid = beaconUuid;
        this.devicePersonality = devicePersonality;
    }

    public String getDeviceName() {
        System.out.println("Backend: " + "DeviceDTO getDeviceName = " + deviceName);
        return deviceName;
    }

    public String getBeaconUuid() {
        System.out.println("Backend: " + "DeviceDTO getBeaconUuid = " + beaconUuid);
        return beaconUuid;
    }

    public Personality getDevicePersonality() {
        System.out.println("Backend: " + "DeviceDTO getDevicePersonality = " + devicePersonality);
        return devicePersonality;
    }
}