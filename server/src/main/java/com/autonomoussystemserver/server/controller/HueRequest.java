package com.autonomoussystemserver.server.controller;

public class HueRequest {
    private final boolean on;
    private final int bri;

    public HueRequest(boolean on, int bri) {
        this.on = on;
        this.bri = bri;
        System.out.println("Backend: " +"HueRequest contructor");
    }

    public boolean isOn() {
        System.out.println("Backend: " +"HueRequest isOn()");
        return on;
    }

    public int getBri() {
        System.out.println("Backend: " +"HueRequest getBri()");
        return bri;
    }
}
