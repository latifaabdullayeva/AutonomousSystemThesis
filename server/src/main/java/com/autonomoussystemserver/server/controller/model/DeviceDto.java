package com.autonomoussystemserver.server.controller.model;

public class DeviceDto {

    private String device_name;
    private String beacon_UUID;
    private String device_personality;

    public DeviceDto(String device_name, String beacon_UUID, String device_personality) {
        this.device_name = device_name;
        this.beacon_UUID = beacon_UUID;
        this.device_personality = device_personality;
    }

    public String getDevice_name() {
        return device_name;
    }

    public String getBeacon_UUID() {
        return beacon_UUID;
    }

    public String getDevice_personality() {
        return device_personality;
    }
}