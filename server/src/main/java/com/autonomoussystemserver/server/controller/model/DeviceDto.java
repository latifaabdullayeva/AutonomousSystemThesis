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
//        System.out.println("DeviceDTO constructor");
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.beaconUuid = beaconUuid;
        this.devicePersonality = devicePersonality;
    }

    public String getDeviceName() {
//        System.out.println("DeviceDTO getDeviceName = " + deviceName);
        return deviceName;
    }

    public String getDeviceType() {
//        System.out.println("DeviceDTO getDeviceType = " + deviceType);
        return deviceType;
    }

    public String getBeaconUuid() {
//        System.out.println("DeviceDTO getBeaconUuid = " + beaconUuid);
        return beaconUuid;
    }

    public Personality getDevicePersonality() {
//        System.out.println("DeviceDTO getDevicePersonality = " + devicePersonality);
        return devicePersonality;
    }

//    @JsonProperty("personality_id")
//    private void unpackNested(Integer id) {
//        this.devicePersonality = new Personality();
//        devicePersonality.setId(id);
//    }
}