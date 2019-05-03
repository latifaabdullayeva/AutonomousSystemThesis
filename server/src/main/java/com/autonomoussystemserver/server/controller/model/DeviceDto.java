package com.autonomoussystemserver.server.controller.model;

public class DeviceDto {
    
    private String name;
    private String personality;

    public DeviceDto(String name, String personality) {
        this.name = name;
        this.personality = personality;
    }

    public String getName() {
        return name;
    }

    public String getPersonality() {
        return personality;
    }
}