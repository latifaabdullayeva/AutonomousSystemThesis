package com.example.autonomoussystemthesis.network.api.devices;

public class Device {
    private final String name;
    private final String personality;

    public Device(String name, String personality) {
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
