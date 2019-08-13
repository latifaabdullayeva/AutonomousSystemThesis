package com.autonomoussystemserver.server.controller.model;

public class DeviceDto {

    private String deviceName;
    private String beaconUuid;
    private String devicePersonality;

    public DeviceDto(String deviceName, String beaconUuid, String devicePersonality) {
        System.out.println("Backend: " + "DeviceDTO constructor");
        this.deviceName = deviceName;
        this.beaconUuid = beaconUuid;
        this.devicePersonality = devicePersonality;
    }

    public String getDeviceName() {
        System.out.println("Backend: " + "DeviceDTO getDeviceName");
        return deviceName;
    }

    public String getBeaconUuid() {
        System.out.println("Backend: " + "DeviceDTO getBeaconUuid");
        return beaconUuid;
    }

    public String getDevicePersonality() {
        System.out.println("Backend: " + "DeviceDTO getDevicePersonality");
        return devicePersonality;
    }
}